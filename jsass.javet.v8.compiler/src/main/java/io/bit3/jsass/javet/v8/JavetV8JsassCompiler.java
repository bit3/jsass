package io.bit3.jsass.javet.v8;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetLogger;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.callback.IV8ModuleResolver;
import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.caoccao.javet.utils.JavetDefaultLogger;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.V8ValueObject;
import com.caoccao.javet.values.reference.V8ValuePromise;
import io.bit3.jsass.JsassCompilationException;
import io.bit3.jsass.JsassCompiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.javet.JavetRuntimeCustomizer;
import io.bit3.jsass.javet.impl.ImporterInterceptor;
import io.bit3.jsass.javet.js.JavaScriptModule;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JavetV8JsassCompiler implements JsassCompiler {

  private static final Method FUNCTION_APPLY_FUNCTION = Arrays
      .stream(Function.class.getMethods())
      .filter(method -> "apply".equals(method.getName()))
      .findFirst()
      .orElseThrow();

  private final Logger logger = LoggerFactory.getLogger(JavetV8JsassCompiler.class);
  private final @NonNull IV8ModuleResolver moduleResolver;
  private final @Nullable IJavetEnginePool<V8Runtime> enginePool;
  private final @Nullable IJavetLogger javetLogger;
  private final @NonNull
  @Builder.Default List<JavaScriptModule> extraJavaScriptModules = List.of();
  private final @NonNull
  @Builder.Default List<JavetRuntimeCustomizer> runtimeCustomizers = List.of();
  private final @NonNull
  @Builder.Default List<String> resultCallbacks = List.of();

  // language=javascript
  private static final String IMPORT_SCRIPT =
      "import * as sass from 'sass';\n"
          + "globalThis.sass = sass";

  // language=javascript
  private static final String COMPILE_SCRIPT =
      "new Promise((resolve, reject) => {\n"
          + "    try {\n"
          + "        const {css, sourceMap} = sass.compileString(globalThis.source, globalThis.options);\n"
          + "        resolve({ css, \"sourceMap\": JSON.stringify(sourceMap) })\n"
          + "    } catch (e) {\n"
          + "        reject(e)\n"
          + "    }\n"
          + "})";

  @Override
  public @NotNull CompletableFuture<Output> compileString(
      @NonNull String source,
      @Nullable Options options
  ) throws JsassCompilationException {
    try {
      if (null == enginePool) {
        return compileStringWithSingletonRuntime(source, options);
      } else {
        return compileStringUsingEnginePool(enginePool, source, options);
      }
    } catch (JavetException e) {
      throw new JsassCompilationException(e);
    }
  }

  private @NotNull CompletableFuture<Output> compileStringWithSingletonRuntime(
      @NotNull String source,
      @Nullable Options options
  ) throws JavetException {
    logger.trace("Compile sass using singleton javet instance");
    try (val v8runtime = V8Host.getV8Instance().createV8Runtime()) {
      return compileString0(v8runtime, source, options);
    }
  }

  private @NotNull CompletableFuture<Output> compileStringUsingEnginePool(
      @NotNull IJavetEnginePool<V8Runtime> enginePool,
      @NotNull String source,
      @Nullable Options options
  ) throws JavetException {
    logger.trace("Compile sass using engine pool");
    try (
        val v8engine = enginePool.getEngine();
        val v8runtime = v8engine.getV8Runtime()
    ) {
      return compileString0(v8runtime, source, options);
    }
  }

  private @NotNull CompletableFuture<Output> compileString0(
      @NotNull V8Runtime v8runtime,
      @NotNull String source,
      @Nullable Options options
  ) throws JavetException {
    val result = new CompletableFuture<Output>();
    try (
        val sassOptions = buildSassOptions(v8runtime, options)
    ) {
      v8runtime.setV8ModuleResolver(moduleResolver);
      v8runtime.setLogger(
          Optional
              .ofNullable(javetLogger)
              .orElseGet(() -> new JavetDefaultLogger(JavetV8JsassCompiler.class.getName()))
      );

      v8runtime
          .getExecutor(IMPORT_SCRIPT)
          .setModule(true)
          .executeVoid();

      for (val extraJavaScriptModule : extraJavaScriptModules) {
        v8runtime
            .getExecutor(extraJavaScriptModule.getScript())
            .setResourceName(extraJavaScriptModule.getResourceName())
            .setModule(true)
            .executeVoid();
      }

      for (val runtimeCustomizer : runtimeCustomizers) {
        runtimeCustomizer.apply(v8runtime);
      }

      val globalObject = v8runtime.getGlobalObject();
      globalObject.setString("source", source);
      globalObject.set("options", sassOptions);

      val continueScript = resultCallbacks.isEmpty()
          ? ".then(r => r)"
          : resultCallbacks.stream().map(s -> ".then(" + s + ")").collect(Collectors.joining(""));

      final var compileScript = COMPILE_SCRIPT + continueScript;
      if (logger.isTraceEnabled()) {
        logger.trace("Execute:\n{}",
            compileScript.lines().map(l -> "> " + l).collect(Collectors.joining("\n")));
      }

      try (val v8promise = (V8ValuePromise) v8runtime.getExecutor(compileScript).execute()) {
        v8runtime.await();

        while (v8promise.isPending()) {
          Thread.yield();
          v8runtime.await();
        }

        if (v8promise.isFulfilled()) {
          try {
            val object = (V8ValueObject) v8promise.getResult();
            result.complete(new Output(object.getString("css"), object.getString("sourceMap")));
          } catch (JavetException e) {
            result.completeExceptionally(e);
          }
        } else if (v8promise.isRejected()) {
          final var reason = v8promise.getResult();
          result.completeExceptionally(new RuntimeException(reason.toString()));
        }
      }
    }
    return result;
  }

  /**
   * Building the sass options object in javet v8 values.
   *
   * @param v8runtime The v8 runtime.
   * @param options The jsass options object.
   * @return The javet v8 options object.
   */
  private static @NotNull V8Value buildSassOptions(
      @NotNull V8Runtime v8runtime,
      @Nullable Options options
  ) throws JavetException {
    if (null == options) {
      return v8runtime.createV8ValueNull();
    }

    val sassOptions = v8runtime.createV8ValueObject();

    val rootImporter = options.getImporter();
    if (null != rootImporter) {
      try (val v8ValueObject = v8runtime.createV8ValueObject()) {
        v8ValueObject.bind(new ImporterInterceptor(rootImporter));
        sassOptions.set("importer", v8ValueObject);
      }
    }

    // TODO loadPaths
    setOptionalString(
        sassOptions,
        "syntax",
        Optional.ofNullable(options.getInputSyntax())
            .map(Objects::toString)
            .map(String::toLowerCase)
            .orElse(null)
    );

    setOptionalString(
        sassOptions,
        "url",
        Optional.ofNullable(options.getUrl()).map(URL::toString).orElse(null)
    );

    setOptionalString(
        sassOptions,
        "charset",
        options.getCharset()
    );

    setOptionalBoolean(
        sassOptions,
        "sourceMap",
        options.getSourceMap()
    );

    setOptionalBoolean(
        sassOptions,
        "sourceMapIncludeSources",
        options.getSourceMapIncludeSources()
    );

    setOptionalString(
        sassOptions,
        "style",
        Optional.ofNullable(options.getStyle())
            .map(Objects::toString)
            .map(String::toLowerCase)
            .orElse(null)
    );

    // TODO functions
//    val functions = options.getFunctions();
//    if (null != functions && !functions.isEmpty()) {
//      try (val v8ValueObject = v8runtime.createV8ValueObject()) {
//        for (val entry : functions.entrySet()) {
//          val declaration = entry.getKey();
//          val sassFunction = entry.getValue();
//
//          val javascript = Optional
//              .ofNullable(sassFunction.getFunctionBody())
//              .filter(s -> !s.isBlank())
//              .orElse(null);
//          val callback = sassFunction.getCallback();
//          val callbackName = sassFunction.getCallbackName();
//
//          if (null != javascript && null != callback) {
//            val context = new JavetCallbackContext(
//                callbackName,
//                callback,
//                FUNCTION_APPLY_FUNCTION
//            );
//            v8runtime.getGlobalObject().bindFunction(context);
//            try (val v8ValueFunction = v8runtime.createV8ValueFunction(context)) {
//
//            }
//          } else {
//
//          }
//        }
//      }
//    }

    val importers = options.getImporters();
    if (null != importers && !importers.isEmpty()) {
      try (val v8ValueArray = v8runtime.createV8ValueArray()) {
        for (Importer importer : importers) {
          try (val v8ValueObject = v8runtime.createV8ValueObject()) {
            v8ValueObject.bind(new ImporterInterceptor(importer));
            v8ValueArray.push(v8ValueObject);
          }
        }
        sassOptions.set("importers", v8ValueArray);
      }
    }

    setOptionalBoolean(
        sassOptions,
        "alertAscii",
        options.getAlertAscii()
    );

    setOptionalBoolean(
        sassOptions,
        "alertColor",
        options.getAlertColor()
    );

    // TODO fatalDeprecations
    // TODO logger

    setOptionalBoolean(
        sassOptions,
        "quietDeps",
        options.getQuietDeps()
    );

    // TODO silenceDeprecations

    setOptionalBoolean(
        sassOptions,
        "verbose",
        options.getVerbose()
    );

    return sassOptions;
  }

  private static void setOptionalString(
      @NotNull V8ValueObject object,
      @NotNull String key,
      @Nullable String value
  ) throws JavetException {
    if (null != value && !value.isBlank()) {
      object.setString(key, value);
    }
  }

  private static void setOptionalBoolean(
      @NotNull V8ValueObject object,
      @NotNull String key,
      @Nullable Boolean value
  ) throws JavetException {
    if (null != value) {
      object.setBoolean(key, value);
    }
  }
}

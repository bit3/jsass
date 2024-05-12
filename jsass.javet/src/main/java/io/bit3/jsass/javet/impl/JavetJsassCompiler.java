package io.bit3.jsass.javet.impl;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetLogger;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.callback.IV8ModuleResolver;
import com.caoccao.javet.interop.converters.JavetObjectConverter;
import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.caoccao.javet.interop.executors.IV8Executor;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.V8ValueError;
import com.caoccao.javet.values.reference.V8ValueObject;
import com.caoccao.javet.values.reference.V8ValuePromise;
import io.bit3.jsass.JsassCompilationException;
import io.bit3.jsass.JsassCompiler;
import io.bit3.jsass.Output;
import io.bit3.jsass.Resources;
import io.bit3.jsass.StringOptions;
import io.bit3.jsass.Utils;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.javet.JavetRuntimeCustomizer;
import io.bit3.jsass.javet.JavetSlf4JLogger;
import io.bit3.jsass.javet.js.JavaScriptModule;
import io.bit3.jsass.javet.js.JavaScriptModules;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavetJsassCompiler<R extends V8Runtime> implements JsassCompiler {

  private static final String WRAP_OPTIONS_SCRIPT = Objects.requireNonNull(
      Resources.toString(
          JavetJsassCompiler.class.getClassLoader(),
          "io/bit3/jsass/javet/impl/wrap-options.js"
      )
  );

  public static final String SASS_COMPILE_SCRIPT = Objects.requireNonNull(
      Resources.toString(
          JavetJsassCompiler.class.getClassLoader(),
          "io/bit3/jsass/javet/impl/sass-compile-script.js"
      )
  );

  public static final Method FUNCTION_APPLY_FUNCTION = Arrays
      .stream(Function.class.getMethods())
      .filter(method -> "apply".equals(method.getName()))
      .findFirst()
      .orElseThrow();

  private final Logger logger = LoggerFactory.getLogger(JavetJsassCompiler.class);

  @NonNull
  private final IV8ModuleResolver moduleResolver;

  @NonNull
  private final V8Host v8Host;

  @Nullable
  private final IJavetEnginePool<R> enginePool;

  @Nullable
  private final IJavetLogger javetLogger;

  @NonNull
  private final List<JavaScriptModule> extraJavaScriptModules;

  @NonNull
  private final List<JavetRuntimeCustomizer> runtimeCustomizers;

  @NonNull
  private final String compileScript;

  @NonNull
  private final List<String> resultCallbacks;

  public JavetJsassCompiler(
      @NonNull IV8ModuleResolver moduleResolver,
      @NonNull V8Host v8Host,
      @Nullable IJavetEnginePool<R> enginePool,
      @Nullable IJavetLogger javetLogger,
      @NonNull List<JavaScriptModule> extraJavaScriptModules,
      @NonNull List<JavetRuntimeCustomizer> runtimeCustomizers,
      @Nullable String compileScript,
      @NonNull List<String> resultCallbacks
  ) {
    this.moduleResolver = moduleResolver;
    this.v8Host = v8Host;
    this.enginePool = enginePool;
    this.javetLogger = javetLogger;
    this.extraJavaScriptModules = extraJavaScriptModules;
    this.runtimeCustomizers = runtimeCustomizers;
    this.compileScript = Optional.ofNullable(compileScript).orElse(SASS_COMPILE_SCRIPT);
    this.resultCallbacks = resultCallbacks;
  }

  @Override
  public @NotNull CompletableFuture<Output> compileString(
      @NonNull String source,
      @Nullable StringOptions options
  ) throws JsassCompilationException {
    try {
      if (null == enginePool) {
        return compileStringWithSingletonRuntime(source, options);
      } else {
        return compileStringUsingEnginePool(enginePool, source, options);
      }
    } catch (JavetException | IOException e) {
      throw new JsassCompilationException(e);
    }
  }

  private @NotNull CompletableFuture<Output> compileStringWithSingletonRuntime(
      @NotNull String source,
      @Nullable StringOptions options
  ) throws JavetException, IOException {
    logger.trace("Compile sass using singleton javet instance");
    try (val v8runtime = v8Host.createV8Runtime()) {
      return compileString0(v8runtime, source, options);
    }
  }

  private @NotNull CompletableFuture<Output> compileStringUsingEnginePool(
      @NotNull IJavetEnginePool<R> enginePool,
      @NotNull String source,
      @Nullable StringOptions options
  ) throws JavetException, IOException {
    logger.trace("Compile sass using engine pool");
    try (
        val v8engine = enginePool.getEngine();
        val v8runtime = v8engine.getV8Runtime()
    ) {
      return compileString0(v8runtime, source, options);
    }
  }

  private @NotNull CompletableFuture<Output> compileString0(
      @NotNull V8Runtime v8Runtime,
      @NotNull String source,
      @Nullable StringOptions options
  ) throws JavetException {
    val result = new CompletableFuture<Output>();
    try (
        val sassOptions = buildSassOptions(v8Runtime, options)
    ) {
      val javetObjectConverter = new JavetObjectConverter();
      v8Runtime.setConverter(javetObjectConverter);

      v8Runtime.setV8ModuleResolver(moduleResolver);
      v8Runtime.setLogger(
          Optional
              .ofNullable(javetLogger)
              .orElseGet(() -> new JavetSlf4JLogger("sass"))
      );

      for (val runtimeCustomizer : runtimeCustomizers) {
        runtimeCustomizer.apply(v8Runtime);
      }

      loadJavaScriptModule(v8Runtime, JavaScriptModules.IMPORT_SASS_MODULE);

      for (val extraJavaScriptModule : extraJavaScriptModules) {
        loadJavaScriptModule(v8Runtime, extraJavaScriptModule);
      }

      val globalObject = v8Runtime.getGlobalObject();
      globalObject.set("location", Map.of("href", "https://example.com"));
      globalObject.setString("source", source);
      globalObject.set("options", sassOptions);

      v8Runtime.getExecutor(WRAP_OPTIONS_SCRIPT).executeVoid();

      val continueScript = resultCallbacks.isEmpty()
          ? ".then(r => r)"
          : resultCallbacks.stream().map(s -> ".then(" + s + ")").collect(Collectors.joining(""));

      final var combinedScript = Utils.stripTrailingSemicolon(this.compileScript) + continueScript;
      if (logger.isTraceEnabled()) {
        logger.trace("Execute:\n{}",
            combinedScript.lines().map(l -> "> " + l).collect(Collectors.joining("\n")));
      }

      try (val v8promise = (V8ValuePromise) v8Runtime.getExecutor(combinedScript).execute()) {
        v8Runtime.await();

        while (v8promise.isPending()) {
          Thread.yield();
          v8Runtime.await();
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
          final var message = reason instanceof V8ValueError
              ? ((V8ValueError) reason).getStack()
              : reason.toString();
          result.completeExceptionally(new JsassCompilationException(message));
        }
      }
    } finally {
      val globalObject = v8Runtime.getGlobalObject();
      globalObject.delete("location");
      globalObject.delete("URL");
      globalObject.delete("source");
      globalObject.delete("options");
    }
    return result;
  }

  private static void loadJavaScriptModule(
      @NotNull V8Runtime v8Runtime,
      @NotNull JavaScriptModule javaScriptModule
  ) throws JavetException {
    final var executor = v8Runtime.getExecutor(javaScriptModule.getScript());

    if (null != javaScriptModule.getResourceName()) {
      executor.setResourceName(javaScriptModule.getResourceName());
    }

    executor.setModule(javaScriptModule.isModule()).executeVoid();
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
      @Nullable StringOptions options
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
        Optional.ofNullable(options.getUrl()).map(URI::toString).orElse(null)
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

  public static class JavetV8JsassCompilerBuilder {

    public JavetV8JsassCompilerBuilder apply(UnaryOperator<JavetV8JsassCompilerBuilder> operator) {
      return operator.apply(this);
    }
  }
}

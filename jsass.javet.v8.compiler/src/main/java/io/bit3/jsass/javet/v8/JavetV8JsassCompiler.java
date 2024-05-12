package io.bit3.jsass.javet.v8;

import com.caoccao.javet.enums.JSRuntimeType;
import com.caoccao.javet.interfaces.IJavetLogger;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.callback.IV8ModuleResolver;
import com.caoccao.javet.interop.engine.IJavetEnginePool;
import io.bit3.jsass.JsassCompilationException;
import io.bit3.jsass.JsassCompiler;
import io.bit3.jsass.Output;
import io.bit3.jsass.Resources;
import io.bit3.jsass.StringOptions;
import io.bit3.jsass.javet.JavetRuntimeCustomizer;
import io.bit3.jsass.javet.impl.JavetJsassCompiler;
import io.bit3.jsass.javet.js.JavaScriptModule;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavetV8JsassCompiler implements JsassCompiler {

  private static final String URL_POLYFILL = Resources.toString(
      JavetV8JsassCompiler.class.getClassLoader(),
      "io/bit3/jsass/javet/v8/url-polyfill.js"
  );

  private final JavetJsassCompiler<V8Runtime> compiler;

  @Builder
  public JavetV8JsassCompiler(
      @NonNull IV8ModuleResolver moduleResolver,
      @Nullable IJavetEnginePool<V8Runtime> enginePool,
      @Nullable IJavetLogger javetLogger,
      @NonNull @Singular List<JavaScriptModule> extraJavaScriptModules,
      @NonNull @Singular List<JavetRuntimeCustomizer> runtimeCustomizers,
      @Nullable String compileScript,
      @NonNull @Singular List<String> resultCallbacks
  ) {
    if (null != enginePool &&
        !JSRuntimeType.V8.equals(enginePool.getConfig().getJSRuntimeType())) {
      throw new IllegalArgumentException("JS runtime type of enginePool is not JSRuntimeType.V8");
    }

    compiler = new JavetJsassCompiler<>(
        moduleResolver,
        V8Host.getV8Instance(),
        enginePool,
        javetLogger,
        extraJavaScriptModules,
        Stream.concat(
            runtimeCustomizers.stream(),
            Stream.of(v8Runtime -> v8Runtime.getExecutor(URL_POLYFILL).executeVoid())
        ).collect(Collectors.toList()),
        compileScript,
        resultCallbacks
    );
  }

  @Override
  public @NotNull CompletableFuture<Output> compileString(
      @NonNull String source,
      @Nullable StringOptions options
  ) throws JsassCompilationException {
    return compiler.compileString(source, options);
  }

}

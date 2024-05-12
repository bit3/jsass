package io.bit3.jsass.javet.node.js;

import io.bit3.jsass.javet.js.JavaScriptModules;
import io.bit3.jsass.javet.node.JavetNodeJsassCompiler;
import java.util.function.UnaryOperator;

public final class PostcssHelper {
  private PostcssHelper() {
    throw new UnsupportedOperationException();
  }

  public static UnaryOperator<JavetNodeJsassCompiler.JavetNodeJsassCompilerBuilder> enableAutoprefixer() {
    return builder -> builder
        .extraJavaScriptModule(JavaScriptModules.IMPORT_POSTCSS_MODULE)
        .extraJavaScriptModule(JavaScriptModules.IMPORT_AUTOPREFIXER_MODULE)
        .resultCallback(
            // language=javascript
            "(result) => globalThis.postcss([ globalThis.autoprefixer ])\n"
                + ".process(result.css, { map: result.sourceMap })\n"
                + ".then(r => ({ ...result, css: r.css, sourceMap: r.map?.toJSON() }))"
        );
  }
}

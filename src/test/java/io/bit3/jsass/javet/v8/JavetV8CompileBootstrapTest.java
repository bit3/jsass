package io.bit3.jsass.javet.v8;

import io.bit3.jsass.JsassCompilationException;
import io.bit3.jsass.Resources;
import io.bit3.jsass.StringOptions;
import io.bit3.jsass.javet.JavetSlf4JLogger;
import io.bit3.jsass.javet.webjars.WebjarV8ModuleResolver;
import io.bit3.jsass.json.jackson.JacksonJsonDeserializer;
import io.bit3.jsass.webjar.WebjarImporter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.webjars.WebJarAssetLocator;

/**
 * Compile bootstrap using {@link JavetV8JsassCompiler}, {@link WebjarV8ModuleResolver} and
 * {@link WebjarImporter}.
 */
class JavetV8CompileBootstrapTest {

  @Test
  void test()
      throws ExecutionException, InterruptedException, TimeoutException, URISyntaxException {
    final var webJarAssetLocator = new WebJarAssetLocator();

    val moduleResolver = WebjarV8ModuleResolver
        .builder()
        .locator(webJarAssetLocator)
        .jsonDeserializer(new JacksonJsonDeserializer())
        .build();

    val compiler = JavetV8JsassCompiler
        .builder()
        .moduleResolver(moduleResolver)
        .javetLogger(new JavetSlf4JLogger())
        .build();
    val options = StringOptions
        .builder()
        .url(new URI("webjar:style.scss"))
        .sourceMap(false)
        .importer(WebjarImporter.builder().build())
        .build();

    try {
      val result = compiler.compileString(
          // language=scss
          "@import \"bootstrap/scss/bootstrap\";",
          options
      );

      val output = result.get(10, TimeUnit.SECONDS);

      Assertions.assertEquals(
          Objects.requireNonNull(
              Resources.toString(
                  "assets/bootstrap.css"
              )
          ).trim(),
          output.getCss().trim()
      );
    } catch (JsassCompilationException e) {
      throw new RuntimeException(e);
    }
  }

}

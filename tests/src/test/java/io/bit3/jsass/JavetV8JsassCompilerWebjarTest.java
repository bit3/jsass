package io.bit3.jsass;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.bit3.jsass.javet.JavetSlf4JLogger;
import io.bit3.jsass.javet.v8.JavetV8JsassCompiler;
import io.bit3.jsass.javet.webjars.WebjarV8ModuleResolver;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import io.bit3.jsass.javet.webjars.json.JacksonJsonDeserializer;
import lombok.val;
import org.junit.jupiter.api.Test;

public class JavetV8JsassCompilerWebjarTest {

  @Test
  void test() throws ExecutionException, InterruptedException, TimeoutException {
    val moduleResolver = WebjarV8ModuleResolver
        .builder()
        .jsonDeserializer(new JacksonJsonDeserializer())
        .build();

    val compiler = JavetV8JsassCompiler
        .builder()
        .moduleResolver(moduleResolver)
        .javetLogger(new JavetSlf4JLogger())
        .build();
    val options = Options
        .builder()
        .sourceMap(true)
        .build();

    try {
      val result = compiler.compileString(
          // language=scss
          "h1 {\n"
              + "  font-size: 40px;\n"
              + "  code {\n"
              + "    font-face: Roboto Mono;\n"
              + "  }\n"
              + "}",
          options
      );

      val output = result.get(10, TimeUnit.SECONDS);

      assertEquals(
          // language=css
          "h1 {\n"
              + "  font-size: 40px;\n"
              + "}\n"
              + "h1 code {\n"
              + "  font-face: Roboto Mono;\n"
              + "}",
          output.getCss()
      );
      assertEquals(
          "{\"version\":3,\"sourceRoot\":\"\",\"sources\":[\"data:;charset=utf-8,h1%20%7B%0A%20%20font-size:%2040px;%0A%20%20code%20%7B%0A%20%20%20%20font-face:%20Roboto%20Mono;%0A%20%20%7D%0A%7D\"],\"names\":[],\"mappings\":\"AAAA;EACE;;AACA;EACE\"}",
          output.getSourceMap()
      );
    } catch (JsassCompilationException e) {
      throw new RuntimeException(e);
    }
  }

}

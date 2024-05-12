package io.bit3.jsass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionTest {

  private JsassCompiler compiler;
  private StringOptions options;

  /**
   * Set up the compiler and the compiler options for each run.
   *
   * @throws URISyntaxException Throws if the resource URI is invalid.
   */
  @BeforeEach
  public void setUp() {
    compiler = new JsassCompiler();
    options = new StringOptions();
  }

  @Test
  public void testExceptionForString() {
    assertThrows(
      JsassCompilationException.class,
      () -> compiler.compileString(
        "@error \"Test exception\";",
        new URI("/input.scss"),
        new URI("/output.css"),
        options
      )
    );
  }

  @Test
  public void testExceptionForSassFile() {
    URL inputFile = ExceptionTest.class.getResource("/sass/error.sass");

    assertThrows(
      JsassCompilationException.class,
      () -> compiler.compileFile(
        inputFile.toURI(),
        new URI("/output.css"),
        options
      )
    );
  }

  @Test
  public void testExceptionForScssFile() {
    URL inputFile = ExceptionTest.class.getResource("/scss/error.scss");

    assertThrows(
      JsassCompilationException.class,
      () -> compiler.compileFile(
        inputFile.toURI(),
        new URI("/output.css"),
        options
      )
    );
  }
}

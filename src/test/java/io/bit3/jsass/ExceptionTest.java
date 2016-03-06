package io.bit3.jsass;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import io.bit3.jsass.importer.Import;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ExceptionTest {

  private Compiler compiler;
  private Options options;

  /**
   * Set up the compiler and the compiler options for each run.
   *
   * @throws URISyntaxException Throws if the resource URI is invalid.
   */
  @Before
  public void setUp() throws IOException, URISyntaxException {
    compiler = new Compiler();
    options = new Options();
  }

  @Test(expected = CompilationException.class)
  public void testExceptionForString() throws Exception {
    compiler.compileString(
        "@error \"Test exception\";",
        new URI("/input.scss"),
        new URI("/output.css"),
        options
    );
  }

  @Test(expected = CompilationException.class)
  public void testExceptionForSassFile() throws Exception {
    URL inputFile = ExceptionTest.class.getResource("/sass/error.sass");

    compiler.compileFile(
        inputFile.toURI(),
        new URI("/output.css"),
        options
    );
  }

  @Test(expected = CompilationException.class)
  public void testExceptionForScssFile() throws Exception {
    URL inputFile = ExceptionTest.class.getResource("/scss/error.scss");

    compiler.compileFile(
        inputFile.toURI(),
        new URI("/output.css"),
        options
    );
  }
}

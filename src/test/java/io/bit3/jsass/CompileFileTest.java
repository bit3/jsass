package io.bit3.jsass;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CompileFileTest {

  private String syntax;
  private OutputStyle outputStyle;
  private Compiler compiler;
  private Options options;
  private File sourceFile;
  private File targetDir;
  private File targetCssWithoutMapFile;
  private File targetCssWithMapFile;
  private File targetSourceMapFile;
  private URL expectedCssWithoutMapUrl;
  private URL expectedCssWithMapUrl;

  /**
   * Create test running with specific syntax and output style.
   *
   * @param syntax      The syntax (scss or sass).
   * @param outputStyle The output style.
   */
  public CompileFileTest(String syntax, OutputStyle outputStyle) {
    this.syntax = syntax;
    this.outputStyle = outputStyle;
  }

  /**
   * Return data for running the test.
   *
   * <p>This data contain a set of syntax (scss or sass) and output style in all combinations.</p>
   */
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][]{
            {"scss", OutputStyle.COMPRESSED},
            {"scss", OutputStyle.NESTED},
            {"sass", OutputStyle.COMPRESSED},
            {"sass", OutputStyle.NESTED},
        }
    );
  }

  /**
   * Set up the compiler and the compiler options for each run.
   *
   * <p>Using the system temp directory as output directory.</p>
   *
   * @throws URISyntaxException Throws if the resource URI is invalid.
   */
  @Before
  public void setUp() throws URISyntaxException {
    compiler = new Compiler();

    String incPath = String.format("/%s/inc", syntax);
    URL incUrl = getClass().getResource(incPath);

    options = new Options();
    options.getIncludePaths().add(new File(incUrl.toURI()));
    options.getFunctionProviders().add(new TestFunctions());
    options.getImporters().add(new TestImporter());

    String sourcePath = String.format("/%s/input.%s", syntax, syntax);
    URL sourceUrl = getClass().getResource(sourcePath);
    sourceFile = new File(sourceUrl.toURI());

    String property = "java.io.tmpdir";
    String tempDirPath = System.getProperty(property);
    targetDir = new File(tempDirPath, "jsass_unit");

    if (targetDir.exists()) {
      tearDown();
    }
    if (!targetDir.mkdirs()) {
      throw new RuntimeException("Could not create temporary directory");
    }

    String targetCssWithoutMapPath = "output-without-map.css";
    targetCssWithoutMapFile = new File(targetDir, targetCssWithoutMapPath);

    String targetCssWithMapPath = "output-with-map.css";
    targetCssWithMapFile = new File(targetDir, targetCssWithMapPath);

    String targetSourceMapPath = "output-with-map.css.map";
    targetSourceMapFile = new File(targetDir, targetSourceMapPath);

    final String outputStyle = this.outputStyle.toString().toLowerCase();

    String expectedCssWithoutMapPath = String.format(
        "/%s/%s/output-without-map.css",
        syntax,
        outputStyle
    );
    expectedCssWithoutMapUrl = getClass().getResource(expectedCssWithoutMapPath);

    String expectedCssWithMapPath = String.format(
        "/%s/%s/output-with-map.css",
        syntax,
        outputStyle
    );
    expectedCssWithMapUrl = getClass().getResource(expectedCssWithMapPath);
  }

  /**
   * Clean up temporary directory after test completion.
   */
  @After
  public void tearDown() {
    if (null != targetDir && targetDir.exists()) {
      File[] files = targetDir.listFiles();
      if (null != files) {
        for (File file : files) {
          file.delete();
        }
      }

      targetDir.delete();
    }
  }

  @Test
  public void testWithoutMap() throws Exception {
    File targetCssFile = new File(targetCssWithoutMapFile.toURI());

    try {
      options.setOutputStyle(outputStyle);

      SuccessOutput output =
              compiler.compileFile(sourceFile.toURI(), targetCssFile.toURI(), options);

      assertEquals(output.getCss(), expectedCssWithoutMapUrl);
    } finally {
      targetCssFile.delete();
    }
  }

  @Test
  public void testWithMap() throws Exception {
    File targetCssFile = new File(targetCssWithMapFile.toURI());
    File targetMapFile = new File(targetSourceMapFile.toURI());

    try {
      options.setOutputStyle(outputStyle);
      options.setSourceMapFile(targetMapFile.toURI());

      SuccessOutput output =
              compiler.compileFile(sourceFile.toURI(), targetCssFile.toURI(), options);

      assertEquals(output.getCss(), expectedCssWithMapUrl);
    } finally {
      targetCssFile.delete();
      targetMapFile.delete();
    }
  }

  private void assertEquals(String actual, URL expectedSource) throws IOException {
    String expected = IOUtils.toString(expectedSource);

    Assert.assertEquals(
        String.format("Compile input.%s into %s output format failed", syntax, outputStyle),
        expected,
        actual
    );
  }
}
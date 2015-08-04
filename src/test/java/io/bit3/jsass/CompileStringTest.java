package io.bit3.jsass;

import static io.bit3.jsass.Assert.assertSuccessful;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.io.IOUtils;
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
public class CompileStringTest {

  private String syntax;
  private OutputStyle outputStyle;
  private boolean isIndentedStyle;
  private Compiler compiler;
  private Options options;
  private File sourceFile;
  private String source;
  private File targetCssFile;
  private File targetSourceMapFile;
  private URL expectedCssWithoutMapUrl;
  private URL expectedCssWithMapUrl;

  /**
   * Create test running with specific syntax and output style.
   *
   * @param syntax          The syntax (scss or sass).
   * @param outputStyle     The output style.
   * @param isIndentedStyle Options flag for the data syntax.
   */
  public CompileStringTest(String syntax, OutputStyle outputStyle, boolean isIndentedStyle) {
    this.syntax = syntax;
    this.outputStyle = outputStyle;
    this.isIndentedStyle = isIndentedStyle;
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
            {"scss", OutputStyle.COMPRESSED, false},
            {"scss", OutputStyle.NESTED, false},
            {"sass", OutputStyle.COMPRESSED, true},
            {"sass", OutputStyle.NESTED, true},
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
  public void setUp() throws IOException, URISyntaxException {
    compiler = new Compiler();

    String incPath = String.format("/%s/inc", syntax);
    URL incUrl = getClass().getResource(incPath);

    options = new Options();
    options.setIsIndentedSyntaxSrc(isIndentedStyle);
    options.getIncludePaths().add(new File(incUrl.toURI()));
    options.getFunctionProviders().add(new TestFunctions());
    options.getImporters().add(new TestImporter());

    String sourcePath = String.format("/%s/input.%s", syntax, syntax);
    URL sourceUrl = CompileStringTest.class.getResource(sourcePath);
    sourceFile = new File(sourceUrl.toURI());
    source = IOUtils.toString(sourceUrl);

    String property = "java.io.tmpdir";
    String tempDirPath = System.getProperty(property);
    File tempDir = new File(tempDirPath);

    String targetCssPath = "output.css";
    targetCssFile = new File(tempDir, targetCssPath);

    String targetSourceMapPath = "output-with-map.css.map";
    targetSourceMapFile = new File(tempDir, targetSourceMapPath);

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

  @Test
  public void testWithoutMap() throws Exception {
    options.setOutputStyle(outputStyle);

    Output output = compiler.compileString(
        source,
        sourceFile.toURI(),
        targetCssFile.toURI(),
        options
    );

    assertSuccessful(output, syntax, outputStyle);
    assertEquals(output.getCss(), expectedCssWithoutMapUrl);
    assertNull(output.getSourceMap());
    assertFalse(targetCssFile.exists());
  }

  @Test
  public void testWithMap() throws Exception {
    options.setOutputStyle(outputStyle);
    options.setSourceMapFile(targetSourceMapFile.toURI());

    Output output = compiler.compileString(
        source,
        sourceFile.toURI(),
        targetCssFile.toURI(),
        options
    );

    assertSuccessful(output, syntax, outputStyle);
    assertEquals(output.getCss(), expectedCssWithMapUrl);
    assertNotNull(output.getSourceMap());
    assertFalse(targetCssFile.exists());
    assertFalse(targetSourceMapFile.exists());
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
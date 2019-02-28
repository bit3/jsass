package io.bit3.jsass;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CompileStringTest extends AbstractCompileTest {

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
   */
  public CompileStringTest() {
    super();
  }

  /**
   * Return data for running the test.
   *
   * <p>This data contain a set of syntax (scss or sass) and output style in all combinations.</p>
   */
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
  private void setUp() throws IOException, URISyntaxException {
    compiler = new Compiler();

    String incPath = String.format("/%s/inc", syntax);
    URL incUrl = getClass().getResource(incPath);

    options = new Options();
    options.setIsIndentedSyntaxSrc(isIndentedStyle);
    options.getIncludePaths().add(new File(incUrl.toURI()));
    options.getFunctionProviders().add(testFunctions);
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

  @ParameterizedTest
  @MethodSource("data")
  public void testWithoutMap(String syntax, OutputStyle outputStyle, boolean isIndentedStyle) throws Exception {
    this.syntax = syntax;
    this.outputStyle = outputStyle;
    this.isIndentedStyle = isIndentedStyle;
    this.setUp();

    options.setOutputStyle(outputStyle);

    Output output = compiler.compileString(
        source,
        sourceFile.toURI(),
        targetCssFile.toURI(),
        options
    );

    assertEquals(output.getCss(), expectedCssWithoutMapUrl);
    assertNull(output.getSourceMap());
    assertFalse(targetCssFile.exists());
    assertSpecialFunctions();
  }

  @ParameterizedTest
  @MethodSource("data")
  public void testWithMap(String syntax, OutputStyle outputStyle, boolean isIndentedStyle) throws Exception {
    this.syntax = syntax;
    this.outputStyle = outputStyle;
    this.isIndentedStyle = isIndentedStyle;
    this.setUp();

    options.setOutputStyle(outputStyle);
    options.setSourceMapFile(targetSourceMapFile.toURI());

    Output output = compiler.compileString(
        source,
        sourceFile.toURI(),
        targetCssFile.toURI(),
        options
    );

    assertEquals(output.getCss(), expectedCssWithMapUrl);
    assertNotNull(output.getSourceMap());
    assertFalse(targetCssFile.exists());
    assertFalse(targetSourceMapFile.exists());
    assertSpecialFunctions();
  }
}
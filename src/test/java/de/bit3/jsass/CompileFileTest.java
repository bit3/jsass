package de.bit3.jsass;

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

import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class CompileFileTest {
    private String      syntax;
    private OutputStyle outputStyle;
    private Compiler    compiler;
    private Options     options;
    private File        sourceFile;
    private File        targetDir;
    private File        targetCssWithoutMapFile;
    private File        targetCssWithMapFile;
    private File        targetSourceMapFile;
    private URL         expectedCssWithoutMapUrl;
    private URL         expectedCssWithMapUrl;
    private URL         expectedSourceMapUrl;

    public CompileFileTest(String syntax, OutputStyle outputStyle) {
        this.syntax = syntax;
        this.outputStyle = outputStyle;
    }

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

    @Before
    public void setUp() throws URISyntaxException {
        compiler = new Compiler();

        options = new Options();
        options.getFunctionProviders().add(new TestFunctions());

        String outputStyle = this.outputStyle.toString().toLowerCase();

        String sourcePath = String.format("/%s/input.%s", syntax, syntax);
        URL    sourceUrl  = CompileFileTest.class.getResource(sourcePath);
        sourceFile = new File(sourceUrl.toURI());

        String property    = "java.io.tmpdir";
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

        String expectedCssWithoutMapPath = String.format("/%s/%s/output-without-map.css", syntax, outputStyle);
        expectedCssWithoutMapUrl = CompileFileTest.class.getResource(expectedCssWithoutMapPath);

        String expectedCssWithMapPath = String.format("/%s/%s/output-with-map.css", syntax, outputStyle);
        expectedCssWithMapUrl = CompileFileTest.class.getResource(expectedCssWithMapPath);

        String expectedSourceMapPath = String.format("/%s/%s/output-with-map.css.map", syntax, outputStyle);
        expectedSourceMapUrl = CompileFileTest.class.getResource(expectedSourceMapPath);
    }

    @After
    public void tearDown() {
        if (null != targetDir && targetDir.exists()) {
            for (File file : targetDir.listFiles()) {
                file.delete();
            }

            targetDir.delete();
        }
    }

    @Test
    public void testWithoutMap() throws Exception {
        File targetCssFile = new File(targetCssWithoutMapFile.toURI());

        try {
            options.setOutputStyle(outputStyle);

            Output output = compiler.compileFile(sourceFile, targetCssFile, options);

            assertEquals(output.getCss(), expectedCssWithoutMapUrl);
        } catch (CompilationException exception) {
            fail("Compilation failed: " + exception.getMessage());
        } finally {
            targetCssFile.delete();
        }
    }

    @Test
    public void testWithMap() throws Exception {
        File sourceFile    = new File(this.sourceFile.toURI());
        File targetCssFile = new File(targetCssWithMapFile.toURI());
        File targetMapFile = new File(targetSourceMapFile.toURI());

        try {
            options.setOutputStyle(outputStyle);
            options.setSourceMapFile(targetMapFile);

            Output output = compiler.compileFile(sourceFile, targetCssFile, options);

            assertEquals(output.getCss(), expectedCssWithMapUrl);
            // assertEquals(output.getSourceMap(), expectedSourceMapUrl);
        } catch (CompilationException exception) {
            fail("Compilation failed: " + exception.getMessage());
        } finally {
            targetCssFile.delete();
            targetMapFile.delete();
        }
    }

    private void assertEquals(String actual, URL expectedSource) throws IOException {
        String expected = IOUtils.toString(expectedSource);

        Assert.assertEquals(expected, actual);
    }
}
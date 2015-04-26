package io.bit3.jsass;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.lang.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CompileStringTest {
    private String      syntax;
    private OutputStyle outputStyle;
    private boolean     isIndentedStyle;
    private Compiler compiler;
    private Options     options;
    private File        sourceFile;
    private String      source;
    private File        targetCssFile;
    private File        targetSourceMapFile;
    private URL         expectedCssWithoutMapUrl;
    private URL         expectedCssWithMapUrl;

    public CompileStringTest(String syntax, OutputStyle outputStyle, boolean isIndentedStyle) {
        this.syntax = syntax;
        this.outputStyle = outputStyle;
        this.isIndentedStyle = isIndentedStyle;
    }

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

        String outputStyle = this.outputStyle.toString().toLowerCase();

        String sourcePath = String.format("/%s/input.%s", syntax, syntax);
        URL    sourceUrl  = CompileStringTest.class.getResource(sourcePath);
        sourceFile = new File(sourceUrl.toURI());
        source = IOUtils.toString(sourceUrl);

        String property    = "java.io.tmpdir";
        String tempDirPath = System.getProperty(property);
        File   tempDir     = new File(tempDirPath);

        String targetCssPath = "output.css";
        targetCssFile = new File(tempDir, targetCssPath);

        String targetSourceMapPath = "output-with-map.css.map";
        targetSourceMapFile = new File(tempDir, targetSourceMapPath);

        String expectedCssWithoutMapPath = String.format("/%s/%s/output-without-map.css", syntax, outputStyle);
        expectedCssWithoutMapUrl = getClass().getResource(expectedCssWithoutMapPath);

        String expectedCssWithMapPath = String.format("/%s/%s/output-with-map.css", syntax, outputStyle);
        expectedCssWithMapUrl = getClass().getResource(expectedCssWithMapPath);
    }

    @Test
    public void testWithoutMap() throws Exception {
        try {
            options.setOutputStyle(outputStyle);

            Output output = compiler.compileString(source, sourceFile.toURI(), targetCssFile.toURI(), options);

            assertEquals(output.getCss(), expectedCssWithoutMapUrl);
            assertNull(output.getSourceMap());
            assertFalse(targetCssFile.exists());
        } catch (CompilationException exception) {
            System.out.print("error message: ");
            System.out.println(exception.getMessage());
            fail("Compilation failed: " + exception.getMessage());
        }
    }

    @Test
    public void testWithMap() throws Exception {
        try {
            options.setOutputStyle(outputStyle);
            options.setSourceMapFile(targetSourceMapFile.toURI());

            Output output = compiler.compileString(source, sourceFile.toURI(), targetCssFile.toURI(), options);

            assertEquals(output.getCss(), expectedCssWithMapUrl);
            assertNotNull((Object) output.getSourceMap());
            assertFalse(targetCssFile.exists());
            assertFalse(targetSourceMapFile.exists());
        } catch (CompilationException exception) {
            fail("Compilation failed: " + exception.getMessage());
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
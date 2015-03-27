package de.bit3.jsass;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CompilerTest {
    @Test
    public void testParseScssNestedString() throws Exception {
        /*
        URL sourceUrl   = CompilerTest.class.getResource("/scss/input.scss");
        URL expectedUrl = CompilerTest.class.getResource("/scss/nested/output-with-map.css");

        File inputPath     = new File(sourceUrl.toURI());
        File outputPath    = new File(inputPath.getParentFile(), "output-with-map.css");
        File sourceMapFile = new File(inputPath.getParentFile(), "output-with-map.css.map");

        try {
            Options options = new Options();
            options.setOutputStyle(OutputStyle.NESTED);
            options.setSourceMapFile(sourceMapFile);
            options.setIsIndentedSyntaxSrc(false);

            Compiler compiler = new Compiler();
            Output output = compiler.compileString(IOUtils.toString(sourceUrl), inputPath, outputPath, options);

            String expected = IOUtils.toString(expectedUrl);
            String actual = output.getCss();

            assertEquals(expected, actual);
        } catch (CompilationException exception) {
            fail("Compilation failed: " + exception.getMessage());
        } finally {
            outputPath.delete();
            sourceMapFile.delete();
        }
        */
    }

    @Test
    public void testParseScssNestedFile() throws Exception {
        URL sourceUrl   = CompilerTest.class.getResource("/scss/input.scss");
        URL expectedUrl = CompilerTest.class.getResource("/scss/nested/output-with-map.css");

        File inputPath     = new File(sourceUrl.toURI());
        File outputPath    = new File(inputPath.getParentFile(), "output-with-map.css");
        File sourceMapFile = new File(inputPath.getParentFile(), "output-with-map.css.map");

        try {
            Options options = new Options();
            options.setOutputStyle(OutputStyle.NESTED);
            options.setSourceMapFile(sourceMapFile);

            Compiler compiler = new Compiler();
            Output output = compiler.compileFile(inputPath, outputPath, options);

            String expected = IOUtils.toString(expectedUrl);
            String actual = output.getCss();

            assertEquals(expected, actual);
        } catch (CompilationException exception) {
            fail("Compilation failed: " + exception.getMessage());
        } finally {
            outputPath.delete();
            sourceMapFile.delete();
        }
    }
}
package io.bit3.jsass;

import io.bit3.jsass.importer.Import;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class CleanSourceMapTest {

    private Compiler compiler;
    private Options options;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        compiler = new Compiler();
        options = new Options();
        options.setOutputStyle(OutputStyle.COMPRESSED);
        options.setSourceMapFile(new URI("style.css.map"));
    }

    @Test
    public void test() throws CompilationException, URISyntaxException {
        Importer importer = new Importer();

        options.setSourceMapFile(new URI("/output.css.map"));
        options.setSourceMapContents(true);
        options.getImporters().add(importer);

        Output output = compiler.compileString(
            "foo { bar: func(); } @import 'import';",
            new URI("/input.scss"),
            new URI("/output.css"),
            options
        );

        final String sourceMap = output.getSourceMap();

        assertFalse(sourceMap.contains("$jsass"));
        assertFalse(sourceMap.contains("JSASS_CUSTOM.scss"));
        assertFalse(sourceMap.contains("JSASS_PRE_IMPORT.scss"));
        assertFalse(sourceMap.contains("JSASS_POST_IMPORT.scss"));
    }


    public static class Importer implements io.bit3.jsass.importer.Importer {

        protected List<String> importPaths = new LinkedList<>();

        @Override
        public Collection<Import> apply(
            String url, Import previous
        ) {
            this.importPaths.add(previous.getAbsoluteUri().toString());

            List<Import> imports = new LinkedList<>();

            if ("import".equals(url)) {
                try {
                    imports.add(
                        new Import(
                            new URI("import"),
                            new URI("/foo/import.scss"),
                            "foo { bar: func(); } @import 'imported'; bar { foo: func(); }"
                        )
                    );
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            if ("imported".equals(url)) {
                try {
                    imports.add(
                        new Import(
                            new URI("imported"),
                            new URI("/bar/imported.scss"),
                            "foo { bar: func(); }"
                        )
                    );
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            return imports;
        }
    }
}
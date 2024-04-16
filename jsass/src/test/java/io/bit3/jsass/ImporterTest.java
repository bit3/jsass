package io.bit3.jsass;

import io.bit3.jsass.importer.ImporterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ImporterTest {

  private JsassCompiler compiler;
  private Options options;

  /**
   * Set up the compiler and the compiler options for each run.
   *
   * @throws URISyntaxException Throws if the resource URI is invalid.
   */
  @BeforeEach
  public void setUp() throws IOException, URISyntaxException {
    compiler = new JsassCompiler();
    options = new Options();
  }

  @Test
  public void testCall() throws Exception {
    Functions functions = new Functions();
    Importer importer = new Importer();

    options.setSourceMapFile(new URI("/output.css.map"));
    options.getFunctionProviders().add(functions);
    options.getImporters().add(importer);

    Output output = compiler.compileString(
        "foo { bar: func(); } @import 'import';",
        new URI("/input.scss"),
        new URI("/output.css"),
        options
    );

    assertFalse(functions.calls == 0);
    assertFalse(importer.importPaths.isEmpty());

    assertArrayEquals(
        new String[]{
            "/input.scss",
            "/foo/import.scss"
        },
        importer.importPaths.toArray()
    );
  }

  public static class Functions {

    protected int calls = 0;

    public String func() {
      calls++;
      return "World";
    }
  }

  public static class Importer implements io.bit3.jsass.importer.Importer {

    protected List<String> importPaths = new LinkedList<>();

    @Override
    public Collection<ImporterResult> apply(
        String url, ImporterResult previous
    ) {
      this.importPaths.add(previous.getAbsoluteUri().toString());

      List<ImporterResult> imports = new LinkedList<>();

      if ("import".equals(url)) {
        try {
          imports.add(
              new ImporterResult(
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
              new ImporterResult(
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

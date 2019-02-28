package io.bit3.jsass;

import io.bit3.jsass.importer.Import;
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

  private Compiler compiler;
  private Options options;

  /**
   * Set up the compiler and the compiler options for each run.
   *
   * @throws URISyntaxException Throws if the resource URI is invalid.
   */
  @BeforeEach
  public void setUp() throws IOException, URISyntaxException {
    compiler = new Compiler();
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

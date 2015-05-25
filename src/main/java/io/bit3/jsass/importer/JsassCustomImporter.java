package io.bit3.jsass.importer;

import io.bit3.jsass.Compiler;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.type.SassString;
import sass.SassLibrary;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JsassCustomImporter implements Importer {

  private final SassLibrary sass;
  private final ImportStack importStack;

  public JsassCustomImporter(SassLibrary sass, ImportStack importStack) {
    this.sass = sass;
    this.importStack = importStack;
  }

  @Override
  public Collection<Import> apply(
      String url, Import previous, Context originalContext
  ) {
    List<Import> list = new LinkedList<>();

    list.add(createCustomHeaderImport(previous));

    return list;
  }

  private Import createCustomHeaderImport(Import previous) {
    int id = importStack.register(previous);

    StringBuilder source = new StringBuilder();

    // $jsass-void: jsass_import_stack_push(<id>) !global;
    source.append(
        String.format(
            "$jsass-void: jsass_import_stack_push(%d) !global;\n",
            id
        )
    );

    try {
      return new Import(
          new URI(previous.getUri() + "/JSASS_CUSTOM.scss"),
          new URI(""),
          source.toString()
      );
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}

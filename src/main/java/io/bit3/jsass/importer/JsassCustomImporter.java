package io.bit3.jsass.importer;

import io.bit3.jsass.Compiler;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.type.SassString;
import sass.SassLibrary;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class JsassCustomImporter implements Importer {

  private final SassLibrary sass;

  public JsassCustomImporter(SassLibrary sass) {
    this.sass = sass;
  }

  @Override
  public Collection<Import> apply(
      String url, Import previous, Context originalContext
  ) {
    List<Import> list = new LinkedList<>();

    list.add(createCustomHeaderImport(url));

    return list;
  }

  private Import createCustomHeaderImport(String url) {
    String quotedUrl = sass.sass_string_quote(
        url,
        (byte) SassString.DEFAULT_QUOTE_CHARACTER
    ).getString(0);

    StringBuilder source = new StringBuilder();

    // $jsass-void: jsass_import_stack_push("<path>", "") !global;
    source.append(
        String.format(
            "$jsass-void: jsass_import_stack_push(%s, \"\") !global;\n",
            quotedUrl
        )
    );

    try {
      return new Import(
          new URI("/JSASS_CUSTOM.scss"),
          new URI(""),
          source.toString()
      );
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}

package io.bit3.jsass;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;

public class TestImporter implements Importer {

  @Override
  public Collection<Import> apply(String url, String previous, Context originalContext) {
    if ("functions".equals(url)) {
      return new LinkedList<>();
    }

    if ("include".equals(url)) {
      // Importers does not support SASS syntax, so we enforce scss here
      // String syntax = previous.substring(previous.length() - 4);
      String syntax = "scss";
      String resourcePath = String.format("/%s/src/include.%s", syntax, syntax);
      URL resource = getClass().getResource(resourcePath);

      String basePath = String.format("/%s/src", syntax);
      URL base = getClass().getResource(basePath);

      try {
        String contents = IOUtils.toString(resource);

        Import importSource = new Import(
            new URI(String.format("include.%s", syntax)),
            base.toURI(),
            contents
        );

        Collection<Import> list = new LinkedList<>();
        list.add(importSource);
        return list;
      } catch (IOException | URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

    return null;
  }
}

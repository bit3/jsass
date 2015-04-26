package io.bit3.jsass.importer;

import io.bit3.jsass.context.Context;

import java.util.Collection;

public class ChainImporter implements Importer {

  private final Collection<Importer> importers;

  public ChainImporter(Collection<Importer> importers) {
    this.importers = importers;
  }

  @Override
  public Collection<Import> apply(String url, String previous, Context originalContext) {
    Collection<Import> imports;

    for (Importer importer : importers) {
      imports = importer.apply(url, previous, originalContext);

      if (null != imports) {
        return imports;
      }
    }

    return null;
  }
}

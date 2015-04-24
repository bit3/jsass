package de.bit3.jsass.importer;

import de.bit3.jsass.context.Context;

import java.util.Collection;

public interface Importer {

  Collection<Import> apply(String url, String previous, Context originalContext);
}

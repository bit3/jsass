package io.bit3.jsass.importer;

import java.util.Collection;

@FunctionalInterface
public interface Importer {
  Collection<Import> apply(String url, Import previous);
}

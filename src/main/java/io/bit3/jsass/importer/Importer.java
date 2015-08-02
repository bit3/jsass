package io.bit3.jsass.importer;

import java.util.Collection;

public interface Importer {
  Collection<Import> apply(String url, Import previous);
}

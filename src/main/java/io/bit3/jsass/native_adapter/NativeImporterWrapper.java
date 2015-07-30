package io.bit3.jsass.native_adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import io.bit3.jsass.importer.Import;
import io.bit3.jsass.importer.Importer;

class NativeImporterWrapper {
  private final Importer importer;

  public NativeImporterWrapper(Importer importer) {
    this.importer = importer;
  }

  public Collection<NativeImport> apply(String url, NativeImport previousNative) {
    try {
      Import previous = new Import(previousNative.uri, previousNative.base, previousNative.contents, previousNative.sourceMap);
      Collection<Import> imports = this.importer.apply(url, previous);

      return imports
          .stream()
          .map(NativeImport::new)
          .collect(Collectors.toList());
    } catch (Throwable e) {
      return Collections.emptyList();
    }
  }
}

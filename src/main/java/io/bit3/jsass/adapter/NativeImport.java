package io.bit3.jsass.adapter;

import io.bit3.jsass.importer.Import;

import java.net.URI;

class NativeImport {

  public final String uri;

  public final String base;

  public final String contents;

  public final String sourceMap;

  public NativeImport(final Import sassImport) {
    final URI uri = sassImport.getUri();
    final URI base = sassImport.getBase();
    final String contents = sassImport.getContents();
    final String sourceMap = sassImport.getSourceMap();

    String uriString = null == uri ? "" : uri.toString();
    if (uriString.startsWith("file:")) {
      uriString = uriString.substring(5);
    }

    String baseString = null == base ? "" : base.toString();
    if (baseString.startsWith("file:")) {
      baseString = baseString.substring(5);
    }

    this.uri = uriString;
    this.base = baseString;
    this.contents = null == contents ? "" : contents;
    this.sourceMap = null == sourceMap ? "" : sourceMap;
  }

  public NativeImport(String uri, String base, String contents, String sourceMap) {
    this.uri = uri;
    this.base = base;
    this.contents = contents;
    this.sourceMap = sourceMap;
  }
}

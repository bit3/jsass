package io.bit3.jsass.adapter;

import io.bit3.jsass.importer.Import;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

class NativeImport {

  public final String importPath;

  public final String absolutePath;

  public final String contents;

  public final String sourceMap;

  public final String errorMessage;

  public NativeImport(final Import sassImport) {
    final URI uri = sassImport.getImportUri();
    final URI base = sassImport.getAbsoluteUri();
    final String contents = sassImport.getContents();
    final String sourceMap = sassImport.getSourceMap();

    String uriString = "";
    if (null != uri) {
      if ("file".equals(uri.getScheme())) {
        uriString = new File(uri).getAbsolutePath();
      } else {
        uriString = uri.toString();
      }
    }

    String baseString = "";
    if (null != base) {
      if ("file".equals(base.getScheme())) {
        baseString = new File(base).getAbsolutePath();
      } else {
        baseString = base.toString();
      }
    }

    this.importPath = uriString;
    this.absolutePath = baseString;
    this.contents = null == contents ? "" : contents;
    this.sourceMap = null == sourceMap ? "" : sourceMap;
    this.errorMessage = "";
  }

  public NativeImport(String importPath, String absolutePath, String contents, String sourceMap) {
    this.importPath = importPath;
    this.absolutePath = absolutePath;
    this.contents = contents;
    this.sourceMap = sourceMap;
    this.errorMessage = "";
  }

  public NativeImport(Throwable throwable) {
    importPath = "";
    absolutePath = "";
    contents = "";
    sourceMap = "";

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);

    String message = throwable.getMessage();
    if (StringUtils.isNotEmpty(message)) {
      printWriter.append(message).append(System.lineSeparator());
    }
    throwable.printStackTrace(printWriter);

    errorMessage = stringWriter.toString();
  }
}

package io.bit3.jsass.adapter;

import io.bit3.jsass.importer.Import;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Objects;

class NativeImport {

  public final String importPath;

  public final String absolutePath;

  public final String contents;

  public final String sourceMap;

  public final String errorMessage;

  public NativeImport(final Import sassImport) {
    final URI importUri = sassImport.getImportUri();
    final URI absoluteUri = sassImport.getAbsoluteUri();

    String uriString = "";
    if (null != importUri) {
      if ("file".equals(importUri.getScheme())) {
        uriString = new File(importUri).getAbsolutePath();
      } else {
        uriString = importUri.toString();
      }
    }

    String absoluteString = "";
    if (null != absoluteUri) {
      if ("file".equals(absoluteUri.getScheme())) {
        absoluteString = new File(absoluteUri).getAbsolutePath();
      } else {
        absoluteString = absoluteUri.toString();
      }
    }

    this.importPath = uriString;
    this.absolutePath = absoluteString;
    this.contents = Objects.toString(sassImport.getContents(), "");
    this.sourceMap = Objects.toString(sassImport.getSourceMap(), "");
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

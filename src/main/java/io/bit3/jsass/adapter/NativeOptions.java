package io.bit3.jsass.adapter;

import io.bit3.jsass.function.FunctionWrapper;

class NativeOptions {
  public final FunctionWrapper[] functions;

  public final NativeImporterWrapper[] headerImporters;

  public final NativeImporterWrapper[] importers;

  public final String includePath;

  public final String indent;

  public final boolean isIndentedSyntaxSrc;

  public final String linefeed;

  public final boolean omitSourceMapUrl;

  public final int outputStyle;

  public final String pluginPath;

  public final int precision;

  public final boolean sourceComments;

  public final boolean sourceMapContents;

  public final boolean sourceMapEmbed;

  public final String sourceMapFile;

  public final String sourceMapRoot;

  NativeOptions(
      FunctionWrapper[] functions,
      NativeImporterWrapper[] headerImporters,
      NativeImporterWrapper[] importers,
      String includePath,
      String indent,
      boolean isIndentedSyntaxSrc,
      String linefeed,
      boolean omitSourceMapUrl,
      int outputStyle,
      String pluginPath,
      int precision,
      boolean sourceComments,
      boolean sourceMapContents,
      boolean sourceMapEmbed,
      String sourceMapFile,
      String sourceMapRoot
  ) {
    this.functions = functions;
    this.headerImporters = headerImporters;
    this.importers = importers;
    this.includePath = includePath;
    this.indent = indent;
    this.isIndentedSyntaxSrc = isIndentedSyntaxSrc;
    this.linefeed = linefeed;
    this.omitSourceMapUrl = omitSourceMapUrl;
    this.outputStyle = outputStyle;
    this.pluginPath = pluginPath;
    this.precision = precision;
    this.sourceComments = sourceComments;
    this.sourceMapContents = sourceMapContents;
    this.sourceMapEmbed = sourceMapEmbed;
    this.sourceMapFile = sourceMapFile;
    this.sourceMapRoot = sourceMapRoot;
  }
}

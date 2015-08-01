package io.bit3.jsass.adapter;

import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.FileContext;
import io.bit3.jsass.context.StringContext;
import io.bit3.jsass.function.FunctionWrapper;
import io.bit3.jsass.function.FunctionWrapperFactory;
import io.bit3.jsass.importer.Importer;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter to native functions.
 */
public class NativeAdapter {
  static {
    NativeLoader.loadLibrary();
  }

  private final FunctionWrapperFactory functionWrapperFactory;

  public NativeAdapter(FunctionWrapperFactory functionWrapperFactory) {
    this.functionWrapperFactory = functionWrapperFactory;
  }

  /**
   * Compile a file context.
   *
   * @return The compiled result.
   */
  public Output compile(FileContext context) {
    NativeFileContext nativeContext = convertToNativeContext(context);
    return compileFile(nativeContext);
  }

  /**
   * Compile a string context.
   *
   * @return The compiled result.
   */
  public Output compile(StringContext context) {
    NativeStringContext nativeContext = convertToNativeContext(context);
    return compileString(nativeContext);
  }

  private NativeFileContext convertToNativeContext(FileContext context) {
    String inputPath = convertToString(context.getInputPath());
    String outputPath = convertToString(context.getOutputPath());
    NativeOptions options = convertToNativeOptions(context);

    return new NativeFileContext(inputPath, outputPath, options);
  }

  private NativeStringContext convertToNativeContext(StringContext context) {
    String source = context.getString();
    String inputPath = convertToString(context.getInputPath());
    String outputPath = convertToString(context.getOutputPath());
    NativeOptions options = convertToNativeOptions(context);

    return new NativeStringContext(source, inputPath, outputPath, options);
  }

  private String convertToString(URI uri) {
    if (null == uri) {
      return "";
    }

    String string = uri.toString();

    if (string.startsWith("file:")) {
      string = string.substring(5);
    }

    return string;
  }

  private NativeOptions convertToNativeOptions(Context context) {
    Options options = context.getOptions();

    List<FunctionWrapper> functionWrappersList = functionWrapperFactory
        .compileFunctions(context, options.getFunctionProviders());
    FunctionWrapper[] functionWrappers = functionWrappersList
        .toArray(new FunctionWrapper[functionWrappersList.size()]);

    List<Importer> headerImportersList = options.getHeaderImporters();
    NativeImporterWrapper[] headerImporters = headerImportersList
        .stream()
        .map(NativeImporterWrapper::new)
        .collect(Collectors.toList())
        .toArray(new NativeImporterWrapper[headerImportersList.size()]);

    Collection<Importer> importersList = options.getImporters();
    NativeImporterWrapper[] importers = importersList
        .stream()
        .map(NativeImporterWrapper::new)
        .collect(Collectors.toList())
        .toArray(new NativeImporterWrapper[importersList.size()]);

    List<File> includePathsList = options.getIncludePaths();
    String includePath = includePathsList
        .stream()
        .map(File::getAbsolutePath)
        .collect(Collectors.joining(File.separator));

    String indent = options.getIndent();

    boolean isIndentedSyntaxSrc = options.isIndentedSyntaxSrc();

    String linefeed = options.getLinefeed();
    boolean omitSourceMapUrl = options.isOmitSourceMapUrl();
    int outputStyle = options.getOutputStyle().numeric;
    String pluginPath = options.getPluginPath();
    int precision = options.getPrecision();
    boolean sourceComments = options.isSourceComments();
    boolean sourceMapContents = options.isSourceMapContents();
    boolean sourceMapEmbed = options.isSourceMapEmbed();
    String sourceMapFile = convertToString(options.getSourceMapFile());
    String sourceMapRoot = convertToString(options.getSourceMapRoot());

    return new NativeOptions(
        functionWrappers,
        headerImporters,
        importers,
        includePath,
        indent,
        isIndentedSyntaxSrc,
        linefeed,
        omitSourceMapUrl,
        outputStyle,
        pluginPath,
        precision,
        sourceComments,
        sourceMapContents,
        sourceMapEmbed,
        sourceMapFile,
        sourceMapRoot
    );
  }

  /**
   * Native call.
   */
  private native Output compileFile(NativeFileContext context);

  /**
   * Native call.
   */
  private native Output compileString(NativeStringContext context);
}

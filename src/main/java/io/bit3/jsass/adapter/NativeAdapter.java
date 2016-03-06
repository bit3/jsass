package io.bit3.jsass.adapter;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.FileContext;
import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.context.StringContext;
import io.bit3.jsass.function.FunctionWrapper;
import io.bit3.jsass.function.FunctionWrapperFactory;
import io.bit3.jsass.function.JsassCustomFunctions;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.importer.JsassCustomHeaderImporter;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  public Output compile(FileContext context, ImportStack importStack) throws CompilationException {
    NativeFileContext nativeContext = convertToNativeContext(context, importStack);
    return checkForError(compileFile(nativeContext));
  }

  /**
   * Compile a string context.
   *
   * @return The compiled result.
   */
  public Output compile(StringContext context, ImportStack importStack)
          throws CompilationException {
    NativeStringContext nativeContext = convertToNativeContext(context, importStack);
    return checkForError(compileString(nativeContext));
  }

  /**
   * Checks if the given {@link Output} contains an error.
   *
   * @return The given {@link Output}
   * @throws CompilationException If an error was found in the given {@link Output}
   */
  private static Output checkForError(Output output) throws CompilationException {
    if (output.getErrorStatus() != 0) {
      throw new CompilationException(output);
    }
    return output;
  }

  private NativeFileContext convertToNativeContext(
      FileContext context, ImportStack importStack
  ) {
    String inputPath = convertToString(context.getInputPath());
    String outputPath = convertToString(context.getOutputPath());
    NativeOptions options = convertToNativeOptions(context, importStack);

    return new NativeFileContext(inputPath, outputPath, options);
  }

  private NativeStringContext convertToNativeContext(
      StringContext context, ImportStack importStack
  ) {
    String source = context.getString();
    String inputPath = convertToString(context.getInputPath());
    String outputPath = convertToString(context.getOutputPath());
    NativeOptions options = convertToNativeOptions(context, importStack);

    return new NativeStringContext(source, inputPath, outputPath, options);
  }

  private static String convertToString(URI uri) {
    if (null == uri) {
      return "";
    }

    String string = uri.toString();

    if ("file".equals(uri.getScheme())) {
      string = new File(uri).getAbsolutePath();
    }

    return string;
  }

  private NativeOptions convertToNativeOptions(Context context, ImportStack importStack) {
    Options options = context.getOptions();

    FunctionWrapper[] functionWrappers = getFunctionWrappers(context, importStack, options);
    NativeImporterWrapper[] headerImporters = getHeaderImporters(importStack, options);
    Collection<Importer> importersList = options.getImporters();
    NativeImporterWrapper[] importers = importersList
        .stream()
        .map(i -> new NativeImporterWrapper(importStack, i))
        .collect(Collectors.toList())
        .toArray(new NativeImporterWrapper[importersList.size()]);

    List<File> includePathsList = options.getIncludePaths();
    String includePath = includePathsList
        .stream()
        .map(File::getAbsolutePath)
        .collect(Collectors.joining(File.pathSeparator));

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

  private FunctionWrapper[] getFunctionWrappers(Context context, ImportStack importStack, Options options) {
    List<FunctionWrapper> functionWrappersList;
    try (
        Stream<FunctionWrapper> functionWrappersStream = Stream
            .concat(
                functionWrapperFactory
                    .compileFunctions(importStack, context, new JsassCustomFunctions(importStack))
                    .stream(),
                functionWrapperFactory
                    .compileFunctions(importStack, context, options.getFunctionProviders())
                    .stream()
            );
    ) {
      functionWrappersList = functionWrappersStream
          .collect(Collectors.toList());
    }
    return functionWrappersList
        .toArray(new FunctionWrapper[functionWrappersList.size()]);
  }

  private NativeImporterWrapper[] getHeaderImporters(ImportStack importStack, Options options) {
    List<Importer> headerImportersList = options.getHeaderImporters();
    NativeImporterWrapper[] headerImporters;
    try (
      Stream<NativeImporterWrapper> headerImportersStream = Stream
          .concat(
              Stream.of(new JsassCustomHeaderImporter(importStack)),
              headerImportersList.stream()
          )
          .map(i -> new NativeImporterWrapper(importStack, i));
    ) {
      headerImporters = headerImportersStream
          .collect(Collectors.toList())
          .toArray(new NativeImporterWrapper[headerImportersList.size()]);
    }
    return headerImporters;
  }

  /**
   * Native call.
   */
  private static native Output compileFile(NativeFileContext context);

  /**
   * Native call.
   */
  private static native Output compileString(NativeStringContext context);
}

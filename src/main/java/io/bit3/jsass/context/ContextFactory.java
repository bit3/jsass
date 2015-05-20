package io.bit3.jsass.context;

import com.sun.jna.Memory;
import io.bit3.jsass.Options;
import io.bit3.jsass.OutputStyle;
import io.bit3.jsass.function.FunctionCallbackFactory;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.importer.ImporterCallbackFactory;
import sass.SassLibrary;
import sass.SassLibrary.Sass_Data_Context;
import sass.SassLibrary.Sass_File_Context;
import sass.SassLibrary.Sass_Options;

import java.io.File;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

/**
 * Factory to create libsass contexts from jsass contexts.
 */
public class ContextFactory {

  /**
   * The libsass adapter instance.
   */
  private final SassLibrary sass;

  /**
   * Create a new factory with specific libsass adapter instance.
   *
   * @param sass The libsass adapter instance.
   */
  public ContextFactory(SassLibrary sass) {
    this.sass = sass;
  }

  /**
   * Create a libsass file context.
   *
   * @param context The jsass file context.
   * @return The newly created libsass file context.
   */
  public Sass_File_Context create(FileContext context) {
    URI inputPath = context.getInputPath();

    // create context
    Sass_File_Context fileContext = sass.sass_make_file_context(inputPath.toString());

    // configure context
    Sass_Options libsassOptions = sass.sass_file_context_get_options(fileContext);
    configure(context, libsassOptions);

    return fileContext;
  }

  /**
   * Create a libsass data context.
   *
   * @param context The jsass string context.
   * @return The newly created libsass data context.
   */
  public Sass_Data_Context create(StringContext context) {
    String string = context.getString();
    Charset charset = context.getCharset();

    byte[] bytes = string.getBytes(charset);
    Memory memory = new Memory(bytes.length + 1);
    memory.write(0, bytes, 0, bytes.length);
    memory.setByte(bytes.length, (byte) 0);

    ByteBuffer buffer = memory.getByteBuffer(0, memory.size());

    // create context
    Sass_Data_Context dataContext = sass.sass_make_data_context(buffer);

    // configure context
    Sass_Options libsassOptions = sass.sass_data_context_get_options(dataContext);
    configure(context, libsassOptions);

    return dataContext;
  }

  /**
   * Configure a libsass context with jsass options.
   *
   * @param context        The compilation context.
   * @param libsassOptions The libsass options.
   */
  private void configure(Context context, Sass_Options libsassOptions) {
    final Options javaOptions = context.getOptions();

    // Note: support for local file URIs
    // When compiling in a data context, using protocol paths is absolutely valid,
    // but not for local files! That's why we remove the leading file: from local URIs.

    final SassLibrary.Sass_Function_List functions = createFunctions(
        context,
        javaOptions.getFunctionProviders()
    );
    sass.sass_option_set_c_functions(libsassOptions, functions);

    final SassLibrary.Sass_Importer_List headers = createImporters(
        context,
        javaOptions.getHeaderImporters()
    );
    sass.sass_option_set_c_headers(libsassOptions, headers);

    final SassLibrary.Sass_Importer_List importers = createImporters(
            context,
            javaOptions.getImporters()
    );
    sass.sass_option_set_c_importers(libsassOptions, importers);

    final String includePaths = joinFilePaths(javaOptions.getIncludePaths());
    sass.sass_option_set_include_path(libsassOptions, includePaths);

    final String indent = javaOptions.getIndent();
    sass.sass_option_set_indent(libsassOptions, indent);

    final URI inputPath = context.getInputPath();
    String inputPathString = null == inputPath ? "" : inputPath.toString();
    inputPathString = inputPathString.replaceFirst("^file:", "");
    sass.sass_option_set_input_path(libsassOptions, inputPathString);

    final byte   isIndentedSyntaxSrc = createBooleanByte(javaOptions.isIndentedSyntaxSrc());
    sass.sass_option_set_is_indented_syntax_src(libsassOptions, isIndentedSyntaxSrc);

    final String linefeed = javaOptions.getLinefeed();
    sass.sass_option_set_linefeed(libsassOptions, linefeed);

    final byte omitSourceMapUrl = createBooleanByte(javaOptions.isOmitSourceMapUrl());
    sass.sass_option_set_omit_source_map_url(libsassOptions, omitSourceMapUrl);

    final URI outputPath = context.getOutputPath();
    String outputPathString = null == outputPath ? "" : outputPath.toString();
    outputPathString = outputPathString.replaceFirst("^file:", "");
    sass.sass_option_set_output_path(libsassOptions, outputPathString);

    final int outputStyle = mapOutputStyle(javaOptions.getOutputStyle());
    sass.sass_option_set_output_style(libsassOptions, outputStyle);

    String pluginPath = javaOptions.getPluginPath();
    sass.sass_option_set_plugin_path(libsassOptions, pluginPath);

    final int  precision      = javaOptions.getPrecision();
    sass.sass_option_set_precision(libsassOptions, precision);

    final byte sourceComments = createBooleanByte(javaOptions.isSourceComments());
    sass.sass_option_set_source_comments(libsassOptions, sourceComments);

    final byte sourceMapContents = createBooleanByte(javaOptions.isSourceMapContents());
    sass.sass_option_set_source_map_contents(libsassOptions, sourceMapContents);

    final byte sourceMapEmbed = createBooleanByte(javaOptions.isSourceMapEmbed());
    sass.sass_option_set_source_map_embed(libsassOptions, sourceMapEmbed);

    URI sourceMapFileUri = javaOptions.getSourceMapFile();
    String sourceMapFile = null == sourceMapFileUri ? "" : sourceMapFileUri.toString();
    sourceMapFile = sourceMapFile.replaceFirst("^file:", "");
    sass.sass_option_set_source_map_file(libsassOptions, sourceMapFile);

    URI sourceMapRootUri = javaOptions.getSourceMapRoot();
    String sourceMapRoot = null == sourceMapRootUri ? "" : sourceMapRootUri.toString();
    sourceMapRoot = sourceMapRoot.replaceFirst("^file:", "");
    sass.sass_option_set_source_map_root(libsassOptions, sourceMapRoot);
  }

  /**
   * Map java output style to native libsass output style.
   *
   * @param outputStyle The java output style.
   * @return The native libsass output style.
   */
  private int mapOutputStyle(OutputStyle outputStyle) {
    int result;
    switch (outputStyle) {
      case NESTED:
        result = SassLibrary.Sass_Output_Style.SASS_STYLE_NESTED;
        break;
      case EXPANDED:
        result = SassLibrary.Sass_Output_Style.SASS_STYLE_EXPANDED;
        break;
      case COMPACT:
        result = SassLibrary.Sass_Output_Style.SASS_STYLE_COMPACT;
        break;
      case COMPRESSED:
        result = SassLibrary.Sass_Output_Style.SASS_STYLE_COMPRESSED;
        break;
      default:
        throw new IllegalArgumentException(
            String.format(
                "The given output style \"%s\" cannot be converted to libsass!",
                (null == outputStyle ? "null" : outputStyle.toString())
            )
        );
    }
    return result;
  }

  /**
   * Create a string pointer from a file list, using the absolute paths and OS dependent separator
   * char.
   *
   * @param list The file list.
   * @return Pointer to list of absolute paths.
   */
  private String joinFilePaths(List<File> list) {
    if (null == list || list.isEmpty()) {
      return "";
    }

    String separator = File.pathSeparator;
    StringBuilder string = new StringBuilder();

    for (File file : list) {
      string.append(separator).append(file.getAbsolutePath());
    }

    return string.substring(1);
  }

  /**
   * Create a libsass function list, from a list of java objects.
   *
   * @param functionProviders A list of java objects.
   * @return The newly created libsass function list.
   */
  private SassLibrary.Sass_Function_List createFunctions(Context originalContext,
                                                         List<?> functionProviders) {
    FunctionCallbackFactory functionCallbackFactory = new FunctionCallbackFactory(sass);

    List<SassLibrary.Sass_Function_Entry> callbacks = functionCallbackFactory.compileFunctions(
        originalContext,
        functionProviders
    );

    return functionCallbackFactory.toSassCFunctionList(callbacks);
  }

  /**
   * Create a libsass import callback.
   *
   * @param originalContext The original context, that get compiled.
   * @param importers       A collection of importers.
   * @return The newly created libsass import callback.
   */
  private SassLibrary.Sass_Importer_List createImporters(Context originalContext,
                                                         Collection<Importer> importers) {
    if (importers.isEmpty()) {
      return null;
    }

    ImporterCallbackFactory importerCallbackFactory = new ImporterCallbackFactory(sass);

    return importerCallbackFactory.create(originalContext, importers);
  }

  /**
   * Create native byte boolean.
   *
   * @param bool The boolean value.
   * @return <em>1</em> for <em>true</em> input, otherwise <em>0</em>.
   */
  private byte createBooleanByte(boolean bool) {
    return bool ? (byte) 1 : 0;
  }
}

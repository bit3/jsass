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
    File inputPath = context.getInputPath();

    // create context
    Sass_File_Context
        fileContext =
        sass.sass_make_file_context(inputPath.getAbsolutePath());

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
    File inputPath = context.getInputPath();
    File outputPath = context.getOutputPath();
    Options javaOptions = context.getOptions();

    int precision = javaOptions.getPrecision();
    int outputStyle = mapOutputStyle(javaOptions.getOutputStyle());
    byte sourceComments = createBooleanByte(javaOptions.isSourceComments());
    byte sourceMapEmbed = createBooleanByte(javaOptions.isSourceMapEmbed());
    byte sourceMapContents = createBooleanByte(javaOptions.isSourceMapContents());
    byte omitSourceMapUrl = createBooleanByte(javaOptions.isOmitSourceMapUrl());
    byte isIndentedSyntaxSrc = createBooleanByte(javaOptions.isIndentedSyntaxSrc());
    String inputPathString = null == inputPath ? "" : inputPath.getAbsolutePath();
    String outputPathString = null == outputPath ? "" : outputPath.getAbsolutePath();
    String imagePath = javaOptions.getImageUrl();
    String includePaths = joinFilePaths(javaOptions.getIncludePaths());
    String sourceMapFile = null == javaOptions.getSourceMapFile()
                           ? ""
                           : javaOptions.getSourceMapFile().getAbsolutePath();
    SassLibrary.Sass_C_Function_List
        functions =
        createFunctions(javaOptions.getFunctionProviders());
    SassLibrary.Sass_C_Import_Callback
        importer =
        createImporter(context, javaOptions.getImporters());

    sass.sass_option_set_precision(libsassOptions, precision);
    sass.sass_option_set_output_style(libsassOptions, outputStyle);
    sass.sass_option_set_source_comments(libsassOptions, sourceComments);
    sass.sass_option_set_source_map_embed(libsassOptions, sourceMapEmbed);
    sass.sass_option_set_source_map_contents(libsassOptions, sourceMapContents);
    sass.sass_option_set_omit_source_map_url(libsassOptions, omitSourceMapUrl);
    sass.sass_option_set_is_indented_syntax_src(libsassOptions, isIndentedSyntaxSrc);
    sass.sass_option_set_input_path(libsassOptions, inputPathString);
    sass.sass_option_set_output_path(libsassOptions, outputPathString);
    sass.sass_option_set_image_path(libsassOptions, imagePath);
    sass.sass_option_set_include_path(libsassOptions, includePaths);
    sass.sass_option_set_source_map_file(libsassOptions, sourceMapFile);
    sass.sass_option_set_c_functions(libsassOptions, functions);
    sass.sass_option_set_importer(libsassOptions, importer);
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
  private SassLibrary.Sass_C_Function_List createFunctions(List<?> functionProviders) {
    FunctionCallbackFactory functionCallbackFactory = new FunctionCallbackFactory(sass);

    List<SassLibrary.Sass_C_Function_Callback>
        callbacks =
        functionCallbackFactory.compileFunctions(functionProviders);

    return functionCallbackFactory.toSassCFunctionList(callbacks);
  }

  /**
   * Create a libsass import callback.
   *
   * @param originalContext The original context, that get compiled.
   * @param importers       A collection of importers.
   * @return The newly created libsass import callback.
   */
  private SassLibrary.Sass_C_Import_Callback createImporter(Context originalContext,
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

package de.bit3.jsass.context;

import com.sun.jna.Memory;
import de.bit3.jsass.importer.Importer;
import de.bit3.jsass.Options;
import de.bit3.jsass.OutputStyle;
import de.bit3.jsass.function.FunctionCallbackFactory;
import de.bit3.jsass.importer.ImporterCallbackFactory;
import sass.SassLibrary;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

public class ContextFactory {
    private final SassLibrary SASS;

    public ContextFactory(SassLibrary SASS) {
        this.SASS = SASS;
    }

    public SassLibrary.Sass_File_Context create(FileContext context) {
        File    inputPath  = context.getInputPath();
        File    outputPath = context.getOutputPath();
        Options options    = context.getOptions();

        // create context
        SassLibrary.Sass_File_Context fileContext = SASS.sass_make_file_context(inputPath.getAbsolutePath());

        // configure context
        SassLibrary.Sass_Options libsassOptions = SASS.sass_file_context_get_options(fileContext);
        configure(context, inputPath, outputPath, libsassOptions, options);

        return fileContext;
    }

    public SassLibrary.Sass_Data_Context create(StringContext context) {
        String  string     = context.getString();
        Charset charset    = context.getCharset();
        File    inputPath  = context.getInputPath();
        File    outputPath = context.getOutputPath();
        Options options    = context.getOptions();

        byte[]  bytes  = string.getBytes(charset);
        Memory memory = new Memory(bytes.length + 1);
        memory.write(0, bytes, 0, bytes.length);
        memory.setByte(bytes.length, (byte) 0);

        ByteBuffer buffer = memory.getByteBuffer(0, memory.size());

        // create context
        SassLibrary.Sass_Data_Context dataContext = SASS.sass_make_data_context(buffer);

        // configure context
        SassLibrary.Sass_Options libsassOptions = SASS.sass_data_context_get_options(dataContext);
        configure(context, inputPath, outputPath, libsassOptions, options);

        return dataContext;
    }

    /**
     * Configure sass.
     *
     * @param libsassOptions The libsass options.
     * @param javaOptions    The java options.
     */
    private void configure(Context context, File inputPath, File outputPath, SassLibrary.Sass_Options libsassOptions, Options javaOptions) {
        int    precision           = javaOptions.getPrecision();
        int    outputStyle         = mapOutputStyle(javaOptions.getOutputStyle());
        byte   sourceComments      = createBooleanByte(javaOptions.isSourceComments());
        byte   sourceMapEmbed      = createBooleanByte(javaOptions.isSourceMapEmbed());
        byte   sourceMapContents   = createBooleanByte(javaOptions.isSourceMapContents());
        byte   omitSourceMapUrl    = createBooleanByte(javaOptions.isOmitSourceMapUrl());
        byte   isIndentedSyntaxSrc = createBooleanByte(javaOptions.isIndentedSyntaxSrc());
        String inputPathString     = null == inputPath ? "" : inputPath.getAbsolutePath();
        String outputPathString    = null == outputPath ? "" : outputPath.getAbsolutePath();
        String imagePath           = javaOptions.getImageUrl();
        String includePaths        = joinFilePaths(javaOptions.getIncludePaths());
        String sourceMapFile = null == javaOptions.getSourceMapFile()
                ? ""
                : javaOptions.getSourceMapFile().getAbsolutePath();
        SassLibrary.Sass_C_Function_List   functions = createFunctions(javaOptions.getFunctionProviders());
        SassLibrary.Sass_C_Import_Callback importer  = createImporter(context, javaOptions.getImporters());

        SASS.sass_option_set_precision(libsassOptions, precision);
        SASS.sass_option_set_output_style(libsassOptions, outputStyle);
        SASS.sass_option_set_source_comments(libsassOptions, sourceComments);
        SASS.sass_option_set_source_map_embed(libsassOptions, sourceMapEmbed);
        SASS.sass_option_set_source_map_contents(libsassOptions, sourceMapContents);
        SASS.sass_option_set_omit_source_map_url(libsassOptions, omitSourceMapUrl);
        SASS.sass_option_set_is_indented_syntax_src(libsassOptions, isIndentedSyntaxSrc);
        SASS.sass_option_set_input_path(libsassOptions, inputPathString);
        SASS.sass_option_set_output_path(libsassOptions, outputPathString);
        SASS.sass_option_set_image_path(libsassOptions, imagePath);
        SASS.sass_option_set_include_path(libsassOptions, includePaths);
        SASS.sass_option_set_source_map_file(libsassOptions, sourceMapFile);
        SASS.sass_option_set_c_functions(libsassOptions, functions);
        SASS.sass_option_set_importer(libsassOptions, importer);
    }

    /**
     * Map java output style to native libsass output style.
     *
     * @param outputStyle The java output style.
     * @return The native libsass output style.
     */
    private int mapOutputStyle(OutputStyle outputStyle) {
        int result = SassLibrary.Sass_Output_Style.SASS_STYLE_NESTED;
        switch (outputStyle) {
            case EXPANDED:
                result = SassLibrary.Sass_Output_Style.SASS_STYLE_EXPANDED;
                break;
            case COMPACT:
                result = SassLibrary.Sass_Output_Style.SASS_STYLE_COMPACT;
                break;
            case COMPRESSED:
                result = SassLibrary.Sass_Output_Style.SASS_STYLE_COMPRESSED;
                break;
        }
        return result;
    }

    /**
     * Create a string pointer from a file list, using the absolute paths and OS dependent separator char.
     *
     * @param list The file list.
     * @return Pointer to list of absolute paths.
     */
    private String joinFilePaths(List<File> list) {
        if (null == list || list.isEmpty()) {
            return "";
        }

        String        separator = File.pathSeparator;
        StringBuilder string    = new StringBuilder();

        for (File file : list) {
            string.append(separator).append(file.getAbsolutePath());
        }

        return string.substring(1);
    }

    private SassLibrary.Sass_C_Function_List createFunctions(List<?> functionProviders) {
        FunctionCallbackFactory functionCallbackFactory = new FunctionCallbackFactory(SASS);

        List<SassLibrary.Sass_C_Function_Callback> callbacks = functionCallbackFactory.compileFunctions(functionProviders);

        return functionCallbackFactory.toSassCFunctionList(callbacks);
    }

    private SassLibrary.Sass_C_Import_Callback createImporter(Context originalContext, Collection<Importer> importers) {
        if (importers.isEmpty()) {
            return null;
        }

        ImporterCallbackFactory importerCallbackFactory = new ImporterCallbackFactory(SASS);

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

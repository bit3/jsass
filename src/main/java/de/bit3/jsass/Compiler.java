package de.bit3.jsass;

import com.sun.jna.Native;
import sass.SassLibrary;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Hello world!
 */
public class Compiler {
    /**
     * SASS library adapter.
     */
    private SassLibrary SASS = (SassLibrary) Native.loadLibrary("sass", SassLibrary.class);

    /**
     * Compile string.
     *
     * @param string     The input string.
     * @param inputPath  The input path.
     * @param outputPath The output path.
     * @param options    The compile options.
     * @return The compilation output.
     * @throws CompilationException If the compilation failed.
     */
    public Output compileString(String string, File inputPath, File outputPath, Options options) throws CompilationException {
        // create file context
        SassLibrary.Sass_Data_Context dataContext = null;

        try {
            ByteBuffer byteBuffer = StandardCharsets.US_ASCII.encode(string);

            System.out.println("-----");
            System.out.println(string);
            System.out.println("-----");
            //System.out.println(StandardCharsets.UTF_8.decode(byteBuffer));
            System.out.println("-----");

            // create context
            dataContext = SASS.sass_make_data_context(byteBuffer);

            // configure context
            SassLibrary.Sass_Options libsassOptions = SASS.sass_data_context_get_options(dataContext);
            configure(inputPath, outputPath, libsassOptions, options);

            // compile file
            SASS.sass_compile_data_context(dataContext);

            // check error status
            SassLibrary.Sass_Context context = SASS.sass_data_context_get_context(dataContext);
            checkErrorStatus(context);

            return createOutput(context);
        } finally {
            if (null != dataContext) {
                // free context
                SASS.sass_delete_data_context(dataContext);
            }
        }
    }

    /**
     * Compile file.
     *
     * @param inputPath  The input path.
     * @param outputPath The output path.
     * @param options    The compile options.
     * @return The compilation output.
     * @throws CompilationException If the compilation failed.
     */
    public Output compileFile(File inputPath, File outputPath, Options options) throws CompilationException {
        // create file context
        SassLibrary.Sass_File_Context fileContext = null;

        try {
            // create context
            fileContext = SASS.sass_make_file_context(inputPath.getAbsolutePath());

            // configure context
            SassLibrary.Sass_Options libsassOptions = SASS.sass_file_context_get_options(fileContext);
            configure(inputPath, outputPath, libsassOptions, options);

            // compile file
            SASS.sass_compile_file_context(fileContext);

            // check error status
            SassLibrary.Sass_Context context = SASS.sass_file_context_get_context(fileContext);
            checkErrorStatus(context);

            return createOutput(context);
        } finally {
            if (null != fileContext) {
                // free context
                SASS.sass_delete_file_context(fileContext);
            }
        }
    }

    /**
     * Configure sass.
     *
     * @param libsassOptions The libsass options.
     * @param javaOptions    The java options.
     */
    private void configure(File inputPath, File outputPath, SassLibrary.Sass_Options libsassOptions, Options javaOptions) {
        int    precision           = javaOptions.getPrecision();
        int    outputStyle         = mapOutputStyle(javaOptions.getOutputStyle());
        byte   sourceComments      = createBooleanByte(javaOptions.isSourceComments());
        byte   sourceMapEmbed      = createBooleanByte(javaOptions.isSourceMapEmbed());
        byte   sourceMapContents   = createBooleanByte(javaOptions.isSourceMapContents());
        byte   omitSourceMapUrl    = createBooleanByte(javaOptions.isOmitSourceMapUrl());
        byte   isIndentedSyntaxSrc = createBooleanByte(javaOptions.isIndentedSyntaxSrc());
        String inputPathString     = inputPath.getAbsolutePath();
        String outputPathString    = outputPath.getAbsolutePath();
        String imagePath           = javaOptions.getImageUrl();
        String includePaths        = joinFilePaths(javaOptions.getIncludePaths());
        String sourceMapFile       = null == javaOptions.getSourceMapFile() ? "" : javaOptions.getSourceMapFile().getAbsolutePath();
        SassLibrary.Sass_C_Function_List functions = createFunctions(javaOptions.getFunctionProviders());
        // TODO sass_option_set_importer

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
        // TODO sass_option_set_importer
    }

    /**
     * Check the error status.
     *
     * @param context The sass context.
     * @throws CompilationException If the error status is not <em>0</em>.
     */
    private void checkErrorStatus(SassLibrary.Sass_Context context) throws CompilationException {
        int status = SASS.sass_context_get_error_status(context);

        if (status != 0) {
            String file = SASS.sass_context_get_error_file(context);
            String message = SASS.sass_context_get_error_message(context);

            throw new CompilationException(status, file + ": " + message);
        }
    }

    /**
     * Create output from context.
     *
     * @param context The sass context.
     * @return The output.
     */
    private Output createOutput(SassLibrary.Sass_Context context) {
        String css       = SASS.sass_context_get_output_string(context);
        String sourceMap = SASS.sass_context_get_source_map_string(context);

        System.out.println("-----");
        System.out.println(css);
        System.out.println("-----");

        return new Output(css, sourceMap);
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

#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <sass_context.h>
#include "io_bit3_jsass_NativeAdapter.h"

// #define X_DEBUG

#ifdef X_DEBUG
int debug_call_stack_size = 0;

void indent() {
    for (int i=1; i<debug_call_stack_size; i++) {
        printf("    ");
    }
    printf(" | ");
}
#endif


jobject call_class_object_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name,
        const char *method_signature
) {
#ifdef X_DEBUG
    debug_call_stack_size++;
    indent();
    printf("call_class_object_method :: %s %s\n", method_name, method_signature);
#endif

    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);

#ifdef X_DEBUG
    indent();
    printf("  method id: %p\n", j_method);
#endif

    jobject result = (*env)->CallObjectMethod(env, j_object, j_method);

#ifdef X_DEBUG
    indent();
    printf("  result: %p\n", result);
    debug_call_stack_size--;
#endif

    return result;
}

const char *call_class_string_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name
) {
#ifdef X_DEBUG
    debug_call_stack_size++;
#endif

    const char *method_signature = "()Ljava/lang/String;";

#ifdef X_DEBUG
    indent();
    printf("call_class_string_method :: %p->%p->%s %s\n", j_object, j_class, method_name, method_signature);
#endif

    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);

#ifdef X_DEBUG
    indent();
    printf("  method id: %p\n", j_method);
#endif

    jobject j_result = (*env)->CallObjectMethod(env, j_object, j_method);

#ifdef X_DEBUG
    indent();
    printf("  intermediate result: %p\n", j_result);
#endif

    const char *c_string;

    if (j_result) {
        c_string = (*env)->GetStringUTFChars(env, j_result, 0);
    } else {
        c_string = "";
    }

#ifdef X_DEBUG
    indent();
    printf("  result: %s\n", c_string);
    debug_call_stack_size--;
#endif

    return c_string;
}

bool call_class_boolean_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name
) {
#ifdef X_DEBUG
    debug_call_stack_size++;
#endif

    const char *method_signature = "()Z";

#ifdef X_DEBUG
    indent();
    printf("call_class_boolean_method :: %s %s\n", method_name, method_signature);
#endif

    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);

#ifdef X_DEBUG
    indent();
    printf("  method id: %p\n", j_method);
#endif

    bool result = (*env)->CallBooleanMethod(env, j_object, j_method);

#ifdef X_DEBUG
    indent();
    printf("  result: %s\n", result ? "true" : "false");
    debug_call_stack_size--;
#endif

    return result;
}

int call_class_int_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name
) {
#ifdef X_DEBUG
    debug_call_stack_size++;
#endif

    const char *method_signature = "()I";

#ifdef X_DEBUG
    indent();
    printf("call_class_int_method :: %s %s\n", method_name, method_signature);
#endif

    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);

#ifdef X_DEBUG
    indent();
    printf("  method id: %p\n", j_method);
#endif

    int result = (*env)->CallIntMethod(env, j_object, j_method);

#ifdef X_DEBUG
    indent();
    printf("  result: %d\n", result);
    debug_call_stack_size--;
#endif

    return result;
}

jobject call_object_method(
        JNIEnv *env,
        jobject j_object,
        const char *method_name,
        const char *method_signature
) {
#ifdef X_DEBUG
    debug_call_stack_size++;
    indent();
    printf("call_object_method :: %p->%s %s\n", j_object, method_name, method_signature);
#endif

    jclass j_class = (*env)->GetObjectClass(env, j_object);

#ifdef X_DEBUG
    indent();
    printf("  class: %p\n", j_class);
#endif

    jobject result = call_class_object_method(env, j_object, j_class, method_name, method_signature);

#ifdef X_DEBUG
    debug_call_stack_size--;
#endif

    return result;
}

const char *call_string_method(
        JNIEnv *env,
        jobject j_object,
        const char *method_name
) {
#ifdef X_DEBUG
    debug_call_stack_size++;
    indent();
    printf("call_string_method :: %p->%s\n", j_object, method_name);
#endif

    jclass j_class = (*env)->GetObjectClass(env, j_object);

#ifdef X_DEBUG
    indent();
    printf("  class: %p\n", j_class);
#endif

    const char *result = call_class_string_method(env, j_object, j_class, method_name);

#ifdef X_DEBUG
    debug_call_stack_size--;
#endif

    return result;
}

bool call_boolean_method(
        JNIEnv *env,
        jobject j_object,
        const char *method_name
) {
#ifdef X_DEBUG
    debug_call_stack_size++;
    indent();
    printf("call_boolean_method :: %p->%s\n", j_object, method_name);
#endif

    jclass j_class = (*env)->GetObjectClass(env, j_object);

#ifdef X_DEBUG
    indent();
    printf("  class: %p\n", j_class);
#endif

    bool result = call_class_boolean_method(env, j_object, j_class, method_name);

#ifdef X_DEBUG
    debug_call_stack_size--;
#endif

    return result;
}

const char *uri_to_string(
        JNIEnv *env,
        jobject j_uri
) {
#ifdef X_DEBUG
    debug_call_stack_size++;
    indent();
    printf("uri_to_string :: %p\n", j_uri);
#endif

    const char* c_string;

    if (j_uri) {
        jobject j_string = call_object_method(env, j_uri, "toString", "()Ljava/lang/String;");

#ifdef X_DEBUG
        indent();
        printf("  j_string: %s\n", (*env)->GetStringUTFChars(env, j_string, 0));
#endif

        jclass j_class = (*env)->GetObjectClass(env, j_string);
        jmethodID j_method = (*env)->GetMethodID(env, j_class, "startsWith", "(Ljava/lang/String;)Z");
        jstring j_prefix = (*env)->NewStringUTF(env, "file:");
        bool startWithFile = (*env)->CallBooleanMethod(env, j_string, j_method, j_prefix);

        if (startWithFile) {
            j_method = (*env)->GetMethodID(env, j_class, "substring", "(I)Ljava/lang/String;");
            j_string = (*env)->CallObjectMethod(env, j_string, j_method, 5);

#ifdef X_DEBUG
            indent();
            printf("  j_string: %s\n", (*env)->GetStringUTFChars(env, j_string, 0));
#endif
        }

        c_string = (*env)->GetStringUTFChars(env, j_string, 0);
    } else {
        c_string = "";
    }

#ifdef X_DEBUG
    indent();
    printf("  result: %s\n", c_string);
    debug_call_stack_size--;
#endif

    return c_string;
}

/**
 *
 */
jobjectArray create_output(JNIEnv *env, const char *css_string, const char *source_map_string) {
    return 0;
}

jobject parse_and_execute(JNIEnv *env, struct Sass_Compiler* sass_compiler, struct Sass_Context *sass_context) {
    sass_compiler_parse(sass_compiler);
    sass_compiler_execute(sass_compiler);

    const char *c_css = sass_context_get_output_string(sass_context);
    const char *c_source_map = sass_context_get_source_map_string(sass_context);
    int error_status = sass_context_get_error_status(sass_context);
    const char *c_json_error = sass_context_get_error_json(sass_context);

    jstring j_css = (*env)->NewStringUTF(env, c_css);
    jstring j_source_map = (*env)->NewStringUTF(env, c_source_map);
    jstring j_json_error = (*env)->NewStringUTF(env, c_json_error);

    jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/Output");
    jmethodID j_method = (*env)->GetMethodID(env, j_class, "<init>", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V");
    jobject j_output = (*env)->NewObject(env, j_class, j_method, j_css, j_source_map, error_status, j_json_error);

    sass_delete_compiler(sass_compiler);

    return j_output;
}

void configure_options(JNIEnv *env, jobject j_context, struct Sass_Options *sass_options) {
    jobject j_options = call_object_method(
            env, j_context, "getOptions", "()Lio/bit3/jsass/Options;"
    );

    jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/Options");

#ifdef X_DEBUG
    indent();
    printf("configure_options: %p -> %p\n", j_options, sass_options);
#endif

    // Context.getOutputPath()
    jobject j_output_path = call_object_method(env, j_context, "getOutputPath", "()Ljava/net/URI;");
    const char *c_output_path = uri_to_string(env, j_output_path);

#ifdef X_DEBUG
    indent();
    printf("  output path: %s\n", c_output_path);
#endif

    sass_option_set_output_path(sass_options, c_output_path);

    // Options.getFunctionProviders()
    // Options.getHeaderImporters()
    // Options.getImporters()
    // Options.getIncludePaths()

    // Options.getIndent()
    const char *c_indent = call_class_string_method(env, j_options, j_class, "getIndent");

#ifdef X_DEBUG
    indent();
    printf("  indent: \"%s\"\n", c_indent);
#endif

    sass_option_set_indent(sass_options, c_indent);

    // Options.isIndentedSyntaxSrc()
    bool c_is_indented_syntax = call_class_boolean_method(env, j_options, j_class, "isIndentedSyntaxSrc");

#ifdef X_DEBUG
    indent();
    printf("  indented syntax: %s\n", c_is_indented_syntax ? "true" : "false");
#endif

    sass_option_set_is_indented_syntax_src(sass_options, c_is_indented_syntax);

    // Options.getLinefeed()
    const char *c_linefeed = call_class_string_method(env, j_options, j_class, "getLinefeed");

#ifdef X_DEBUG
    indent();
    printf("  linefeed: %s\n", c_linefeed);
#endif

    sass_option_set_linefeed(sass_options, c_linefeed);

    // Options.isOmitSourceMapUrl()
    bool c_is_omit_source_map_url = call_class_boolean_method(env, j_options, j_class, "isOmitSourceMapUrl");

#ifdef X_DEBUG
    indent();
    printf("  omit source map url: %s\n", c_is_omit_source_map_url ? "true" : "false");
#endif

    sass_option_set_omit_source_map_url(sass_options, c_is_omit_source_map_url);

    // Options.getOutputStyle()
    jobject j_output_style = call_class_object_method(env, j_options, j_class, "getOutputStyle", "()Lio/bit3/jsass/OutputStyle;");
    jclass j_output_style_class = (*env)->GetObjectClass(env, j_output_style);
    jfieldID j_field = (*env)->GetFieldID(env, j_output_style_class, "NUMERIC", "I");
    jint j_output_style_numeric = (*env)->GetIntField(env, j_output_style, j_field);

    switch ((int) j_output_style_numeric) {
        case 2:
#ifdef X_DEBUG
            indent();
            printf("  output style: expanded\n");
#endif

            sass_option_set_output_style(sass_options, SASS_STYLE_EXPANDED);
            break;

        case 3:
#ifdef X_DEBUG
            indent();
            printf("  output style: compact\n");
#endif

            sass_option_set_output_style(sass_options, SASS_STYLE_COMPACT);
            break;

        case 4:
#ifdef X_DEBUG
            indent();
            printf("  output style: compressed\n");
#endif

            sass_option_set_output_style(sass_options, SASS_STYLE_COMPRESSED);
            break;

        default:
#ifdef X_DEBUG
            indent();
            printf("  output style: nested\n");
#endif

            sass_option_set_output_style(sass_options, SASS_STYLE_NESTED);
    }

    // Options.getPluginPath()
    const char *c_plugin_path = call_class_string_method(env, j_options, j_class, "getPluginPath");

#ifdef X_DEBUG
    indent();
    printf("  plugin path: %s\n", c_plugin_path);
#endif

    sass_option_set_plugin_path(sass_options, c_plugin_path);

    // Options.getPrecision()
    int c_precision = call_class_int_method(env, j_options, j_class, "getPrecision");

#ifdef X_DEBUG
    indent();
    printf("  precision: %d\n", c_precision);
#endif

    sass_option_set_precision(sass_options, c_precision);

    // Options.isSourceComments()
    bool c_is_source_comments = call_class_boolean_method(env, j_options, j_class, "isSourceComments");

#ifdef X_DEBUG
    indent();
    printf("  source comments: %s\n", c_is_source_comments ? "true" : "false");
#endif

    sass_option_set_source_comments(sass_options, c_is_source_comments);

    // Options.isSourceMapContents()
    bool c_is_source_map_contents = call_class_boolean_method(env, j_options, j_class, "isSourceMapContents");

#ifdef X_DEBUG
    indent();
    printf("  source map contents: %s\n", c_is_source_map_contents ? "true" : "false");
#endif

    sass_option_set_source_map_contents(sass_options, c_is_source_map_contents);

    // Options.isSourceMapEmbed()
    bool c_is_source_map_embed = call_class_boolean_method(env, j_options, j_class, "isSourceMapEmbed");

#ifdef X_DEBUG
    indent();
    printf("  source map embed: %s\n", c_is_source_map_embed ? "true" : "false");
#endif

    sass_option_set_source_map_embed(sass_options, c_is_source_map_embed);

    // Options.getSourceMapFile()
    jobject j_source_map_file = call_class_object_method(env, j_options, j_class, "getSourceMapFile", "()Ljava/net/URI;");
    const char *c_source_map_file = uri_to_string(env, j_source_map_file);

#ifdef X_DEBUG
    indent();
    printf("  source map file: %s\n", c_source_map_file);
#endif

    sass_option_set_source_map_file(sass_options, c_source_map_file);

    // Options.getSourceMapRoot()
    jobject j_source_map_root = call_class_object_method(env, j_options, j_class, "getSourceMapRoot", "()Ljava/net/URI;");
    const char *c_source_map_root = uri_to_string(env, j_source_map_root);

#ifdef X_DEBUG
    indent();
    printf("  source map root: %s\n", c_source_map_root);
#endif

    sass_option_set_source_map_root(sass_options, c_source_map_root);
}

JNIEXPORT jobjectArray JNICALL Java_io_bit3_jsass_NativeAdapter_compileFile
        (JNIEnv *env, jobject j_adapter, jobject j_context) {
    jobject j_input_path = call_object_method(env, j_context, "getInputPath", "()Ljava/net/URI;");
    const char *c_input_path = uri_to_string(env, j_input_path);

    struct Sass_File_Context *sass_context = sass_make_file_context(c_input_path);
    struct Sass_Options *sass_options = sass_file_context_get_options(sass_context);
    configure_options(env, j_context, sass_options);
    sass_file_context_set_options(sass_context, sass_options);
    struct Sass_Compiler *sass_compiler = sass_make_file_compiler(sass_context);

    return parse_and_execute(env, sass_compiler, (struct Sass_Context*)sass_context);
}

JNIEXPORT jobjectArray JNICALL Java_io_bit3_jsass_NativeAdapter_compileString
        (JNIEnv *env, jobject j_adapter, jobject j_context) {
    const char *c_source_string = call_string_method(env, j_context, "getString");
    char *c_source_buffer = strdup(c_source_string);

    struct Sass_Data_Context *sass_context = sass_make_data_context(c_source_buffer);
    struct Sass_Options *sass_options = sass_data_context_get_options(sass_context);
    configure_options(env, j_context, sass_options);
    sass_data_context_set_options(sass_context, sass_options);
    struct Sass_Compiler *sass_compiler = sass_make_data_compiler(sass_context);

    return parse_and_execute(env, sass_compiler, (struct Sass_Context*)sass_context);
}

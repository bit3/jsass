#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <sass.h>
#include <sass2scss.h>
#include <stdlib.h>
#include "jni_helper.c"
#include "io_bit3_jsass_adapter_NativeAdapter.h"

// #define DEBUG

/**
 * Convert a sass value into a java value.
 *
 * @param sass_value The sass value.
 *
 * @return The java value.
 */
jobject convert_sass_value_to_java(JNIEnv *env, const union Sass_Value *sass_value) {
    jobject j_value = 0;

    if (sass_value_is_number(sass_value)) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(number)\n");
        #endif

        double c_value = sass_number_get_value(sass_value);
        const char *c_unit = sass_number_get_unit(sass_value);

        jstring j_unit = (*env)->NewStringUTF(env, c_unit);

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassNumber");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "(DLjava/lang/String;)V");
        j_value = (*env)->NewObject(env, j_class, j_constructor, c_value, j_unit);

        (*env)->DeleteLocalRef(env, j_unit);
        (*env)->DeleteLocalRef(env, j_class);
    }

    else if (sass_value_is_string(sass_value)) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(string)\n");
        #endif

        const char *c_value = sass_string_get_value(sass_value);
        bool c_quoted = sass_string_is_quoted(sass_value);

        jstring j_string;

        if (c_quoted) {
            char *c_string = sass_string_unquote(c_value);
            j_string = (*env)->NewStringUTF(env, c_string);
            free(c_string);
        } else {
            j_string = (*env)->NewStringUTF(env, c_value);
        }

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassString");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "(Ljava/lang/String;Z)V");
        j_value = (*env)->NewObject(env, j_class, j_constructor, j_string, c_quoted);

        (*env)->DeleteLocalRef(env, j_string);
        (*env)->DeleteLocalRef(env, j_class);
    }

    else if (sass_value_is_boolean(sass_value)) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(boolean)\n");
        #endif

        bool c_value = sass_boolean_get_value(sass_value);

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassBoolean");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "(Z)V");
        j_value = (*env)->NewObject(env, j_class, j_constructor, c_value);

        (*env)->DeleteLocalRef(env, j_class);
    }

    else if (sass_value_is_color(sass_value)) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(color)\n");
        #endif

        double red = sass_color_get_r(sass_value);
        double green = sass_color_get_g(sass_value);
        double blue = sass_color_get_b(sass_value);
        double alpha = sass_color_get_a(sass_value);

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassColor");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "(DDDD)V");
        j_value = (*env)->NewObject(env, j_class, j_constructor, red, green, blue, alpha);

        (*env)->DeleteLocalRef(env, j_class);
    }

    else if (sass_value_is_list(sass_value)) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(list)\n");
        #endif

        size_t c_length = sass_list_get_length(sass_value);
        enum Sass_Separator c_separator = sass_list_get_separator(sass_value);
        bool c_bracketed = sass_list_get_is_bracketed(sass_value);

        jclass j_separator_class = (*env)->FindClass(env, "io/bit3/jsass/Separator");
        jobject j_separator;

        if (SASS_SPACE == c_separator) {
            jfieldID j_field = (*env)->GetStaticFieldID(env, j_separator_class, "SPACE", "Lio/bit3/jsass/Separator;");
            j_separator = (*env)->GetStaticObjectField(env, j_separator_class, j_field);
        } else {
            jfieldID j_field = (*env)->GetStaticFieldID(env, j_separator_class, "COMMA", "Lio/bit3/jsass/Separator;");
            j_separator = (*env)->GetStaticObjectField(env, j_separator_class, j_field);
        }

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassList");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "(ILio/bit3/jsass/Separator;Z)V");
        j_value = (*env)->NewObject(env, j_class, j_constructor, (jint) c_length, j_separator, c_bracketed);

        jmethodID j_add_method = (*env)->GetMethodID(env, j_class, "add", "(Ljava/lang/Object;)Z");

        for (size_t i = 0; i < c_length; i++) {
            union Sass_Value *c_item = sass_list_get_value(sass_value, i);
            jobject j_item = convert_sass_value_to_java(env, c_item);
            (*env)->CallBooleanMethod(env, j_value, j_add_method, j_item);
            (*env)->DeleteLocalRef(env, j_item);
        }

        (*env)->DeleteLocalRef(env, j_separator);
        (*env)->DeleteLocalRef(env, j_separator_class);
        (*env)->DeleteLocalRef(env, j_class);
    }

    else if (sass_value_is_map(sass_value)) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(map)\n");
        #endif

        size_t c_length = sass_map_get_length(sass_value);

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassMap");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "(I)V");
        j_value = (*env)->NewObject(env, j_class, j_constructor, (jint) c_length);

        jmethodID j_put_method = (*env)->GetMethodID(
                env, j_class, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
        );

        for (size_t i = 0; i < c_length; i++) {
            union Sass_Value *c_key = sass_map_get_key(sass_value, i);
            union Sass_Value *c_item = sass_map_get_value(sass_value, i);
            jobject j_key = convert_sass_value_to_java(env, c_key);
            jobject j_item = convert_sass_value_to_java(env, c_item);
            (*env)->CallObjectMethod(env, j_value, j_put_method, j_key, j_item);
            (*env)->DeleteLocalRef(env, j_key);
            (*env)->DeleteLocalRef(env, j_item);
        }

        (*env)->DeleteLocalRef(env, j_class);
    }

    else if (sass_value_is_error(sass_value)) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(error)\n");
        #endif

        char *c_message = sass_error_get_message(sass_value);
        jstring j_message = (*env)->NewStringUTF(env, c_message);

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassError");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "(Ljava/lang/String;)V");
        j_value = (*env)->NewObject(env, j_class, j_constructor, j_message);

        free(c_message);
        (*env)->DeleteLocalRef(env, j_class);
        (*env)->DeleteLocalRef(env, j_message);
    }

    else if (sass_value_is_warning(sass_value)) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(warning)\n");
        #endif

        char *c_message = sass_error_get_message(sass_value);
        jstring j_message = (*env)->NewStringUTF(env, c_message);

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassWarning");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "(Ljava/lang/String;)V");
        j_value = (*env)->NewObject(env, j_class, j_constructor, j_message);

        free(c_message);
        (*env)->DeleteLocalRef(env, j_class);
        (*env)->DeleteLocalRef(env, j_message);
    }

    if (!j_value) {
        #ifdef DEBUG
        printf("convert_sass_value_to_java(null)\n");
        #endif

        jclass j_class = (*env)->FindClass(env, "io/bit3/jsass/type/SassNull");
        jmethodID j_constructor = (*env)->GetMethodID(env, j_class, "<init>", "()V");
        j_value = (*env)->NewObject(env, j_class, j_constructor);

        (*env)->DeleteLocalRef(env, j_class);
    }

    // (*env)->DeleteLocalRef(env, j_value);

    return j_value;
}

/**
 * Convert a java value into a sass value.
 *
 * @param j_value The java value.
 *
 * @return The sass value.
 */
union Sass_Value *convert_java_value_to_sass(JNIEnv *env, jobject j_value) {
    if (!j_value) {
        return sass_make_null();
    }

    jclass j_type_class = (*env)->GetObjectClass(env, j_value);
    jfieldID j_type_field = (*env)->GetStaticFieldID(env, j_type_class, "TYPE", "I");

    if (!j_type_field) {
        char message[128];
        sprintf(message, "Java value %p does not have any type", j_value);

        (*env)->DeleteLocalRef(env, j_type_class);

        return sass_make_error(message);
    }

    jint type = (*env)->GetStaticIntField(env, j_type_class, j_type_field);
    union Sass_Value *sass_result = 0;

    if (1 == type) {
        sass_result = sass_make_null();
    } else if (2 == type) {
        double c_value = call_class_double_method(env, j_value, j_type_class, "doubleValue");
        char *c_unit = call_class_string_method(env, j_value, j_type_class, "getUnit");

        sass_result = sass_make_number(c_value, c_unit);

        free(c_unit);
    } else if (3 == type) {
        char *c_string = call_class_string_method(env, j_value, j_type_class, "getValue");
        bool c_quoted = call_class_boolean_method(env, j_value, j_type_class, "isQuoted");

        if (c_quoted) {
            char c_quote = call_class_char_method(env, j_value, j_type_class, "getQuote");
            char *c_temp = sass_string_quote(c_string, c_quote);
            free(c_string);
            c_string = c_temp;
        }

        union Sass_Value *sass_string = sass_make_string(c_string);
        sass_string_set_quoted(sass_string, c_quoted);

        sass_result = sass_string;

        free(c_string);
    } else if (4 == type) {
        bool c_value = call_class_boolean_method(env, j_value, j_type_class, "getValue");

        sass_result = sass_make_boolean(c_value);
    } else if (5 == type) {
        double c_red = call_class_double_method(env, j_value, j_type_class, "getRed");
        double c_green = call_class_double_method(env, j_value, j_type_class, "getGreen");
        double c_blue = call_class_double_method(env, j_value, j_type_class, "getBlue");
        double c_alpha = call_class_double_method(env, j_value, j_type_class, "getAlpha");

        sass_result = sass_make_color(c_red, c_green, c_blue, c_alpha);
    } else if (6 == type) {
        int c_size = call_class_int_method(env, j_value, j_type_class, "size");

        jobject j_separator = call_class_object_method(
                env, j_value, j_type_class, "getSeparator", "()Lio/bit3/jsass/Separator;"
        );
        jclass j_separator_class = (*env)->GetObjectClass(env, j_separator);
        jfieldID j_separator_field = (*env)->GetFieldID(env, j_separator_class, "character", "C");
        jchar c_separator_char = (*env)->GetCharField(env, j_separator, j_separator_field);

        enum Sass_Separator c_separator = SASS_COMMA;
        if (' ' == c_separator_char) {
            c_separator = SASS_SPACE;
        }

        bool c_bracketed = call_class_boolean_method(
                env, j_value, j_type_class, "isBracketed"
        );

        union Sass_Value *sass_list = sass_make_list((size_t) c_size, c_separator, c_bracketed);

        jmethodID j_get_method = (*env)->GetMethodID(env, j_type_class, "get", "(I)Ljava/lang/Object;");

        for (int i = 0; i < c_size; i++) {
            jobject j_item = (*env)->CallObjectMethod(env, j_value, j_get_method, i);
            union Sass_Value *c_item = convert_java_value_to_sass(env, j_item);
            sass_list_set_value(sass_list, (size_t) i, c_item);
            (*env)->DeleteLocalRef(env, j_item);
        }

        (*env)->DeleteLocalRef(env, j_separator_class);
        (*env)->DeleteLocalRef(env, j_separator);

        sass_result = sass_list;
    } else if (7 == type) {
        int c_size = call_class_int_method(env, j_value, j_type_class, "size");

        union Sass_Value *sass_map = sass_make_map((size_t) c_size);

        jobject j_entry_set = call_class_object_method(env, j_value, j_type_class, "entrySet", "(I)Ljava/util/Set;");
        jobject j_iterator = call_object_method(env, j_value, "iterator", "()Ljava/util/Iterator;");
        jclass j_iterator_class = (*env)->GetObjectClass(env, j_iterator);
        jmethodID j_iterator_hasNext_method = (*env)->GetMethodID(env, j_iterator_class, "hasNext", "()Z");
        jmethodID j_iterator_next_method = (*env)->GetMethodID(env, j_iterator_class, "next", "()Ljava/lang/Object;");

        jclass j_entry_class = (*env)->FindClass(env, "java/util/Map$Entry");
        jmethodID j_entry_getKey_method = (*env)->GetMethodID(env, j_entry_class, "getKey", "()Ljava/lang/Object;");
        jmethodID j_entry_getValue_method = (*env)->GetMethodID(env, j_entry_class, "getValue", "()Ljava/lang/Object;");

        size_t i = 0;
        while ((*env)->CallBooleanMethod(env, j_iterator, j_iterator_hasNext_method)) {
            jobject j_entry = (*env)->CallObjectMethod(env, j_iterator, j_iterator_next_method);
            jobject j_key = (*env)->CallObjectMethod(env, j_entry, j_entry_getKey_method);
            jobject j_item = (*env)->CallObjectMethod(env, j_entry, j_entry_getValue_method);

            union Sass_Value *c_key = convert_java_value_to_sass(env, j_key);
            union Sass_Value *c_item = convert_java_value_to_sass(env, j_item);

            sass_map_set_key(sass_map, i, c_key);
            sass_map_set_value(sass_map, i, c_item);

            i++;
            (*env)->DeleteLocalRef(env, j_item);
            (*env)->DeleteLocalRef(env, j_key);
            (*env)->DeleteLocalRef(env, j_entry);
        }

        (*env)->DeleteLocalRef(env, j_iterator_class);
        (*env)->DeleteLocalRef(env, j_iterator);
        (*env)->DeleteLocalRef(env, j_entry_class);
        (*env)->DeleteLocalRef(env, j_entry_set);

        sass_result = sass_map;
    } else if (8 == type) {
        char *message = call_class_string_method(env, j_value, j_type_class, "getMessage");
        sass_result = sass_make_error(message);
        free(message);
    } else if (9 == type) {
        char *message = call_class_string_method(env, j_value, j_type_class, "getMessage");
        sass_result = sass_make_warning(message);
        free(message);
    }

    (*env)->DeleteLocalRef(env, j_type_class);

    if (sass_result) {
        return sass_result;
    }

    char message[128];
    sprintf(message, "Java type %d cannot be converted into sass", type);
    return sass_make_error(message);
}

/**
 * Structure that store all relevant informations for a callback.
 */
struct function_cookie {
    JNIEnv *env;
    jmethodID j_method;
    jobject j_object;
};

/**
 * Function callback that bypass the call into the java world.
 *
 * @param sass_arguments      The function arguments list.
 * @param sass_function_entry The sass function entry.
 * @param sass_compiler       The sass compiler.
 *
 * @return The return value of the function.
 */
union Sass_Value *function_callback(
        const union Sass_Value *sass_arguments,
        Sass_Function_Entry sass_function_entry,
        struct Sass_Compiler *sass_compiler
) {
    void *cookie = sass_function_get_cookie(sass_function_entry);
    struct function_cookie *c_function_cookie = (struct function_cookie *) cookie;

    JNIEnv *env = c_function_cookie->env;

    jobject j_arguments = convert_sass_value_to_java(env, sass_arguments);

    jobject j_result = (*env)->CallObjectMethod(
            env,
            c_function_cookie->j_object,
            c_function_cookie->j_method,
            j_arguments
    );

    union Sass_Value *sass_result = convert_java_value_to_sass(env, j_result);

    (*env)->DeleteLocalRef(env, j_result);
    (*env)->DeleteLocalRef(env, j_arguments);

    return sass_result;
}

/**
 * Structure that store all relevant informations for a callback.
 */
struct importer_cookie {
    JNIEnv *env;
    jobject j_object;
};

/**
 * Importer callback that bypass the call into the java world.
 *
 * @param sass_arguments      The function arguments list.
 * @param sass_function_entry The sass function entry.
 * @param sass_compiler       The sass compiler.
 *
 * @return The return value of the function.
 */
Sass_Import_List importer_callback(
        const char *c_url,
        Sass_Importer_Entry sass_importer_entry,
        struct Sass_Compiler *sass_compiler
) {
    void *cookie = sass_importer_get_cookie(sass_importer_entry);
    Sass_Import_Entry sass_last_import_entry = sass_compiler_get_last_import(sass_compiler);
    const char *c_last_import_imp_path = sass_import_get_imp_path(sass_last_import_entry);
    const char *c_last_import_abs_path = sass_import_get_abs_path(sass_last_import_entry);
    const char *c_last_import_source = sass_import_get_source(sass_last_import_entry);
    const char *c_last_import_srcmap = sass_import_get_srcmap(sass_last_import_entry);

    struct importer_cookie *c_importer_cookie = (struct importer_cookie *) cookie;

    JNIEnv *env = c_importer_cookie->env;

    jstring j_url = (*env)->NewStringUTF(env, c_url);

    // recreate a java import object
    jstring j_last_import_imp_path = c_last_import_imp_path ? (*env)->NewStringUTF(env, c_last_import_imp_path) : 0;
    jstring j_last_import_abs_path = c_last_import_abs_path ? (*env)->NewStringUTF(env, c_last_import_abs_path) : 0;
    jstring j_last_import_source = c_last_import_source ? (*env)->NewStringUTF(env, c_last_import_source) : 0;
    jstring j_last_import_srcmap = c_last_import_srcmap ? (*env)->NewStringUTF(env, c_last_import_srcmap) : 0;
    jclass j_import_class = (*env)->FindClass(env, "io/bit3/jsass/adapter/NativeImport");
    jmethodID j_import_constructor = (*env)->GetMethodID(
            env, j_import_class, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"
    );
    jobject j_last_import = (*env)->NewObject(
            env, j_import_class, j_import_constructor,
            j_last_import_imp_path, j_last_import_abs_path, j_last_import_source, j_last_import_srcmap
    );

    // call the java importer
    jclass j_importer_class = (*env)->FindClass(env, "io/bit3/jsass/adapter/NativeImporterWrapper");
    jmethodID j_importer_method = (*env)->GetMethodID(
            env, j_importer_class, "apply",
            "(Ljava/lang/String;Lio/bit3/jsass/adapter/NativeImport;)Ljava/util/Collection;"
    );
    jobject j_imports = (*env)->CallObjectMethod(
            env, c_importer_cookie->j_object, j_importer_method, j_url, j_last_import
    );

    Sass_Import_List sass_import_list = 0;

    if (j_imports) {
        jclass j_imports_class = (*env)->FindClass(env, "java/util/Collection");
        jsize j_imports_length = call_class_int_method(env, j_imports, j_imports_class, "size");

        // convert into sass_import_list
        sass_import_list = sass_make_import_list((size_t) j_imports_length);

        jobject j_iterator = call_object_method(env, j_imports, "iterator", "()Ljava/util/Iterator;");
        jclass j_iterator_class = (*env)->GetObjectClass(env, j_iterator);
        jmethodID j_iterator_hasNext_method = (*env)->GetMethodID(env, j_iterator_class, "hasNext", "()Z");
        jmethodID j_iterator_next_method = (*env)->GetMethodID(env, j_iterator_class, "next", "()Ljava/lang/Object;");

        jfieldID j_import_path_property = (*env)->GetFieldID(
                env, j_import_class, "importPath", "Ljava/lang/String;"
        );
        jfieldID j_import_absolute_path_property = (*env)->GetFieldID(
                env, j_import_class, "absolutePath", "Ljava/lang/String;"
        );
        jfieldID j_import_contents_property = (*env)->GetFieldID(
                env, j_import_class, "contents", "Ljava/lang/String;"
        );
        jfieldID j_import_sourceMap_property = (*env)->GetFieldID(
                env, j_import_class, "sourceMap", "Ljava/lang/String;"
        );
        jfieldID j_import_errorMessage_property = (*env)->GetFieldID(
                env, j_import_class, "errorMessage", "Ljava/lang/String;"
        );

        size_t i = 0;
        while ((*env)->CallBooleanMethod(env, j_iterator, j_iterator_hasNext_method)) {
            jobject j_import = (*env)->CallObjectMethod(env, j_iterator, j_iterator_next_method);

            char *c_import_path = get_field_string(env, j_import, j_import_path_property);
            char *c_import_absolute_path= get_field_string(env, j_import, j_import_absolute_path_property);
            char *c_import_contents = get_field_string(env, j_import, j_import_contents_property);
            char *c_import_sourceMap = get_field_string(env, j_import, j_import_sourceMap_property);
            char *c_error_message = get_field_string(env, j_import, j_import_errorMessage_property);

            Sass_Import_Entry sass_import_entry = sass_make_import(
                    c_import_path, c_import_absolute_path, c_import_contents, c_import_sourceMap
            );
            if (strlen(c_error_message)) {
                sass_import_set_error(sass_import_entry, c_error_message, 0, 0);
            }
            sass_import_set_list_entry(sass_import_list, i, sass_import_entry);

            // Free memory - c_import_contents and c_import_sourceMap ownership is taken by sass_make_import()
            free(c_import_path);
            free(c_import_absolute_path);
            free(c_error_message);
            (*env)->DeleteLocalRef(env, j_import);
            i++;
        }

        (*env)->DeleteLocalRef(env, j_imports_class);
        (*env)->DeleteLocalRef(env, j_iterator_class);
        (*env)->DeleteLocalRef(env, j_iterator);
    }

    // cleanup
    (*env)->DeleteLocalRef(env, j_imports);
    (*env)->DeleteLocalRef(env, j_last_import);
    (*env)->DeleteLocalRef(env, j_last_import_srcmap);
    (*env)->DeleteLocalRef(env, j_last_import_source);
    (*env)->DeleteLocalRef(env, j_last_import_abs_path);
    (*env)->DeleteLocalRef(env, j_last_import_imp_path);
    (*env)->DeleteLocalRef(env, j_url);
    (*env)->DeleteLocalRef(env, j_importer_class);
    (*env)->DeleteLocalRef(env, j_import_class);

    return sass_import_list;
}

/**
 * Walk over function and release the jni references.
 */
void release_function_cookie_references(JNIEnv *env, Sass_Function_List c_functions) {
    for(size_t i = 0; true; i++) {
        Sass_Function_Entry c_function = sass_function_get_list_entry(c_functions, i);

        if (!c_function) {
            break;
        }

        void *cookie = sass_function_get_cookie(c_function);
        struct function_cookie *c_function_cookie = (struct function_cookie *) cookie;

        (*env)->DeleteLocalRef(env, c_function_cookie->j_object);
    }
}

/**
 * Walk over function and release the jni references.
 */
void release_importers_cookie_references(JNIEnv *env, Sass_Importer_List c_importers) {
    for(size_t i = 0; true; i++) {
        Sass_Importer_Entry c_importer = sass_importer_get_list_entry(c_importers, i);

        if (!c_importer) {
            break;
        }

        void *cookie = sass_importer_get_cookie(c_importer);
        struct importer_cookie *c_importer_cookie = (struct importer_cookie *) cookie;

        (*env)->DeleteLocalRef(env, c_importer_cookie->j_object);
    }
}

/**
 * Walk over function and importer cookies and release the jni references.
 */
void release_all_cookie_references(JNIEnv *env, struct Sass_Context *sass_context) {
    struct Sass_Options *sass_options = sass_context_get_options(sass_context);

    Sass_Function_List c_functions = sass_option_get_c_functions(sass_options);
    release_function_cookie_references(env, c_functions);

    Sass_Importer_List c_headers = sass_option_get_c_headers(sass_options);
    release_importers_cookie_references(env, c_headers);

    Sass_Importer_List c_importers = sass_option_get_c_importers(sass_options);
    release_importers_cookie_references(env, c_importers);
}

/**
 * Parse and execute the compilation and create the output object.
 *
 * @param env           The JNI environment.
 * @param sass_compiler The sass compiler.
 * @param sass_context  The sass context.
 *
 * @return The output object.
 */
jobject parse_and_execute(JNIEnv *env, struct Sass_Compiler *sass_compiler, struct Sass_Context *sass_context) {
    sass_compiler_parse(sass_compiler);
    sass_compiler_execute(sass_compiler);

    const char *c_css = sass_context_get_output_string(sass_context);
    const char *c_source_map = sass_context_get_source_map_string(sass_context);
    int error_status = sass_context_get_error_status(sass_context);
    const char *c_error_json = sass_context_get_error_json(sass_context);
    const char *c_error_text = sass_context_get_error_text(sass_context);
    const char *c_error_message = sass_context_get_error_message(sass_context);
    const char *c_error_file = sass_context_get_error_file(sass_context);
    const char *c_error_src = sass_context_get_error_src(sass_context);

    jstring j_css = (*env)->NewStringUTF(env, c_css);
    jstring j_source_map = (*env)->NewStringUTF(env, c_source_map);
    jstring j_error_json = (*env)->NewStringUTF(env, c_error_json);
    jstring j_error_text = (*env)->NewStringUTF(env, c_error_text);
    jstring j_error_message = (*env)->NewStringUTF(env, c_error_message);
    jstring j_error_file = (*env)->NewStringUTF(env, c_error_file);
    jstring j_error_src = (*env)->NewStringUTF(env, c_error_src);

    jclass j_class;
    jmethodID j_method;
    jobject j_output = 0;

    if (0 != error_status) {
        j_class = (*env)->FindClass(env, "io/bit3/jsass/CompilationException");
        j_method = (*env)->GetMethodID(
                env, j_class, "<init>",
                "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V"
        );
        jthrowable j_exception = (*env)->NewObject(
                env, j_class, j_method,
                error_status, j_error_json, j_error_text, j_error_message, j_error_file, j_error_src
        );
        (*env)->Throw(env, j_exception);
    } else {
        j_class = (*env)->FindClass(env, "io/bit3/jsass/Output");
        j_method = (*env)->GetMethodID(
                env, j_class, "<init>",
                "(Ljava/lang/String;Ljava/lang/String;)V"
        );
        j_output = (*env)->NewObject(
                env, j_class, j_method, j_css, j_source_map
        );
    }

    (*env)->DeleteLocalRef(env, j_class);
    (*env)->DeleteLocalRef(env, j_css);
    (*env)->DeleteLocalRef(env, j_source_map);
    (*env)->DeleteLocalRef(env, j_error_json);
    (*env)->DeleteLocalRef(env, j_error_text);
    (*env)->DeleteLocalRef(env, j_error_message);
    (*env)->DeleteLocalRef(env, j_error_file);
    (*env)->DeleteLocalRef(env, j_error_src);
    release_all_cookie_references(env, sass_context);

    sass_delete_compiler(sass_compiler);

    return j_output;
}

/**
 * Set custom functions in the sass options.
 *
 * @param env             The JNI environment.
 * @param sass_options    The sass options.
 * @param j_options       The java options.
 * @param j_options_class The java options class.
 */
void configure_functions(
        JNIEnv *env,
        struct Sass_Options *sass_options,
        jobject j_options,
        jobject j_options_class
) {
    jarray j_functions = get_class_object_property(
            env, j_options, j_options_class, "functions", "[Lio/bit3/jsass/function/FunctionWrapper;"
    );
    jsize j_functions_length = (*env)->GetArrayLength(env, j_functions);
    jclass j_function_wrapper_class = (*env)->FindClass(env, "io/bit3/jsass/function/FunctionWrapper");
    jmethodID j_function_wrapper_apply_method = (*env)->GetMethodID(
            env, j_function_wrapper_class, "apply", "(Lio/bit3/jsass/type/SassValue;)Lio/bit3/jsass/type/SassValue;"
    );
    Sass_Function_List sass_function_list = sass_make_function_list((size_t) j_functions_length);

    for (size_t i = 0; i < j_functions_length; i++) {
        jobject j_function = (*env)->GetObjectArrayElement(env, j_functions, (jsize) i);

        struct function_cookie *cookie = malloc(sizeof(struct function_cookie));
        cookie->env = env;
        cookie->j_method = j_function_wrapper_apply_method;
        cookie->j_object = j_function;

        jobject j_function_declaration = call_object_method(
                env, j_function, "getDeclaration", "()Lio/bit3/jsass/function/FunctionDeclaration;"
        );
        char *c_signature = call_string_method(
                env, j_function_declaration, "getSignature"
        );
        (*env)->DeleteLocalRef(env, j_function_declaration);

        Sass_Function_Entry sass_function_entry = sass_make_function(
                c_signature, (Sass_Function_Fn) function_callback, cookie
        );
        sass_function_set_list_entry(sass_function_list, i, sass_function_entry);

        free(c_signature);
    }

    sass_option_set_c_functions(sass_options, sass_function_list);

    (*env)->DeleteLocalRef(env, j_function_wrapper_class);
    (*env)->DeleteLocalRef(env, j_functions);
}

/**
 * Set header importers in the sass options.
 *
 * @param env             The JNI environment.
 * @param sass_options    The sass options.
 * @param j_options       The java options.
 * @param j_options_class The java options class.
 */
void configure_header_importers(JNIEnv *env, struct Sass_Options *sass_options, jobject j_options,
                                jobject j_options_class) {
    jarray j_header_importers = get_class_object_property(
            env, j_options, j_options_class, "headerImporters", "[Lio/bit3/jsass/adapter/NativeImporterWrapper;"
    );
    jsize j_header_importers_length = (*env)->GetArrayLength(env, j_header_importers);
    Sass_Importer_List sass_header_importer_list = sass_make_importer_list((size_t) j_header_importers_length);

    for (size_t i = 0; i < j_header_importers_length; i++) {
        jobject j_importer = (*env)->GetObjectArrayElement(env, j_header_importers, (jsize) i);

        struct importer_cookie *cookie = malloc(sizeof(struct importer_cookie));
        cookie->env = env;
        cookie->j_object = j_importer;

        Sass_Importer_Entry sass_importer_entry = sass_make_importer(
                (Sass_Importer_Fn) importer_callback, (size_t) i, cookie
        );
        sass_importer_set_list_entry(sass_header_importer_list, i, sass_importer_entry);
    }

    sass_option_set_c_headers(sass_options, sass_header_importer_list);

    (*env)->DeleteLocalRef(env, j_header_importers);
}

/**
 * Set importers in the sass options.
 *
 * @param env             The JNI environment.
 * @param sass_options    The sass options.
 * @param j_options       The java options.
 * @param j_options_class The java options class.
 */
void configure_importers(JNIEnv *env, struct Sass_Options *sass_options, jobject j_options, jobject j_options_class) {
    jarray j_importers = get_class_object_property(
            env, j_options, j_options_class, "importers", "[Lio/bit3/jsass/adapter/NativeImporterWrapper;"
    );
    jsize j_importers_length = (*env)->GetArrayLength(env, j_importers);
    Sass_Importer_List sass_importer_list = sass_make_importer_list((size_t) j_importers_length);

    for (size_t i = 0; i < j_importers_length; i++) {
        jobject j_importer = (*env)->GetObjectArrayElement(env, j_importers, (jsize) i);

        struct importer_cookie *cookie = malloc(sizeof(struct importer_cookie));
        cookie->env = env;
        cookie->j_object = j_importer;

        Sass_Importer_Entry sass_importer_entry = sass_make_importer(
                (Sass_Importer_Fn) importer_callback, (size_t) i, cookie
        );
        sass_importer_set_list_entry(sass_importer_list, i, sass_importer_entry);
    }

    sass_option_set_c_importers(sass_options, sass_importer_list);

    (*env)->DeleteLocalRef(env, j_importers);
}

/**
 * Configure the sass options corresponding to the java context options.
 *
 * @param env           The JNI environment.
 * @param j_context     The java context.
 * @param sass_options  The sass options.
 */
void configure_options(JNIEnv *env, jobject j_context, struct Sass_Options *sass_options) {
    jobject j_options = get_object_property(env, j_context, "options", "Lio/bit3/jsass/adapter/NativeOptions;");

    jclass j_options_class = (*env)->FindClass(env, "io/bit3/jsass/adapter/NativeOptions");

    // NativeContext.inputPath
    char *c_input_path = get_string_property(env, j_context, "inputPath");
    #ifdef DEBUG
    printf("option->input_path = %s\n", c_input_path);
    #endif
    sass_option_set_input_path(sass_options, c_input_path);
    free(c_input_path);

    // NativeContext.outputPath
    char *c_output_path = get_string_property(env, j_context, "outputPath");
    #ifdef DEBUG
    printf("option->output_path = %s\n", c_output_path);
    #endif
    sass_option_set_output_path(sass_options, c_output_path);
    free(c_output_path);

    // NativeOptions.functions
    configure_functions(env, sass_options, j_options, j_options_class);

    // NativeOptions.headerImporters
    configure_header_importers(env, sass_options, j_options, j_options_class);

    // NativeOptions.importers
    configure_importers(env, sass_options, j_options, j_options_class);

    // NativeOptions.includePath
    char *c_include_path = get_class_string_property(env, j_options, j_options_class, "includePath");
    #ifdef DEBUG
    printf("option->include_path = %s\n", c_include_path);
    #endif
    sass_option_set_include_path(sass_options, c_include_path);
    free(c_include_path);

    // NativeOptions.indent
    const char *c_indent = get_class_string_property(env, j_options, j_options_class, "indent");
    #ifdef DEBUG
    printf("option->indent = %s\n", c_indent);
    #endif
    sass_option_set_indent(sass_options, c_indent);
    // no free here, libsass takes ownership of memory !!!

    // NativeOptions.indentedSyntaxSrc
    bool c_is_indented_syntax_src = get_class_bool_property(env, j_options, j_options_class, "isIndentedSyntaxSrc");
    #ifdef DEBUG
    printf("option->is_indented_syntax_src = %d\n", c_is_indented_syntax_src);
    #endif
    sass_option_set_is_indented_syntax_src(sass_options, c_is_indented_syntax_src);

    // NativeOptions.linefeed
    const char *c_linefeed = get_class_string_property(env, j_options, j_options_class, "linefeed");
    #ifdef DEBUG
    printf("option->linefeed = %s\n", c_linefeed);
    #endif
    sass_option_set_linefeed(sass_options, c_linefeed);
    // no free here, libsass takes ownership of memory !!!

    // NativeOptions.omitSourceMapUrl
    bool c_omit_source_map_url = get_class_bool_property(env, j_options, j_options_class, "omitSourceMapUrl");
    #ifdef DEBUG
    printf("option->omit_source_map_url = %d\n", c_omit_source_map_url);
    #endif
    sass_option_set_omit_source_map_url(sass_options, c_omit_source_map_url);

    // NativeOptions.outputStyle
    jint j_output_style_numeric = get_class_int_property(env, j_options, j_options_class, "outputStyle");
    #ifdef DEBUG
    printf("option->output_style = %d\n", (int) j_output_style_numeric);
    #endif
    switch ((int) j_output_style_numeric) {
        case 2:
            sass_option_set_output_style(sass_options, SASS_STYLE_EXPANDED);
            break;

        case 3:
            sass_option_set_output_style(sass_options, SASS_STYLE_COMPACT);
            break;

        case 4:
            sass_option_set_output_style(sass_options, SASS_STYLE_COMPRESSED);
            break;

        default:
            sass_option_set_output_style(sass_options, SASS_STYLE_NESTED);
    }

    // NativeOptions.pluginPath
    char *c_plugin_path = get_class_string_property(env, j_options, j_options_class, "pluginPath");
    #ifdef DEBUG
    printf("option->plugin_path = %s\n", c_plugin_path);
    #endif
    sass_option_set_plugin_path(sass_options, c_plugin_path);
    free(c_plugin_path);

    // NativeOptions.precision
    int c_precision = get_class_int_property(env, j_options, j_options_class, "precision");
    #ifdef DEBUG
    printf("option->precision = %d\n", c_precision);
    #endif
    sass_option_set_precision(sass_options, c_precision);

    // NativeOptions.sourceComments
    bool c_source_comments = get_class_bool_property(env, j_options, j_options_class, "sourceComments");
    #ifdef DEBUG
    printf("option->source_comments = %d\n", c_source_comments);
    #endif
    sass_option_set_source_comments(sass_options, c_source_comments);

    // NativeOptions.sourceMapContents
    bool c_source_map_contents = get_class_bool_property(env, j_options, j_options_class, "sourceMapContents");
    #ifdef DEBUG
    printf("option->source_map_contents = %d\n", c_source_map_contents);
    #endif
    sass_option_set_source_map_contents(sass_options, c_source_map_contents);

    // NativeOptions.sourceMapEmbed
    bool c_source_map_embed = get_class_bool_property(env, j_options, j_options_class, "sourceMapEmbed");
    #ifdef DEBUG
    printf("option->source_map_embed = %s\n", c_source_map_embed);
    #endif
    sass_option_set_source_map_embed(sass_options, c_source_map_embed);

    // NativeOptions.sourceMapFile
    char *c_source_map_file = get_class_string_property(env, j_options, j_options_class, "sourceMapFile");
    #ifdef DEBUG
    printf("option->source_map_file = %s\n", c_source_map_file);
    #endif
    sass_option_set_source_map_file(sass_options, c_source_map_file);
    free(c_source_map_file);

    // NativeOptions.sourceMapRoot
    char *c_source_map_root = get_class_string_property(env, j_options, j_options_class, "sourceMapRoot");
    #ifdef DEBUG
    printf("option->source_map_root = %s\n", c_source_map_root);
    #endif
    sass_option_set_source_map_root(sass_options, c_source_map_root);
    free(c_source_map_root);

    (*env)->DeleteLocalRef(env, j_options_class);
}

/**
 * The java-native functions.
 */

JNIEXPORT jobjectArray JNICALL Java_io_bit3_jsass_adapter_NativeAdapter_compileFile
        (JNIEnv *env, jclass j_adapter, jobject j_context) {
    char *c_input_path = get_string_property(env, j_context, "inputPath");

    struct Sass_File_Context *sass_context = sass_make_file_context(c_input_path);
    struct Sass_Options *sass_options = sass_file_context_get_options(sass_context);
    configure_options(env, j_context, sass_options);
    struct Sass_Compiler *sass_compiler = sass_make_file_compiler(sass_context);

    free(c_input_path);
    (*env)->DeleteLocalRef(env, j_adapter);
    (*env)->DeleteLocalRef(env, j_context);

    jobject output = parse_and_execute(env, sass_compiler, (struct Sass_Context *) sass_context);

    sass_delete_file_context(sass_context);

    return output;
}

JNIEXPORT jobjectArray JNICALL Java_io_bit3_jsass_adapter_NativeAdapter_compileString
        (JNIEnv *env, jclass j_adapter, jobject j_context) {
    char *c_source_string = get_string_property(env, j_context, "source");

    struct Sass_Data_Context *sass_context = sass_make_data_context(c_source_string);
    struct Sass_Options *sass_options = sass_data_context_get_options(sass_context);
    configure_options(env, j_context, sass_options);
    struct Sass_Compiler *sass_compiler = sass_make_data_compiler(sass_context);

    (*env)->DeleteLocalRef(env, j_adapter);
    (*env)->DeleteLocalRef(env, j_context);

    jobject output = parse_and_execute(env, sass_compiler, (struct Sass_Context *) sass_context);

    sass_delete_data_context(sass_context);

    return output;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeAdapter_sass2scss
        (JNIEnv *env, jclass j_adapter, jstring j_source, jint j_options) {
    const char *c_source = (*env)->GetStringUTFChars(env, j_source, 0);
    const int c_options = (int) j_options;

    (*env)->DeleteLocalRef(env, j_adapter);

    char *c_output = sass2scss(c_source, c_options);
    (*env)->ReleaseStringUTFChars(env, j_source, c_source);

    jstring j_output = (*env)->NewStringUTF(env, c_output);
    free(c_output);

    return j_output;
}

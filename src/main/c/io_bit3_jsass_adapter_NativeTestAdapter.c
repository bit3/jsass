#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <sass.h>
#include <sass2scss.h>
#include <stdlib.h>
#include "io_bit3_jsass_adapter_NativeAdapter.c"
#include "io_bit3_jsass_adapter_NativeTestAdapter.h"

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassPositiveNumberToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_number(678, "");
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassNegativeNumberToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_number(-429, "");
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassZeroNumberToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_number(0, "");
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassPositiveNumberWithUnitToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_number(397, "px");
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassNegativeNumberWithUnitToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_number(-241, "em");
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassZeroNumberWithUnitToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_number(0, "%");
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

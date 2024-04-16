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

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassStringToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_string("Nullam vel sem");
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassQuotedStringToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_qstring("In ac felis");
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassTrueToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_boolean(true);
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassFalseToJava
        (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_boolean(false);
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassRgbColorToJava
  (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_color(.74, .32, .56, 1);
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

JNIEXPORT jobject JNICALL Java_io_bit3_jsass_adapter_NativeTestAdapter_testSassRgbaColorToJava
  (JNIEnv *env, jobject j_context) {
    union Sass_Value *sass_value = sass_make_color(.97, .24, .48, .5);
    jobject j_value = convert_sass_value_to_java(env, sass_value);
    return j_value;
}

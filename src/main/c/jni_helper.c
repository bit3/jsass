/*
 * A collection of methods that makes working with Java objects easier.
 */

/**
 * Call a JNI method and return its jobject value.
 *
 * @param env              The JNI environment.
 * @param j_object         The jobject to call the method on.
 * @param j_class          The class the method is defined in.
 * @param method_name      The name of the method.
 * @param method_signature The signature of the method.
 *
 * @return The return value of the method.
 */
jobject call_class_object_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name,
        const char *method_signature
) {
    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);
    jobject result = (*env)->CallObjectMethod(env, j_object, j_method);

    return result;
}

/**
 * Call a JNI method and return its jstring as c string.
 *
 * The method will dup the methods result and release the jstring directly.
 * Then it returns the duplicated c string.
 *
 * @param env         The JNI environment.
 * @param j_object    The jobject to call the method on.
 * @param j_class     The class the method is defined in.
 * @param method_name The name of the method.
 *
 * @return The return value of the method.
 */
char *call_class_string_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name
) {
    const char *method_signature = "()Ljava/lang/String;";
    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);
    jobject j_result = (*env)->CallObjectMethod(env, j_object, j_method);
    char *c_string;

    if (j_result) {
        const char *c_temp = (*env)->GetStringUTFChars(env, j_result, 0);
        c_string = strdup(c_temp);
        (*env)->ReleaseStringUTFChars(env, j_result, c_temp);
    } else {
        c_string = strdup("");
    }

    return c_string;
}

/**
 * Call a JNI method and return its char value.
 *
 * @param env         The JNI environment.
 * @param j_object    The jobject to call the method on.
 * @param j_class     The class the method is defined in.
 * @param method_name The name of the method.
 *
 * @return The return value of the method.
 */
char call_class_char_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name
) {
    const char *method_signature = "()C";
    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);
    char result = (char) (*env)->CallCharMethod(env, j_object, j_method);

    return result;
}

/**
 * Call a JNI method and return its boolean value.
 *
 * @param env         The JNI environment.
 * @param j_object    The jobject to call the method on.
 * @param j_class     The class the method is defined in.
 * @param method_name The name of the method.
 *
 * @return The return value of the method.
 */
bool call_class_boolean_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name
) {
    const char *method_signature = "()Z";
    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);
    bool result = (*env)->CallBooleanMethod(env, j_object, j_method);

    return result;
}

/**
 * Call a JNI method and return its int value.
 *
 * @param env         The JNI environment.
 * @param j_object    The jobject to call the method on.
 * @param j_class     The class the method is defined in.
 * @param method_name The name of the method.
 *
 * @return The return value of the method.
 */
int call_class_int_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name
) {
    const char *method_signature = "()I";
    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);
    int result = (*env)->CallIntMethod(env, j_object, j_method);

    return result;
}

/**
 * Call a JNI method and return its double value.
 *
 * @param env         The JNI environment.
 * @param j_object    The jobject to call the method on.
 * @param j_class     The class the method is defined in.
 * @param method_name The name of the method.
 *
 * @return The return value of the method.
 */
double call_class_double_method(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *method_name
) {
    const char *method_signature = "()D";
    jmethodID j_method = (*env)->GetMethodID(env, j_class, method_name, method_signature);
    double result = (*env)->CallDoubleMethod(env, j_object, j_method);

    return result;
}

/**
 * Call a JNI method and return its jobject value.
 *
 * The jclass is derived from the jobject.
 *
 * @param env              The JNI environment.
 * @param j_object         The jobject to call the method on.
 * @param method_name      The name of the method.
 * @param method_signature The signature of the method.
 *
 * @return The return value of the method.
 */
jobject call_object_method(
        JNIEnv *env,
        jobject j_object,
        const char *method_name,
        const char *method_signature
) {
    jclass j_class = (*env)->GetObjectClass(env, j_object);
    jobject result = call_class_object_method(env, j_object, j_class, method_name, method_signature);

    (*env)->DeleteLocalRef(env, j_class);

    return result;
}

/**
 * Call a JNI method and return its jstring as c string.
 *
 * The jclass is derived from the jobject.
 *
 * The method will dup the methods result and release the jstring directly.
 * Then it returns the duplicated c string.
 *
 * @param env              The JNI environment.
 * @param j_object         The jobject to call the method on.
 * @param method_name      The name of the method.
 *
 * @return The return value of the method.
 */
char *call_string_method(
        JNIEnv *env,
        jobject j_object,
        const char *method_name
) {
    jclass j_class = (*env)->GetObjectClass(env, j_object);
    char *result = call_class_string_method(env, j_object, j_class, method_name);

    (*env)->DeleteLocalRef(env, j_class);

    return result;
}

/**
 * Call a JNI method and return its boolean value.
 *
 * The jclass is derived from the jobject.
 *
 * @param env              The JNI environment.
 * @param j_object         The jobject to call the method on.
 * @param method_name      The name of the method.
 *
 * @return The return value of the method.
 */
bool call_boolean_method(
        JNIEnv *env,
        jobject j_object,
        const char *method_name
) {
    jclass j_class = (*env)->GetObjectClass(env, j_object);
    bool result = call_class_boolean_method(env, j_object, j_class, method_name);

    (*env)->DeleteLocalRef(env, j_class);

    return result;
}

/**
 * Return a jobject property value.
 *
 * @param env           The JNI environment.
 * @param j_object      The jobject to call get the property value from.
 * @param j_class       The class the property is defined in.
 * @param property_name The name of the property.
 *
 * @return The return value of the property.
 */
jobject get_class_object_property(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *property_name,
        const char *property_signature
) {
    jfieldID j_field = (*env)->GetFieldID(env, j_class, property_name, property_signature);
    jobject j_result = (*env)->GetObjectField(env, j_object, j_field);

    return j_result;
}

/**
 * Return a jstring property value as c string.
 *
 * The method will dup the property value and release the jstring directly.
 * Then it returns the duplicated c string.
 *
 * @param env           The JNI environment.
 * @param j_object      The jobject to call get the property value from.
 * @param j_field       The field id of the property.
 *
 * @return The return value of the property.
 */
char *get_field_string(
        JNIEnv *env,
        jobject j_object,
        jfieldID j_field
) {
    jobject j_result = (*env)->GetObjectField(env, j_object, j_field);
    char *c_string;

    if (j_result) {
        const char *c_temp = (*env)->GetStringUTFChars(env, j_result, 0);
        c_string = strdup(c_temp);
        (*env)->ReleaseStringUTFChars(env, j_result, c_temp);
    } else {
        c_string = strdup("");
    }

    return c_string;
}

/**
 * Return a jstring property value as c string.
 *
 * The method will dup the property value and release the jstring directly.
 * Then it returns the duplicated c string.
 *
 * @param env           The JNI environment.
 * @param j_object      The jobject to call get the property value from.
 * @param j_class       The class the property is defined in.
 * @param property_name The name of the property.
 *
 * @return The return value of the property.
 */
char *get_class_string_property(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *property_name
) {
    const char *property_signature = "Ljava/lang/String;";
    jfieldID j_field = (*env)->GetFieldID(env, j_class, property_name, property_signature);
    jobject j_result = (*env)->GetObjectField(env, j_object, j_field);

    char *c_string;

    if (j_result) {
        const char *c_temp = (*env)->GetStringUTFChars(env, j_result, 0);
        c_string = strdup(c_temp);
        (*env)->ReleaseStringUTFChars(env, j_result, c_temp);
    } else {
        c_string = strdup("");
    }

    return c_string;
}

/**
 * Return a boolean property value.
 *
 * @param env           The JNI environment.
 * @param j_object      The jobject to call get the property value from.
 * @param j_class       The class the property is defined in.
 * @param property_name The name of the property.
 *
 * @return The return value of the property.
 */
bool get_class_bool_property(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *property_name
) {
    const char *property_signature = "Z";
    jfieldID j_field = (*env)->GetFieldID(env, j_class, property_name, property_signature);
    jboolean j_result = (*env)->GetBooleanField(env, j_object, j_field);

    return j_result;
}

/**
 * Return a int property value.
 *
 * @param env           The JNI environment.
 * @param j_object      The jobject to call get the property value from.
 * @param j_class       The class the property is defined in.
 * @param property_name The name of the property.
 *
 * @return The return value of the property.
 */
int get_class_int_property(
        JNIEnv *env,
        jobject j_object,
        jclass j_class,
        const char *property_name
) {
    const char *property_signature = "I";
    jfieldID j_field = (*env)->GetFieldID(env, j_class, property_name, property_signature);
    jint j_result = (*env)->GetIntField(env, j_object, j_field);

    return j_result;
}

/**
 * Return a jobject property value.
 *
 * @param env           The JNI environment.
 * @param j_object      The jobject to call get the property value from.
 * @param property_name The name of the property.
 *
 * @return The return value of the property.
 */
jobject get_object_property(
        JNIEnv *env,
        jobject j_object,
        const char *property_name,
        const char *property_signature
) {
    jclass j_class = (*env)->GetObjectClass(env, j_object);
    jobject j_result = get_class_object_property(env, j_object, j_class, property_name, property_signature);

    (*env)->DeleteLocalRef(env, j_class);

    return j_result;
}

/**
 * Return a jstring property value as c string.
 *
 * The method will dup the property value and release the jstring directly.
 * Then it returns the duplicated c string.
 *
 * @param env           The JNI environment.
 * @param j_object      The jobject to call get the property value from.
 * @param j_class       The class the property is defined in.
 * @param property_name The name of the property.
 *
 * @return The return value of the property.
 */
char *get_string_property(
        JNIEnv *env,
        jobject j_object,
        const char *property_name
) {
    jclass j_class = (*env)->GetObjectClass(env, j_object);
    char* c_result = get_class_string_property(env, j_object, j_class, property_name);

    (*env)->DeleteLocalRef(env, j_class);

    return c_result;
}

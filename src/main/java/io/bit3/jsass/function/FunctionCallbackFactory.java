package io.bit3.jsass.function;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import io.bit3.jsass.annotation.DefaultBooleanValue;
import io.bit3.jsass.annotation.DefaultByteValue;
import io.bit3.jsass.annotation.DefaultCharacterValue;
import io.bit3.jsass.annotation.DefaultDoubleValue;
import io.bit3.jsass.annotation.DefaultFloatValue;
import io.bit3.jsass.annotation.DefaultIntegerValue;
import io.bit3.jsass.annotation.DefaultLongValue;
import io.bit3.jsass.annotation.DefaultShortValue;
import io.bit3.jsass.annotation.DefaultStringValue;
import io.bit3.jsass.annotation.Name;
import sass.SassLibrary;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Factory that create libsass function callbacks and wrap them into {@see
 * io.bit3.jsass.FunctionWrapper}s.
 */
public class FunctionCallbackFactory {

  /**
   * SASS library adapter.
   */
  private SassLibrary sass;

  public FunctionCallbackFactory(SassLibrary sass) {
    this.sass = sass;
  }

  /**
   * Compile methods from all objects into libsass functions.
   *
   * @param objects A list of "function provider" objects.
   * @return The newly created list of libsass callbacks.
   */
  public List<SassLibrary.Sass_C_Function_Callback> compileFunctions(List<?> objects) {
    List<SassLibrary.Sass_C_Function_Callback> callbacks = new LinkedList<>();

    for (Object object : objects) {
      List<SassLibrary.Sass_C_Function_Callback> objectCallbacks = compileFunctions(object);

      callbacks.addAll(objectCallbacks);
    }

    return callbacks;
  }

  /**
   * Compile methods from an object into libsass functions.
   *
   * @param object The "function provider" object.
   * @return The newly created list of libsass callbacks.
   */
  public List<SassLibrary.Sass_C_Function_Callback> compileFunctions(Object object) {
    Class<?> functionClass = object.getClass();
    Method[] methods = functionClass.getDeclaredMethods();
    List<FunctionDeclaration> declarations = new LinkedList<>();

    for (Method method : methods) {
      int modifiers = method.getModifiers();

      if (!Modifier.isPublic(modifiers)) {
        continue;
      }

      FunctionDeclaration declaration = createDeclaration(object, method);
      declarations.add(declaration);
    }

    List<SassLibrary.Sass_C_Function_Callback> callbacks = new LinkedList<>();

    for (FunctionDeclaration declaration : declarations) {
      FunctionWrapper wrapper = new FunctionWrapper(sass, declaration);
      SassLibrary.Sass_C_Function_Callback callback = sass.sass_make_function(
          declaration.signature,
          wrapper,
          null
      );

      callbacks.add(callback);
    }

    return callbacks;
  }

  /**
   * Convert a java list of libsass function callbacks into a libsass function list.
   *
   * @param callbacks The java list of libsass function callbacks.
   * @return The newly created libsass function list.
   */
  public SassLibrary.Sass_C_Function_List toSassCFunctionList(
      List<SassLibrary.Sass_C_Function_Callback> callbacks) {
    SassLibrary.Sass_C_Function_List functionList = sass.sass_make_function_list(
        new NativeSize(
            callbacks.size()
        )
    );

    int index = 0;
    for (SassLibrary.Sass_C_Function_Callback callback : callbacks) {
      sass.sass_function_set_list_entry(functionList, new NativeSize(index), callback);
      index++;
    }

    return functionList;
  }

  /**
   * Create a function declaration from an object method.
   *
   * @param object The object.
   * @param method The method.
   * @return The newly created function declaration.
   */
  public FunctionDeclaration createDeclaration(Object object, Method method) {
    StringBuilder signature = new StringBuilder();
    Parameter[] parameters = method.getParameters();
    Object[] defaultValues = new Object[parameters.length];

    signature.append(method.getName()).append("(");

    for (int index = 0; index < parameters.length; index++) {
      Parameter parameter = parameters[index];
      String name = getParameterName(parameter);

      if (index > 0) {
        signature.append(", ");
      }

      signature.append("$").append(name);

      Object defaultValue = getDefaultValue(parameter);

      if (null != defaultValue) {
        signature.append(": ").append(formatDefaultValue(defaultValue));
      }

      defaultValues[index] = defaultValue;
    }

    signature.append(")");

    return new FunctionDeclaration(
        signature.toString(),
        object,
        method,
        method.getParameterTypes()
    );
  }

  /**
   * Determine annotated name of a method parameter.
   *
   * @param parameter The method parameter.
   * @return The parameters name.
   */
  private String getParameterName(Parameter parameter) {
    Name annotation = parameter.getAnnotation(Name.class);

    if (null == annotation) {
      return parameter.getName();
    }

    return annotation.value();
  }

  /**
   * Determine annotated default parameter value.
   *
   * @param parameter The method parameter.
   * @return The parameters default value.
   */
  private Object getDefaultValue(Parameter parameter) {
    Class<?> type = parameter.getType();

    if (CharSequence.class.isAssignableFrom(type)) {
      DefaultStringValue defaultStringValue = parameter.getAnnotation(DefaultStringValue.class);

      if (null != defaultStringValue) {
        return defaultStringValue.value();
      }
    }

    if (Byte.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type)) {
      DefaultByteValue defaultByteValue = parameter.getAnnotation(DefaultByteValue.class);

      if (null != defaultByteValue) {
        return defaultByteValue.value();
      }
    }

    if (Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type)) {
      DefaultShortValue defaultShortValue = parameter.getAnnotation(DefaultShortValue.class);

      if (null != defaultShortValue) {
        return defaultShortValue.value();
      }
    }

    if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
      DefaultIntegerValue defaultIntegerValue = parameter.getAnnotation(DefaultIntegerValue.class);

      if (null != defaultIntegerValue) {
        return defaultIntegerValue.value();
      }
    }

    if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
      DefaultLongValue defaultLongValue = parameter.getAnnotation(DefaultLongValue.class);

      if (null != defaultLongValue) {
        return defaultLongValue.value();
      }
    }

    if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
      DefaultFloatValue defaultFloatValue = parameter.getAnnotation(DefaultFloatValue.class);

      if (null != defaultFloatValue) {
        return defaultFloatValue.value();
      }
    }

    if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
      DefaultDoubleValue defaultDoubleValue = parameter.getAnnotation(DefaultDoubleValue.class);

      if (null != defaultDoubleValue) {
        return defaultDoubleValue.value();
      }
    }

    if (Character.class.isAssignableFrom(type) || char.class.isAssignableFrom(type)) {
      DefaultCharacterValue defaultCharacterValue = parameter.getAnnotation(
          DefaultCharacterValue.class
      );

      if (null != defaultCharacterValue) {
        return defaultCharacterValue.value();
      }
    }

    if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
      DefaultBooleanValue defaultBooleanValue = parameter.getAnnotation(DefaultBooleanValue.class);

      if (null != defaultBooleanValue) {
        return defaultBooleanValue.value();
      }
    }

    return null;
  }

  /**
   * Format a default value for libsass function signature.
   *
   * @param value The default value.
   * @return The formated default value.
   */
  private String formatDefaultValue(Object value) {
    if (value instanceof Boolean) {
      return ((Boolean) value) ? "true" : "false";
    }

    if (value instanceof Number) {
      return value.toString();
    }

    if (value instanceof Collection) {
      StringBuilder builder = new StringBuilder();

      builder.append("(");
      boolean first = true;
      for (Object item : ((Collection) value)) {
        if (first) {
          first = false;
        } else {
          builder.append(",");
        }
        builder.append(formatDefaultValue(item));
      }
      builder.append(")");

      return builder.toString();
    }

    // fallback to quoted string
    String string = value.toString();
    string = sass.sass_string_unquote(string).getString(0);
    return sass.sass_string_quote(string, (byte) '\'').getString(0);
  }
}

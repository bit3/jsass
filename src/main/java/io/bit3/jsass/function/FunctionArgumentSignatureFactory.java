package io.bit3.jsass.function;

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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

public class FunctionArgumentSignatureFactory {

  /**
   * Create a new factory.
   */
  public List<FunctionArgumentSignature> createDefaultArgumentSignature(Method method,
                                                                        Parameter parameter) {
    List<FunctionArgumentSignature> list = new LinkedList<>();

    String name = getParameterName(parameter);
    Object defaultValue = getDefaultValue(parameter);
    list.add(new FunctionArgumentSignature(name, defaultValue));

    return list;
  }

  /**
   * Determine annotated name of a method parameter.
   *
   * @param parameter The method parameter.
   * @return The parameters name.
   */
  public String getParameterName(Parameter parameter) {
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
  public Object getDefaultValue(Parameter parameter) {
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
}

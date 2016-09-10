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
import io.bit3.jsass.type.TypeUtils;

import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

public class FunctionArgumentSignatureFactory {

  /**
   * Create a new factory.
   */
  public List<FunctionArgumentSignature> createDefaultArgumentSignature(Parameter parameter) {
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

    if (TypeUtils.isaString(type)) {
      return getStringDefaultValue(parameter);
    }

    if (TypeUtils.isaByte(type)) {
      return getByteDefaultValue(parameter);
    }

    if (TypeUtils.isaShort(type)) {
      return getShortDefaultValue(parameter);
    }

    if (TypeUtils.isaInteger(type)) {
      return getIntegerDefaultValue(parameter);
    }

    if (TypeUtils.isaLong(type)) {
      return getLongDefaultValue(parameter);
    }

    if (TypeUtils.isaFloat(type)) {
      return getFloatDefaultValue(parameter);
    }

    if (TypeUtils.isaDouble(type)) {
      return getDoubleDefaultValue(parameter);
    }

    if (TypeUtils.isaCharacter(type)) {
      return getCharacterDefaultValue(parameter);
    }

    if (TypeUtils.isaBoolean(type)) {
      return getBooleanDefaultValue(parameter);
    }

    return null;
  }

  private static Object getStringDefaultValue(Parameter parameter) {
    DefaultStringValue defaultStringValue = parameter.getAnnotation(DefaultStringValue.class);

    if (null != defaultStringValue) {
      return defaultStringValue.value();
    }

    return null;
  }

  private static Object getByteDefaultValue(Parameter parameter) {
    DefaultByteValue defaultByteValue = parameter.getAnnotation(DefaultByteValue.class);

    if (null != defaultByteValue) {
      return defaultByteValue.value();
    }

    return null;
  }

  private static Object getShortDefaultValue(Parameter parameter) {
    DefaultShortValue defaultShortValue = parameter.getAnnotation(DefaultShortValue.class);

    if (null != defaultShortValue) {
      return defaultShortValue.value();
    }

    return null;
  }

  private static Object getIntegerDefaultValue(Parameter parameter) {
    DefaultIntegerValue defaultIntegerValue = parameter.getAnnotation(DefaultIntegerValue.class);

    if (null != defaultIntegerValue) {
      return defaultIntegerValue.value();
    }

    return null;
  }

  private static Object getLongDefaultValue(Parameter parameter) {
    DefaultLongValue defaultLongValue = parameter.getAnnotation(DefaultLongValue.class);

    if (null != defaultLongValue) {
      return defaultLongValue.value();
    }

    return null;
  }

  private static Object getFloatDefaultValue(Parameter parameter) {
    DefaultFloatValue defaultFloatValue = parameter.getAnnotation(DefaultFloatValue.class);

    if (null != defaultFloatValue) {
      return defaultFloatValue.value();
    }

    return null;
  }

  private static Object getDoubleDefaultValue(Parameter parameter) {
    DefaultDoubleValue defaultDoubleValue = parameter.getAnnotation(DefaultDoubleValue.class);

    if (null != defaultDoubleValue) {
      return defaultDoubleValue.value();
    }

    return null;
  }

  private static Object getCharacterDefaultValue(Parameter parameter) {
    DefaultCharacterValue defaultCharacterValue = parameter.getAnnotation(
        DefaultCharacterValue.class
    );

    if (null != defaultCharacterValue) {
      return defaultCharacterValue.value();
    }

    return null;
  }

  private static Object getBooleanDefaultValue(Parameter parameter) {
    DefaultBooleanValue defaultBooleanValue = parameter.getAnnotation(DefaultBooleanValue.class);

    if (null != defaultBooleanValue) {
      return defaultBooleanValue.value();
    }

    return null;
  }
}

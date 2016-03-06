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

    if (isaString(type)) {
      return getStringDefaultValue(parameter);
    }

    if (isaByte(type)) {
      return getByteDefaultValue(parameter);
    }

    if (isaShort(type)) {
      return getShortDefaultValue(parameter);
    }

    if (isaInteger(type)) {
      return getIntegerDefaultValue(parameter);
    }

    if (isaLong(type)) {
      return getLongDefaultValue(parameter);
    }

    if (isaFloat(type)) {
      return getFloatDefaultValue(parameter);
    }

    if (isaDouble(type)) {
      return getDoubleDefaultValue(parameter);
    }

    if (isaCharacter(type)) {
      return getCharacterDefaultValue(parameter);
    }

    if (isaBoolean(type)) {
      return getBooleanDefaultValue(parameter);
    }

    return null;
  }

  private boolean isaString(Class<?> type) {
    return CharSequence.class.isAssignableFrom(type);
  }

  private Object getStringDefaultValue(Parameter parameter) {
    DefaultStringValue defaultStringValue = parameter.getAnnotation(DefaultStringValue.class);

    if (null != defaultStringValue) {
      return defaultStringValue.value();
    }

    return null;
  }

  private boolean isaByte(Class<?> type) {
    return Byte.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type);
  }

  private Object getByteDefaultValue(Parameter parameter) {
    DefaultByteValue defaultByteValue = parameter.getAnnotation(DefaultByteValue.class);

    if (null != defaultByteValue) {
      return defaultByteValue.value();
    }

    return null;
  }

  private boolean isaShort(Class<?> type) {
    return Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type);
  }

  private Object getShortDefaultValue(Parameter parameter) {
    DefaultShortValue defaultShortValue = parameter.getAnnotation(DefaultShortValue.class);

    if (null != defaultShortValue) {
      return defaultShortValue.value();
    }

    return null;
  }

  private boolean isaInteger(Class<?> type) {
    return Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type);
  }

  private Object getIntegerDefaultValue(Parameter parameter) {
    DefaultIntegerValue defaultIntegerValue = parameter.getAnnotation(DefaultIntegerValue.class);

    if (null != defaultIntegerValue) {
      return defaultIntegerValue.value();
    }

    return null;
  }

  private boolean isaLong(Class<?> type) {
    return Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type);
  }

  private Object getLongDefaultValue(Parameter parameter) {
    DefaultLongValue defaultLongValue = parameter.getAnnotation(DefaultLongValue.class);

    if (null != defaultLongValue) {
      return defaultLongValue.value();
    }

    return null;
  }

  private boolean isaFloat(Class<?> type) {
    return Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type);
  }

  private Object getFloatDefaultValue(Parameter parameter) {
    DefaultFloatValue defaultFloatValue = parameter.getAnnotation(DefaultFloatValue.class);

    if (null != defaultFloatValue) {
      return defaultFloatValue.value();
    }

    return null;
  }

  private boolean isaDouble(Class<?> type) {
    return Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type);
  }

  private Object getDoubleDefaultValue(Parameter parameter) {
    DefaultDoubleValue defaultDoubleValue = parameter.getAnnotation(DefaultDoubleValue.class);

    if (null != defaultDoubleValue) {
      return defaultDoubleValue.value();
    }

    return null;
  }

  private boolean isaCharacter(Class<?> type) {
    return Character.class.isAssignableFrom(type) || char.class.isAssignableFrom(type);
  }

  private Object getCharacterDefaultValue(Parameter parameter) {
    DefaultCharacterValue defaultCharacterValue = parameter.getAnnotation(
        DefaultCharacterValue.class
    );

    if (null != defaultCharacterValue) {
      return defaultCharacterValue.value();
    }

    return null;
  }

  private boolean isaBoolean(Class<?> type) {
    return Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type);
  }

  private Object getBooleanDefaultValue(Parameter parameter) {
    DefaultBooleanValue defaultBooleanValue = parameter.getAnnotation(DefaultBooleanValue.class);

    if (null != defaultBooleanValue) {
      return defaultBooleanValue.value();
    }

    return null;
  }
}

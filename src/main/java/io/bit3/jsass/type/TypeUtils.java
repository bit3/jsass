package io.bit3.jsass.type;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper class to convert libsass to java values and vise versa.
 */
public final class TypeUtils {
  private TypeUtils() {
  }

  /**
   * Try to convert "any" java object into a responsible sass value.
   */
  public static SassValue convertToSassValue(Object value) {
    if (null == value) {
      return SassNull.SINGLETON;
    }

    if (value instanceof SassValue) {
      return (SassValue) value;
    }

    Class cls = value.getClass();

    if (isaBoolean(cls)) {
      return new SassBoolean((Boolean) value);
    }

    if (isaNumber(cls)) {
      return new SassNumber(((Number) value).doubleValue(), "");
    }

    if (isaString(cls) || isaCharacter(cls)) {
      return new SassString(value.toString());
    }

    if (value instanceof Collection) {
      return new SassList(
          ((Collection<?>) value)
              .stream()
              .map(TypeUtils::convertToSassValue)
              .collect(Collectors.toList())
      );
    }

    if (value instanceof Map) {
      return ((Map<?, ?>) value).entrySet()
          .stream()
          .collect(Collectors.toMap(
              entry -> entry.getKey().toString(),
              entry -> TypeUtils.convertToSassValue(entry.getValue()),
              (origin, duplicate) -> origin,
              SassMap::new
          ));
    }

    if (value instanceof Throwable) {
      Throwable throwable = (Throwable) value;
      StringWriter stringWriter = new StringWriter();
      PrintWriter printWriter = new PrintWriter(stringWriter);

      String message = throwable.getMessage();
      if (StringUtils.isNotEmpty(message)) {
        printWriter.append(message).append(System.lineSeparator());
      }
      throwable.printStackTrace(printWriter);

      return new SassError(stringWriter.toString());
    }

    return new SassError(
        String.format(
            "Could not convert object of type %s into a sass value",
            value.getClass().toString()
        )
    );
  }

  private static boolean isaNumber(Class cls) {
    return Number.class.isAssignableFrom(cls)
        || byte.class.isAssignableFrom(cls)
        || short.class.isAssignableFrom(cls)
        || int.class.isAssignableFrom(cls)
        || long.class.isAssignableFrom(cls)
        || float.class.isAssignableFrom(cls)
        || double.class.isAssignableFrom(cls);
  }

  public static boolean isaString(Class<?> type) {
    return CharSequence.class.isAssignableFrom(type);
  }

  public static boolean isaByte(Class<?> type) {
    return Byte.class.isAssignableFrom(type) || byte.class.isAssignableFrom(type);
  }

  public static boolean isaShort(Class<?> type) {
    return Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type);
  }

  public static boolean isaInteger(Class<?> type) {
    return Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type);
  }

  public static boolean isaLong(Class<?> type) {
    return Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type);
  }

  public static boolean isaFloat(Class<?> type) {
    return Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type);
  }

  public static boolean isaDouble(Class<?> type) {
    return Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type);
  }

  public static boolean isaCharacter(Class<?> type) {
    return Character.class.isAssignableFrom(type) || char.class.isAssignableFrom(type);
  }

  public static boolean isaBoolean(Class<?> type) {
    return Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type);
  }
}

package io.bit3.jsass.type;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper class to convert libsass to java values and vise versa.
 */
public class TypeUtils {
  /**
   * Try to convert "any" java object into a responsible sass value.
   */
  public static SassValue convertToSassValue(Object value) {
    if (null == value) {
      return new SassNull();
    }

    if (value instanceof SassValue) {
      return (SassValue) value;
    }

    Class cls = value.getClass();

    if (boolean.class.isAssignableFrom(cls) || value instanceof Boolean) {
      return new SassBoolean((Boolean) value);
    }

    if (
        byte.class.isAssignableFrom(cls)
            || short.class.isAssignableFrom(cls)
            || int.class.isAssignableFrom(cls)
            || long.class.isAssignableFrom(cls)
            || float.class.isAssignableFrom(cls)
            || double.class.isAssignableFrom(cls)
            || value instanceof Number
        ) {
      return new SassNumber(((Number) value).doubleValue(), "");
    }

    if (char.class.isAssignableFrom(cls) || value instanceof CharSequence) {
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
      SassMap map = new SassMap();

      for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
        String key = entry.getKey().toString();
        SassValue item = TypeUtils.convertToSassValue(entry.getValue());

        map.put(key, item);
      }

      return map;
    }

    if (value instanceof Exception) {
      return new SassError(((Exception) value).getMessage());
    }

    return new SassError(
        String.format(
            "Could not convert object of type %s into a sass value",
            value.getClass().toString()
        )
    );
  }
}

package de.bit3.jsass.type;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import de.bit3.jsass.CompilationException;
import de.bit3.jsass.Separator;
import sass.SassLibrary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to convert libsass to java values and vise versa.
 */
public class TypeUtils {

  /**
   * Convert a libsass value into java.
   *
   * @param sass  The SASS library adapter.
   * @param value The libsass value.
   * @return The corresponding java value.
   * @throws CompilationException If the value cannot be converted.
   */
  public static Object decodeValue(SassLibrary sass, SassLibrary.Sass_Value value)
      throws CompilationException {
    if (sass.sass_value_is_null(value) > 0) {
      return null;
    }

    if (sass.sass_value_is_number(value) > 0) {
      return decodeNumber(sass, value);
    }

    if (sass.sass_value_is_string(value) > 0) {
      return decodeString(sass, value);
    }

    if (sass.sass_value_is_boolean(value) > 0) {
      return decodeBoolean(sass, value);
    }

    if (sass.sass_value_is_color(value) > 0) {
      return decodeColor(sass, value);
    }

    if (sass.sass_value_is_list(value) > 0) {
      return decodeList(sass, value);
    }

    if (sass.sass_value_is_map(value) > 0) {
      return decodeMap(sass, value);
    }

    throw new CompilationException(-1, "SASS value has an unknown type");
  }

  /**
   * Convert a libsass number into a java number.
   *
   * @param sass  The SASS library adapter.
   * @param value The libsass number.
   * @return The corresponding java number.
   */
  public static SassNumber decodeNumber(SassLibrary sass, SassLibrary.Sass_Value value) {
    double number = sass.sass_number_get_value(value);
    String unit = sass.sass_number_get_unit(value);

    return new SassNumber(number, unit);
  }

  /**
   * Convert a libsass string into a java string.
   *
   * @param sass  The SASS library adapter.
   * @param value The libsass string.
   * @return The corresponding java string.
   */
  public static SassString decodeString(SassLibrary sass, SassLibrary.Sass_Value value) {
    String string = sass.sass_string_get_value(value);
    String unquoted = sass.sass_string_unquote(string).getString(0);

    boolean quoted = !string.equals(unquoted);

    return new SassString(unquoted, quoted);
  }

  /**
   * Convert a libsass boolean into a java boolean.
   *
   * @param sass  The SASS library adapter.
   * @param value The libsass boolean.
   * @return The corresponding java boolean.
   */
  public static Boolean decodeBoolean(SassLibrary sass, SassLibrary.Sass_Value value) {
    return sass.sass_boolean_get_value(value) > 0;
  }

  /**
   * Convert a libsass color into a java color.
   *
   * @param sass  The SASS library adapter.
   * @param value The libsass color.
   * @return The corresponding java color.
   */
  public static SassColor decodeColor(SassLibrary sass, SassLibrary.Sass_Value value) {
    double red = sass.sass_color_get_r(value);
    double green = sass.sass_color_get_g(value);
    double blue = sass.sass_color_get_b(value);
    double alpha = sass.sass_color_get_a(value);

    return new SassColor(red, green, blue, alpha);
  }

  /**
   * Convert a libsass list into a java list.
   *
   * @param sass  The SASS library adapter.
   * @param value The libsass list.
   * @return The corresponding java list.
   */
  public static SassList decodeList(SassLibrary sass, SassLibrary.Sass_Value value)
      throws CompilationException {
    long size = sass.sass_list_get_length(value).longValue();
    int separator = sass.sass_list_get_separator(value);

    SassList sassList = new SassList();

    if (SassLibrary.Sass_Separator.SASS_SPACE == separator) {
      sassList.setSeparator(Separator.SPACE);
    } else {
      sassList.setSeparator(Separator.COMMA);
    }

    for (int index = 0; index < size; index++) {
      SassLibrary.Sass_Value item = sass.sass_list_get_value(value, new NativeSize(index));
      Object decodedItem = decodeValue(sass, item);
      sassList.add(decodedItem);
    }

    return sassList;
  }

  /**
   * Convert a libsass map into a java map.
   *
   * @param sass  The SASS library adapter.
   * @param value The libsass map.
   * @return The corresponding java map.
   */
  public static Map<?, ?> decodeMap(SassLibrary sass, SassLibrary.Sass_Value value)
      throws CompilationException {
    long size = sass.sass_map_get_length(value).longValue();

    Map<Object, Object> map = new HashMap<>();

    for (int index = 0; index < size; index++) {
      NativeSize nativeIndex = new NativeSize(index);

      SassLibrary.Sass_Value itemKey = sass.sass_map_get_key(value, nativeIndex);
      SassLibrary.Sass_Value itemValue = sass.sass_map_get_value(value, nativeIndex);

      Object decodedKey = decodeValue(sass, itemKey);
      Object decodedValue = decodeValue(sass, itemValue);

      map.put(decodedKey, decodedValue);
    }

    return map;
  }

  /**
   * Convert a java value into a libsass value.
   *
   * @param sass  The SASS library adapter.
   * @param value The java value.
   * @return The corresponding libsass value.
   */
  public static SassLibrary.Sass_Value encodeValue(SassLibrary sass, Object value)
      throws CompilationException {
    if (null == value) {
      return sass.sass_make_null();
    }

    if (value instanceof Number) {
      return encodeNumber(sass, (Number) value);
    }

    if (value instanceof CharSequence) {
      return encodeString(sass, (CharSequence) value);
    }

    if (value instanceof Boolean) {
      return encodeBoolean(sass, (Boolean) value);
    }

    if (value instanceof SassColor) {
      return encodeColor(sass, (SassColor) value);
    }

    if (value instanceof Collection) {
      return encodeList(sass, (Collection<?>) value);
    }

    if (value instanceof Map) {
      return encodeMap(sass, (Map<?, ?>) value);
    }

    throw new CompilationException(-1, String
        .format("Java object %s cannot be converted to SASS", value.getClass().getName()));
  }

  /**
   * Convert a java number into a libsass number.
   *
   * @param sass   The SASS library adapter.
   * @param number The java number.
   * @return The corresponding libsass number.
   */
  public static SassLibrary.Sass_Value encodeNumber(SassLibrary sass, Number number) {
    double value = number.doubleValue();
    String unit = "";

    if (number instanceof SassNumber) {
      unit = ((SassNumber) number).getUnit();
    }

    return sass.sass_make_number(value, unit);
  }

  /**
   * Convert a java string into a libsass string.
   *
   * @param sass     The SASS library adapter.
   * @param sequence The java string.
   * @return The corresponding libsass string.
   */
  public static SassLibrary.Sass_Value encodeString(SassLibrary sass, CharSequence sequence) {
    String value = sequence.toString();

    if (sequence instanceof SassString) {
      SassString string = (SassString) sequence;

      if (string.isQuoted()) {
        value = sass.sass_string_quote(value, (byte) string.getQuote()).getString(0);
      }
    } else {
      value = sass.sass_string_quote(value, (byte) '\'').getString(0);
    }

    return sass.sass_make_string(value);
  }

  /**
   * Convert a java boolean into a libsass boolean.
   *
   * @param sass The SASS library adapter.
   * @param bool The java boolean.
   * @return The corresponding libsass boolean.
   */
  public static SassLibrary.Sass_Value encodeBoolean(SassLibrary sass, Boolean bool) {
    return sass.sass_make_boolean(bool ? (byte) 1 : 0);
  }

  /**
   * Convert a java color into a libsass color.
   *
   * @param sass  The SASS library adapter.
   * @param color The java color.
   * @return The corresponding libsass color.
   */
  public static SassLibrary.Sass_Value encodeColor(SassLibrary sass, SassColor color) {
    double red = color.getRed();
    double green = color.getGreen();
    double blue = color.getBlue();
    double alpha = color.getAlpha();

    return sass.sass_make_color(red, green, blue, alpha);
  }

  /**
   * Convert a java list into a libsass list.
   *
   * @param sass The SASS library adapter.
   * @param list The java list.
   * @return The corresponding libsass list.
   */
  public static SassLibrary.Sass_Value encodeList(SassLibrary sass, Collection<?> list)
      throws CompilationException {
    int libsassSeparator = SassLibrary.Sass_Separator.SASS_COMMA;

    if (list instanceof SassList) {
      Separator sep = ((SassList) list).getSeparator();
      switch (sep) {
        case COMMA:
          // use existing value
          break;

        case SPACE:
          libsassSeparator = SassLibrary.Sass_Separator.SASS_SPACE;
          break;

        default:
          throw new IllegalAccessError(
              String.format(
                  "The separator \"%s\" cannot be converted to any libsass separator",
                  (null == sep ? "null" : sep.toString())
              )
          );
      }
    }

    SassLibrary.Sass_Value
        value =
        sass.sass_make_list(new NativeSize(list.size()), libsassSeparator);

    int index = 0;
    for (Object item : list) {
      sass.sass_list_set_value(value, new NativeSize(index), encodeValue(sass, item));
      index++;
    }

    return value;
  }

  /**
   * Convert a java map into a libsass map.
   *
   * @param sass The SASS library adapter.
   * @param map  The java map.
   * @return The corresponding libsass map.
   */
  public static SassLibrary.Sass_Value encodeMap(SassLibrary sass, Map<?, ?> map)
      throws CompilationException {
    SassLibrary.Sass_Value value = sass.sass_make_map(new NativeSize(map.size()));

    int index = 0;
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      SassLibrary.Sass_Value itemKey = encodeValue(sass, entry.getKey());
      SassLibrary.Sass_Value itemValue = encodeValue(sass, entry.getValue());
      NativeSize nativeIndex = new NativeSize(index);

      sass.sass_map_set_key(value, nativeIndex, itemKey);
      sass.sass_map_set_value(value, nativeIndex, itemValue);
      index++;
    }

    return value;
  }
}

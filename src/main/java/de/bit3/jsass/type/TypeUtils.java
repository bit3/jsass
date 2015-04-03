package de.bit3.jsass.type;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import de.bit3.jsass.CompilationException;
import de.bit3.jsass.Separator;
import sass.SassLibrary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TypeUtils {
    public static Object decodeValue(SassLibrary SASS, SassLibrary.Sass_Value value) throws CompilationException {
        if (SASS.sass_value_is_null(value) > 0) {
            return null;
        }

        if (SASS.sass_value_is_number(value) > 0) {
            return decodeNumber(SASS, value);
        }

        if (SASS.sass_value_is_string(value) > 0) {
            return decodeString(SASS, value);
        }

        if (SASS.sass_value_is_boolean(value) > 0) {
            return decodeBoolean(SASS, value);
        }

        if (SASS.sass_value_is_color(value) > 0) {
            return decodeColor(SASS, value);
        }

        if (SASS.sass_value_is_list(value) > 0) {
            return decodeList(SASS, value);
        }

        if (SASS.sass_value_is_map(value) > 0) {
            return decodeMap(SASS, value);
        }

        throw new CompilationException(-1, "SASS value has an unknown type");
    }

    public static SassNumber decodeNumber(SassLibrary SASS, SassLibrary.Sass_Value value) {
        double number = SASS.sass_number_get_value(value);
        String unit   = SASS.sass_number_get_unit(value);

        return new SassNumber(number, unit);
    }

    public static SassString decodeString(SassLibrary SASS, SassLibrary.Sass_Value value) {
        String string   = SASS.sass_string_get_value(value);
        String unquoted = SASS.sass_string_unquote(string).getString(0);

        boolean quoted = !string.equals(unquoted);

        return new SassString(unquoted, quoted);
    }

    public static Boolean decodeBoolean(SassLibrary SASS, SassLibrary.Sass_Value value) {
        return SASS.sass_boolean_get_value(value) > 0;
    }

    public static SassColor decodeColor(SassLibrary SASS, SassLibrary.Sass_Value value) {
        double red   = SASS.sass_color_get_r(value);
        double green = SASS.sass_color_get_g(value);
        double blue  = SASS.sass_color_get_b(value);
        double alpha = SASS.sass_color_get_a(value);

        return new SassColor(red, green, blue, alpha);
    }

    public static SassList decodeList(SassLibrary SASS, SassLibrary.Sass_Value value) throws CompilationException {
        long size      = SASS.sass_list_get_length(value).longValue();
        int  separator = SASS.sass_list_get_separator(value);

        SassList sassList = new SassList();

        if (SassLibrary.Sass_Separator.SASS_SPACE == separator) {
            sassList.setSeparator(Separator.SPACE);
        } else {
            sassList.setSeparator(Separator.COMMA);
        }

        for (int index = 0; index < size; index++) {
            SassLibrary.Sass_Value item = SASS.sass_list_get_value(value, new NativeSize(index));
            Object decodedItem = decodeValue(SASS, item);
            sassList.add(decodedItem);
        }

        return sassList;
    }

    public static Map<?, ?> decodeMap(SassLibrary SASS, SassLibrary.Sass_Value value) throws CompilationException {
        long size = SASS.sass_map_get_length(value).longValue();

        Map<Object, Object> map = new HashMap<>();

        for (int index = 0; index < size; index++) {
            NativeSize nativeIndex = new NativeSize(index);

            SassLibrary.Sass_Value itemKey = SASS.sass_map_get_key(value, nativeIndex);
            SassLibrary.Sass_Value itemValue = SASS.sass_map_get_value(value, nativeIndex);

            Object decodedKey = decodeValue(SASS, itemKey);
            Object decodedValue = decodeValue(SASS, itemValue);

            map.put(decodedKey, decodedValue);
        }

        return map;
    }

    public static SassLibrary.Sass_Value encodeValue(SassLibrary SASS, Object value) throws CompilationException {
        if (null == value) {
            return SASS.sass_make_null();
        }

        if (value instanceof Number) {
            return encodeNumber(SASS, (Number) value);
        }

        if (value instanceof CharSequence) {
            return encodeString(SASS, (CharSequence) value);
        }

        if (value instanceof Boolean) {
            return encodeBoolean(SASS, (Boolean) value);
        }

        if (value instanceof SassColor) {
            return encodeColor(SASS, (SassColor) value);
        }

        if (value instanceof Collection) {
            return encodeList(SASS, (Collection<?>) value);
        }

        if (value instanceof Map) {
            return encodeMap(SASS, (Map<?, ?>) value);
        }

        throw new CompilationException(-1, String.format("Java object %s cannot be converted to SASS", value.getClass().getName()));
    }

    public static SassLibrary.Sass_Value encodeNumber(SassLibrary SASS, Number number) {
        double value = number.doubleValue();
        String unit  = "";

        if (number instanceof SassNumber) {
            unit = ((SassNumber) number).getUnit();
        }

        return SASS.sass_make_number(value, unit);
    }

    public static SassLibrary.Sass_Value encodeString(SassLibrary SASS, CharSequence sequence) {
        String value = sequence.toString();

        if (sequence instanceof SassString) {
            SassString string = (SassString) sequence;

            if (string.isQuoted()) {
                value = SASS.sass_string_quote(value, (byte) string.getQuote()).getString(0);
            }
        } else {
            value = SASS.sass_string_quote(value, (byte) '\'').getString(0);
        }

        return SASS.sass_make_string(value);
    }

    public static SassLibrary.Sass_Value encodeBoolean(SassLibrary SASS, Boolean bool) {
        return SASS.sass_make_boolean(bool ? (byte) 1 : 0);
    }

    public static SassLibrary.Sass_Value encodeColor(SassLibrary SASS, SassColor color) {
        double red   = color.getRed();
        double green = color.getGreen();
        double blue  = color.getBlue();
        double alpha = color.getAlpha();

        return SASS.sass_make_color(red, green, blue, alpha);
    }

    public static SassLibrary.Sass_Value encodeList(SassLibrary SASS, Collection<?> list) throws CompilationException {
        int separator = SassLibrary.Sass_Separator.SASS_COMMA;

        if (list instanceof SassList) {
            switch (((SassList) list).getSeparator()) {
                case SPACE:
                    separator = SassLibrary.Sass_Separator.SASS_SPACE;
                    break;
            }
        }

        SassLibrary.Sass_Value value = SASS.sass_make_list(new NativeSize(list.size()), separator);

        int index = 0;
        for (Object item : list) {
            SASS.sass_list_set_value(value, new NativeSize(index), encodeValue(SASS, item));
            index++;
        }

        return value;
    }

    public static SassLibrary.Sass_Value encodeMap(SassLibrary SASS, Map<?, ?> map) throws CompilationException {
        SassLibrary.Sass_Value value = SASS.sass_make_map(new NativeSize(map.size()));

        int index = 0;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            SassLibrary.Sass_Value itemKey = encodeValue(SASS, entry.getKey());
            SassLibrary.Sass_Value itemValue = encodeValue(SASS, entry.getValue());
            NativeSize nativeIndex = new NativeSize(index);

            SASS.sass_map_set_key(value, nativeIndex, itemKey);
            SASS.sass_map_set_value(value, nativeIndex, itemValue);
            index++;
        }

        return value;
    }
}

package io.bit3.jsass.function.arguments;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.importer.Import;

import java.util.List;

public class FloatArgumentConverter implements ArgumentConverter {

  @Override
  public Object convert(
      List<?> remainingArguments, Import lastImport, Context context
  ) {
    if (remainingArguments.isEmpty()) {
      return null;
    }

    Object value = remainingArguments.remove(0);

    // ignore null value
    if (null == value) {
      return null;
    }

    // value is already in a compatible type
    if (
        Float.class.isAssignableFrom(value.getClass())
        || float.class.isAssignableFrom(value.getClass())
        ) {
      return value;
    }

    if (value instanceof Number) {
      value = ((Number) value).floatValue();
    } else {
      value = Float.parseFloat(value.toString());
    }

    return value;
  }
}

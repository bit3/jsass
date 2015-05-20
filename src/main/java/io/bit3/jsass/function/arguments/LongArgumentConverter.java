package io.bit3.jsass.function.arguments;

import io.bit3.jsass.context.Context;

import java.util.List;

public class LongArgumentConverter implements ArgumentConverter {

  @Override
  public Object convert(
      List<?> remainingArguments, Context context
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
        Long.class.isAssignableFrom(value.getClass())
        || long.class.isAssignableFrom(value.getClass())
        ) {
      return value;
    }

    if (value instanceof Number) {
      value = ((Number) value).longValue();
    } else {
      value = Long.parseLong(value.toString());
    }

    return value;
  }
}

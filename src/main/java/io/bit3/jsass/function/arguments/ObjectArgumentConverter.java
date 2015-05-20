package io.bit3.jsass.function.arguments;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.importer.Import;

import java.util.List;

public class ObjectArgumentConverter implements ArgumentConverter {

  private final Class<?> targetType;

  public ObjectArgumentConverter(Class<?> targetType) {
    this.targetType = targetType;
  }

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

    if (targetType.isAssignableFrom(value.getClass())) {
      return value;
    }

    throw new RuntimeException(
        String.format(
            "Cannot convert SASS type %s to Java type %s",
            value.getClass().getName(),
            targetType.getName()
        )
    );
  }
}

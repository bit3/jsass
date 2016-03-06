package io.bit3.jsass.function.arguments.converter;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.function.FunctionArgumentSignature;
import io.bit3.jsass.function.FunctionArgumentSignatureFactory;
import io.bit3.jsass.type.SassNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class ObjectArgumentConverter implements ArgumentConverter {

  private final Class<?> targetType;

  public ObjectArgumentConverter(Class<?> targetType) {
    this.targetType = targetType;
  }

  @Override
  public Object convert(
      List<?> remainingArguments, ImportStack importStack, Context context) {
    if (remainingArguments.isEmpty()) {
      return null;
    }

    Object value = remainingArguments.remove(0);

    // ignore null value
    if (null == value || value instanceof SassNull) {
      return null;
    }

    if (targetType.isAssignableFrom(value.getClass())) {
      return value;
    }

    throw new ConversionException(
        String.format(
            "Cannot convert SASS type %s to Java type %s",
            value.getClass().getName(),
            targetType.getName()
        )
    );
  }

  @Override
  public List<FunctionArgumentSignature> argumentSignatures(
      Object object, Method method, Parameter parameter, FunctionArgumentSignatureFactory factory
  ) {
    return factory.createDefaultArgumentSignature(parameter);
  }
}

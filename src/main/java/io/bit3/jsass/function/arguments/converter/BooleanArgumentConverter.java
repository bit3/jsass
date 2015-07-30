package io.bit3.jsass.function.arguments.converter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.function.FunctionArgumentSignature;
import io.bit3.jsass.function.FunctionArgumentSignatureFactory;
import io.bit3.jsass.type.SassBoolean;
import io.bit3.jsass.type.SassNull;

public class BooleanArgumentConverter implements ArgumentConverter {

  @Override
  public Object convert(
      List<?> remainingArguments, Context context
  ) {
    if (remainingArguments.isEmpty()) {
      return null;
    }

    Object value = remainingArguments.remove(0);

    // ignore null value
    if (null == value || value instanceof SassNull) {
      return null;
    }

    // value is sass value
    if (value instanceof SassBoolean) {
      return ((SassBoolean) value).getValue();
    }

    // value is already in a compatible type
    if (
        Boolean.class.isAssignableFrom(value.getClass())
            || byte.class.isAssignableFrom(value.getClass())
        ) {
      return value;
    }

    return Boolean.valueOf(value.toString());
  }

  @Override
  public List<FunctionArgumentSignature> argumentSignatures(
      Object object, Method method, Parameter parameter, FunctionArgumentSignatureFactory factory
  ) {
    return factory.createDefaultArgumentSignature(method, parameter);
  }
}

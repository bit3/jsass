package io.bit3.jsass.function.arguments.converter;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.function.FunctionArgumentSignature;
import io.bit3.jsass.function.FunctionArgumentSignatureFactory;
import io.bit3.jsass.type.SassNull;
import io.bit3.jsass.type.TypeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class IntegerArgumentConverter implements ArgumentConverter {

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

    // value is already in a compatible type
    if (TypeUtils.isaInteger(value.getClass())) {
      return value;
    }

    if (value instanceof Number) {
      value = ((Number) value).intValue();
    } else {
      value = Integer.parseInt(value.toString());
    }

    return value;
  }

  @Override
  public List<FunctionArgumentSignature> argumentSignatures(
      Object object, Method method, Parameter parameter, FunctionArgumentSignatureFactory factory
  ) {
    return factory.createDefaultArgumentSignature(parameter);
  }
}

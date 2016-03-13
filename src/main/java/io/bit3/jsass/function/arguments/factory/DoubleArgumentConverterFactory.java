package io.bit3.jsass.function.arguments.factory;

import io.bit3.jsass.function.arguments.converter.ArgumentConverter;
import io.bit3.jsass.function.arguments.converter.DoubleArgumentConverter;
import io.bit3.jsass.type.TypeUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class DoubleArgumentConverterFactory implements ArgumentConverterFactory {

  @Override
  public boolean canHandle(Class<?> targetType) {
    return TypeUtils.isaDouble(targetType);
  }

  @Override
  public ArgumentConverter create(Object object, Method method, Parameter parameter) {
    return new DoubleArgumentConverter();
  }
}

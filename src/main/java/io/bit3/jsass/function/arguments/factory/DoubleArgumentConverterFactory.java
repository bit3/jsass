package io.bit3.jsass.function.arguments.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import io.bit3.jsass.function.arguments.converter.ArgumentConverter;
import io.bit3.jsass.function.arguments.converter.DoubleArgumentConverter;

public class DoubleArgumentConverterFactory implements ArgumentConverterFactory {

  @Override
  public boolean canHandle(Class<?> targetType) {
    return Double.class.isAssignableFrom(targetType)
        || double.class.isAssignableFrom(targetType);
  }

  @Override
  public ArgumentConverter create(Object object, Method method, Parameter parameter) {
    return new DoubleArgumentConverter();
  }
}

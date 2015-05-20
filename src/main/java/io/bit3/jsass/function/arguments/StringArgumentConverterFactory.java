package io.bit3.jsass.function.arguments;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class StringArgumentConverterFactory implements ArgumentConverterFactory {

  @Override
  public boolean canHandle(Class<?> targetType) {
    return String.class.isAssignableFrom(targetType);
  }

  @Override
  public ArgumentConverter create(Object object, Method method, Parameter parameter) {
    return new StringArgumentConverter();
  }
}

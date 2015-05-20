package io.bit3.jsass.function.arguments;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class IntegerArgumentConverterFactory implements ArgumentConverterFactory {

  @Override
  public boolean canHandle(Class<?> targetType) {
    return Integer.class.isAssignableFrom(targetType)
           || int.class.isAssignableFrom(targetType);
  }

  @Override
  public ArgumentConverter create(Object object, Method method, Parameter parameter) {
    return new IntegerArgumentConverter();
  }
}

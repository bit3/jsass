package io.bit3.jsass.function.arguments.factory;

import io.bit3.jsass.function.arguments.converter.ArgumentConverter;
import io.bit3.jsass.function.arguments.converter.ShortArgumentConverter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ShortArgumentConverterFactory implements ArgumentConverterFactory {

  @Override
  public boolean canHandle(Class<?> targetType) {
    return Short.class.isAssignableFrom(targetType)
        || short.class.isAssignableFrom(targetType);
  }

  @Override
  public ArgumentConverter create(Object object, Method method, Parameter parameter) {
    return new ShortArgumentConverter();
  }
}

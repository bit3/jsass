package io.bit3.jsass.function.arguments.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import io.bit3.jsass.function.arguments.converter.ArgumentConverter;

public interface ArgumentConverterFactory {
  public boolean canHandle(Class<?> targetType);

  public ArgumentConverter create(Object object, Method method, Parameter parameter);
}

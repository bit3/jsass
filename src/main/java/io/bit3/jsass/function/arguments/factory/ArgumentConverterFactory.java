package io.bit3.jsass.function.arguments.factory;

import io.bit3.jsass.function.arguments.converter.ArgumentConverter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface ArgumentConverterFactory {
  boolean canHandle(Class<?> targetType);

  ArgumentConverter create(Object object, Method method, Parameter parameter);
}

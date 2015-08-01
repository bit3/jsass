package io.bit3.jsass.function.arguments.factory;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.function.arguments.converter.ArgumentConverter;
import io.bit3.jsass.function.arguments.converter.ContextArgumentConverter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ContextArgumentConverterFactory implements ArgumentConverterFactory {

  @Override
  public boolean canHandle(Class<?> targetType) {
    return Context.class.isAssignableFrom(targetType);
  }

  @Override
  public ArgumentConverter create(Object object, Method method, Parameter parameter) {
    return new ContextArgumentConverter();
  }
}

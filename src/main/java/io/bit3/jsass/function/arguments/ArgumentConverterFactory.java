package io.bit3.jsass.function.arguments;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface ArgumentConverterFactory {
  public boolean canHandle(Class<?> targetType);
  public ArgumentConverter create(Object object, Method method, Parameter parameter);
}

package io.bit3.jsass.function.arguments.factory;

import io.bit3.jsass.function.arguments.converter.ArgumentConverter;
import io.bit3.jsass.function.arguments.converter.LastImportArgumentConverter;
import io.bit3.jsass.importer.Import;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class LastImportArgumentConverterFactory implements ArgumentConverterFactory {

  @Override
  public boolean canHandle(Class<?> targetType) {
    return Import.class.isAssignableFrom(targetType);
  }

  @Override
  public ArgumentConverter create(Object object, Method method, Parameter parameter) {
    return new LastImportArgumentConverter();
  }
}

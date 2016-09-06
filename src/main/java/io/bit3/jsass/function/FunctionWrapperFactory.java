package io.bit3.jsass.function;

import io.bit3.jsass.annotation.DebugFunction;
import io.bit3.jsass.annotation.ErrorFunction;
import io.bit3.jsass.annotation.WarnFunction;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.ImportStack;
import io.bit3.jsass.function.arguments.converter.ArgumentConverter;
import io.bit3.jsass.function.arguments.converter.ObjectArgumentConverter;
import io.bit3.jsass.function.arguments.factory.ArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.BooleanArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.ByteArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.CharacterArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.ContextArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.DoubleArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.FloatArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.IntegerArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.LastImportArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.LongArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.ShortArgumentConverterFactory;
import io.bit3.jsass.function.arguments.factory.StringArgumentConverterFactory;
import io.bit3.jsass.type.SassString;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory that create libsass function callbacks and wrap them into {@link
 * io.bit3.jsass.function.FunctionWrapper}s.
 */
public class FunctionWrapperFactory {

  private final FunctionArgumentSignatureFactory functionArgumentSignatureFactory;

  private final List<ArgumentConverterFactory> argumentConverterFactories;

  /**
   * Create a new factory.
   */
  public FunctionWrapperFactory(
      FunctionArgumentSignatureFactory functionArgumentSignatureFactory
  ) {
    this.functionArgumentSignatureFactory = functionArgumentSignatureFactory;

    // TODO move this into options and make it configurable
    this.argumentConverterFactories = new LinkedList<>();
    this.argumentConverterFactories.add(new BooleanArgumentConverterFactory());
    this.argumentConverterFactories.add(new ByteArgumentConverterFactory());
    this.argumentConverterFactories.add(new CharacterArgumentConverterFactory());
    this.argumentConverterFactories.add(new ContextArgumentConverterFactory());
    this.argumentConverterFactories.add(new DoubleArgumentConverterFactory());
    this.argumentConverterFactories.add(new FloatArgumentConverterFactory());
    this.argumentConverterFactories.add(new IntegerArgumentConverterFactory());
    this.argumentConverterFactories.add(new LastImportArgumentConverterFactory());
    this.argumentConverterFactories.add(new LongArgumentConverterFactory());
    this.argumentConverterFactories.add(new ShortArgumentConverterFactory());
    this.argumentConverterFactories.add(new StringArgumentConverterFactory());
  }

  /**
   * Compile methods from all objects into libsass functions.
   *
   * @param importStack The import stack.
   * @param objects     A list of "function provider" objects.
   * @return The newly created list of libsass callbacks.
   */
  public List<FunctionWrapper> compileFunctions(
      ImportStack importStack, Context context, List<?> objects
  ) {
    List<FunctionWrapper> callbacks = new LinkedList<>();

    for (Object object : objects) {
      List<FunctionWrapper> objectCallbacks = compileFunctions(importStack, context, object);

      callbacks.addAll(objectCallbacks);
    }

    return callbacks;
  }

  /**
   * Compile methods from an object into libsass functions.
   *
   * @param importStack The import stack.
   * @param object      The "function provider" object.
   * @return The newly created list of libsass callbacks.
   */
  public List<FunctionWrapper> compileFunctions(
      ImportStack importStack, Context context, Object object
  ) {
    Class<?> functionClass = object.getClass();
    Method[] methods = functionClass.getDeclaredMethods();
    List<FunctionDeclaration> declarations = new LinkedList<>();

    for (Method method : methods) {
      int modifiers = method.getModifiers();

      if (!Modifier.isPublic(modifiers)) {
        continue;
      }

      FunctionDeclaration declaration = createDeclaration(importStack, context, object, method);
      declarations.add(declaration);
    }

    return declarations.stream().map(FunctionWrapper::new).collect(Collectors.toList());
  }

  /**
   * Create a function declaration from an object method.
   *
   * @param importStack The import stack.
   * @param object      The object.
   * @param method      The method.
   * @return The newly created function declaration.
   */
  public FunctionDeclaration createDeclaration(
      ImportStack importStack, Context context, Object object, Method method
  ) {
    StringBuilder signature = new StringBuilder();
    Parameter[] parameters = method.getParameters();

    List<ArgumentConverter> argumentConverters = new ArrayList<>(method.getParameterCount());
    signature.append(method.getName()).append("(");

    int parameterCount = 0;
    for (Parameter parameter : parameters) {
      ArgumentConverter argumentConverter = createArgumentConverter(object, method, parameter);

      argumentConverters.add(argumentConverter);

      List<FunctionArgumentSignature> list = argumentConverter.argumentSignatures(
          object,
          method,
          parameter,
          functionArgumentSignatureFactory
      );

      for (FunctionArgumentSignature functionArgumentSignature : list) {
        String name = functionArgumentSignature.getName();
        Object defaultValue = functionArgumentSignature.getDefaultValue();

        if (parameterCount > 0) {
          signature.append(", ");
        }

        signature.append("$").append(name);

        if (null != defaultValue) {
          signature.append(": ").append(formatDefaultValue(defaultValue));
        }

        parameterCount++;
      }
    }

    signature.append(")");

    // Overwrite signature with special ones
    if (method.isAnnotationPresent(WarnFunction.class)) {
      signature.setLength(0);
      signature.append("@warn");
    } else if (method.isAnnotationPresent(ErrorFunction.class)) {
      signature.setLength(0);
      signature.append("@error");
    } else if (method.isAnnotationPresent(DebugFunction.class)) {
      signature.setLength(0);
      signature.append("@debug");
    }

    return new FunctionDeclaration(
        importStack,
        context,
        signature.toString(),
        object,
        method,
        argumentConverters
    );
  }

  /**
   * Format a default value for libsass function signature.
   *
   * @param value The default value.
   * @return The formated default value.
   */
  private String formatDefaultValue(Object value) {
    if (value instanceof Boolean) {
      return ((Boolean) value) ? "true" : "false";
    }

    if (value instanceof Number) {
      return value.toString();
    }

    if (value instanceof Collection) {
      return formatCollectionValue((Collection) value);
    }

    String string = value.toString();

    if (string.startsWith("$")) {
      return string;
    }

    return SassString.escape(string);
  }

  private String formatCollectionValue(Collection value) {
    StringBuilder builder = new StringBuilder();

    builder.append("(");
    boolean first = true;
    for (Object item : value) {
      if (first) {
        first = false;
      } else {
        builder.append(",");
      }
      builder.append(formatDefaultValue(item));
    }
    builder.append(")");

    return builder.toString();
  }

  private ArgumentConverter createArgumentConverter(
      Object object,
      Method method,
      Parameter parameter
  ) {
    Class<?> type = parameter.getType();

    for (ArgumentConverterFactory factory : argumentConverterFactories) {
      if (factory.canHandle(type)) {
        return factory.create(object, method, parameter);
      }
    }

    return new ObjectArgumentConverter(type);
  }
}

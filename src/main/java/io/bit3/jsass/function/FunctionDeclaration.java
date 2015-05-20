package io.bit3.jsass.function;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.function.arguments.ArgumentConverter;
import io.bit3.jsass.importer.Import;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Contains all informations about a declared custom function.
 */
public class FunctionDeclaration {

  private final Stack<Import> importStack;

  private final Context context;

  /**
   * The libsass function signature.
   *
   * <p>e.g. hello($name: "world")</p>
   */
  protected final String signature;

  /**
   * The object instance to call the method on.
   */
  protected final Object object;

  /**
   * The method to call.
   */
  protected final Method method;

  /**
   * List of argument converters that are used to convert the method parameter values.
   */
  protected final List<ArgumentConverter> argumentConverters;

  /**
   * Create a new function declaration.
   *
   * @param importStack
   * @param signature          The libsass function signature.
   * @param object             The object instance to call the method on.
   * @param method             The method to call.
   * @param argumentConverters List of argument converters.
   */
  public FunctionDeclaration(
      Stack<Import> importStack, Context context, String signature, Object object, Method method,
      List<ArgumentConverter> argumentConverters
  ) {
    this.importStack = importStack;
    this.context = context;
    this.signature = signature;
    this.object = object;
    this.method = method;
    this.argumentConverters = argumentConverters;
  }

  /**
   * Return the libsass function signature.
   *
   * @return The libsass function signature.
   */
  public String getSignature() {
    return signature;
  }

  /**
   * Return the object instance to call the method on.
   *
   * @return The object instance to call the method on.
   */
  public Object getObject() {
    return object;
  }

  /**
   * Return the method to call.
   *
   * @return The method to call.
   */
  public Method getMethod() {
    return method;
  }

  /**
   * Return the list of argument converters.
   *
   * @return List of argument converters.
   */
  public List<ArgumentConverter> getArgumentConverters() {
    return argumentConverters;
  }

  /**
   * Invoke the method with the given list of arguments.
   *
   * <p>This will convert the libsass arguments into java value.</p>
   *
   * @param arguments List of libsass arguments.
   * @return The method result.
   */
  public Object invoke(List<?> arguments) {
    ArrayList<Object> values = new ArrayList<>(argumentConverters.size());

    for (ArgumentConverter argumentConverter : argumentConverters) {
      Object value = argumentConverter.convert(arguments, importStack, context);
      values.add(value);
    }

    try {
      return method.invoke(object, values.toArray());
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}


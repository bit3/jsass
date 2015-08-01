package io.bit3.jsass.function;

import io.bit3.jsass.type.SassList;
import io.bit3.jsass.type.SassValue;

/**
 * Wraps a java function as libsass function and pass the arguments through.
 */
public class FunctionWrapper {

  /**
   * The original function declaration, that is created by the factory.
   */
  private final FunctionDeclaration declaration;

  /**
   * Create a new wrapper for the given function.
   *
   * @param declaration The function declaration.
   */
  public FunctionWrapper(FunctionDeclaration declaration) {
    this.declaration = declaration;
  }

  public FunctionDeclaration getDeclaration() {
    return declaration;
  }

  /**
   * Call the function.
   */
  public SassValue apply(SassValue value) {
    SassList sassList;

    if (value instanceof SassList) {
      sassList = (SassList) value;
    } else {
      sassList = new SassList();
      sassList.add(value);
    }

    return declaration.invoke(sassList);
  }
}

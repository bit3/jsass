package io.bit3.jsass.function;

import com.sun.jna.Pointer;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.type.SassList;
import io.bit3.jsass.type.TypeUtils;
import sass.SassLibrary;

/**
 * Wraps a java function as libsass function and pass the arguments through.
 */
public class FunctionWrapper implements SassLibrary.Sass_Function_Fn {

  /**
   * SASS library adapter.
   */
  private final SassLibrary sass;

  /**
   * The original function declaration, that is created by the factory.
   */
  private final FunctionDeclaration declaration;

  /**
   * Create a new wrapper for the given function.
   *
   * @param sass        The libsass adapter.
   * @param declaration The function declaration.
   */
  public FunctionWrapper(SassLibrary sass, FunctionDeclaration declaration) {
    this.sass = sass;
    this.declaration = declaration;
  }

  @Override
  public SassLibrary.Sass_Value apply(SassLibrary.Sass_Value value, Pointer cb,
                                      SassLibrary.Sass_Options options) {
    try {
      Object decodedValue = TypeUtils.decodeValue(sass, value);
      SassList sassList;

      if (decodedValue instanceof SassList) {
        sassList = (SassList) decodedValue;
      } else {
        sassList = new SassList();
        sassList.add(decodedValue);
      }

      Object result = declaration.invoke(sassList);

      return TypeUtils.encodeValue(sass, result);
    } catch (CompilationException e) {
      throw new RuntimeException(e);
    }
  }
}


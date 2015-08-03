package io.bit3.jsass.function;

import io.bit3.jsass.context.ImportStack;

import java.net.URISyntaxException;

/**
 * Provider of jsass specific callback functions.
 */
public class JsassCustomFunctions {
  /**
   * The import stack.
   */
  private final ImportStack importStack;

  /**
   * Create a new function provider.
   */
  public JsassCustomFunctions(ImportStack importStack) {
    this.importStack = importStack;
  }

  /**
   * Push an import to the import stack.
   */
  public Object jsass_import_stack_push(int id)
      throws URISyntaxException {
    this.importStack.push(id);

    return null;
  }

  /**
   * Pop an import from the import stack.
   */
  public Object jsass_import_stack_pop() {
    this.importStack.pop();

    return null;
  }
}

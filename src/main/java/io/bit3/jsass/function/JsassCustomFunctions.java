package io.bit3.jsass.function;

import io.bit3.jsass.importer.Import;
import io.bit3.jsass.type.SassList;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import io.bit3.jsass.context.ImportStack;

public class JsassCustomFunctions {

  private final ImportStack importStack;

  public JsassCustomFunctions(ImportStack importStack) {
    this.importStack = importStack;
  }

  public Object jsass_import_stack_push(int id)
      throws URISyntaxException {
    this.importStack.push(id);

    return null;
  }

  public Object jsass_import_stack_pop() {
    this.importStack.pop();

    return null;
  }
}

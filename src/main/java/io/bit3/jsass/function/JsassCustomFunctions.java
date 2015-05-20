package io.bit3.jsass.function;

import io.bit3.jsass.importer.Import;
import io.bit3.jsass.type.SassList;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Stack;

public class JsassCustomFunctions {

  private final Stack<Import> importStack;

  public JsassCustomFunctions(Stack<Import> importStack) {
    this.importStack = importStack;
  }

  public SassList jsass_import_stack_push(String importPath, String basePath)
      throws URISyntaxException {
    this.importStack.push(
        new Import(
            new URI(importPath),
            new URI(basePath)
        )
    );

    return new SassList(Arrays.asList(importPath, basePath));
  }

  public SassList jsass_import_stack_pop() {
    this.importStack.pop();

    Import importSource = importStack.peek();
    String importPath = importSource.getUri().toString();
    String basePath = importSource.getBase().toString();

    return new SassList(Arrays.asList(importPath, basePath));
  }
}

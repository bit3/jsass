package io.bit3.jsass.context;

import io.bit3.jsass.importer.Import;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ImportStack {

  private Map<Integer, Import> registry = new HashMap<>();
  private Stack<Import>        stack    = new Stack<>();

  public int register(Import importSource) {
    int id = registry.size() + 1;
    registry.put(id, importSource);
    return id;
  }

  public void push(int id) {
    stack.push(registry.get(id));
  }

  public void pop() {
    stack.pop();
  }

  public Import peek() {
    return stack.peek();
  }
}

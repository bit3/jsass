package io.bit3.jsass.context;

import io.bit3.jsass.importer.Import;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Stack for tracking currently evaluated file.
 */
public class ImportStack {

  /**
   * A registry where each import will be registered by an incrementing ID.
   */
  private Map<Integer, Import> registry = new HashMap<>();

  /**
   * The current import stack.
   */
  private Deque<Import> stack = new LinkedList<>();

  /**
   * Register a new import, return the registration ID.
   */
  public int register(Import importSource) {
    int id = registry.size() + 1;
    registry.put(id, importSource);
    return id;
  }

  /**
   * Push an import to the stack by its ID.
   */
  public void push(int id) {
    stack.push(registry.get(id));
  }

  /**
   * Pop an import from the stack.
   */
  public void pop() {
    stack.pop();
  }

  /**
   * Return the current import.
   */
  public Import peek() {
    return stack.peek();
  }
}

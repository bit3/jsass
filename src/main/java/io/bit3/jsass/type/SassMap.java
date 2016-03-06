package io.bit3.jsass.type;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A sass value list.
 */
public class SassMap extends LinkedHashMap<String, SassValue> implements SassValue {
  private static final long serialVersionUID = -65270174805160025L;
  public static final int TYPE = 7;

  public SassMap() {
    // an empty map
  }

  public SassMap(Map<? extends String, ? extends SassValue> map) {
    super(map);
  }

  public SassMap(int initialCapacity) {
    super(initialCapacity);
  }

  public SassMap(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  public SassMap(int initialCapacity, float loadFactor, boolean accessOrder) {
    super(initialCapacity, loadFactor, accessOrder);
  }

  /**
   * Return a libsass compatible (key: value) string representation.
   *
   * @return A libsass compatible (key: value) string representation.
   */
  @Override
  public String toString() {
    String map = entrySet()
        .stream()
        .map(e -> e.getKey() + ": " + e.getValue())
        .collect(Collectors.joining(", "));

    return "(" + map + ")";
  }
}

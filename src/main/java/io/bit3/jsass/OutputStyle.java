package io.bit3.jsass;

public enum OutputStyle {
  /**
   * Nested style.
   */
  NESTED(1),

  /**
   * Expanded style.
   */
  EXPANDED(2),

  /**
   * Compact style.
   */
  COMPACT(3),

  /**
   * Compressed style.
   */
  COMPRESSED(4);

  /**
   * The numeric representation.
   *
   * <p>This is primary used in the native adapter.
   */
  public final int numeric;

  OutputStyle(int numeric) {
    this.numeric = numeric;
  }
}

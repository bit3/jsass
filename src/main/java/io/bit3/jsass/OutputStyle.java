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

  public final int NUMERIC;

  OutputStyle(int numeric) {
    NUMERIC = numeric;
  }
}

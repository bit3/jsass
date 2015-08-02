package io.bit3.jsass.type;

public class SassError implements SassValue {
  public static final int TYPE = 8;

  private final String message;

  public SassError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

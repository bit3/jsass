package io.bit3.jsass.type;

public class SassWarning implements SassValue {
  public static final int TYPE = 9;

  private final String message;

  public SassWarning(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

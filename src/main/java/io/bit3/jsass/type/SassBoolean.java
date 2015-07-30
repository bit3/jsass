package io.bit3.jsass.type;

public class SassBoolean implements SassValue {
  public static final int TYPE = 4;

  private boolean value;

  public SassBoolean(boolean value) {
    this.value = value;
  }

  public boolean getValue() {
    return value;
  }

  public void setValue(boolean value) {
    this.value = value;
  }
}

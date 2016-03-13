package io.bit3.jsass.type;

public class SassNull implements SassValue {
  public static final int TYPE = 1;

  public static final SassNull SINGLETON = new SassNull();

  private SassNull() {
  }
}

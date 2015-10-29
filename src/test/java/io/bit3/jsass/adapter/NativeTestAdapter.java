package io.bit3.jsass.adapter;

public class NativeTestAdapter {
  static {
    NativeTestLoader.loadTestLibrary();
  }

  public native Object testSassPositiveNumberToJava();

  public native Object testSassNegativeNumberToJava();

  public native Object testSassZeroNumberToJava();

  public native Object testSassPositiveNumberWithUnitToJava();

  public native Object testSassNegativeNumberWithUnitToJava();

  public native Object testSassZeroNumberWithUnitToJava();
}

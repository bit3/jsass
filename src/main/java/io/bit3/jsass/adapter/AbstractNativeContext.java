package io.bit3.jsass.adapter;

abstract class AbstractNativeContext implements NativeContext {
  public final String inputPath;

  public final String outputPath;

  public final NativeOptions options;

  protected AbstractNativeContext(String inputPath, String outputPath, NativeOptions options) {
    this.inputPath = inputPath;
    this.outputPath = outputPath;
    this.options = options;
  }
}

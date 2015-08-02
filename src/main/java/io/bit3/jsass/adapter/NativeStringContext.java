package io.bit3.jsass.adapter;

class NativeStringContext extends AbstractNativeContext {
  public final String source;

  protected NativeStringContext(
      String source, String inputPath, String outputPath, NativeOptions options
  ) {
    super(inputPath, outputPath, options);
    this.source = source;
  }
}

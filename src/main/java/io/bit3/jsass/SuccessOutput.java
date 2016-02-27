package io.bit3.jsass;

/**
 * The Output of a successful compilation.
 */
public interface SuccessOutput {

  /**
   * Get the css output.
   *
   * @return The css output.
   */
  String getCss();

  /**
   * Get the source map output.
   *
   * @return The source map output.
   */
  String getSourceMap();
}

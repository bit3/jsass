package io.bit3.jsass;

import java.io.Serializable;

/**
 * The SASS compilation output.
 */
public class Output implements Serializable {
  private static final long serialVersionUID = 5649502387645755010L;

  /**
   * The css output.
   */
  private final String css;

  /**
   * The source map output.
   */
  private final String sourceMap;

  /**
   * Create a new output.
   */
  public Output(
      String css,
      String sourceMap
  ) {
    this.css = css;
    this.sourceMap = sourceMap;
  }

  /**
   * Get the css output.
   *
   * @return The css output.
   */
  public String getCss() {
    return css;
  }

  /**
   * Get the source map output.
   *
   * @return The source map output.
   */
  public String getSourceMap() {
    return sourceMap;
  }
}

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
   * The error status, not zero means an error occurred.
   */
  private final int errorStatus;

  private final String errorJson;

  private final String errorText;

  private final String errorMessage;

  private final String errorFile;

  private final String errorSrc;

  /**
   * Create a new output.
   */
  public Output(
      String css,
      String sourceMap,
      int errorStatus,
      String errorJson,
      String errorText,
      String errorMessage,
      String errorFile,
      String errorSrc
  ) {
    this.css = css;
    this.sourceMap = sourceMap;
    this.errorStatus = errorStatus;
    this.errorJson = errorJson;
    this.errorText = errorText;
    this.errorMessage = errorMessage;
    this.errorFile = errorFile;
    this.errorSrc = errorSrc;
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

  public int getErrorStatus() {
    return errorStatus;
  }

  public String getErrorJson() {
    return errorJson;
  }

  public String getErrorText() {
    return errorText;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String getErrorFile() {
    return errorFile;
  }

  public String getErrorSrc() {
    return errorSrc;
  }
}

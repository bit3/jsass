package io.bit3.jsass;

/**
 * The SASS compilation output.
 */
public class Output {

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

  private final String jsonError;

  public Output(String css, String sourceMap, int errorStatus, String jsonError) {
    this.css = css;
    this.sourceMap = sourceMap;
    this.errorStatus = errorStatus;
    this.jsonError = jsonError;
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

  public String getJsonError() {
    return jsonError;
  }
}

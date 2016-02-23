package io.bit3.jsass;

/**
 * The SASS compilation output.
 */
public class Output implements SuccessOutput, FailureOutput {

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

  @Override
  public String getCss() {
    return css;
  }

  @Override
  public String getSourceMap() {
    return sourceMap;
  }

  @Override
  public int getErrorStatus() {
    return errorStatus;
  }

  @Override
  public String getErrorJson() {
    return errorJson;
  }

  @Override
  public String getErrorText() {
    return errorText;
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String getErrorFile() {
    return errorFile;
  }

  @Override
  public String getErrorSrc() {
    return errorSrc;
  }
}

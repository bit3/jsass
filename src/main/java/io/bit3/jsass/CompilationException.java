package io.bit3.jsass;

public class CompilationException extends Exception {
  private static final long serialVersionUID = 630234764149041048L;

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
   * Constructs a new exception.
   */
  public CompilationException(
      int errorStatus,
      String errorJson,
      String errorText,
      String errorMessage,
      String errorFile,
      String errorSrc
  ) {
    super(errorMessage);
    this.errorStatus = errorStatus;
    this.errorJson = errorJson;
    this.errorText = errorText;
    this.errorMessage = errorMessage;
    this.errorFile = errorFile;
    this.errorSrc = errorSrc;
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

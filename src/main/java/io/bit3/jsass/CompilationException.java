package io.bit3.jsass;

public class CompilationException extends Exception {

  private Output output;

  public CompilationException(Output output) {
    super(output.getErrorText());
    this.output = output;
  }

  public Output getOutput() {
    return output;
  }

  /**
   * @see Output#getErrorStatus()
   */
  public int getErrorStatus() {
    return getOutput().getErrorStatus();
  }

  /**
   * @see Output#getErrorJson()
   */
  public String getErrorJson() {
    return getOutput().getErrorJson();
  }

  /**
   * @see Output#getErrorText()
   */
  public String getErrorText() {
    return getOutput().getErrorText();
  }

  /**
   * @see Output#getErrorMessage()
   */
  public String getErrorMessage() {
    return getOutput().getErrorMessage();
  }

  /**
   * @see Output#getErrorFile()
   */
  public String getErrorFile() {
    return getOutput().getErrorFile();
  }

  /**
   * @see Output#getErrorSrc()
   */
  public String getErrorSrc() {
    return getOutput().getErrorSrc();
  }
}

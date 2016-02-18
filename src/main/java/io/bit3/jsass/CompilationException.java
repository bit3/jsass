package io.bit3.jsass;

public class CompilationException extends Exception {

  private Output output;

  public CompilationException(Output output) {
    super(output.getErrorText());
    this.output = output;
  }

  /**
   * @return The {@link Output} of the failed compilation.
   */
  public Output getOutput() {
    return output;
  }

  public int getErrorStatus() {
    return getOutput().getErrorStatus();
  }

  public String getErrorJson() {
    return getOutput().getErrorJson();
  }

  public String getErrorText() {
    return getOutput().getErrorText();
  }

  public String getErrorMessage() {
    return getOutput().getErrorMessage();
  }

  public String getErrorFile() {
    return getOutput().getErrorFile();
  }

  public String getErrorSrc() {
    return getOutput().getErrorSrc();
  }
}

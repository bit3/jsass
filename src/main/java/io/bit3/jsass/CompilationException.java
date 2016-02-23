package io.bit3.jsass;

public class CompilationException extends Exception implements FailureOutput {

  private FailureOutput output;

  public CompilationException(FailureOutput output) {
    super(output.getErrorText());
    this.output = output;
  }

  /**
   * @return The {@link FailureOutput} of the failed compilation.
   */
  public FailureOutput getOutput() {
    return output;
  }

  @Override
  public int getErrorStatus() {
    return getOutput().getErrorStatus();
  }

  @Override
  public String getErrorJson() {
    return getOutput().getErrorJson();
  }

  @Override
  public String getErrorText() {
    return getOutput().getErrorText();
  }

  @Override
  public String getErrorMessage() {
    return getOutput().getErrorMessage();
  }

  @Override
  public String getErrorFile() {
    return getOutput().getErrorFile();
  }

  @Override
  public String getErrorSrc() {
    return getOutput().getErrorSrc();
  }
}

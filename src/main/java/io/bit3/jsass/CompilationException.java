package io.bit3.jsass;

public class CompilationException extends Exception implements ErrorOutput {

  private ErrorOutput output;

  public CompilationException(ErrorOutput output) {
    super(output.getErrorText());
    this.output = output;
  }

  /**
   * @return The {@link ErrorOutput} of the failed compilation.
   */
  public ErrorOutput getOutput() {
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

package io.bit3.jsass;

public class CompilationException extends Exception {

  private int status;

  public CompilationException(int status, String message) {
    super(message);
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}

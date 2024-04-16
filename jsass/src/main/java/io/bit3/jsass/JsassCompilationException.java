package io.bit3.jsass;

public class JsassCompilationException extends Exception {
  private static final long serialVersionUID = 5390684251666644906L;

  public JsassCompilationException() {
  }

  public JsassCompilationException(String message) {
    super(message);
  }

  public JsassCompilationException(String message, Throwable cause) {
    super(message, cause);
  }

  public JsassCompilationException(Throwable cause) {
    super(cause);
  }

  public JsassCompilationException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

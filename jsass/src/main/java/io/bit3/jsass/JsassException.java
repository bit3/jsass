package io.bit3.jsass;

public class JsassException extends Exception {
  private static final long serialVersionUID = 5390684251666644906L;

  public JsassException() {
  }

  public JsassException(String message) {
    super(message);
  }

  public JsassException(String message, Throwable cause) {
    super(message, cause);
  }

  public JsassException(Throwable cause) {
    super(cause);
  }

  public JsassException(String message, Throwable cause, boolean enableSuppression,
                        boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

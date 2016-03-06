package io.bit3.jsass;

import io.bit3.jsass.context.Context;

public class UnsupportedContextException extends RuntimeException {
  private final Context context;

  public UnsupportedContextException(Context context) {
    super(
        String.format(
            "Context type \"%s\" is not supported",
            null == context ? "null" : context.getClass().getName()
        )
    );
    this.context = context;
  }

  public Context getContext() {
    return context;
  }
}

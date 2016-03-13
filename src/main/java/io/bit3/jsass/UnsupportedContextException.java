package io.bit3.jsass;

import io.bit3.jsass.context.Context;

public class UnsupportedContextException extends RuntimeException {
  private static final long serialVersionUID = -5832354814221827572L;
  private final Context context;

  /**
   * Construct a new unsupported context exception with the given context.
   *
   * @param context The unsupported {@link Context} instance.
   */
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

package io.bit3.jsass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Custom assertions.
 */
public class Assert {
  /**
   * Asserts that the compilation was successful.
   */
  public static void assertSuccessful(Output output, String syntax, OutputStyle outputStyle) {
    String message = String.format(
        "Compile input.%s into %s output format failed with error status (%d)\n"
            + "%s\n"
            + "%s",
        syntax,
        outputStyle,
        output.getErrorStatus(),
        output.getErrorMessage(),
        output.getErrorJson()
    );

    assertEquals(
        message,
        0,
        output.getErrorStatus()
    );
    assertNull(
        message,
        output.getErrorJson()
    );
  }
}

package io.bit3.jsass;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class PrecisionTest {

  private static final String scss = ".something {\n"
          + "  padding: 0 0.8em (5/7) 0.8em;\n"
          + "}";

  Compiler compiler;
  Options options;

  /**
   * Set up test defaults.
   */
  @Before
  public void init() {
    compiler = new Compiler();
    options = new Options();
    options.setOutputStyle(OutputStyle.COMPRESSED);
  }

  @Test
  public void testLowPrecision() throws CompilationException {
    options.setPrecision(5);

    Output output = compiler.compileString(scss, options);

    assertThat(output.getCss(), containsString("padding:0 0.8em 0.71429 0.8em"));
  }

  @Test
  public void testHighPrecision() throws CompilationException {
    options.setPrecision(10);

    Output output = compiler.compileString(scss, options);

    assertThat(output.getCss(), containsString("padding:0 0.8em 0.7142857143 0.8em"));
  }
}

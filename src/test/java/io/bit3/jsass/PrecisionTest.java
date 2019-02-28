package io.bit3.jsass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class PrecisionTest {

  private static final String scss = ".something {\n"
          + "  padding: 0 0.8em (5/7) 0.8em;\n"
          + "}";

  Compiler compiler;
  Options options;

  /**
   * Set up test defaults.
   */
  @BeforeEach
  public void init() {
    compiler = new Compiler();
    options = new Options();
    options.setOutputStyle(OutputStyle.COMPRESSED);
  }

  private Output getOutput(int precision) throws CompilationException {
    options.setPrecision(precision);
    return compiler.compileString(scss, options);
  }

  @Test
  public void testLowPrecisionOsX() throws CompilationException {
    Output output = getOutput(5);

    assertThat(output.getCss(), not(containsString("padding:0 0.8em 0.71429 0.8em")));
  }

  @Test
  public void testHighPrecisionOsX() throws CompilationException {
    Output output = getOutput(10);

    assertThat(output.getCss(), not(containsString("padding:0 0.8em 0.7142857143 0.8em")));
  }

  @Test
  public void testLowPrecisionLinux() throws CompilationException {
    Output output = getOutput(5);

    assertThat(output.getCss(), containsString("padding:0 0.8em .71429 0.8em"));
  }

  @Test
  public void testHighPrecisionLinux() throws CompilationException {
    Output output = getOutput(10);

    assertThat(output.getCss(), containsString("padding:0 0.8em .7142857143 0.8em"));
  }
}

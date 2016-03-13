package io.bit3.jsass.context;

import io.bit3.jsass.Options;

import java.net.URI;

/**
 * A context to compile a string in memory.
 */
public class StringContext extends AbstractContext {
  private static final long serialVersionUID = -3016862884676346652L;

  /**
   * The in-memory sass code.
   */
  private String string;

  /**
   * Create a new string context.
   *
   * @param string     The in-memory sass code.
   * @param inputPath  The input path used for calculation path changes, or <em>null</em>.
   * @param outputPath The output path used for calculation path changes, or <em>null</em>.
   * @param options    The compiler options.
   */
  public StringContext(String string, URI inputPath, URI outputPath, Options options) {
    super(inputPath, outputPath, options);
    this.string = string;
  }

  /**
   * Return the in-memory sass code.
   *
   * @return The in-memory sass code.
   */
  public String getString() {
    return string;
  }

}

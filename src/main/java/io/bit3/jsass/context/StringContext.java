package io.bit3.jsass.context;

import io.bit3.jsass.Options;

import org.apache.commons.io.Charsets;

import java.io.File;
import java.nio.charset.Charset;

/**
 * A context to compile a string in memory.
 */
public class StringContext extends AbstractContext {

  /**
   * The in-memory sass code.
   */
  private String string;

  /**
   * The charset of the in-memory sass code.
   */
  private Charset charset;

  /**
   * Create a new string context.
   *
   * @param string     The in-memory sass code.
   * @param inputPath  The input path used for calculation path changes, or <em>null</em>.
   * @param outputPath The output path used for calculation path changes, or <em>null</em>.
   * @param options    The compiler options.
   */
  public StringContext(String string, File inputPath, File outputPath, Options options) {
    this(string, Charsets.UTF_8, inputPath, outputPath, options);
  }

  /**
   * Create a new string context.
   *
   * @param string     The in-memory sass code.
   * @param charset    The charset of the in-memory sass code.
   * @param inputPath  The input path used for calculation path changes, or <em>null</em>.
   * @param outputPath The output path used for calculation path changes, or <em>null</em>.
   * @param options    The compiler options.
   */
  public StringContext(String string, Charset charset, File inputPath, File outputPath,
                       Options options) {
    super(inputPath, outputPath, options);
    this.string = string;
    this.charset = charset;
  }

  /**
   * Return the in-memory sass code.
   *
   * @return The in-memory sass code.
   */
  public String getString() {
    return string;
  }

  /**
   * Return the charset of the in-memory sass code.
   *
   * @return The charset of the in-memory sass code.
   */
  public Charset getCharset() {
    return charset;
  }
}

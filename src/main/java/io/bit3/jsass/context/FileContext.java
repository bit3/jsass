package io.bit3.jsass.context;

import io.bit3.jsass.Options;

import java.net.URI;

/**
 * A context to compile a file from the filesystem.
 */
public class FileContext extends AbstractContext {
  private static final long serialVersionUID = -1235536277472179438L;

  /**
   * Create a new string context.
   *
   * @param inputPath  The input path to read the file from.
   * @param outputPath The output path to write the file to, or <em>null</em> to in-memory
   *                   compilation.
   * @param options    The compiler options.
   */
  public FileContext(URI inputPath, URI outputPath, Options options) {
    super(inputPath, outputPath, options);
  }
}

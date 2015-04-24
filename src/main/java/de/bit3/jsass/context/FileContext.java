package de.bit3.jsass.context;

import de.bit3.jsass.Options;

import java.io.File;

/**
 * A context to compile a file from the filesystem.
 */
public class FileContext extends AbstractContext {

  /**
   * Create a new string context.
   *
   * @param inputPath  The input path to read the file from.
   * @param outputPath The output path to write the file to, or <em>null</em> to in-memory
   *                   compilation.
   * @param options    The compiler options.
   */
  public FileContext(File inputPath, File outputPath, Options options) {
    super(inputPath, outputPath, options);
  }
}

package io.bit3.jsass.context;

import io.bit3.jsass.Options;

import java.io.File;

/**
 * Abstract context that contain all shared informations for each context.
 */
public class AbstractContext implements Context {

  /**
   * The input file path.
   */
  private final File inputPath;

  /**
   * The output file path.
   */
  private final File outputPath;

  /**
   * The compiler options.
   */
  private final Options options;

  /**
   * Initialize the context with input path, output path and options.
   *
   * @param inputPath  The input file path.
   * @param outputPath The output file path.
   * @param options    The compiler options.
   */
  public AbstractContext(File inputPath, File outputPath, Options options) {
    this.inputPath = inputPath;
    this.outputPath = outputPath;
    this.options = options;
  }

  @Override
  public File getInputPath() {
    return inputPath;
  }

  @Override
  public File getOutputPath() {
    return outputPath;
  }

  @Override
  public Options getOptions() {
    return options;
  }
}

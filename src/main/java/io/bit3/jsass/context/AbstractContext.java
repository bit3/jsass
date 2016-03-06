package io.bit3.jsass.context;

import io.bit3.jsass.Options;

import java.net.URI;

/**
 * Abstract context that contain all shared informations for each context.
 */
public abstract class AbstractContext implements Context {
  private static final long serialVersionUID = 913074077778518500L;

  /**
   * The input file path.
   */
  private final URI inputPath;

  /**
   * The output file path.
   */
  private final URI outputPath;

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
  public AbstractContext(URI inputPath, URI outputPath, Options options) {
    this.inputPath = inputPath;
    this.outputPath = outputPath;
    this.options = options;
  }

  @Override
  public URI getInputPath() {
    return inputPath;
  }

  @Override
  public URI getOutputPath() {
    return outputPath;
  }

  @Override
  public Options getOptions() {
    return options;
  }
}

package de.bit3.jsass.context;

import de.bit3.jsass.Options;

import java.io.File;

public interface Context {

  /**
   * Return the input file path.
   *
   * @return The input file path or <em>null</em> if no path is specified.
   */
  File getInputPath();

  /**
   * Return the output file path.
   *
   * @return The output file path or <em>null</em> if no path is specified.
   */
  File getOutputPath();

  /**
   * Return the compiler options.
   *
   * @return The compiler options.
   */
  Options getOptions();
}

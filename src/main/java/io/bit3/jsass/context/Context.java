package io.bit3.jsass.context;

import io.bit3.jsass.Options;

import java.io.Serializable;
import java.net.URI;

public interface Context extends Serializable {

  /**
   * Return the input file path.
   *
   * @return The input file path or <em>null</em> if no path is specified.
   */
  URI getInputPath();

  /**
   * Return the output file path.
   *
   * @return The output file path or <em>null</em> if no path is specified.
   */
  URI getOutputPath();

  /**
   * Return the compiler options.
   *
   * @return The compiler options.
   */
  Options getOptions();
}

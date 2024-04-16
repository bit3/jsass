package io.bit3.jsass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import java.io.Serializable;

/**
 * The SASS compilation output.
 */
@Value
public class Output {
  /**
   * The css output.
   */
  String css;

  /**
   * The source map output.
   */
  String sourceMap;
}

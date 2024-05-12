package io.bit3.jsass.webjar;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebjarLocation {
  private final String webjar;
  private final String partialPath;
}

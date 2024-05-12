package io.bit3.jsass.importer;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
@AllArgsConstructor
public class CanonicalizeContext {
  private final boolean fromImport;
  private final @Nullable URI containingUrl;
}

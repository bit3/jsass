package io.bit3.jsass.importer;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Importer {

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/importer/#canonicalize">https://sass-lang.com/documentation/js-api/interfaces/importer/#canonicalize</a>
   */
  @Nullable
  URI canonicalize(@NotNull String url, @NotNull CanonicalizeContext context);

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/importer/#load">https://sass-lang.com/documentation/js-api/interfaces/importer/#load</a>
   */
  @Nullable
  ImporterResult load(@NotNull URI canonicalUrl);
}

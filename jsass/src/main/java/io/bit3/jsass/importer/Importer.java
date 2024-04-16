package io.bit3.jsass.importer;

import java.net.URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Importer {

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importer/#canonicalize">https://sass-lang.com/documentation/js-api/interfaces/importer/#canonicalize</a>
   */
  @Nullable
  URL canonicalize(@NotNull String url);

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importer/#load">https://sass-lang.com/documentation/js-api/interfaces/importer/#load</a>
   */
  @Nullable
  ImporterResult load(@NotNull URL canonicalUrl);

}

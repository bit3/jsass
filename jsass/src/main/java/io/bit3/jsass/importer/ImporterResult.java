package io.bit3.jsass.importer;

import io.bit3.jsass.StringOptions.Syntax;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.URI;

/**
 * The result of a custom importer, can be used to import files and strings.
 */
public interface ImporterResult {

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importerresult/#contents">https://sass-lang.com/documentation/js-api/interfaces/importerresult/#contents</a>
   */
  @NotNull
  String getContents();

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importerresult/#sourceMapUrl">https://sass-lang.com/documentation/js-api/interfaces/importerresult/#sourceMapUrl</a>
   */
  @Nullable
  URI getSourceMapUrl();

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importerresult/#syntax">https://sass-lang.com/documentation/js-api/interfaces/importerresult/#syntax</a>
   */
  @NotNull
  Syntax getSyntax();

  static DefaultImporterResult.DefaultImporterResultBuilder builder() {
    return DefaultImporterResult.builder();
  }

}

package io.bit3.jsass.importer;

import io.bit3.jsass.Options;
import io.bit3.jsass.Options.Syntax;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.URL;

@Value
@Builder
public class DefaultImporterResult implements ImporterResult {

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importerresult/#contents">https://sass-lang.com/documentation/js-api/interfaces/importerresult/#contents</a>
   */
  @NotNull
  String contents;

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importerresult/#sourceMapUrl">https://sass-lang.com/documentation/js-api/interfaces/importerresult/#sourceMapUrl</a>
   */
  @Nullable
  URL sourceMapUrl;

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importerresult/#syntax">https://sass-lang.com/documentation/js-api/interfaces/importerresult/#syntax</a>
   */
  @NotNull
  Options.Syntax syntax = Syntax.SCSS;

}

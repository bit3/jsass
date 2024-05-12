package io.bit3.jsass.importer;

import io.bit3.jsass.StringOptions;
import io.bit3.jsass.StringOptions.Syntax;
import java.net.URI;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  URI sourceMapUrl;

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/importerresult/#syntax">https://sass-lang.com/documentation/js-api/interfaces/importerresult/#syntax</a>
   */
  @Builder.Default
  @NotNull
  StringOptions.Syntax syntax = Syntax.SCSS;

}

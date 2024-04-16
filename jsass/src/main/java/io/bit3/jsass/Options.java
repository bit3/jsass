package io.bit3.jsass;

import io.bit3.jsass.importer.Importer;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

/**
 * The compiler options.
 */
@Value
@Builder
public class Options {

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithimporter/#importer">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithimporter/#importer</a>
   */
  @Nullable
  Importer importer;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/StringOptionsWithoutImporter#syntax">https://sass-lang.com/documentation/js-api/interfaces/StringOptionsWithoutImporter#syntax</a>
   */
  @Nullable
  Syntax inputSyntax;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithimporter/#url">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithimporter/#url</a>
   */
  @Nullable
  URL url;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#charset>https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#charset</a>
   */
  @Nullable
  String charset;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#sourceMap">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#sourceMap</a>
   */
  @Nullable
  Boolean sourceMap;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#sourceMapIncludeSources>https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#sourceMapIncludeSources</a>
   */
  @Nullable
  Boolean sourceMapIncludeSources;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#style>https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#style</a>
   */
  @Nullable
  OutputStyle style;

  /**
   * @see <a href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#functions">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#functions</a>
   */
  @Nullable
  Map<String, SassFunction> functions;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#importers">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#importers</a>
   */
  @Nullable
  List<Importer> importers;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#alertAscii">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#alertAscii</a>
   */
  @Nullable
  Boolean alertAscii;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#alertColor">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#alertColor</a>
   */
  @Nullable
  Boolean alertColor;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#logger">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#logger</a>
   */
  @Nullable
  SassLogger logger;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#quietDeps">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#quietDeps</a>
   */
  @Nullable
  Boolean quietDeps;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#verbose">https://sass-lang.com/documentation/js-api/interfaces/stringoptionswithoutimporter/#verbose</a>
   */
  @Nullable
  Boolean verbose;

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/types/syntax/">https://sass-lang.com/documentation/js-api/types/syntax/</a>
   */
  public enum Syntax {
    /**
     * 'scss' is the <a href="https://sass-lang.com/documentation/syntax#scss">SCSS syntax</a>.
     */
    SCSS,
    /**
     * 'indented' is the <a
     * href="https://sass-lang.com/documentation/syntax#the-indented-syntax">indented syntax</a>
     */
    INDENTED,
    /**
     * 'css' is plain CSS, which is parsed like SCSS but forbids the use of any special Sass
     * features.
     */
    CSS
  }

  /**
   * @see <a
   * href="https://sass-lang.com/documentation/js-api/types/outputstyle/">https://sass-lang.com/documentation/js-api/types/outputstyle/</a>
   */
  public enum OutputStyle {
    /**
     * (the default for Dart Sass) writes each selector and declaration on its own line.
     */
    EXPANDED,

    /**
     * removes as many extra characters as possible, and writes the entire stylesheet on a single
     * line.
     */
    COMPRESSED
  }

}

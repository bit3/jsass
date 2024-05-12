package io.bit3.jsass.webjar;

import static io.bit3.jsass.JsassConstants.CSS_EXTENSION;
import static io.bit3.jsass.JsassConstants.SASS_EXTENSION;
import static io.bit3.jsass.JsassConstants.SCSS_EXTENSION;

import io.bit3.jsass.StringOptions.Syntax;
import io.bit3.jsass.Resources;
import io.bit3.jsass.importer.AmbiguousImportException;
import io.bit3.jsass.importer.CanonicalizeContext;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.importer.ImporterResult;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webjars.NotFoundException;
import org.webjars.WebJarAssetLocator;

@Builder
public class WebjarImporter implements Importer {

  private static final String NODE_MODULES_PATH_PREFIX = "node_modules/";
  private static final String WEBJAR_SCHEME = "webjar";
  private final Logger logger = LoggerFactory.getLogger(WebjarImporter.class.getName());

  @Builder.Default
  @NonNull
  private final ClassLoader classLoader = WebjarImporter.class.getClassLoader();

  @Builder.Default
  @NotNull
  private final WebJarAssetLocator locator = new WebJarAssetLocator();

  @Builder.Default
  @NotNull
  private final Charset charset = StandardCharsets.UTF_8;

  @Override
  @SneakyThrows
  public @Nullable URI canonicalize(@NotNull String url, @NotNull CanonicalizeContext context) {
    logger.trace("Canonicalize url `{}`, referer: `{}`", url, context.getContainingUrl());

    try {
      final var uri = new URI(url);
      if (WEBJAR_SCHEME.equals(uri.getScheme())) {
        final var webjarLocation = resolveWebjarLocation(uri);
        if (null != webjarLocation) {
          final var fullPath = resolveWebjarFullPath(webjarLocation);
          if (null != fullPath) {
            logger.trace("Url `{}` is already a valid webjar url, keep it", url);
            return uri;
          }

          final var parts = PathParts.fromWebjarLocation(webjarLocation);
          return resolveWebjarAsset(url, parts);
        }
      } else if (null != uri.getScheme()) {
        logger.trace("Url `{}` not a webjar url, skipping", url);
        return null;
      }
    } catch (URISyntaxException e) {
      // ignore
    }

    final var parts = parsePath(url);

    return resolveWebjarAsset(url, parts);
  }

  private @Nullable URI resolveWebjarAsset(@NotNull String url, PathParts parts)
      throws AmbiguousImportException {
    if (parts != null) {
      final var variants = determineVariants(parts);
      final var existingVariants = variants
          .stream()
          .filter(variant -> {
            try {
              locator.getFullPath(variant.webjarName, variant.getPath());
              return true;
            } catch (NotFoundException e) {
              return false;
            }
          })
          .map(PathParts::buildUri)
          .collect(Collectors.toList());

      if (logger.isTraceEnabled()) {
        logger.trace(
            "Resolved url `{}` to `{}`",
            url,
            existingVariants.stream().map(URI::toString).collect(Collectors.joining("`, `"))
        );
      }

      if (existingVariants.size() > 1) {
        throw new AmbiguousImportException(url, existingVariants);
      } else if (!existingVariants.isEmpty()) {
        return existingVariants.get(0);
      }
    }

    return null;
  }

  private List<PathParts> determineVariants(PathParts parts) {
    if (parts.basename.endsWith(SASS_EXTENSION) || parts.basename.endsWith(SCSS_EXTENSION)) {
      return List.of(
          // try the import "as it is"
          parts,
          // prepend basename with `_`
          parts.toBuilder().basename("_" + parts.basename).build()
      );
    }

    return List.of(
        // try the import "as it is"
        parts,
        // {basename}.sass
        parts.toBuilder().basename(parts.basename + SASS_EXTENSION).build(),
        // {basename}.scss
        parts.toBuilder().basename(parts.basename + SCSS_EXTENSION).build(),
        // _{basename}.sass
        parts.toBuilder().basename("_" + parts.basename + SASS_EXTENSION).build(),
        // _{basename}.scss
        parts.toBuilder().basename("_" + parts.basename + SCSS_EXTENSION).build()
    );
  }

  private @Nullable PathParts parsePath(String url) {
    var path = url;
    while (path.startsWith("/")) {
      path = path.substring(1);
    }
    while (path.startsWith("../")) {
      path = path.substring(3);
    }
    if (path.startsWith(NODE_MODULES_PATH_PREFIX)) {
      path = path.substring(NODE_MODULES_PATH_PREFIX.length());
    }
    final var indexOfSlash = path.indexOf('/');
    if (indexOfSlash != -1) {
      var webjarName = path.substring(0, indexOfSlash);
      path = path.substring(indexOfSlash + 1);

      if (webjarName.startsWith("@")) {
        final var nextIndexOfSlash = path.indexOf('/');
        if (nextIndexOfSlash != -1) {
          webjarName += "/" + path.substring(0, nextIndexOfSlash);
          path = path.substring(nextIndexOfSlash + 1);
        }
      }

      final var lastIndexOfSlash = path.lastIndexOf('/');
      final String basename;
      if (lastIndexOfSlash != -1) {
        // path := `some/path/to/file`
        basename = path.substring(lastIndexOfSlash + 1);
        path = path.substring(0, lastIndexOfSlash + 1);
      } else {
        // path := `file`
        basename = path;
        path = "/";
      }

      return new PathParts(webjarName, path, basename);
    }

    return null;
  }

  @Override
  @SneakyThrows
  public @Nullable ImporterResult load(@NotNull URI canonicalUrl) {
    if (WEBJAR_SCHEME.equals(canonicalUrl.getScheme())) {
      final var fullPath = resolveWebjarFullPath(canonicalUrl);

      if (null == fullPath) {
        return null;
      }

      final var scssString = Objects.requireNonNull(
          Resources.toString(classLoader, fullPath, charset),
          () -> "Webjar resource " + fullPath + " not found"
      );
//      final var mapString = Resources.toString(classLoader, scssString + ".map", charset);
      final var syntax = determineSyntax(fullPath);

      logger.trace("Webjar URL `{}` resolved to `{}`", canonicalUrl, fullPath);

      return ImporterResult.builder()
          .contents(scssString)
//          .sourceMapUrl(mapString)
          .syntax(syntax)
          .build();
    } else {
      logger.trace("Cannot load non-webjar URL `{}`, skipping", canonicalUrl);
    }
    return null;
  }

  private @Nullable WebjarLocation resolveWebjarLocation(@NotNull URI canonicalUrl) {
    try {
      if (null != canonicalUrl.getSchemeSpecificPart()) {
        final var schemeSpecificPart = canonicalUrl.getSchemeSpecificPart();
        final var indexOfSlash = schemeSpecificPart.indexOf('/');
        final var webjarName = schemeSpecificPart.substring(0, indexOfSlash);
        final var path = schemeSpecificPart.substring(indexOfSlash + 1);
        return WebjarLocation.builder().webjar(webjarName).partialPath(path).build();
      }

      final var webjarName = canonicalUrl.getHost();
      final var path = canonicalUrl.getPath();
      return WebjarLocation.builder().webjar(webjarName).partialPath(path).build();
    } catch (NotFoundException e) {
      return null;
    }
  }

  private @Nullable String resolveWebjarFullPath(@NotNull URI canonicalUrl) {
    return Optional
        .ofNullable(resolveWebjarLocation(canonicalUrl))
        .map(this::resolveWebjarFullPath)
        .orElse(null);
  }

  private @Nullable String resolveWebjarFullPath(@NotNull WebjarLocation webjarLocation) {
    try {
      return locator.getFullPath(webjarLocation.getWebjar(), webjarLocation.getPartialPath());
    } catch (NotFoundException e) {
      return null;
    }
  }

  private static @NotNull Syntax determineSyntax(String path) {
    if (path.endsWith(SASS_EXTENSION)) {
      return Syntax.INDENTED;
    }

    if (path.endsWith(CSS_EXTENSION)) {
      return Syntax.CSS;
    }

    return Syntax.SCSS;
  }

  @Data
  @Builder(toBuilder = true)
  private static class PathParts {

    /**
     * The webjar name, extracted from path. The part after `/node_modules/`.
     */
    private final String webjarName;

    /**
     * The directory path, ends with `/`. The part between the webjar name and the basename.
     */
    private final String directoryPath;

    /**
     * The basename.
     */
    private final String basename;

    public String getPath() {
      return directoryPath + basename;
    }

    @SneakyThrows
    public @NotNull URI buildUri() {
      return new URI(WEBJAR_SCHEME, getWebjarName()+ "/" + getPath(), null);
    }

    public static @NotNull PathParts fromWebjarLocation(@NotNull WebjarLocation webjarLocation) {
      var path = webjarLocation.getPartialPath();
      final var lastIndexOfSlash = path.lastIndexOf('/');
      final String basename;
      if (lastIndexOfSlash != -1) {
        // path := `some/path/to/file`
        basename = path.substring(lastIndexOfSlash + 1);
        path = path.substring(0, lastIndexOfSlash + 1);
      } else {
        // path := `file`
        basename = path;
        path = "/";
      }

      return new PathParts(webjarLocation.getWebjar(), path, basename);
    }
  }
}

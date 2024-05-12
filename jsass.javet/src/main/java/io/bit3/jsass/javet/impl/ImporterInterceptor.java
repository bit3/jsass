package io.bit3.jsass.javet.impl;

import com.caoccao.javet.annotations.V8Function;
import io.bit3.jsass.Utils;
import io.bit3.jsass.importer.CanonicalizeContext;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.importer.ImporterResult;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
@SuppressWarnings("unused")
public class ImporterInterceptor {

  private final Importer importer;

  @V8Function
  @Nullable
  public String canonicalize(@NotNull String url, @NotNull Map<String, Object> context) {
    final var canonicalizeContext = CanonicalizeContext
        .builder()
        .fromImport(
            Optional
                .ofNullable(context.get("fromImport"))
                .filter(Boolean.class::isInstance)
                .map(Boolean.class::cast)
                .orElse(false)
        )
        .containingUrl(
            Optional
                .ofNullable(context.get("containingUrl"))
                .map(Utils::toUri)
                .orElse(null)
        )
        .build();
    return Optional.ofNullable(importer.canonicalize(url, canonicalizeContext)).map(Objects::toString).orElse(null);
  }

  @V8Function
  @Nullable
  @SneakyThrows
  public Map<String, String> load(@NotNull String canonicalUrl) {
    final var result = importer.load(new URI(canonicalUrl));
    if (null == result) {
      return null;
    }

    final var map = new HashMap<String, String>(3);
    map.put("contents", result.getContents());
    map.put("sourceMapUrl", Objects.toString(result.getSourceMapUrl(), null));
    map.put("syntax", result.getSyntax().toString().toLowerCase(Locale.ENGLISH));
    return map;
  }

}

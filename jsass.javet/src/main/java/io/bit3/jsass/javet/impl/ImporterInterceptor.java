package io.bit3.jsass.javet.impl;

import com.caoccao.javet.annotations.V8Function;
import io.bit3.jsass.importer.Importer;
import io.bit3.jsass.importer.ImporterResult;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.URL;

@AllArgsConstructor
public class ImporterInterceptor {
  private final Importer importer;

  @V8Function
  @Nullable
  public URL canonicalize(@NotNull String url) {
    return importer.canonicalize(url);
  }

  @V8Function
  @Nullable
  public ImporterResult load(@NotNull URL canonicalUrl) {
    return importer.load(canonicalUrl);
  }
}

package io.bit3.jsass.importer;

import io.bit3.jsass.JsassException;
import org.jetbrains.annotations.NotNull;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class AmbiguousImportException extends JsassException {

  public AmbiguousImportException(@NotNull String importUrl, @NotNull List<URI> ambiguousStylesheets) {
    super(
        "Multiple stylesheets found for import " + importUrl + ", found " +
            ambiguousStylesheets.stream().map(URI::toString)
                .collect(Collectors.joining("`, `", "`", "`"))
    );
  }

}

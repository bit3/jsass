package io.bit3.jsass;

import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import java.net.URI;

public final class Utils {
  private Utils() {
    throw new UnsupportedOperationException();
  }

  @Contract("!null -> !null; null -> null")
  public static String stripTrailingSemicolon(String input) {
    if (null == input) {
      return null;
    }

    var result = input.stripTrailing();
    var indexOfLastSemicolon = result.length() - 1;
    while (result.charAt(indexOfLastSemicolon) == ';') {
      indexOfLastSemicolon--;
    }
    indexOfLastSemicolon++;

    return indexOfLastSemicolon < result.length() ? result.substring(0, indexOfLastSemicolon) : result;
  }

  @Contract("!null -> !null; null -> null")
  @SneakyThrows
  public static URI toUri(Object object) {
    if (null == object) {
      return null;
    }

    return new URI(object.toString());
  }
}

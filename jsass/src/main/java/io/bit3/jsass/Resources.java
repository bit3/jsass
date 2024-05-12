package io.bit3.jsass;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

public class Resources {

  private Resources() {
    throw new UnsupportedOperationException();
  }

  @SneakyThrows
  public static @Nullable String toString(
      @NonNull String resourceName
  ) {
    return toString(Resources.class.getClassLoader(), resourceName, StandardCharsets.UTF_8);
  }

  @SneakyThrows
  public static @Nullable String toString(
      @NonNull String resourceName,
      @NonNull Charset charset
  ) {
    return toString(Resources.class.getClassLoader(), resourceName, charset);
  }

  @SneakyThrows
  public static @Nullable String toString(
      @NonNull ClassLoader classLoader,
      @NonNull String resourceName
  ) {
    return toString(classLoader, resourceName, StandardCharsets.UTF_8);
  }

  @SneakyThrows
  public static @Nullable String toString(
      @NonNull ClassLoader classLoader,
      @NonNull String resourceName,
      @NonNull Charset charset
  ) {
    try (final var inputStream = classLoader.getResourceAsStream(resourceName)) {
      if (null == inputStream) {
        return null;
      }

      return new String(inputStream.readAllBytes(), charset);
    }
  }
}

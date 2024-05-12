package io.bit3.jsass.javet.js;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Value
@Builder
public class ResourceJavaScriptModule implements JavaScriptModule {

  @NonNull
  @Builder.Default
  ClassLoader classLoader = ResourceJavaScriptModule.class.getClassLoader();

  @NonNull
  @Builder.Default
  Charset charset = StandardCharsets.UTF_8;

  @NonNull
  String resource;

  @Nullable
  String resourceName;

  @Builder.Default
  boolean isModule = true;

  @SneakyThrows
  @Override
  public @NotNull String getScript() {
    try (val inputStream = classLoader.getResourceAsStream(resource)) {
      return null != inputStream ? new String(inputStream.readAllBytes(), charset) : "";
    } catch (IOException e) {
      throw new ResourceNotFoundException("Resource " + resource + " not found", e);
    }
  }

  public static class ResourceNotFoundException extends IOException {

    public ResourceNotFoundException(String message, Throwable cause) {
      super(message, cause);
    }

  }
}

package io.bit3.jsass.javet.webjars;

import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.callback.IV8ModuleResolver;
import com.caoccao.javet.values.reference.IV8Module;
import com.caoccao.javet.values.reference.V8Module;
import io.bit3.jsass.javet.webjars.json.JsonDeserializer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webjars.WebJarAssetLocator;

@Builder
public class WebjarV8ModuleResolver implements IV8ModuleResolver {

  private final Logger logger = LoggerFactory.getLogger(WebjarV8ModuleResolver.class.getName());

  @Builder.Default
  @NonNull
  private final ClassLoader classLoader = WebjarV8ModuleResolver.class.getClassLoader();

  @Builder.Default
  @NonNull
  private final WebJarAssetLocator locator = new WebJarAssetLocator();

  @NonNull
  private final JsonDeserializer jsonDeserializer;

  private final Map<String, V8Module> modules = new HashMap<>();

  @Override
  @SneakyThrows
  public IV8Module resolve(
      V8Runtime v8Runtime,
      String resourceName,
      IV8Module v8ModuleReferrer
  ) {
    final var referrerResourceName = v8ModuleReferrer.getResourceName();
    logger.trace("Resolve import `{}`, referer: `{}`", resourceName, referrerResourceName);

    final var fullPath = resourceName.startsWith("./") || resourceName.startsWith("../")
        ? getRelativeResource(v8ModuleReferrer, resourceName)
        : getDefaultResource(resourceName);

    if (null != fullPath) {
      return modules.computeIfAbsent(fullPath, k -> {
        logger.trace("Resolved import `{}` (referer: `{}`) to `classpath:{}`", resourceName,
            referrerResourceName, fullPath);
        return loadAndCompileModule(v8Runtime, fullPath);
      });
    }

    return null;
  }

  @SneakyThrows
  private V8Module loadAndCompileModule(V8Runtime v8Runtime, String fullPath) {
    try (
        final var inputStream = Objects.requireNonNull(
            classLoader.getResourceAsStream(fullPath),
            () -> "Webjar resource " + fullPath + " not found"
        )
    ) {
      final var scriptString = new String(inputStream.readAllBytes(),
          StandardCharsets.UTF_8);

      return v8Runtime
          .getExecutor(scriptString)
          .setResourceName(fullPath)
          .compileV8Module();
    }
  }

  private String getDefaultResource(
      @NotNull String resourceName
  ) {
    final var defaultResourcePath = determineDefaultResourcePathFromPackageJson(resourceName);
    if (null != defaultResourcePath) {
      return locator.getFullPath(resourceName, defaultResourcePath);
    }

    return null;
  }

  @SneakyThrows
  private @Nullable String determineDefaultResourcePathFromPackageJson(String resourceName) {
    final var packageJsonPath = locator.getFullPath(resourceName, "package.json");
    try (final var inputStream = classLoader.getResourceAsStream(packageJsonPath)) {
      final var packageJson = jsonDeserializer.deserialize(inputStream);
      final var exports = packageJson.get("exports");
      if (exports instanceof Map) {
        final var defaultExports = ((Map<?, ?>) exports).get("default");
        if (defaultExports instanceof Map) {
          return Optional
              .ofNullable(((Map<?, ?>) defaultExports).get("default"))
              .map(Objects::toString)
              .map(s -> s.startsWith("./") ? s.substring(2) : s)
              .orElse(null);
        }
      }

      final var module = packageJson.get("module");
      if (null != module) {
        return Optional
            .of(module)
            .map(Objects::toString)
            .map(s -> s.startsWith("./") ? s.substring(2) : s)
            .orElse(null);
      }

      final var main = packageJson.get("main");
      if (null != main) {
        return Optional
            .of(main)
            .map(Objects::toString)
            .map(s -> s.startsWith("./") ? s.substring(2) : s)
            .orElse(null);
      }
    }

    return null;
  }

  @SneakyThrows
  private static @NonNull String getRelativeResource(
      @NotNull IV8Module v8ModuleReferrer,
      @NotNull String resourceName
  ) {
    final var refererResourceName = v8ModuleReferrer.getResourceName();

    return new URI(refererResourceName).resolve(resourceName).toString();
  }
}

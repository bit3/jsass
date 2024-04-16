package io.bit3.jsass.javet.js;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JavaScriptModule {
  @NotNull String getScript();
  @Nullable String getResourceName();
}

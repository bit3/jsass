package io.bit3.jsass.javet.js;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

@Value
@Builder
public class PlainJavaScriptModule implements JavaScriptModule {

  @NonNull
  String script;

  @Nullable
  String resourceName;

  @Builder.Default
  boolean isModule = true;

}

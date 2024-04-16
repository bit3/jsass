package io.bit3.jsass;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SassLogger {
  void warn(@NotNull String message, @NotNull Options options);
  void debug(@NotNull String message, @NotNull Options options);

  interface Options {
    @NotNull SourceSpan getSpan();
  }

  interface SourceSpan {
    @Nullable String getContext();
    @NotNull SourceLocation getStart();
    @NotNull SourceLocation getEnd();
    @NotNull String getText();
    @Nullable String getUrl();

    interface SourceLocation {
      int getLine();
      int getColumn();
      int getOffset();
    }
  }
}

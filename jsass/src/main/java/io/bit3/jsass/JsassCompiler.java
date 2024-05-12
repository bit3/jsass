package io.bit3.jsass;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.CompletableFuture;

/**
 * The compiler compiles SCSS files, strings and contexts.
 */
public interface JsassCompiler {

  /**
   * Compile source.
   *
   * @param source The input source.
   * @param options The compile options.
   * @return The compilation output.
   * @throws JsassCompilationException If the compilation failed.
   */
  @NotNull
  CompletableFuture<Output> compileString(@NotNull String source, @Nullable StringOptions options) throws JsassCompilationException;

}

package io.bit3.jsass;

import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.function.Function;

@Value
@Builder
public class SassFunction {

  /**
   * A javascript function body.
   *
   * <p><pre>{@code (args) {
   *   return args[0]
   * }}</pre></p>
   *
   * @see <a href="https://www.caoccao.com/Javet/reference/v8_values/v8_function.html#what-is-the-source-code-of-a-function-in-v8">https://www.caoccao.com/Javet/reference/v8_values/v8_function.html#what-is-the-source-code-of-a-function-in-v8</a>
   */
  @Nullable
  String functionBody;

  /**
   *
   */
  @Nullable
  String callbackName;

  @Nullable
  Function<List<Object>, Object> callback;

  public SassFunction(
      @Nullable String functionBody,
      @Nullable String callbackName,
      @Nullable Function<List<Object>, Object> callback
  ) {
    if ((null == functionBody || functionBody.isBlank()) && null == callback) {
      throw new IllegalArgumentException("At least a javascript or callback is required, none given");
    }

    if (null != functionBody && !functionBody.isBlank() && null != callback && (null == callbackName || callbackName.isBlank())) {
      throw new IllegalArgumentException("If javascript and callback both defined, a callback name is required");
    }

    this.functionBody = functionBody;
    this.callbackName = callbackName;
    this.callback = callback;
  }
}

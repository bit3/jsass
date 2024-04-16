package io.bit3.jsass.javet;

import com.caoccao.javet.interop.V8Runtime;

@FunctionalInterface
public interface JavetRuntimeCustomizer {
  void apply(V8Runtime runtime);
}

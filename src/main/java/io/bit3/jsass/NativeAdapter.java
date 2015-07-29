package io.bit3.jsass;

import io.bit3.jsass.context.FileContext;
import io.bit3.jsass.context.StringContext;

/**
 * Adapter to native functions.
 */
public class NativeAdapter {
  static {
    // System.loadLibrary("libsass");
    // System.loadLibrary("libjsass_native_adapater");
    System.load("/home/lins/workspaces/bit3/jsass/src/main/resources/linux-x86-64/libsass.so");
    System.load("/home/lins/workspaces/bit3/jsass/src/main/resources/linux-x86-64/libjsass_native_adapter.so");
  }

  /**
   * Compile a file context.
   *
   * @return The compiled result.
   */
  public Output compile(FileContext context) {
    return compileFile(context);
  }

  /**
   * Compile a string context.
   *
   * @return The compiled result.
   */
  public Output compile(StringContext context) {
    return compileString(context);
  }

  /**
   * Native call, see file src/main/c/io_bit3_jsass_NativeAdapter.c.
   */
  private native Output compileFile(FileContext context);

  /**
   * Native call, see file src/main/c/io_bit3_jsass_NativeAdapter.c.
   */
  private native Output compileString(StringContext context);
}

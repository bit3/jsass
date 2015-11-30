package io.bit3.jsass.adapter;

import java.io.File;

/**
 * This loader handle the extraction and loading of the shared library files from the jar.
 */
final class NativeTestLoader {
  /**
   * Load the shared libraries.
   */
  public static void loadTestLibrary() {
    try {
      File tmpDir = new File(System.getProperty("java.io.tmpdir"));
      File dir = File.createTempFile("libjsass-", ".d", tmpDir);
      dir.delete();
      dir.mkdir();
      dir.deleteOnExit();

      if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
        System.load(NativeLoader.saveLibrary(dir, "sass"));
      }

      System.load(NativeLoader.saveLibrary(dir, "jsass_test"));
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
      exception.printStackTrace(System.err);
      throw new RuntimeException(exception);
    }
  }
}

package io.bit3.jsass.adapter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * This loader handle the extraction and loading of the shared library files from the jar.
 */
final class NativeLoader {
  /**
   * Load the shared libraries.
   */
  public static void loadLibrary() {
    try {
      File tmpDir = new File(System.getProperty("java.io.tmpdir"));
      File dir = File.createTempFile("libjsass-", ".d", tmpDir);
      dir.delete();
      dir.mkdir();
      dir.deleteOnExit();

      if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
        System.load(saveLibrary(dir, "sass"));
      }

      System.load(saveLibrary(dir, "jsass"));
    } catch (Exception exception) {
      System.err.println(exception.getMessage());
      exception.printStackTrace(System.err);
      throw new RuntimeException(exception);
    }
  }

  /**
   * Find the right shared library, depending on the operating system and architecture.
   *
   * @throws UnsupportedOperationException Throw an exception if no native library for this
   * platform was found.
   */
  static URL findLibraryResource(String library) {
    String osName = System.getProperty("os.name").toLowerCase();
    String osArch = System.getProperty("os.arch").toLowerCase();
    String platform;
    String fileExtension;

    if (osName.startsWith("win")) {
      fileExtension = "dll";

      switch (osArch) {
        case "i386":
        case "x86":
          platform = "windows-x32";
          break;

        case "amd64":
        case "x86_64":
          platform = "windows-x64";
          break;

        default:
          throw new UnsupportedOperationException(
              "Platform " + osName + ":" + osArch + " not supported"
          );
      }
    } else if (osName.startsWith("linux")) {
      fileExtension = "so";

      switch (osArch) {
        case "amd64":
        case "x86_64":
          platform = "linux-x64";
          break;

        default:
          throw new UnsupportedOperationException(
              "Platform " + osName + ":" + osArch + " not supported"
          );
      }
    } else if (osName.startsWith("mac")) {
      fileExtension = "dylib";
      platform = "darwin";
    } else {
      throw new UnsupportedOperationException(
          "Platform " + osName + ":" + osArch + " not supported"
      );
    }

    String resourceName = "/" + platform + "/" + library + "." + fileExtension;

    URL resource = NativeLoader.class.getResource(resourceName);

    if (null == resource) {
      throw new UnsupportedOperationException(
          "Platform " + osName + ":" + osArch + " not supported"
      );
    }

    return resource;
  }

  /**
   * Save the right shared library in the given temporary directory.
   */
  static String saveLibrary(File dir, String library) throws IOException {
    library = "lib" + library;

    URL libraryResource = findLibraryResource(library);

    String basename = FilenameUtils.getName(libraryResource.getPath());
    File file = new File(dir, basename);
    file.deleteOnExit();

    try (
        InputStream in = libraryResource.openStream();
        OutputStream out = new FileOutputStream(file)
    ) {
      IOUtils.copy(in, out);
    }

    return file.getAbsolutePath();
  }
}

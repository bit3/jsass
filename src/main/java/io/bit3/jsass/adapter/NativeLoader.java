package io.bit3.jsass.adapter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;

/**
 * This loader handle the extraction and loading of the shared library files from the jar.
 */
final class NativeLoader {
  /**
   * Load the shared libraries.
   */
  public static void loadLibrary() {
    try {
      File dir = Files.createTempDirectory("libjsass-").toFile();
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
   * @throws UnsupportedOperationException Throw an exception if no native library for this platform
   *                                       was found.
   */
  static URL findLibraryResource(String library) {
    String osName = System.getProperty("os.name").toLowerCase();
    String osArch = System.getProperty("os.arch").toLowerCase();
    String resourceName = null;

    if (osName.startsWith("win")) {
      resourceName = determineWindowsLibrary(library, osName, osArch);
    } else if (osName.startsWith("linux")) {
      resourceName = determineLinuxLibrary(library, osName, osArch);
    } else if (osName.startsWith("mac")) {
      resourceName = determineMacLibrary(library);
    } else {
      unsupportedPlatform(osName, osArch);
    }

    URL resource = NativeLoader.class.getResource(resourceName);

    if (null == resource) {
      unsupportedPlatform(osName, osArch);
    }

    return resource;
  }

  /**
   * Determine the right windows library depending on the architecture.
   *
   * @param library The library name.
   * @param osName  The operating system name.
   * @param osArch  The system architecture.
   * @return The library resource.
   * @throws UnsupportedOperationException Throw an exception if no native library for this platform
   *                                       was found.
   */
  private static String determineWindowsLibrary(String library, String osName, String osArch) {
    String resourceName;
    String platform = null;
    String fileExtension = "dll";

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

    resourceName = "/" + platform + "/" + library + "." + fileExtension;
    return resourceName;
  }

  /**
   * Determine the right linux library depending on the architecture.
   *
   * @param library The library name.
   * @param osName  The operating system name.
   * @param osArch  The system architecture.
   * @return The library resource.
   * @throws UnsupportedOperationException Throw an exception if no native library for this platform
   *                                       was found.
   */
  private static String determineLinuxLibrary(String library, String osName, String osArch) {
    String resourceName;
    String platform = null;
    String fileExtension = "so";

    switch (osArch) {
      case "amd64":
      case "x86_64":
        platform = "linux-x64";
        break;

      default:
        unsupportedPlatform(osName, osArch);
    }

    resourceName = "/" + platform + "/" + library + "." + fileExtension;
    return resourceName;
  }

  /**
   * Determine the right mac library depending on the architecture.
   *
   * @param library The library name.
   * @return The library resource.
   */
  private static String determineMacLibrary(String library) {
    String resourceName;
    String platform = "darwin";
    String fileExtension = "dylib";
    resourceName = "/" + platform + "/" + library + "." + fileExtension;
    return resourceName;
  }

  /**
   * Save the shared library in the given temporary directory.
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

  private static void unsupportedPlatform(String osName, String osArch) {
    throw new UnsupportedOperationException(
        "Platform " + osName + ":" + osArch + " not supported"
    );
  }
}

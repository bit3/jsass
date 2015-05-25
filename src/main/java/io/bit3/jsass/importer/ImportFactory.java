package io.bit3.jsass.importer;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import sass.SassLibrary;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 * Factory to convert imports from java to libsass and wise versa.
 */
public class ImportFactory {

  /**
   * SASS library adapter.
   */
  private final SassLibrary sass;

  /**
   * Create a new import factory.
   *
   * @param sass The SASS library adapter.
   */
  public ImportFactory(SassLibrary sass) {
    this.sass = sass;
  }

  /**
   * Create a libsass import entry from a java import.
   *
   * @param importSource The java import object.
   * @return The libsass import entry representing the java import object.
   */
  public SassLibrary.Sass_Import_Entry create(Import importSource) {
    Memory sourceMemory;
    Memory sourceMapMemory;

    String path = importSource.getUri().toString();

    String base = null == importSource.getBase() ? "" : importSource.getBase().toString();

    if (null == importSource.getContents()) {
      sourceMemory = new Memory(1);
      sourceMemory.setByte(0, (byte) 0);
    } else {
      byte[] bytes = importSource.getContents().getBytes(importSource.getContentsCharset());
      sourceMemory = new Memory(bytes.length + 1);
      sourceMemory.write(0, bytes, 0, bytes.length);
      sourceMemory.setByte(bytes.length, (byte) 0);
    }

    ByteBuffer source = sourceMemory.getByteBuffer(0, sourceMemory.size());

    if (null == importSource.getSourceMap()) {
      sourceMapMemory = new Memory(1);
      sourceMapMemory.setByte(0, (byte) 0);
    } else {
      byte[] bytes = importSource.getSourceMap().getBytes(importSource.getSourceMapCharset());
      sourceMapMemory = new Memory(bytes.length + 1);
      sourceMapMemory.write(0, bytes, 0, bytes.length);
      sourceMapMemory.setByte(bytes.length, (byte) 0);
    }

    ByteBuffer sourceMap = sourceMapMemory.getByteBuffer(0, sourceMapMemory.size());

    SassLibrary.Sass_Import_Entry entry = sass.sass_make_import(path, base, source, sourceMap);

    // Caution: libsass obtain ownership of the memories
    Pointer.nativeValue(sourceMemory, 0);
    Pointer.nativeValue(sourceMapMemory, 0);

    return entry;
  }

  /**
   * Create a java import from a libsass import entry.
   *
   * @param importEntry The libsass import entry.
   * @return The java import object representing the libsass import entry.
   */
  public Import create(SassLibrary.Sass_Import_Entry importEntry) {
    String path = sass.sass_import_get_path(importEntry);
    String base = sass.sass_import_get_base(importEntry);
    String source = sass.sass_import_get_source(importEntry);
    String sourceMap = sass.sass_import_get_srcmap(importEntry);

    try {
      return new Import(new URI(path), new URI(base), source, sourceMap);
    } catch (URISyntaxException exception) {
      throw new RuntimeException(exception);
    }
  }
}

package io.bit3.jsass.importer;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import io.bit3.jsass.context.Context;
import sass.SassLibrary;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * Wraps a java importer into a libsass import function.
 */
public class ImporterWrapper implements SassLibrary.Sass_Importer_Fn {

  /**
   * SASS library adapter.
   */
  private final SassLibrary sass;

  /**
   * The original compiled context.
   */
  private final Context originalContext;

  /**
   * The custom importer.
   */
  private final Importer importer;

  /**
   * Create a new import wrapper.
   *
   * @param sass            The SASS library adapter.
   * @param originalContext The original compiled context.
   * @param importer        The custom importer.
   */
  public ImporterWrapper(SassLibrary sass, Context originalContext, Importer importer) {
    this.sass = sass;
    this.originalContext = originalContext;
    this.importer = importer;
  }

  @Override
  public SassLibrary.Sass_Import_List apply(Pointer url, Pointer cb,
                                            SassLibrary.Sass_Compiler compiler) {
    SassLibrary.Sass_Import_Entry lastImportEntry = sass.sass_compiler_get_last_import(compiler);
    String previous = null == lastImportEntry
        ? ""
        : sass.sass_import_get_path(lastImportEntry);

    Collection<Import> imports = importer.apply(
        url.getString(0),
        previous,
        originalContext
    );

    // return 0 to let libsass handle the import itself
    if (null == imports) {
      return null;
    }

    SassLibrary.Sass_Import_List list = sass.sass_make_import_list(new NativeSize(imports.size()));

    int index = 0;
    String path;
    String base;
    byte[] bytes;
    Memory sourceMemory;
    ByteBuffer source;
    Memory sourceMapMemory;
    ByteBuffer sourceMap;

    for (Import importSource : imports) {
      path = importSource.getUri().toString();

      base = null == importSource.getBase() ? "" : importSource.getBase().toString();

      if (null == importSource.getContents()) {
        sourceMemory = new Memory(1);
        sourceMemory.setByte(0, (byte) 0);
      } else {
        bytes = importSource.getContents().getBytes(importSource.getContentsCharset());
        sourceMemory = new Memory(bytes.length + 1);
        sourceMemory.write(0, bytes, 0, bytes.length);
        sourceMemory.setByte(bytes.length, (byte) 0);
      }

      source = sourceMemory.getByteBuffer(0, sourceMemory.size());

      if (null == importSource.getSourceMap()) {
        sourceMapMemory = new Memory(1);
        sourceMapMemory.setByte(0, (byte) 0);
      } else {
        bytes = importSource.getSourceMap().getBytes(importSource.getSourceMapCharset());
        sourceMapMemory = new Memory(bytes.length + 1);
        sourceMapMemory.write(0, bytes, 0, bytes.length);
        sourceMapMemory.setByte(bytes.length, (byte) 0);
      }

      sourceMap = sourceMapMemory.getByteBuffer(0, sourceMapMemory.size());

      SassLibrary.Sass_Import_Entry entry = sass.sass_make_import(path, base, source, sourceMap);
      sass.sass_import_set_list_entry(list, new NativeSize(index), entry);
      index++;
    }

    return list;
  }
}

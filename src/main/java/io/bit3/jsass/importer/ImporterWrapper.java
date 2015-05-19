package io.bit3.jsass.importer;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import com.sun.jna.Pointer;
import io.bit3.jsass.context.Context;
import sass.SassLibrary;

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
   * The import factory.
   */
  private final ImportFactory importFactory;

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
   * @param importFactory   The import factory.
   * @param originalContext The original compiled context.
   * @param importer        The custom importer.
   */
  public ImporterWrapper(SassLibrary sass, ImportFactory importFactory, Context originalContext,
                         Importer importer) {
    this.sass = sass;
    this.importFactory = importFactory;
    this.originalContext = originalContext;
    this.importer = importer;
  }

  @Override
  public SassLibrary.Sass_Import_List apply(Pointer url, Pointer cb,
                                            SassLibrary.Sass_Compiler compiler) {
    SassLibrary.Sass_Import_Entry lastImportEntry = sass.sass_compiler_get_last_import(compiler);
    Import lastImport = importFactory.create(lastImportEntry);

    Collection<Import> imports = importer.apply(
        url.getString(0),
        lastImport,
        originalContext
    );

    // return 0 to let libsass handle the import itself
    if (null == imports) {
      return null;
    }

    SassLibrary.Sass_Import_List list = sass.sass_make_import_list(new NativeSize(imports.size()));

    int index = 0;

    for (Import importSource : imports) {
      SassLibrary.Sass_Import_Entry entry = importFactory.create(importSource);
      sass.sass_import_set_list_entry(list, new NativeSize(index), entry);
      index++;
    }

    return list;
  }
}

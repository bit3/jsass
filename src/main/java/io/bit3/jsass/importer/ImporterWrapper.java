package io.bit3.jsass.importer;

import com.ochafik.lang.jnaerator.runtime.NativeSize;
import com.sun.jna.Pointer;
import io.bit3.jsass.context.Context;
import io.bit3.jsass.context.ImportStack;
import sass.SassLibrary;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * Wraps a java importer into a libsass import function.
 */
public class ImporterWrapper implements SassLibrary.Sass_Importer_Fn {

  /**
   * SASS library adapter.
   */
  private final SassLibrary sass;

  private final ImportStack importStack;

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
   *  @param sass            The SASS library adapter.
   * @param importStack
   * @param importFactory   The import factory.
   * @param originalContext The original compiled context.
   * @param importer        The custom importer.
   */
  public ImporterWrapper(
      SassLibrary sass,
      ImportStack importStack,
      ImportFactory importFactory,
      Context originalContext,
      Importer importer
  ) {
    this.sass = sass;
    this.importStack = importStack;
    this.importFactory = importFactory;
    this.originalContext = originalContext;
    this.importer = importer;
  }

  @Override
  public SassLibrary.Sass_Import_List apply(
      Pointer url, Pointer cb,
      SassLibrary.Sass_Compiler compiler
  ) {
    SassLibrary.Sass_Import_Entry lastImportEntry = sass
        .sass_compiler_get_last_import(compiler);

    try {
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

      boolean isNotJsassCustomImporter = !(importer instanceof JsassCustomImporter);
      SassLibrary.Sass_Import_List list = sass.sass_make_import_list(
          new NativeSize(isNotJsassCustomImporter ? 3 * imports.size() : imports.size())
      );
      SassLibrary.Sass_Import_Entry entry;
      int index = 0;
      for (Import importSource : imports) {
        if (isNotJsassCustomImporter) {
          Import preImport = createPreImport(importSource);
          entry = importFactory.create(preImport);
          sass.sass_import_set_list_entry(list, new NativeSize(index), entry);
          index++;
        }

        entry = importFactory.create(importSource);
        sass.sass_import_set_list_entry(list, new NativeSize(index), entry);
        index++;

        if (isNotJsassCustomImporter) {
          Import postImport = createPostImport(importSource);
          entry = importFactory.create(postImport);
          sass.sass_import_set_list_entry(list, new NativeSize(index), entry);
          index++;
        }
      }

      return list;
    } catch (Throwable throwable) {
      SassLibrary.Sass_Import_Entry entry = sass.sass_import_set_error(
          lastImportEntry,
          throwable.getMessage(),
          new NativeSize(0),
          new NativeSize(0)
      );

      SassLibrary.Sass_Import_List list = sass.sass_make_import_list(new NativeSize(1));
      sass.sass_import_set_list_entry(list, new NativeSize(0), entry);

      return list;
    }
  }

  private Import createPreImport(Import importSource) {
    int id = importStack.register(importSource);

    StringBuilder preSource = new StringBuilder();

    // $jsass-void: jsass_import_stack_push(<id>) !global;
    preSource.append(
        String.format(
            "$jsass-void: jsass_import_stack_push(%d) !global;\n",
            id
        )
    );

    try {
      return new Import(
          new URI(importSource.getUri() + "/JSASS_PRE_IMPORT.scss"),
          importSource.getUri(),
          preSource.toString()
      );
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private Import createPostImport(Import importSource) {
    StringBuilder postSource = new StringBuilder();

    // $jsass-void: jsass_import_stack_pop() !global;
    postSource.append("$jsass-void: jsass_import_stack_pop() !global;\n");

    try {
      return new Import(
          new URI(importSource.getUri() + "/JSASS_POST_IMPORT.scss"),
          importSource.getUri(),
          postSource.toString()
      );
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}

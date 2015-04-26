package io.bit3.jsass.importer;

import io.bit3.jsass.context.Context;
import sass.SassLibrary;

import java.util.Collection;

/**
 * Factory to create a libsass importer callback.
 */
public class ImporterCallbackFactory {

  /**
   * SASS library adapter.
   */
  private final SassLibrary sass;

  /**
   * Create a new factory.
   *
   * @param sass The SASS library adapter.
   */
  public ImporterCallbackFactory(SassLibrary sass) {
    this.sass = sass;
  }

  /**
   * Create a new callback for a list of importers.
   *
   * <p>Wraps a list of custom importers into one chain importer and create one libsass importer
   * callback for this chain.</p>
   *
   * @param originalContext The original context that will be compiled.
   * @param importers       The list of custom importers.
   * @return The newly created libsass import callback.
   */
  public SassLibrary.Sass_C_Import_Callback create(
      Context originalContext,
      Collection<Importer> importers
  ) {
    Importer chain = new ChainImporter(importers);

    ImporterWrapper wrapper = new ImporterWrapper(sass, originalContext, chain);

    return sass.sass_make_importer(wrapper, null);
  }
}

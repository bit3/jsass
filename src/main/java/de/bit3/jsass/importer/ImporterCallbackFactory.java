package de.bit3.jsass.importer;

import de.bit3.jsass.context.Context;
import sass.SassLibrary;

import java.util.Collection;

public class ImporterCallbackFactory {
    /**
     * SASS library adapter.
     */
    private final SassLibrary SASS;

    public ImporterCallbackFactory(SassLibrary SASS) {
        this.SASS = SASS;
    }

    public SassLibrary.Sass_C_Import_Callback create(
            Context originalContext,
            Collection<Importer> importers
    ) {
        Importer chain = new ChainImporter(importers);

        ImporterWrapper wrapper = new ImporterWrapper(SASS, originalContext, chain);

        return SASS.sass_make_importer(wrapper, null);
    }
}

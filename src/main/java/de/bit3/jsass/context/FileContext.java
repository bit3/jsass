package de.bit3.jsass.context;

import de.bit3.jsass.Options;

import java.io.File;

public class FileContext extends AbstractContext {
    public FileContext(File inputPath, File outputPath, Options options) {
        super(inputPath, outputPath, options);
    }
}

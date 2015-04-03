package de.bit3.jsass.context;

import de.bit3.jsass.Options;

import java.io.File;

public class AbstractContext implements Context {
    private final File    inputPath;
    private final File    outputPath;
    private final Options options;

    public AbstractContext(File inputPath, File outputPath, Options options) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.options = options;
    }

    public File getInputPath() {
        return inputPath;
    }

    public File getOutputPath() {
        return outputPath;
    }

    public Options getOptions() {
        return options;
    }
}

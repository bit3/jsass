package de.bit3.jsass.context;

import de.bit3.jsass.Options;

import java.io.File;
import java.nio.charset.Charset;

public class StringContext extends AbstractContext {
    private String  string;
    private Charset charset;

    public StringContext(String string, Charset charset, File inputPath, File outputPath, Options options) {
        super(inputPath, outputPath, options);
        this.string = string;
        this.charset = charset;
    }

    public String getString() {
        return string;
    }

    public Charset getCharset() {
        return charset;
    }
}

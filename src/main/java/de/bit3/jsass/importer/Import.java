package de.bit3.jsass.importer;

import org.apache.commons.io.Charsets;

import java.net.URI;
import java.nio.charset.Charset;

public class Import {
    private final URI     uri;
    private final URI     base;
    private final String  contents;
    private final Charset contentsCharset;
    private final String  sourceMap;
    private final Charset sourceMapCharset;

    public Import(URI uri, URI base) {
        this(uri, base, null, Charsets.UTF_8, null, Charsets.UTF_8);
    }

    public Import(URI uri, URI base, String contents) {
        this(uri, base, contents, Charsets.UTF_8, null, Charsets.UTF_8);
    }

    public Import(URI uri, URI base, String contents, String sourceMap) {
        this(uri, base, contents, Charsets.UTF_8, sourceMap, Charsets.UTF_8);
    }

    public Import(URI uri, URI base, String contents, Charset contentsCharset) {
        this(uri, base, contents, contentsCharset, null, Charsets.UTF_8);
    }

    public Import(URI uri, URI base, String contents, Charset contentsCharset, String sourceMap) {
        this(uri, base, contents, contentsCharset, sourceMap, Charsets.UTF_8);
    }

    public Import(URI uri, URI base, String contents, Charset contentsCharset, String sourceMap, Charset sourceMapCharset) {
        this.uri = uri;
        this.base = base;
        this.contents = contents;
        this.contentsCharset = contentsCharset;
        this.sourceMap = sourceMap;
        this.sourceMapCharset = sourceMapCharset;
    }

    public URI getUri() {
        return uri;
    }

    public URI getBase() {
        return base;
    }

    public String getContents() {
        return contents;
    }

    public Charset getContentsCharset() {
        return contentsCharset;
    }

    public String getSourceMap() {
        return sourceMap;
    }

    public Charset getSourceMapCharset() {
        return sourceMapCharset;
    }
}

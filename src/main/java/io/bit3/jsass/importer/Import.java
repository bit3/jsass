package io.bit3.jsass.importer;

import org.apache.commons.io.Charsets;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * The result of a custom importer, can be used to import files and strings.
 */
public class Import {

  /**
   * The file uri relative to the base uri.
   */
  private final URI uri;

  /**
   * The base uri for this import.
   */
  private final URI base;

  /**
   * The in-memory sass code, may be <em>null</em> when importing a file.
   */
  private final String contents;

  /**
   * The charset of the in-memory sass code.
   */
  private final Charset contentsCharset;

  /**
   * The in-memory source map, may be <em>null</em> if no previous source map exists or when
   * importing a file.
   */
  private final String sourceMap;

  /**
   * The charset of the in-memory source map.
   */
  private final Charset sourceMapCharset;

  /**
   * Create a file import.
   *
   * @param uri  The file uri relative to the base uri.
   * @param base The base uri for this import.
   */
  public Import(URI uri, URI base) {
    this(uri, base, null, Charsets.UTF_8, null, Charsets.UTF_8);
  }

  /**
   * Create a string import.
   *
   * @param uri      The file uri relative to the base uri.
   * @param base     The base uri for this import.
   * @param contents The in-memory sass code.
   */
  public Import(URI uri, URI base, String contents) {
    this(uri, base, contents, Charsets.UTF_8, null, Charsets.UTF_8);
  }

  /**
   * Create a string import.
   *
   * @param uri       The file uri relative to the base uri.
   * @param base      The base uri for this import.
   * @param contents  The in-memory sass code.
   * @param sourceMap The in-memory source map.
   */
  public Import(URI uri, URI base, String contents, String sourceMap) {
    this(uri, base, contents, Charsets.UTF_8, sourceMap, Charsets.UTF_8);
  }

  /**
   * Create a string import.
   *
   * @param uri             The file uri relative to the base uri.
   * @param base            The base uri for this import.
   * @param contents        The in-memory sass code.
   * @param contentsCharset The charset of the in-memory sass code.
   */
  public Import(URI uri, URI base, String contents, Charset contentsCharset) {
    this(uri, base, contents, contentsCharset, null, Charsets.UTF_8);
  }

  /**
   * Create a string import.
   *
   * @param uri             The file uri relative to the base uri.
   * @param base            The base uri for this import.
   * @param contents        The in-memory sass code.
   * @param contentsCharset The charset of the in-memory sass code.
   * @param sourceMap       The in-memory source map.
   */
  public Import(URI uri, URI base, String contents, Charset contentsCharset, String sourceMap) {
    this(uri, base, contents, contentsCharset, sourceMap, Charsets.UTF_8);
  }

  /**
   * Create a string import.
   *
   * @param uri              The file uri relative to the base uri.
   * @param base             The base uri for this import.
   * @param contents         The in-memory sass code.
   * @param contentsCharset  The charset of the in-memory sass code.
   * @param sourceMap        The in-memory source map.
   * @param sourceMapCharset The charset of the in-memory source map.
   */
  public Import(URI uri, URI base, String contents, Charset contentsCharset, String sourceMap,
                Charset sourceMapCharset) {
    this.uri = uri;
    this.base = base;
    this.contents = contents;
    this.contentsCharset = contentsCharset;
    this.sourceMap = sourceMap;
    this.sourceMapCharset = sourceMapCharset;
  }

  /**
   * Get the relative file uri.
   *
   * @return The file uri relative to the base uri.
   */
  public URI getUri() {
    return uri;
  }

  /**
   * Get the base uri.
   *
   * @return The base uri for this import.
   */
  public URI getBase() {
    return base;
  }

  /**
   * Return the in-memory sass code.
   *
   * @return The in-memory sass code or <em>null</em> when importing a file.
   */
  public String getContents() {
    return contents;
  }

  /**
   * Return the charset of the in-memory sass code.
   *
   * @return The charset of the in-memory sass code.
   */
  public Charset getContentsCharset() {
    return contentsCharset;
  }

  /**
   * Return the in-memory source map.
   *
   * @return The in-memory source map or <em>null</em> when importing a file or no previous source
   *         map exists.
   */
  public String getSourceMap() {
    return sourceMap;
  }

  /**
   * Return the charset of the in-memory source map.
   *
   * @return The charset of the in-memory source map.
   */
  public Charset getSourceMapCharset() {
    return sourceMapCharset;
  }
}

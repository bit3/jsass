package io.bit3.jsass.importer;

import java.net.URI;
import java.net.URISyntaxException;

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
   * The in-memory source map, may be <em>null</em> if no previous source map exists or when
   * importing a file.
   */
  private final String sourceMap;

  /**
   * Create a file import.
   *
   * @param uri  The file uri relative to the base uri.
   * @param base The base uri for this import.
   */
  public Import(URI uri, URI base) {
    this(uri, base, null, null);
  }

  /**
   * Create a string import.
   *
   * @param uri      The file uri relative to the base uri.
   * @param base     The base uri for this import.
   * @param contents The in-memory sass code.
   */
  public Import(URI uri, URI base, String contents) {
    this(uri, base, contents, null);
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
    this.uri = uri;
    this.base = base;
    this.contents = contents;
    this.sourceMap = sourceMap;
  }

  /**
   * Create a file import.
   *
   * @param uri  The file uri relative to the base uri.
   * @param base The base uri for this import.
   */
  public Import(String uri, String base) throws URISyntaxException {
    this(uri, base, null, null);
  }

  /**
   * Create a string import.
   *
   * @param uri      The file uri relative to the base uri.
   * @param base     The base uri for this import.
   * @param contents The in-memory sass code.
   */
  public Import(String uri, String base, String contents) throws URISyntaxException {
    this(uri, base, contents, null);
  }

  /**
   * Create a string import.
   *
   * @param uri       The file uri relative to the base uri.
   * @param base      The base uri for this import.
   * @param contents  The in-memory sass code.
   * @param sourceMap The in-memory source map.
   */
  public Import(String uri, String base, String contents, String sourceMap) throws URISyntaxException {
    this.uri = new URI(uri);
    this.base = new URI(base);
    this.contents = contents;
    this.sourceMap = sourceMap;
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
   * Return the in-memory source map.
   *
   * @return The in-memory source map or <em>null</em> when importing a file or no previous source
   * map exists.
   */
  public String getSourceMap() {
    return sourceMap;
  }

}

package io.bit3.jsass.importer;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The result of a custom importer, can be used to import files and strings.
 */
public class Import {

  /**
   * The import uri for this import.
   */
  private final URI importUri;

  /**
   * The absolute uri for this import.
   */
  private final URI absoluteUri;

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
   * @param importUri  The file uri relative to the base uri.
   * @param absoluteUri The base uri for this import.
   */
  public Import(URI importUri, URI absoluteUri) {
    this(importUri, absoluteUri, null, null);
  }

  /**
   * Create a string import.
   *
   * @param importUri      The file uri relative to the base uri.
   * @param absoluteUri     The base uri for this import.
   * @param contents The in-memory sass code.
   */
  public Import(URI importUri, URI absoluteUri, String contents) {
    this(importUri, absoluteUri, contents, null);
  }

  /**
   * Create a string import.
   *
   * @param importUri       The file uri relative to the base uri.
   * @param absoluteUri      The base uri for this import.
   * @param contents  The in-memory sass code.
   * @param sourceMap The in-memory source map.
   */
  public Import(URI importUri, URI absoluteUri, String contents, String sourceMap) {
    this.importUri = importUri;
    this.absoluteUri = absoluteUri;
    this.contents = contents;
    this.sourceMap = sourceMap;
  }

  /**
   * Create a file import.
   *
   * @param importUri  The file uri relative to the base uri.
   * @param absoluteUri The base uri for this import.
   */
  public Import(String importUri, String absoluteUri) throws URISyntaxException {
    this(importUri, absoluteUri, null, null);
  }

  /**
   * Create a string import.
   *
   * @param importUri      The file uri relative to the base uri.
   * @param absoluteUri     The base uri for this import.
   * @param contents The in-memory sass code.
   */
  public Import(String importUri, String absoluteUri, String contents) throws URISyntaxException {
    this(importUri, absoluteUri, contents, null);
  }

  /**
   * Create a string import.
   *
   * @param importUri       The file uri relative to the base uri.
   * @param absoluteUri      The base uri for this import.
   * @param contents  The in-memory sass code.
   * @param sourceMap The in-memory source map.
   */
  public Import(String importUri, String absoluteUri, String contents, String sourceMap)
      throws URISyntaxException {
    URI tempImportUri;
    try {
      tempImportUri = new URI(importUri);
    } catch (URISyntaxException e) {
      tempImportUri = new File(importUri).toURI();
    }
    this.importUri = tempImportUri;

    URI tempAbsoluteUri;
    try {
      tempAbsoluteUri = new URI(absoluteUri);
    } catch (URISyntaxException e) {
      tempAbsoluteUri = new File(absoluteUri).toURI();
    }
    this.absoluteUri = tempAbsoluteUri;

    this.contents = contents;
    this.sourceMap = sourceMap;
  }

  /**
   * Get the import uri.
   *
   * @return The file uri relative to the base uri.
   */
  public URI getImportUri() {
    return importUri;
  }

  /**
   * Get the absolute uri.
   *
   * @return The base uri for this import.
   */
  public URI getAbsoluteUri() {
    return absoluteUri;
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
   * @return The in-memory source map or <em>null</em>.
   */
  public String getSourceMap() {
    return sourceMap;
  }

}

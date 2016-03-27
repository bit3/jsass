package io.bit3.jsass;

import io.bit3.jsass.importer.Importer;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The compiler options.
 */
public class Options implements Serializable {
  private static final long serialVersionUID = -3258968135964372076L;

  /**
   * Custom import functions.
   */
  private transient List<Object> functionProviders = new LinkedList<>();

  private transient List<Importer> headerImporters = new LinkedList<>();

  /**
   * Custom import functions.
   */
  private transient Collection<Importer> importers = new LinkedList<>();

  /**
   * SassList of paths.
   */
  private List<File> includePaths = new LinkedList<>();

  private String indent = "  ";

  /**
   * Treat source_string as sass (as opposed to scss).
   */
  private boolean isIndentedSyntaxSrc = false;

  private String linefeed = System.lineSeparator();

  /**
   * Disable sourceMappingUrl in css output.
   */
  private boolean omitSourceMapUrl = false;

  /**
   * Output style for the generated css code.
   */
  private OutputStyle outputStyle = OutputStyle.NESTED;

  private String pluginPath = null;

  /**
   * Precision for outputting fractional numbers.
   */
  private int precision = 8;

  /**
   * If you want inline source comments.
   */
  private boolean sourceComments = false;

  /**
   * Embed include contents in maps.
   */
  private boolean sourceMapContents = false;

  /**
   * Embed sourceMappingUrl as data uri.
   */
  private boolean sourceMapEmbed = false;

  /**
   * Path to source map file. Enables the source map generating. Used to create sourceMappingUrl.
   */
  private URI sourceMapFile;

  private URI sourceMapRoot;

  public List<Object> getFunctionProviders() {
    return functionProviders;
  }

  public void setFunctionProviders(List<Object> functionProviders) {
    this.functionProviders = functionProviders;
  }

  public List<Importer> getHeaderImporters() {
    return headerImporters;
  }

  public void setHeaderImporters(List<Importer> headerImporters) {
    this.headerImporters = headerImporters;
  }

  public Collection<Importer> getImporters() {
    return importers;
  }

  public void setImporters(Collection<Importer> importers) {
    this.importers = importers;
  }

  public List<File> getIncludePaths() {
    return includePaths;
  }

  public void setIncludePaths(List<File> includePaths) {
    this.includePaths = includePaths;
  }

  public String getIndent() {
    return indent;
  }

  public void setIndent(String indent) {
    this.indent = indent;
  }

  public boolean isIndentedSyntaxSrc() {
    return isIndentedSyntaxSrc;
  }

  public void setIsIndentedSyntaxSrc(boolean isIndentedSyntaxSrc) {
    this.isIndentedSyntaxSrc = isIndentedSyntaxSrc;
  }

  public String getLinefeed() {
    return linefeed;
  }

  public void setLinefeed(String linefeed) {
    this.linefeed = linefeed;
  }

  /**
   * Determine if sourceMappingUrl is omitted in css output.
   *
   * @return <em>true</em> if the sourceMappingUrl is omitted in css output.
   */
  public boolean isOmitSourceMapUrl() {
    return omitSourceMapUrl;
  }

  /**
   * Set if sourceMappingUrl should be omitted in css output.
   *
   * @param omitSourceMapUrl Omit the sourceMappingUrl in css output.
   */
  public void setOmitSourceMapUrl(boolean omitSourceMapUrl) {
    this.omitSourceMapUrl = omitSourceMapUrl;
  }

  /**
   * Return the output style.
   *
   * @return The output style.
   */
  public OutputStyle getOutputStyle() {
    return outputStyle;
  }

  /**
   * Set the output style.
   *
   * @param outputStyle The output style.
   */
  public void setOutputStyle(OutputStyle outputStyle) {
    this.outputStyle = outputStyle;
  }

  public String getPluginPath() {
    return pluginPath;
  }

  public void setPluginPath(String pluginPath) {
    this.pluginPath = pluginPath;
  }

  /**
   * Return the fractional numbers precision.
   *
   * @return The fractional numbers precision.
   */
  public int getPrecision() {
    return precision;
  }

  /**
   * Set the fractional numbers precision.
   *
   * @param precision The fractional numbers precision.
   */
  public void setPrecision(int precision) {
    this.precision = precision;
  }

  /**
   * Determine if inline source comments are generated.
   *
   * @return <em>true</em> if inline source comments are generated.
   */
  public boolean isSourceComments() {
    return sourceComments;
  }

  /**
   * Set if inline source comments should generated.
   *
   * @param sourceComments <em>true</em> if inline source comments should generated.
   */
  public void setSourceComments(boolean sourceComments) {
    this.sourceComments = sourceComments;
  }

  public boolean isSourceMapContents() {
    return sourceMapContents;
  }

  public void setSourceMapContents(boolean sourceMapContents) {
    this.sourceMapContents = sourceMapContents;
  }

  /**
   * Determine if source map is embedded into css output.
   *
   * @return <em>true</em> if the source map is embedded into css output.
   */
  public boolean isSourceMapEmbed() {
    return sourceMapEmbed;
  }

  /**
   * Set if source map is embedded into css output.
   *
   * @param sourceMapEmbed Embed source map into css output.
   */
  public void setSourceMapEmbed(boolean sourceMapEmbed) {
    this.sourceMapEmbed = sourceMapEmbed;
  }

  /**
   * Return the source map file.
   *
   * @return The source map file or <em>null</em> if no source map should generated.
   */
  public URI getSourceMapFile() {
    return sourceMapFile;
  }

  /**
   * Set the source map file.
   *
   * @param sourceMapFile The source map file or <em>null</em> to disable source map generation.
   */
  public void setSourceMapFile(URI sourceMapFile) {
    this.sourceMapFile = sourceMapFile;
  }

  public URI getSourceMapRoot() {
    return sourceMapRoot;
  }

  public void setSourceMapRoot(URI sourceMapRoot) {
    this.sourceMapRoot = sourceMapRoot;
  }
}

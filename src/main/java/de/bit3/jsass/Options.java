package de.bit3.jsass;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * The compiler options.
 */
public class Options {
    /**
     * Output style for the generated css code.
     */
    private OutputStyle outputStyle = OutputStyle.NESTED;

    /**
     * If you want inline source comments.
     */
    private boolean sourceComments;

    /**
     * Path to source map file.
     * Enables the source map generating.
     * Used to create sourceMappingUrl.
     */
    private File sourceMapFile;

    /**
     * Disable sourceMappingUrl in css output
     */
    private boolean omitSourceMapUrl = false;

    /**
     * Embed sourceMappingUrl as data uri.
     */
    private boolean sourceMapEmbed = false;

    /**
     * Embed include contents in maps.
     */
    private boolean sourceMapContents = false;

    /**
     * Treat source_string as sass (as opposed to scss).
     */
    private boolean isIndentedSyntaxSrc = false;

    /**
     * List of paths.
     */
    private List<File> includePaths = new LinkedList<File>();

    /**
     * For the image-url Sass function.
     */
    private String imageUrl = null;

    /**
     * Precision for outputting fractional numbers.
     */
    private int precision;

    /**
     * Custom import functions.
     */
    private List<Function> importFunctions = new LinkedList<Function>();

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

    /**
     * Return the source map file.
     *
     * @return The source map file or <em>null</em> if no source map should generated.
     */
    public File getSourceMapFile() {
        return sourceMapFile;
    }

    /**
     * Set the source map file.
     *
     * @param sourceMapFile The source map file or <em>null</em> to disable source map generation.
     */
    public void setSourceMapFile(File sourceMapFile) {
        this.sourceMapFile = sourceMapFile;
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

    public boolean isSourceMapContents() {
        return sourceMapContents;
    }

    public void setSourceMapContents(boolean sourceMapContents) {
        this.sourceMapContents = sourceMapContents;
    }

    public boolean isIndentedSyntaxSrc() {
        return isIndentedSyntaxSrc;
    }

    public void setIsIndentedSyntaxSrc(boolean isIndentedSyntaxSrc) {
        this.isIndentedSyntaxSrc = isIndentedSyntaxSrc;
    }

    public List<File> getIncludePaths() {
        return includePaths;
    }

    public void setIncludePaths(List<File> includePaths) {
        this.includePaths = includePaths;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
}

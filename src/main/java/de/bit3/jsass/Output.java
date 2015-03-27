package de.bit3.jsass;

public class Output {
    /**
     * The css output.
     */
    private String css;

    /**
     * The source map output;
     */
    private String sourceMap;

    public Output(String css, String sourceMap) {
        this.css = css;
        this.sourceMap = sourceMap;
    }

    /**
     * Get the css output.
     *
     * @return The css output.
     */
    public String getCss() {
        return css;
    }

    /**
     * Get the source map output.
     *
     * @return The source map output.
     */
    public String getSourceMap() {
        return sourceMap;
    }
}

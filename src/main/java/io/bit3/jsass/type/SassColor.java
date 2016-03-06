package io.bit3.jsass.type;

/**
 * A sass color value.
 */
public class SassColor implements SassValue {
  public static final int TYPE = 5;

  /**
   * The red value, between 0.0 and 1.0.
   */
  private double red = 0;

  /**
   * The green value, between 0.0 and 1.0.
   */
  private double green = 0;

  /**
   * The blue value, between 0.0 and 1.0.
   */
  private double blue = 0;

  /**
   * The alpha value, between 0.0 and 1.0.
   */
  private double alpha = 0;

  /**
   * Create an empty (black and full transparent) color.
   */
  public SassColor() {
    // keep default values
  }

  /**
   * Create a RGB color.
   *
   * @param red   The red value, between 0.0 and 1.0.
   * @param green The green value, between 0.0 and 1.0.
   * @param blue  The blue value, between 0.0 and 1.0.
   */
  public SassColor(double red, double green, double blue) {
    this(red, green, blue, 1);
  }

  /**
   * Create a RGB color.
   *
   * @param red   The red value, between 0.0 and 1.0.
   * @param green The green value, between 0.0 and 1.0.
   * @param blue  The blue value, between 0.0 and 1.0.
   * @param alpha The alpha value, between 0.0 and 1.0.
   */
  public SassColor(double red, double green, double blue, double alpha) {
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.alpha = alpha;
  }

  /**
   * Return the red value.
   *
   * @return The red value, between 0.0 and 1.0.
   */
  public double getRed() {
    return red;
  }

  /**
   * Set the red value.
   *
   * @param red The red value, between 0.0 and 1.0.
   */
  public void setRed(double red) {
    this.red = red;
  }

  /**
   * Return the green value.
   *
   * @return The green value, between 0.0 and 1.0.
   */
  public double getGreen() {
    return green;
  }

  /**
   * Set the green value.
   *
   * @param green The green value, between 0.0 and 1.0.
   */
  public void setGreen(double green) {
    this.green = green;
  }

  /**
   * Return the blue value.
   *
   * @return The blue value, between 0.0 and 1.0.
   */
  public double getBlue() {
    return blue;
  }

  /**
   * Set the blue value.
   *
   * @param blue The blue value, between 0.0 and 1.0.
   */
  public void setBlue(double blue) {
    this.blue = blue;
  }

  /**
   * Return the alpha value.
   *
   * @return The alpha value, between 0.0 and 1.0.
   */
  public double getAlpha() {
    return alpha;
  }

  /**
   * Set the alpha value.
   *
   * @param alpha The alpha value, between 0.0 and 1.0.
   */
  public void setAlpha(double alpha) {
    this.alpha = alpha;
  }

  /**
   * Return a libsass compatible rgba(r,g,b,a) string representation.
   *
   * @return A libsass compatible rgba(r,g,b,a) string representation.
   */
  @Override
  public String toString() {
    return String.format("rgba(%f,%f,%f,%f)", red, green, blue, alpha);
  }
}

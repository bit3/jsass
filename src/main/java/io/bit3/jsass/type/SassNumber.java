package io.bit3.jsass.type;

/**
 * A sass number value.
 */
public class SassNumber extends Number implements SassValue {
  private static final long serialVersionUID = -6597488416036924788L;
  public static final int TYPE = 2;

  /**
   * The numeric value.
   */
  private double value;

  /**
   * The unit.
   */
  private String unit;

  /**
   * Create a new number.
   *
   * @param value The numeric value.
   * @param unit  The unit.
   */
  public SassNumber(double value, String unit) {
    this.value = value;
    this.unit = unit;
  }

  /**
   * Return the numeric value.
   *
   * @return The numeric value.
   */
  public double getValue() {
    return value;
  }

  /**
   * Set the numeric value.
   *
   * @param value The numeric value.
   */
  public void setValue(double value) {
    this.value = value;
  }

  /**
   * Return the unit.
   *
   * @return The unit.
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Set the unit.
   *
   * @param unit The unit.
   */
  public void setUnit(String unit) {
    this.unit = unit;
  }

  @Override
  public int intValue() {
    return (int) value;
  }

  @Override
  public long longValue() {
    return (long) value;
  }

  @Override
  public float floatValue() {
    return (float) value;
  }

  @Override
  public double doubleValue() {
    return value;
  }

  @Override
  public String toString() {
    return super.toString() + unit;
  }
}

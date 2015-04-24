package de.bit3.jsass.type;

import de.bit3.jsass.Separator;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A sass value list.
 */
public class SassList extends ArrayList<Object> {

  /**
   * The item separator.
   */
  private Separator separator = Separator.COMMA;

  /**
   * Create an empty list.
   */
  public SassList() {
  }

  /**
   * Create a list from an existing values.
   *
   * @param collection The existing values collection.
   */
  public SassList(Collection<?> collection) {
    super(collection);
  }

  /**
   * Create an empty list with a specific separator character.
   *
   * @param separator The item separator.
   */
  public SassList(Separator separator) {
    this.separator = separator;
  }

  /**
   * Create a list from an existing values, with a specific separator character.
   * @param separator The item separator.
   *
   * @param collection The existing values collection.
   */
  public SassList(Collection<?> collection, Separator separator) {
    super(collection);
    this.separator = separator;
  }

  /**
   * Return the item separator.
   *
   * @return The item separator.
   */
  public Separator getSeparator() {
    return separator;
  }

  /**
   * Set the item separator.
   *
   * @param separator The item separator.
   */
  public void setSeparator(Separator separator) {
    this.separator = separator;
  }

  /**
   * Return a libsass compatible (..) string representation.
   *
   * @return A libsass compatible (..) string representation.
   */
  @Override
  public String toString() {
    return "(" + StringUtils.join(this, separator.character) + ")";
  }
}

package io.bit3.jsass.type;

import io.bit3.jsass.Separator;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A sass value list.
 */
public class SassList extends ArrayList<Object> implements SassValue {
  private static final long serialVersionUID = -2377560327885879156L;
  public static final int TYPE = 6;

  /**
   * The item separator.
   */
  private Separator separator = Separator.COMMA;

  /**
   * Is this list bracketed or not.
   */
  private boolean bracketed = false;

  /**
   * Create an empty list.
   */
  public SassList() {
    // an empty list
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
   * Create a list from an existing values.
   *
   * @param collection The existing values collection.
   * @param bracketed The bracketed status.
   */
  public SassList(Collection<?> collection, boolean bracketed) {
    super(collection);
    this.bracketed = bracketed;
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
   * Create an empty list with a specific separator character.
   *
   * @param separator The item separator.
   * @param bracketed The bracketed status.
   */
  public SassList(Separator separator, boolean bracketed) {
    this.separator = separator;
    this.bracketed = bracketed;
  }

  /**
   * Create a list from an existing values, with a specific separator character.
   *
   * @param collection The existing values collection.
   * @param separator  The item separator.
   */
  public SassList(Collection<?> collection, Separator separator) {
    super(collection);
    this.separator = separator;
  }

  /**
   * Create a list from an existing values, with a specific separator character.
   *
   * @param collection The existing values collection.
   * @param separator  The item separator.
   * @param bracketed The bracketed status.
   */
  public SassList(Collection<?> collection, Separator separator, boolean bracketed) {
    super(collection);
    this.separator = separator;
    this.bracketed = bracketed;
  }

  /**
   * Create a list with initial capacity.
   *
   * @param initialCapacity The initial capacity, see {@link ArrayList#ArrayList(int)}.
   */
  public SassList(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * Create a list with initial capacity.
   *
   * @param initialCapacity The initial capacity, see {@link ArrayList#ArrayList(int)}.
   * @param separator  The item separator.
   */
  public SassList(int initialCapacity, Separator separator) {
    super(initialCapacity);
    this.separator = separator;
  }

  /**
   * Create a list with initial capacity.
   *
   * @param initialCapacity The initial capacity, see {@link ArrayList#ArrayList(int)}.
   * @param separator  The item separator.
   * @param bracketed The bracketed status.
   */
  public SassList(int initialCapacity, Separator separator, boolean bracketed) {
    super(initialCapacity);
    this.separator = separator;
    this.bracketed = bracketed;
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
   * Determine this list is bracketed.
   *
   * @return {@code true} if this list is bracketed.
   */
  public boolean isBracketed() {
    return bracketed;
  }

  /**
   * Set if this list is bracketed or not.
   *
   * @param bracketed The bracketed status.
   */
  public void setBracketed(boolean bracketed) {
    this.bracketed = bracketed;
  }

  /**
   * Return a libsass compatible (..) string representation.
   *
   * @return A libsass compatible (..) string representation.
   */
  @Override
  public String toString() {
    if (bracketed) {
      return "(" + StringUtils.join(this, separator.character) + ")";
    } else {
      return StringUtils.join(this, separator.character);
    }
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if (that == null || getClass() != that.getClass()) {
      return false;
    }

    if (!super.equals(that)) {
      return false;
    }

    SassList objects = (SassList) that;
    return separator == objects.separator && bracketed == objects.bracketed;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), separator, bracketed);
  }
}

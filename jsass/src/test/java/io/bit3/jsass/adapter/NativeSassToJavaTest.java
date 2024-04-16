package io.bit3.jsass.adapter;

import io.bit3.jsass.type.SassBoolean;
import io.bit3.jsass.type.SassColor;
import io.bit3.jsass.type.SassNumber;
import io.bit3.jsass.type.SassString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NativeSassToJavaTest {
  private NativeTestAdapter adapter;

  @BeforeEach
  public void setUp() {
    adapter = new NativeTestAdapter();
  }

  @Test
  public void testSassPositiveNumberToJava() {
    Object object = adapter.testSassPositiveNumberToJava();
    assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    assertEquals(678.0, sassNumber.doubleValue());
    assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassNegativeNumberToJava() {
    Object object = adapter.testSassNegativeNumberToJava();
    assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    assertEquals(-429.0, sassNumber.doubleValue());
    assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassZeroNumberToJava() {
    Object object = adapter.testSassZeroNumberToJava();
    assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    assertEquals(.0, sassNumber.doubleValue());
    assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassPositiveNumberWithUnitToJava() {
    Object object = adapter.testSassPositiveNumberWithUnitToJava();
    assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    assertEquals(397.0, sassNumber.doubleValue());
    assertEquals("px", sassNumber.getUnit());
  }

  @Test
  public void testSassNegativeNumberWithUnitToJava() {
    Object object = adapter.testSassNegativeNumberWithUnitToJava();
    assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    assertEquals(-241.0, sassNumber.doubleValue());
    assertEquals("em", sassNumber.getUnit());
  }

  @Test
  public void testSassZeroNumberWithUnitToJava() {
    Object object = adapter.testSassZeroNumberWithUnitToJava();
    assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    assertEquals(.0, sassNumber.doubleValue());
    assertEquals("%", sassNumber.getUnit());
  }

  @Test
  public void testSassStringToJava() {
    Object object = adapter.testSassStringToJava();
    assertTrue(object instanceof SassString);

    SassString sassString = (SassString) object;
    assertEquals("Nullam vel sem", sassString.getValue());
    assertFalse(sassString.isQuoted());
  }

  @Test
  public void testSassQuotedStringToJava() {
    Object object = adapter.testSassQuotedStringToJava();
    assertTrue(object instanceof SassString);

    SassString sassString = (SassString) object;
    assertEquals("In ac felis", sassString.getValue());
    assertTrue(sassString.isQuoted());
  }

  @Test
  public void testSassTrueToJava() {
    Object object = adapter.testSassTrueToJava();
    assertTrue(object instanceof SassBoolean);

    SassBoolean sassBoolean = (SassBoolean) object;
    assertTrue(sassBoolean.getValue());
  }

  @Test
  public void testSassFalseToJava() {
    Object object = adapter.testSassFalseToJava();
    assertTrue(object instanceof SassBoolean);

    SassBoolean sassBoolean = (SassBoolean) object;
    assertFalse(sassBoolean.getValue());
  }

  @Test
  public void testSassRgbColorToJava() {
    Object object = adapter.testSassRgbColorToJava();
    assertTrue(object instanceof SassColor);

    SassColor sassColor = (SassColor) object;
    assertEquals(.74, sassColor.getRed());
    assertEquals(.32, sassColor.getGreen());
    assertEquals(.56, sassColor.getBlue());
    assertEquals(1.0, sassColor.getAlpha());
  }

  @Test
  public void testSassRgbaColorToJava() {
    Object object = adapter.testSassRgbaColorToJava();
    assertTrue(object instanceof SassColor);

    SassColor sassColor = (SassColor) object;
    assertEquals(.97, sassColor.getRed());
    assertEquals(.24, sassColor.getGreen());
    assertEquals(.48, sassColor.getBlue());
    assertEquals(.5, sassColor.getAlpha());
  }
}

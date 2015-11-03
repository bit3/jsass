package io.bit3.jsass.adapter;

import io.bit3.jsass.type.SassBoolean;
import io.bit3.jsass.type.SassColor;
import io.bit3.jsass.type.SassNumber;

import io.bit3.jsass.type.SassString;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NativeSassToJavaTest {
  private NativeTestAdapter adapter;

  @Before
  public void setUp() {
    adapter = new NativeTestAdapter();
  }

  @Test
  public void testSassPositiveNumberToJava() {
    Object object = adapter.testSassPositiveNumberToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(678.0, sassNumber.doubleValue(), .0);
    Assert.assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassNegativeNumberToJava() {
    Object object = adapter.testSassNegativeNumberToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(-429.0, sassNumber.doubleValue(), .0);
    Assert.assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassZeroNumberToJava() {
    Object object = adapter.testSassZeroNumberToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(.0, sassNumber.doubleValue(), .0);
    Assert.assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassPositiveNumberWithUnitToJava() {
    Object object = adapter.testSassPositiveNumberWithUnitToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(397.0, sassNumber.doubleValue(), .0);
    Assert.assertEquals("px", sassNumber.getUnit());
  }

  @Test
  public void testSassNegativeNumberWithUnitToJava() {
    Object object = adapter.testSassNegativeNumberWithUnitToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(-241.0, sassNumber.doubleValue(), .0);
    Assert.assertEquals("em", sassNumber.getUnit());
  }

  @Test
  public void testSassZeroNumberWithUnitToJava() {
    Object object = adapter.testSassZeroNumberWithUnitToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(.0, sassNumber.doubleValue(), .0);
    Assert.assertEquals("%", sassNumber.getUnit());
  }

  @Test
  public void testSassStringToJava() {
    Object object = adapter.testSassStringToJava();
    Assert.assertTrue(object instanceof SassString);

    SassString sassString = (SassString) object;
    Assert.assertEquals("Nullam vel sem", sassString.getValue());
    Assert.assertFalse(sassString.isQuoted());
  }

  @Test
  public void testSassQuotedStringToJava() {
    Object object = adapter.testSassQuotedStringToJava();
    Assert.assertTrue(object instanceof SassString);

    SassString sassString = (SassString) object;
    Assert.assertEquals("In ac felis", sassString.getValue());
    Assert.assertTrue(sassString.isQuoted());
  }

  @Test
  public void testSassTrueToJava() {
    Object object = adapter.testSassTrueToJava();
    Assert.assertTrue(object instanceof SassBoolean);

    SassBoolean sassBoolean = (SassBoolean) object;
    Assert.assertTrue(sassBoolean.getValue());
  }

  @Test
  public void testSassFalseToJava() {
    Object object = adapter.testSassFalseToJava();
    Assert.assertTrue(object instanceof SassBoolean);

    SassBoolean sassBoolean = (SassBoolean) object;
    Assert.assertFalse(sassBoolean.getValue());
  }

  @Test
  public void testSassRgbColorToJava() {
    Object object = adapter.testSassRgbColorToJava();
    Assert.assertTrue(object instanceof SassColor);

    SassColor sassColor = (SassColor) object;
    Assert.assertEquals(.74, sassColor.getRed(), .0);
    Assert.assertEquals(.32, sassColor.getGreen(), .0);
    Assert.assertEquals(.56, sassColor.getBlue(), .0);
    Assert.assertEquals(1.0, sassColor.getAlpha(), .0);
  }

  @Test
  public void testSassRgbaColorToJava() {
    Object object = adapter.testSassRgbaColorToJava();
    Assert.assertTrue(object instanceof SassColor);

    SassColor sassColor = (SassColor) object;
    Assert.assertEquals(.97, sassColor.getRed(), .0);
    Assert.assertEquals(.24, sassColor.getGreen(), .0);
    Assert.assertEquals(.48, sassColor.getBlue(), .0);
    Assert.assertEquals(.5, sassColor.getAlpha(), .0);
  }
}

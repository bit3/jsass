package io.bit3.jsass.adapter;

import io.bit3.jsass.type.SassNumber;

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
    Assert.assertEquals(678, sassNumber.doubleValue(), 0);
    Assert.assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassNegativeNumberToJava() {
    Object object = adapter.testSassNegativeNumberToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(-429, sassNumber.doubleValue(), 0);
    Assert.assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassZeroNumberToJava() {
    Object object = adapter.testSassZeroNumberToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(0, sassNumber.doubleValue(), 0);
    Assert.assertEquals("", sassNumber.getUnit());
  }

  @Test
  public void testSassPositiveNumberWithUnitToJava() {
    Object object = adapter.testSassPositiveNumberWithUnitToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(397, sassNumber.doubleValue(), 0);
    Assert.assertEquals("px", sassNumber.getUnit());
  }

  @Test
  public void testSassNegativeNumberWithUnitToJava() {
    Object object = adapter.testSassNegativeNumberWithUnitToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(-241, sassNumber.doubleValue(), 0);
    Assert.assertEquals("em", sassNumber.getUnit());
  }

  @Test
  public void testSassZeroNumberWithUnitToJava() {
    Object object = adapter.testSassZeroNumberWithUnitToJava();
    Assert.assertTrue(object instanceof SassNumber);

    SassNumber sassNumber = (SassNumber) object;
    Assert.assertEquals(0, sassNumber.doubleValue(), 0);
    Assert.assertEquals("%", sassNumber.getUnit());
  }
}

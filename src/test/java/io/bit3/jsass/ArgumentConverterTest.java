package io.bit3.jsass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.bit3.jsass.context.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Test for argument converters.
 */
@RunWith(Parameterized.class)
public class ArgumentConverterTest {
  /**
   * The source to compile.
   */
  private static final String SOURCE = "foo { bar: func(%s); }";

  /**
   * The sass value string representation.
   */
  private String sassValue;

  /**
   * The expected java object.
   */
  private Object expectedValue;

  /**
   * The function provider mock.
   */
  private AbstractedCalledMock mock;

  /**
   * The jsass compiler.
   */
  private Compiler compiler;

  /**
   * The jsass compiler options.
   */
  private Options options;

  /**
   * Create new test.
   */
  public ArgumentConverterTest(
      String sassValue,
      Object expectedValue,
      AbstractedCalledMock mock
  ) {
    this.sassValue = sassValue;
    this.expectedValue = expectedValue;
    this.mock = mock;
  }

  /**
   * Return data for running the test.
   *
   * <p>This data contain a set of syntax (scss or sass) and output style in all combinations.</p>
   */
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][]{
            {"true", true, new BooleanArgumentMock()},
            {"16", (byte) 16, new ByteArgumentMock()},
            {"'F'", 'F', new CharacterArgumentMock()},
            {"", Context.class, new ContextArgumentMock()},
            {"16", 16d, new DoubleArgumentMock()},
            {"16", 16f, new FloatArgumentMock()},
            {"16", 16, new IntegerArgumentMock()},
            {"16", 16L, new LongArgumentMock()},
            {"16", (short) 16, new ShortArgumentMock()},
            {"'foo'", "foo", new StringArgumentMock()},
        }
    );
  }

  /**
   * Set up the compiler and the compiler options for each run.
   *
   * @throws URISyntaxException Throws if the resource URI is invalid.
   */
  @Before
  public void setUp() throws IOException, URISyntaxException {
    compiler = new Compiler();
    options = new Options();
  }

  @Test
  public void testCall() throws Exception {
    options.getFunctionProviders().add(mock);

    String source = String.format(SOURCE, sassValue);

    Output output = (Output) compiler.compileString(
        source, new URI("input.scss"), new URI("output.css"), options
    );

    assertTrue(mock.called);
    assertNotNull(mock.actualValue);

    if (expectedValue instanceof Class) {
      assertTrue(((Class<?>) expectedValue).isAssignableFrom(mock.actualValue.getClass()));
    } else {
      assertEquals(expectedValue, mock.actualValue);
    }
  }

  /**
   * Abstract mock function provider base.
   */
  private static class AbstractedCalledMock {
    protected boolean called;
    protected Object actualValue;
  }

  /**
   * Boolean parameter mock function provider.
   */
  public static class BooleanArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Boolean arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * Byte parameter mock function provider.
   */
  public static class ByteArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Byte arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * Character parameter mock function provider.
   */
  public static class CharacterArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Character arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * Context parameter mock function provider.
   */
  public static class ContextArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Context arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * Double parameter mock function provider.
   */
  public static class DoubleArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Double arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * Float parameter mock function provider.
   */
  public static class FloatArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Float arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * Integer parameter mock function provider.
   */
  public static class IntegerArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Integer arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * Long parameter mock function provider.
   */
  public static class LongArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Long arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * Short parameter mock function provider.
   */
  public static class ShortArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(Short arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  /**
   * String parameter mock function provider.
   */
  public static class StringArgumentMock extends AbstractedCalledMock {
    /**
     * Mock function.
     */
    public String func(String arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }
}

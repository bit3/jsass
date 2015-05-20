package io.bit3.jsass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import io.bit3.jsass.context.Context;
import io.bit3.jsass.importer.Import;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ArgumentConverterTest {

  private static final String SOURCE = "foo { bar: func(%s); }";

  private String               sassValue;
  private Object               expectedValue;
  private AbstractedCalledMock mock;
  private Compiler             compiler;
  private Options              options;

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
            {"", Import.class, new ImportArgumentMock()},
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

    compiler.compileString(source, new URI("input.scss"), new URI("output.css"), options);

    assertTrue(mock.called);
    assertNotNull(mock.actualValue);

    if (expectedValue instanceof Class) {
      assertTrue(((Class) expectedValue).isAssignableFrom(mock.actualValue.getClass()));
    } else {
      assertEquals(expectedValue, mock.actualValue);
    }
  }

  private static class AbstractedCalledMock {
    protected boolean called;
    protected Object actualValue;
  }

  public static class BooleanArgumentMock extends AbstractedCalledMock {
    public String func(Boolean arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class ByteArgumentMock extends AbstractedCalledMock {
    public String func(Byte arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class CharacterArgumentMock extends AbstractedCalledMock {
    public String func(Character arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class ContextArgumentMock extends AbstractedCalledMock {
    public String func(Context arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class DoubleArgumentMock extends AbstractedCalledMock {
    public String func(Double arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class FloatArgumentMock extends AbstractedCalledMock {
    public String func(Float arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class IntegerArgumentMock extends AbstractedCalledMock {
    public String func(Integer arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class ImportArgumentMock extends AbstractedCalledMock {
    public String func(Import arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class LongArgumentMock extends AbstractedCalledMock {
    public String func(Long arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class ShortArgumentMock extends AbstractedCalledMock {
    public String func(Short arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }

  public static class StringArgumentMock extends AbstractedCalledMock {
    public String func(String arg) {
      called = true;
      actualValue = arg;
      return "called";
    }
  }
}

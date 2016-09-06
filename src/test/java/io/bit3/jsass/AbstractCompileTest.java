package io.bit3.jsass;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

class AbstractCompileTest {
  String syntax;
  OutputStyle outputStyle;
  TestFunctions testFunctions;

  AbstractCompileTest(String syntax, OutputStyle outputStyle) {
    this.syntax = syntax;
    this.outputStyle = outputStyle;
    this.testFunctions = new TestFunctions();
  }

  void assertEquals(String actual, URL expectedSource) throws IOException {
    String expected = IOUtils.toString(expectedSource);

    Assert.assertEquals(
        String.format("Compile input.%s into %s output format failed", syntax, outputStyle),
        expected,
        actual
    );
  }

  void assertSpecialFunctions() {
    Assert.assertEquals(
        Collections.singletonList("I'm a warn message"),
        testFunctions.warnMessages
    );
    Assert.assertEquals(
        Collections.singletonList("I'm an error message"),
        testFunctions.errorMessages
    );
    Assert.assertEquals(
        Collections.singletonList("I'm a debug message"),
        testFunctions.debugMessages
    );
  }
}

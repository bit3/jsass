package io.bit3.jsass;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

abstract class AbstractCompileTest {
  String syntax;
  OutputStyle outputStyle;
  TestFunctions testFunctions;

  AbstractCompileTest() {
    this.testFunctions = new TestFunctions();
  }

  void assertEquals(String actual, URL expectedSource) throws IOException {
    String expected = IOUtils.toString(expectedSource, StandardCharsets.UTF_8);

    Assertions.assertEquals(
        expected,
        Utils.dos2unix(actual),
        () -> String.format("Compile input.%s into %s output format failed", syntax, outputStyle)
    );
  }

  void assertSpecialFunctions() {
    Assertions.assertEquals(
        Collections.singletonList("I'm a warn message"),
        testFunctions.warnMessages
    );
    Assertions.assertEquals(
        Collections.singletonList("I'm an error message"),
        testFunctions.errorMessages
    );
    Assertions.assertEquals(
        Collections.singletonList("I'm a debug message"),
        testFunctions.debugMessages
    );
  }
}

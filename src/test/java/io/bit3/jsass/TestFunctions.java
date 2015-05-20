package io.bit3.jsass;

import io.bit3.jsass.annotation.DefaultIntegerValue;
import io.bit3.jsass.annotation.DefaultStringValue;
import io.bit3.jsass.annotation.Name;
import io.bit3.jsass.importer.Import;

public class TestFunctions {

  public String hello(@Name("name") @DefaultStringValue("world") String name) {
    return "Hello " + name;
  }

  public int increment(@Name("value") int value,
                       @Name("increment") @DefaultIntegerValue(1) int increment) {
    return value + increment;
  }

  public int decrement(@Name("value") int value,
                       @Name("decrement") @DefaultIntegerValue(1) int decrement) {
    return value - decrement;
  }
}

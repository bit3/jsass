package io.bit3.jsass;

import io.bit3.jsass.annotation.DebugFunction;
import io.bit3.jsass.annotation.DefaultIntegerValue;
import io.bit3.jsass.annotation.DefaultStringValue;
import io.bit3.jsass.annotation.ErrorFunction;
import io.bit3.jsass.annotation.Name;
import io.bit3.jsass.annotation.WarnFunction;
import io.bit3.jsass.type.SassList;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TestFunctions {
  List<String> warnMessages = new LinkedList<>();
  List<String> errorMessages = new LinkedList<>();
  List<String> debugMessages = new LinkedList<>();

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

  @WarnFunction
  public void warn(String message) {
    warnMessages.add(message);
  }

  @ErrorFunction
  public void error(String message) {
    errorMessages.add(message);
  }

  @DebugFunction
  public void debug(String message) {
    debugMessages.add(message);
  }

  static class FallbackCall {
    final String name;
    final SassList arguments;

    FallbackCall(String name, SassList arguments) {
      this.name = name;
      this.arguments = arguments;
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
        return true;
      }

      if (object == null || getClass() != object.getClass()) {
        return false;
      }

      FallbackCall that = (FallbackCall) object;
      return Objects.equals(name, that.name)
          && Objects.equals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, arguments);
    }
  }
}

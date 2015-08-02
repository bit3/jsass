package io.bit3.jsass.function;

public class FunctionArgumentSignature {

  private String name;
  private Object defaultValue;

  public FunctionArgumentSignature(String name, Object defaultValue) {
    this.name = name;
    this.defaultValue = defaultValue;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }
}

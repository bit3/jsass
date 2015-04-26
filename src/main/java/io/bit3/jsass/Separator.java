package io.bit3.jsass;

public enum Separator {
  COMMA(','),
  SPACE(' ');

  public final char character;

  Separator(char character) {
    this.character = character;
  }
}

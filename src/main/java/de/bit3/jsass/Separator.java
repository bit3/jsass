package de.bit3.jsass;

public enum Separator {
    COMMA(','),
    SPACE(' ');

    final char character;

    Separator(char character) {
        this.character = character;
    }
}

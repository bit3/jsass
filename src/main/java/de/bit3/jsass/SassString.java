package de.bit3.jsass;

import java.util.stream.IntStream;

public class SassString implements CharSequence {
    private String value;
    private boolean quoted = true;
    private char quote = '\'';

    public SassString(String value) {
        this.value = value;
    }

    public SassString(String value, boolean quoted) {
        this.value = value;
        this.quoted = quoted;
    }

    public SassString(String value, boolean quoted, char quote) {
        this.value = value;
        this.quoted = quoted;
        this.quote = quote;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isQuoted() {
        return quoted;
    }

    public void setQuoted(boolean quoted) {
        this.quoted = quoted;
    }

    public char getQuote() {
        return quote;
    }

    public void setQuote(char quote) {
        this.quote = quote;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public IntStream chars() {
        return value.chars();
    }

    @Override
    public IntStream codePoints() {
        return value.codePoints();
    }

    @Override
    public String toString() {
        return value;
    }
}

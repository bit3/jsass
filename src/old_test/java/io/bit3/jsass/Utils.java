package io.bit3.jsass;

public class Utils {
    private Utils() {
        throw new UnsupportedOperationException();
    }

    public static String dos2unix(String string) {
        return null == string ? null : string.replace("\r", "");
    }
}

package io.bit3.jsass.adapter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NativeAdapterTest {

    /**
     * Ensure the correct libsass version is used.
     */
    @Test
    void libsassVersion() {
        assertEquals(
                "3.6.6",
                NativeAdapter.libsassVersion()
        );
    }

}

package com.github.pemistahl.lingua.api;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class LanguageDetectorBuilderJavaTest {

    @Test
    public void testSlice() {
        String code = "00001001771500042";
        Assertions.assertThat(code.substring(4, 10)).isEqualTo("100177");
    }
}

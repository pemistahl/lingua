/*
 * Copyright © 2018-2020 Peter M. Stahl pemistahl@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pemistahl.lingua.api;

import org.junit.jupiter.api.Test;

import static com.github.pemistahl.lingua.api.Language.ENGLISH;
import static com.github.pemistahl.lingua.api.Language.FRENCH;
import static com.github.pemistahl.lingua.api.Language.CHINESE;
import static org.assertj.core.api.Assertions.assertThat;

public class LanguageDetectorBuilderJavaTest {

    @Test
    public void assertThatLinguaCanBeUsedWithJava() {
        final LanguageDetector detector = LanguageDetectorBuilder
            .fromLanguages(CHINESE, ENGLISH, FRENCH).build();
        String text = "上海大学是一个好大学  this is a test.";
        assertThat(detector.detectLanguageOf(text)).isEqualTo(CHINESE);
    }
}

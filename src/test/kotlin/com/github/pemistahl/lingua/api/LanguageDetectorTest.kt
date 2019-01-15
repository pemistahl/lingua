/*
 * Copyright 2018-2019 Peter M. Stahl pemistahl@googlemail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pemistahl.lingua.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class LanguageDetectorTest {

    private val detector = LanguageDetector(
        languages = Language.values().toSet().minus(Language.UNKNOWN).toMutableSet(),
        isCachedByMapDB = false
    )

    @AfterEach
    fun afterEach() {
        detector.languages.forEach { it.isExcludedFromDetection = false }
    }

    @Test
    fun `assert that invalid strings return unknown language`() {
        listOf(
            "",
            " \n  \t;",
            "3jfh856%)§"
        ).forEach { str ->
            assertThat(detector.detectLanguageWithRules(str)).isEqualTo(Language.UNKNOWN)
        }
    }

    @Test
    fun `assert that strings with letter 'ß' return German language`() {
        listOf(
            "ß",
            "Fuß",
            "groß und stark"
        ).forEach { str ->
            assertThat(detector.detectLanguageWithRules(str)).isEqualTo(Language.GERMAN)
        }
    }

    @Test
    fun `assert that languages not supporting Latin characters are excluded for input with Latin characters`() {
        assertThat(detector.languages.all { it.isExcludedFromDetection }).isFalse()
        val language = detector.detectLanguageWithRules("language detection is hard")
        assertThat(detector.languages.filterNot { it.hasLatinAlphabet }.all { it.isExcludedFromDetection }).isTrue()
        assertThat(detector.languages.filter { it.hasLatinAlphabet }.all { it.isExcludedFromDetection }).isFalse()
        assertThat(language).isEqualTo(Language.UNKNOWN)
    }

    @Test
    fun `assert that languages not supporting Cyrillic characters are excluded for input with Cyrillic characters`() {
        assertThat(detector.languages.all { it.isExcludedFromDetection }).isFalse()
        val language = detector.detectLanguageWithRules("трудно определить язык")
        assertThat(detector.languages.filterNot { it.hasCyrillicAlphabet }.all { it.isExcludedFromDetection }).isTrue()
        assertThat(detector.languages.filter { it.hasCyrillicAlphabet }.all { it.isExcludedFromDetection }).isFalse()
        assertThat(language).isEqualTo(Language.UNKNOWN)
    }

    @Test
    fun `assert that languages not supporting Arabic characters are excluded for input with Arabic characters`() {
        assertThat(detector.languages.all { it.isExcludedFromDetection }).isFalse()
        val language = detector.detectLanguageWithRules("اكتشاف اللغة صعب")
        assertThat(detector.languages.filterNot { it.hasArabicAlphabet }.all { it.isExcludedFromDetection }).isTrue()
        assertThat(detector.languages.filter { it.hasArabicAlphabet }.all { it.isExcludedFromDetection }).isFalse()
        assertThat(language).isEqualTo(Language.UNKNOWN)
    }
}

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

import com.github.pemistahl.lingua.api.Language.AFRIKAANS
import com.github.pemistahl.lingua.api.Language.ALBANIAN
import com.github.pemistahl.lingua.api.Language.ARABIC
import com.github.pemistahl.lingua.api.Language.BASQUE
import com.github.pemistahl.lingua.api.Language.BELARUSIAN
import com.github.pemistahl.lingua.api.Language.BOKMAL
import com.github.pemistahl.lingua.api.Language.BULGARIAN
import com.github.pemistahl.lingua.api.Language.CATALAN
import com.github.pemistahl.lingua.api.Language.CROATIAN
import com.github.pemistahl.lingua.api.Language.CZECH
import com.github.pemistahl.lingua.api.Language.DANISH
import com.github.pemistahl.lingua.api.Language.DUTCH
import com.github.pemistahl.lingua.api.Language.ENGLISH
import com.github.pemistahl.lingua.api.Language.ESTONIAN
import com.github.pemistahl.lingua.api.Language.FINNISH
import com.github.pemistahl.lingua.api.Language.FRENCH
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.GREEK
import com.github.pemistahl.lingua.api.Language.HUNGARIAN
import com.github.pemistahl.lingua.api.Language.ICELANDIC
import com.github.pemistahl.lingua.api.Language.INDONESIAN
import com.github.pemistahl.lingua.api.Language.IRISH
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.LATIN
import com.github.pemistahl.lingua.api.Language.LATVIAN
import com.github.pemistahl.lingua.api.Language.LITHUANIAN
import com.github.pemistahl.lingua.api.Language.MALAY
import com.github.pemistahl.lingua.api.Language.NORWEGIAN
import com.github.pemistahl.lingua.api.Language.NYNORSK
import com.github.pemistahl.lingua.api.Language.PERSIAN
import com.github.pemistahl.lingua.api.Language.POLISH
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.ROMANIAN
import com.github.pemistahl.lingua.api.Language.RUSSIAN
import com.github.pemistahl.lingua.api.Language.SOMALI
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.TAGALOG
import com.github.pemistahl.lingua.api.Language.TURKISH
import com.github.pemistahl.lingua.api.Language.VIETNAMESE
import com.github.pemistahl.lingua.api.Language.WELSH
import com.github.pemistahl.lingua.api.Language.SLOVAK
import com.github.pemistahl.lingua.api.Language.SLOVENE
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class LanguageTest {

    @Test
    fun `assert that all supported languages are available`() {
        assertThat(Language.all()).containsExactly(
            AFRIKAANS, ALBANIAN, ARABIC, BASQUE, BELARUSIAN, BULGARIAN,
            CATALAN, CROATIAN, CZECH, DANISH, DUTCH, ENGLISH, ESTONIAN,
            FINNISH, FRENCH, GERMAN, GREEK, HUNGARIAN, ICELANDIC, INDONESIAN,
            IRISH, ITALIAN, LATIN, LATVIAN, LITHUANIAN, MALAY, NORWEGIAN,
            PERSIAN, POLISH, PORTUGUESE, ROMANIAN, RUSSIAN, SLOVAK, SLOVENE,
            SOMALI, SPANISH, SWEDISH, TAGALOG, TURKISH, VIETNAMESE, WELSH
        )
    }

    @Test
    fun `assert that all supported spoken languages are available`() {
        assertThat(Language.allSpokenOnes()).containsExactly(
            AFRIKAANS, ALBANIAN, ARABIC, BASQUE, BELARUSIAN, BULGARIAN,
            CATALAN, CROATIAN, CZECH, DANISH, DUTCH, ENGLISH, ESTONIAN,
            FINNISH, FRENCH, GERMAN, GREEK, HUNGARIAN, ICELANDIC, INDONESIAN,
            IRISH, ITALIAN, LATVIAN, LITHUANIAN, MALAY, NORWEGIAN,
            PERSIAN, POLISH, PORTUGUESE, ROMANIAN, RUSSIAN, SLOVAK, SLOVENE,
            SOMALI, SPANISH, SWEDISH, TAGALOG, TURKISH, VIETNAMESE, WELSH
        )
    }

    @Test
    fun `assert that unknown language is excluded from detection`() {
        assertThat(Language.UNKNOWN.isExcludedFromDetection).isTrue()
    }

    @Test
    fun `assert that certain languages use Latin alphabet`() {
        assertThat(Language.values().filter { it.usesLatinAlphabet })
            .containsExactly(
                AFRIKAANS, ALBANIAN, BASQUE, BOKMAL, CATALAN, CROATIAN, CZECH,
                DANISH, DUTCH, ENGLISH, ESTONIAN, FINNISH, FRENCH, GERMAN,
                HUNGARIAN, ICELANDIC, INDONESIAN, IRISH, ITALIAN, LATIN, LATVIAN,
                LITHUANIAN, MALAY, NORWEGIAN, NYNORSK, POLISH, PORTUGUESE,
                ROMANIAN, SLOVAK, SLOVENE, SOMALI, SPANISH, SWEDISH, TAGALOG,
                TURKISH, VIETNAMESE, WELSH
            )
    }

    @Test
    fun `assert that certain languages support Greek alphabet`() {
        assertThat(Language.values().filter { it.usesGreekAlphabet })
            .containsExactly(GREEK)
    }

    @Test
    fun `assert that certain languages support Cyrillic alphabet`() {
        assertThat(Language.values().filter { it.usesCyrillicAlphabet })
            .containsExactly(BELARUSIAN, BULGARIAN, RUSSIAN)
    }

    @Test
    fun `assert that certain languages support Arabic alphabet`() {
        assertThat(Language.values().filter { it.usesArabicAlphabet })
            .containsExactly(ARABIC, PERSIAN)
    }

    @ParameterizedTest
    @CsvSource(
        "af, AFRIKAANS",
        "sq, ALBANIAN",
        "ar, ARABIC",
        "eu, BASQUE",
        "be, BELARUSIAN",
        "nb, BOKMAL",
        "bg, BULGARIAN",
        "ca, CATALAN",
        "hr, CROATIAN",
        "cs, CZECH",
        "da, DANISH",
        "nl, DUTCH",
        "en, ENGLISH",
        "et, ESTONIAN",
        "fi, FINNISH",
        "fr, FRENCH",
        "de, GERMAN",
        "el, GREEK",
        "hu, HUNGARIAN",
        "is, ICELANDIC",
        "id, INDONESIAN",
        "ga, IRISH",
        "it, ITALIAN",
        "la, LATIN",
        "lv, LATVIAN",
        "lt, LITHUANIAN",
        "ms, MALAY",
        "no, NORWEGIAN",
        "nn, NYNORSK",
        "fa, PERSIAN",
        "pl, POLISH",
        "pt, PORTUGUESE",
        "ro, ROMANIAN",
        "ru, RUSSIAN",
        "sk, SLOVAK",
        "sl, SLOVENE",
        "so, SOMALI",
        "es, SPANISH",
        "sv, SWEDISH",
        "tl, TAGALOG",
        "tr, TURKISH",
        "vi, VIETNAMESE",
        "cy, WELSH"
    )
    fun `assert that correct language is returned for iso code`(isoCode: String, language: Language) {
        assertThat(Language.getByIsoCode(isoCode)).isEqualTo(language)
    }

    @Test
    fun `assert that exception is thrown for unknown iso code`() {
        assertThatIllegalArgumentException().isThrownBy {
            Language.getByIsoCode("dfjkglsdfg")
        }.withMessage("language with iso code 'dfjkglsdfg' can not be found")
    }
}

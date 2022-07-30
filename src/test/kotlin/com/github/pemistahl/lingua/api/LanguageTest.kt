/*
 * Copyright © 2018-today Peter M. Stahl pemistahl@gmail.com
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

package com.github.pemistahl.lingua.api

import com.github.pemistahl.lingua.api.Language.AFRIKAANS
import com.github.pemistahl.lingua.api.Language.ALBANIAN
import com.github.pemistahl.lingua.api.Language.ARABIC
import com.github.pemistahl.lingua.api.Language.ARMENIAN
import com.github.pemistahl.lingua.api.Language.AZERBAIJANI
import com.github.pemistahl.lingua.api.Language.BASQUE
import com.github.pemistahl.lingua.api.Language.BELARUSIAN
import com.github.pemistahl.lingua.api.Language.BENGALI
import com.github.pemistahl.lingua.api.Language.BOKMAL
import com.github.pemistahl.lingua.api.Language.BOSNIAN
import com.github.pemistahl.lingua.api.Language.BULGARIAN
import com.github.pemistahl.lingua.api.Language.CATALAN
import com.github.pemistahl.lingua.api.Language.CHINESE
import com.github.pemistahl.lingua.api.Language.CROATIAN
import com.github.pemistahl.lingua.api.Language.CZECH
import com.github.pemistahl.lingua.api.Language.DANISH
import com.github.pemistahl.lingua.api.Language.DUTCH
import com.github.pemistahl.lingua.api.Language.ENGLISH
import com.github.pemistahl.lingua.api.Language.ESPERANTO
import com.github.pemistahl.lingua.api.Language.ESTONIAN
import com.github.pemistahl.lingua.api.Language.FINNISH
import com.github.pemistahl.lingua.api.Language.FRENCH
import com.github.pemistahl.lingua.api.Language.GANDA
import com.github.pemistahl.lingua.api.Language.GEORGIAN
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.GREEK
import com.github.pemistahl.lingua.api.Language.GUJARATI
import com.github.pemistahl.lingua.api.Language.HEBREW
import com.github.pemistahl.lingua.api.Language.HINDI
import com.github.pemistahl.lingua.api.Language.HUNGARIAN
import com.github.pemistahl.lingua.api.Language.ICELANDIC
import com.github.pemistahl.lingua.api.Language.INDONESIAN
import com.github.pemistahl.lingua.api.Language.IRISH
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.JAPANESE
import com.github.pemistahl.lingua.api.Language.KAZAKH
import com.github.pemistahl.lingua.api.Language.KOREAN
import com.github.pemistahl.lingua.api.Language.LATIN
import com.github.pemistahl.lingua.api.Language.LATVIAN
import com.github.pemistahl.lingua.api.Language.LITHUANIAN
import com.github.pemistahl.lingua.api.Language.MACEDONIAN
import com.github.pemistahl.lingua.api.Language.MALAY
import com.github.pemistahl.lingua.api.Language.MAORI
import com.github.pemistahl.lingua.api.Language.MARATHI
import com.github.pemistahl.lingua.api.Language.MONGOLIAN
import com.github.pemistahl.lingua.api.Language.NYNORSK
import com.github.pemistahl.lingua.api.Language.OROMO
import com.github.pemistahl.lingua.api.Language.PERSIAN
import com.github.pemistahl.lingua.api.Language.POLISH
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.PUNJABI
import com.github.pemistahl.lingua.api.Language.ROMANIAN
import com.github.pemistahl.lingua.api.Language.RUSSIAN
import com.github.pemistahl.lingua.api.Language.SERBIAN
import com.github.pemistahl.lingua.api.Language.SHONA
import com.github.pemistahl.lingua.api.Language.SLOVAK
import com.github.pemistahl.lingua.api.Language.SLOVENE
import com.github.pemistahl.lingua.api.Language.SOMALI
import com.github.pemistahl.lingua.api.Language.SOTHO
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWAHILI
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.TAGALOG
import com.github.pemistahl.lingua.api.Language.TAMIL
import com.github.pemistahl.lingua.api.Language.TELUGU
import com.github.pemistahl.lingua.api.Language.THAI
import com.github.pemistahl.lingua.api.Language.TSONGA
import com.github.pemistahl.lingua.api.Language.TSWANA
import com.github.pemistahl.lingua.api.Language.TURKISH
import com.github.pemistahl.lingua.api.Language.UKRAINIAN
import com.github.pemistahl.lingua.api.Language.UNKNOWN
import com.github.pemistahl.lingua.api.Language.URDU
import com.github.pemistahl.lingua.api.Language.VIETNAMESE
import com.github.pemistahl.lingua.api.Language.WELSH
import com.github.pemistahl.lingua.api.Language.XHOSA
import com.github.pemistahl.lingua.api.Language.YORUBA
import com.github.pemistahl.lingua.api.Language.ZULU
import com.github.pemistahl.lingua.internal.Alphabet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource

class LanguageTest {

    @Test
    fun `assert that all supported languages are available`() {
        assertThat(Language.all()).containsExactly(
            AFRIKAANS, ALBANIAN, ARABIC, ARMENIAN, AZERBAIJANI, BASQUE, BELARUSIAN, BENGALI, BOKMAL, BOSNIAN, BULGARIAN,
            CATALAN, CHINESE, CROATIAN, CZECH, DANISH, DUTCH, ENGLISH, ESPERANTO, ESTONIAN,
            FINNISH, FRENCH, GANDA, GEORGIAN, GERMAN, GREEK, GUJARATI, HEBREW, HINDI, HUNGARIAN, ICELANDIC, INDONESIAN,
            IRISH, ITALIAN, JAPANESE, KAZAKH, KOREAN, LATIN, LATVIAN, LITHUANIAN, MACEDONIAN, MALAY, MAORI,
            MARATHI, MONGOLIAN, NYNORSK, OROMO, PERSIAN, POLISH, PORTUGUESE, PUNJABI, ROMANIAN, RUSSIAN, SERBIAN, SHONA,
            SLOVAK, SLOVENE, SOMALI, SOTHO, SPANISH, SWAHILI, SWEDISH, TAGALOG, TAMIL, TELUGU, THAI, TSONGA, TSWANA,
            TURKISH, UKRAINIAN, URDU, VIETNAMESE, WELSH, XHOSA, YORUBA, ZULU
        )
    }

    @Test
    fun `assert that all supported spoken languages are available`() {
        assertThat(Language.allSpokenOnes()).containsExactly(
            AFRIKAANS, ALBANIAN, ARABIC, ARMENIAN, AZERBAIJANI, BASQUE, BELARUSIAN, BENGALI, BOKMAL, BOSNIAN, BULGARIAN,
            CATALAN, CHINESE, CROATIAN, CZECH, DANISH, DUTCH, ENGLISH, ESPERANTO, ESTONIAN,
            FINNISH, FRENCH, GANDA, GEORGIAN, GERMAN, GREEK, GUJARATI, HEBREW, HINDI, HUNGARIAN, ICELANDIC, INDONESIAN,
            IRISH, ITALIAN, JAPANESE, KAZAKH, KOREAN, LATVIAN, LITHUANIAN, MACEDONIAN, MALAY, MAORI, MARATHI, MONGOLIAN,
            NYNORSK, OROMO, PERSIAN, POLISH, PORTUGUESE, PUNJABI, ROMANIAN, RUSSIAN, SERBIAN, SHONA, SLOVAK, SLOVENE,
            SOMALI, SOTHO, SPANISH, SWAHILI, SWEDISH, TAGALOG, TAMIL, TELUGU, THAI, TSONGA, TSWANA,
            TURKISH, UKRAINIAN, URDU, VIETNAMESE, WELSH, XHOSA, YORUBA, ZULU
        )
    }

    @Test
    fun `assert that certain languages support Arabic script`() {
        assertThat(Language.allWithArabicScript()).containsExactly(ARABIC, PERSIAN, URDU)
    }

    @Test
    fun `assert that certain languages support Cyrillic script`() {
        assertThat(Language.allWithCyrillicScript()).containsExactly(
            BELARUSIAN,
            BULGARIAN,
            KAZAKH,
            MACEDONIAN,
            MONGOLIAN,
            RUSSIAN,
            SERBIAN,
            UKRAINIAN
        )
    }

    @Test
    fun `assert that certain languages support Devanagari script`() {
        assertThat(Language.allWithDevanagariScript()).containsExactly(HINDI, MARATHI)
    }

    @Test
    fun `assert that certain languages support Latin script`() {
        assertThat(Language.allWithLatinScript()).containsExactly(
            AFRIKAANS, ALBANIAN, AZERBAIJANI, BASQUE, BOKMAL, BOSNIAN, CATALAN, CROATIAN, CZECH,
            DANISH, DUTCH, ENGLISH, ESPERANTO, ESTONIAN, FINNISH, FRENCH, GANDA, GERMAN,
            HUNGARIAN, ICELANDIC, INDONESIAN, IRISH, ITALIAN, LATIN, LATVIAN,
            LITHUANIAN, MALAY, MAORI, NYNORSK, OROMO, POLISH, PORTUGUESE,
            ROMANIAN, SHONA, SLOVAK, SLOVENE, SOMALI, SOTHO, SPANISH, SWAHILI, SWEDISH, TAGALOG, TSONGA, TSWANA,
            TURKISH, VIETNAMESE, WELSH, XHOSA, YORUBA, ZULU
        )
    }

    @ParameterizedTest
    @MethodSource("filteredLanguagesProvider")
    internal fun `assert that languages support correct alphabets`(
        alphabet: Alphabet,
        expectedLanguages: List<Language>
    ) {
        assertThat(
            Language.values().filter { it.alphabets.contains(alphabet) }
        ).`as`(
            "alphabet '$alphabet'"
        ).containsExactlyElementsOf(
            expectedLanguages
        )
    }

    @ParameterizedTest
    @CsvSource(
        "AF, AFRIKAANS",
        "SQ, ALBANIAN",
        "AR, ARABIC",
        "HY, ARMENIAN",
        "AZ, AZERBAIJANI",
        "EU, BASQUE",
        "BE, BELARUSIAN",
        "BN, BENGALI",
        "NB, BOKMAL",
        "BS, BOSNIAN",
        "BG, BULGARIAN",
        "CA, CATALAN",
        "ZH, CHINESE",
        "HR, CROATIAN",
        "CS, CZECH",
        "DA, DANISH",
        "NL, DUTCH",
        "EN, ENGLISH",
        "EO, ESPERANTO",
        "ET, ESTONIAN",
        "FI, FINNISH",
        "FR, FRENCH",
        "LG, GANDA",
        "KA, GEORGIAN",
        "DE, GERMAN",
        "EL, GREEK",
        "GU, GUJARATI",
        "HE, HEBREW",
        "HI, HINDI",
        "HU, HUNGARIAN",
        "IS, ICELANDIC",
        "ID, INDONESIAN",
        "GA, IRISH",
        "IT, ITALIAN",
        "JA, JAPANESE",
        "KK, KAZAKH",
        "LA, LATIN",
        "LV, LATVIAN",
        "LT, LITHUANIAN",
        "MI, MAORI",
        "MK, MACEDONIAN",
        "MS, MALAY",
        "MR, MARATHI",
        "MN, MONGOLIAN",
        "NN, NYNORSK",
        "OM, OROMO",
        "FA, PERSIAN",
        "PL, POLISH",
        "PT, PORTUGUESE",
        "PA, PUNJABI",
        "RO, ROMANIAN",
        "RU, RUSSIAN",
        "SR, SERBIAN",
        "SN, SHONA",
        "SK, SLOVAK",
        "SL, SLOVENE",
        "SO, SOMALI",
        "ST, SOTHO",
        "ES, SPANISH",
        "SW, SWAHILI",
        "SV, SWEDISH",
        "TL, TAGALOG",
        "TA, TAMIL",
        "TE, TELUGU",
        "TH, THAI",
        "TS, TSONGA",
        "TN, TSWANA",
        "TR, TURKISH",
        "UK, UKRAINIAN",
        "UR, URDU",
        "VI, VIETNAMESE",
        "CY, WELSH",
        "XH, XHOSA",
        "YO, YORUBA",
        "ZU, ZULU"
    )
    fun `assert that correct language is returned for iso code`(isoCode: IsoCode639_1, language: Language) {
        assertThat(Language.getByIsoCode639_1(isoCode)).isEqualTo(language)
    }

    private fun filteredLanguagesProvider() = listOf(
        arguments(Alphabet.ARABIC, listOf(ARABIC, PERSIAN, URDU)),
        arguments(Alphabet.ARMENIAN, listOf(ARMENIAN)),
        arguments(Alphabet.BENGALI, listOf(BENGALI)),
        arguments(
            Alphabet.CYRILLIC,
            listOf(
                BELARUSIAN,
                BULGARIAN,
                KAZAKH,
                MACEDONIAN,
                MONGOLIAN,
                RUSSIAN,
                SERBIAN,
                UKRAINIAN
            )
        ),
        arguments(Alphabet.DEVANAGARI, listOf(HINDI, MARATHI)),
        arguments(Alphabet.GEORGIAN, listOf(GEORGIAN)),
        arguments(Alphabet.GREEK, listOf(GREEK)),
        arguments(Alphabet.GUJARATI, listOf(GUJARATI)),
        arguments(Alphabet.GURMUKHI, listOf(PUNJABI)),
        arguments(Alphabet.HAN, listOf(CHINESE, JAPANESE)),
        arguments(Alphabet.HANGUL, listOf(KOREAN)),
        arguments(Alphabet.HEBREW, listOf(HEBREW)),
        arguments(Alphabet.HIRAGANA, listOf(JAPANESE)),
        arguments(Alphabet.KATAKANA, listOf(JAPANESE)),
        arguments(
            Alphabet.LATIN,
            listOf(
                AFRIKAANS, ALBANIAN, AZERBAIJANI, BASQUE, BOKMAL, BOSNIAN, CATALAN, CROATIAN,
                CZECH, DANISH, DUTCH, ENGLISH, ESPERANTO, ESTONIAN, FINNISH, FRENCH, GANDA,
                GERMAN, HUNGARIAN, ICELANDIC, INDONESIAN, IRISH, ITALIAN,
                LATIN, LATVIAN, LITHUANIAN, MALAY, MAORI, NYNORSK, OROMO,
                POLISH, PORTUGUESE, ROMANIAN, SHONA, SLOVAK, SLOVENE, SOMALI, SOTHO,
                SPANISH, SWAHILI, SWEDISH, TAGALOG, TSONGA, TSWANA, TURKISH, VIETNAMESE,
                WELSH, XHOSA, YORUBA, ZULU
            )
        ),
        arguments(Alphabet.TAMIL, listOf(TAMIL)),
        arguments(Alphabet.TELUGU, listOf(TELUGU)),
        arguments(Alphabet.THAI, listOf(THAI)),
        arguments(Alphabet.NONE, listOf(UNKNOWN))
    )
}

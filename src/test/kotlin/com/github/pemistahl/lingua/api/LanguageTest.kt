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
import com.github.pemistahl.lingua.api.Language.AMHARIC
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
import com.github.pemistahl.lingua.api.Language.SINHALA
import com.github.pemistahl.lingua.api.Language.SLOVAK
import com.github.pemistahl.lingua.api.Language.SLOVENE
import com.github.pemistahl.lingua.api.Language.SOMALI
import com.github.pemistahl.lingua.api.Language.SOTHO
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWAHILI
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.SWISS_GERMAN
import com.github.pemistahl.lingua.api.Language.TAGALOG
import com.github.pemistahl.lingua.api.Language.TAMIL
import com.github.pemistahl.lingua.api.Language.TELUGU
import com.github.pemistahl.lingua.api.Language.THAI
import com.github.pemistahl.lingua.api.Language.TIGRINYA
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
import org.junit.jupiter.params.provider.MethodSource

class LanguageTest {

    @Test
    fun `assert that all supported languages are available`() {
        assertThat(Language.all()).containsExactly(
            AFRIKAANS, ALBANIAN, AMHARIC, ARABIC, ARMENIAN, AZERBAIJANI, BASQUE, BELARUSIAN, BENGALI, BOKMAL, BOSNIAN,
            BULGARIAN, CATALAN, CHINESE, CROATIAN, CZECH, DANISH, DUTCH, ENGLISH, ESPERANTO, ESTONIAN,
            FINNISH, FRENCH, GANDA, GEORGIAN, GERMAN, GREEK, GUJARATI, HEBREW, HINDI, HUNGARIAN, ICELANDIC, INDONESIAN,
            IRISH, ITALIAN, JAPANESE, KAZAKH, KOREAN, LATIN, LATVIAN, LITHUANIAN, MACEDONIAN, MALAY, MAORI,
            MARATHI, MONGOLIAN, NYNORSK, OROMO, PERSIAN, POLISH, PORTUGUESE, PUNJABI, ROMANIAN, RUSSIAN, SERBIAN, SHONA,
            SINHALA, SLOVAK, SLOVENE, SOMALI, SOTHO, SPANISH, SWAHILI, SWEDISH, SWISS_GERMAN, TAGALOG, TAMIL, TELUGU,
            THAI, TIGRINYA, TSONGA, TSWANA, TURKISH, UKRAINIAN, URDU, VIETNAMESE, WELSH, XHOSA, YORUBA, ZULU
        )
    }

    @Test
    fun `assert that all supported spoken languages are available`() {
        assertThat(Language.allSpokenOnes()).containsExactly(
            AFRIKAANS, ALBANIAN, AMHARIC, ARABIC, ARMENIAN, AZERBAIJANI, BASQUE, BELARUSIAN, BENGALI, BOKMAL, BOSNIAN,
            BULGARIAN, CATALAN, CHINESE, CROATIAN, CZECH, DANISH, DUTCH, ENGLISH, ESPERANTO, ESTONIAN,
            FINNISH, FRENCH, GANDA, GEORGIAN, GERMAN, GREEK, GUJARATI, HEBREW, HINDI, HUNGARIAN, ICELANDIC, INDONESIAN,
            IRISH, ITALIAN, JAPANESE, KAZAKH, KOREAN, LATVIAN, LITHUANIAN, MACEDONIAN, MALAY, MAORI, MARATHI, MONGOLIAN,
            NYNORSK, OROMO, PERSIAN, POLISH, PORTUGUESE, PUNJABI, ROMANIAN, RUSSIAN, SERBIAN, SHONA, SINHALA, SLOVAK,
            SLOVENE, SOMALI, SOTHO, SPANISH, SWAHILI, SWEDISH, SWISS_GERMAN, TAGALOG, TAMIL, TELUGU, THAI, TIGRINYA,
            TSONGA, TSWANA, TURKISH, UKRAINIAN, URDU, VIETNAMESE, WELSH, XHOSA, YORUBA, ZULU
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
    fun `assert that certain languages support Ethiopic script`() {
        assertThat(Language.allWithEthiopicScript()).containsExactly(AMHARIC, TIGRINYA)
    }

    @Test
    fun `assert that certain languages support Latin script`() {
        assertThat(Language.allWithLatinScript()).containsExactly(
            AFRIKAANS, ALBANIAN, AZERBAIJANI, BASQUE, BOKMAL, BOSNIAN, CATALAN, CROATIAN, CZECH,
            DANISH, DUTCH, ENGLISH, ESPERANTO, ESTONIAN, FINNISH, FRENCH, GANDA, GERMAN,
            HUNGARIAN, ICELANDIC, INDONESIAN, IRISH, ITALIAN, LATIN, LATVIAN,
            LITHUANIAN, MALAY, MAORI, NYNORSK, OROMO, POLISH, PORTUGUESE,
            ROMANIAN, SHONA, SLOVAK, SLOVENE, SOMALI, SOTHO, SPANISH, SWAHILI, SWEDISH, SWISS_GERMAN, TAGALOG, TSONGA,
            TSWANA, TURKISH, VIETNAMESE, WELSH, XHOSA, YORUBA, ZULU
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
    @MethodSource("iso639_1ToLanguageMappingProvider")
    fun `assert that correct language is returned for iso code`(isoCode: IsoCode639_1, language: List<Language>) {
        assertThat(Language.getByIsoCode639_1(isoCode)).isEqualTo(language)
    }

    private fun iso639_1ToLanguageMappingProvider() = listOf(
        arguments(IsoCode639_1.AF, listOf(AFRIKAANS)),
        arguments(IsoCode639_1.AM, listOf(AMHARIC)),
        arguments(IsoCode639_1.SQ, listOf(ALBANIAN)),
        arguments(IsoCode639_1.AR, listOf(ARABIC)),
        arguments(IsoCode639_1.HY, listOf(ARMENIAN)),
        arguments(IsoCode639_1.AZ, listOf(AZERBAIJANI)),
        arguments(IsoCode639_1.EU, listOf(BASQUE)),
        arguments(IsoCode639_1.BE, listOf(BELARUSIAN)),
        arguments(IsoCode639_1.BN, listOf(BENGALI)),
        arguments(IsoCode639_1.NB, listOf(BOKMAL)),
        arguments(IsoCode639_1.BS, listOf(BOSNIAN)),
        arguments(IsoCode639_1.BG, listOf(BULGARIAN)),
        arguments(IsoCode639_1.CA, listOf(CATALAN)),
        arguments(IsoCode639_1.ZH, listOf(CHINESE)),
        arguments(IsoCode639_1.HR, listOf(CROATIAN)),
        arguments(IsoCode639_1.CS, listOf(CZECH)),
        arguments(IsoCode639_1.DA, listOf(DANISH)),
        arguments(IsoCode639_1.NL, listOf(DUTCH)),
        arguments(IsoCode639_1.EN, listOf(ENGLISH)),
        arguments(IsoCode639_1.EO, listOf(ESPERANTO)),
        arguments(IsoCode639_1.ET, listOf(ESTONIAN)),
        arguments(IsoCode639_1.FI, listOf(FINNISH)),
        arguments(IsoCode639_1.FR, listOf(FRENCH)),
        arguments(IsoCode639_1.LG, listOf(GANDA)),
        arguments(IsoCode639_1.KA, listOf(GEORGIAN)),
        arguments(IsoCode639_1.DE, listOf(GERMAN, SWISS_GERMAN)),
        arguments(IsoCode639_1.EL, listOf(GREEK)),
        arguments(IsoCode639_1.GU, listOf(GUJARATI)),
        arguments(IsoCode639_1.HE, listOf(HEBREW)),
        arguments(IsoCode639_1.HI, listOf(HINDI)),
        arguments(IsoCode639_1.HU, listOf(HUNGARIAN)),
        arguments(IsoCode639_1.IS, listOf(ICELANDIC)),
        arguments(IsoCode639_1.ID, listOf(INDONESIAN)),
        arguments(IsoCode639_1.GA, listOf(IRISH)),
        arguments(IsoCode639_1.IT, listOf(ITALIAN)),
        arguments(IsoCode639_1.JA, listOf(JAPANESE)),
        arguments(IsoCode639_1.KK, listOf(KAZAKH)),
        arguments(IsoCode639_1.LA, listOf(LATIN)),
        arguments(IsoCode639_1.LV, listOf(LATVIAN)),
        arguments(IsoCode639_1.LT, listOf(LITHUANIAN)),
        arguments(IsoCode639_1.MI, listOf(MAORI)),
        arguments(IsoCode639_1.MK, listOf(MACEDONIAN)),
        arguments(IsoCode639_1.MS, listOf(MALAY)),
        arguments(IsoCode639_1.MR, listOf(MARATHI)),
        arguments(IsoCode639_1.MN, listOf(MONGOLIAN)),
        arguments(IsoCode639_1.NN, listOf(NYNORSK)),
        arguments(IsoCode639_1.OM, listOf(OROMO)),
        arguments(IsoCode639_1.FA, listOf(PERSIAN)),
        arguments(IsoCode639_1.PL, listOf(POLISH)),
        arguments(IsoCode639_1.PT, listOf(PORTUGUESE)),
        arguments(IsoCode639_1.PA, listOf(PUNJABI)),
        arguments(IsoCode639_1.RO, listOf(ROMANIAN)),
        arguments(IsoCode639_1.RU, listOf(RUSSIAN)),
        arguments(IsoCode639_1.SR, listOf(SERBIAN)),
        arguments(IsoCode639_1.SN, listOf(SHONA)),
        arguments(IsoCode639_1.SI, listOf(SINHALA)),
        arguments(IsoCode639_1.SK, listOf(SLOVAK)),
        arguments(IsoCode639_1.SL, listOf(SLOVENE)),
        arguments(IsoCode639_1.SO, listOf(SOMALI)),
        arguments(IsoCode639_1.ST, listOf(SOTHO)),
        arguments(IsoCode639_1.ES, listOf(SPANISH)),
        arguments(IsoCode639_1.SW, listOf(SWAHILI)),
        arguments(IsoCode639_1.SV, listOf(SWEDISH)),
        arguments(IsoCode639_1.TL, listOf(TAGALOG)),
        arguments(IsoCode639_1.TA, listOf(TAMIL)),
        arguments(IsoCode639_1.TE, listOf(TELUGU)),
        arguments(IsoCode639_1.TH, listOf(THAI)),
        arguments(IsoCode639_1.TI, listOf(TIGRINYA)),
        arguments(IsoCode639_1.TS, listOf(TSONGA)),
        arguments(IsoCode639_1.TN, listOf(TSWANA)),
        arguments(IsoCode639_1.TR, listOf(TURKISH)),
        arguments(IsoCode639_1.UK, listOf(UKRAINIAN)),
        arguments(IsoCode639_1.UR, listOf(URDU)),
        arguments(IsoCode639_1.VI, listOf(VIETNAMESE)),
        arguments(IsoCode639_1.CY, listOf(WELSH)),
        arguments(IsoCode639_1.XH, listOf(XHOSA)),
        arguments(IsoCode639_1.YO, listOf(YORUBA)),
        arguments(IsoCode639_1.ZU, listOf(ZULU))
    )

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
        arguments(Alphabet.ETHIOPIC, listOf(AMHARIC, TIGRINYA)),
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
                SPANISH, SWAHILI, SWEDISH, SWISS_GERMAN, TAGALOG, TSONGA, TSWANA, TURKISH,
                VIETNAMESE, WELSH, XHOSA, YORUBA, ZULU
            )
        ),
        arguments(Alphabet.SINHALA, listOf(SINHALA)),
        arguments(Alphabet.TAMIL, listOf(TAMIL)),
        arguments(Alphabet.TELUGU, listOf(TELUGU)),
        arguments(Alphabet.THAI, listOf(THAI)),
        arguments(Alphabet.NONE, listOf(UNKNOWN))
    )
}

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

package com.github.pemistahl.lingua.internal

import com.github.pemistahl.lingua.api.Language.AFRIKAANS
import com.github.pemistahl.lingua.api.Language.ALBANIAN
import com.github.pemistahl.lingua.api.Language.AZERBAIJANI
import com.github.pemistahl.lingua.api.Language.BASQUE
import com.github.pemistahl.lingua.api.Language.BELARUSIAN
import com.github.pemistahl.lingua.api.Language.BOKMAL
import com.github.pemistahl.lingua.api.Language.BOSNIAN
import com.github.pemistahl.lingua.api.Language.BULGARIAN
import com.github.pemistahl.lingua.api.Language.CATALAN
import com.github.pemistahl.lingua.api.Language.CHINESE
import com.github.pemistahl.lingua.api.Language.CROATIAN
import com.github.pemistahl.lingua.api.Language.CZECH
import com.github.pemistahl.lingua.api.Language.DANISH
import com.github.pemistahl.lingua.api.Language.DUTCH
import com.github.pemistahl.lingua.api.Language.ESTONIAN
import com.github.pemistahl.lingua.api.Language.FINNISH
import com.github.pemistahl.lingua.api.Language.FRENCH
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.HUNGARIAN
import com.github.pemistahl.lingua.api.Language.ICELANDIC
import com.github.pemistahl.lingua.api.Language.IRISH
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.JAPANESE
import com.github.pemistahl.lingua.api.Language.KAZAKH
import com.github.pemistahl.lingua.api.Language.KOREAN
import com.github.pemistahl.lingua.api.Language.LATVIAN
import com.github.pemistahl.lingua.api.Language.LITHUANIAN
import com.github.pemistahl.lingua.api.Language.MACEDONIAN
import com.github.pemistahl.lingua.api.Language.MAORI
import com.github.pemistahl.lingua.api.Language.MONGOLIAN
import com.github.pemistahl.lingua.api.Language.NYNORSK
import com.github.pemistahl.lingua.api.Language.POLISH
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.ROMANIAN
import com.github.pemistahl.lingua.api.Language.RUSSIAN
import com.github.pemistahl.lingua.api.Language.SERBIAN
import com.github.pemistahl.lingua.api.Language.SLOVAK
import com.github.pemistahl.lingua.api.Language.SLOVENE
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.TURKISH
import com.github.pemistahl.lingua.api.Language.UKRAINIAN
import com.github.pemistahl.lingua.api.Language.VIETNAMESE
import com.github.pemistahl.lingua.api.Language.YORUBA
import java.util.EnumSet

internal object Constant {

    val CHARS_TO_LANGUAGES_MAPPING = mapOf(
        "Ãã" to EnumSet.of(PORTUGUESE, VIETNAMESE),
        "ĄąĘę" to EnumSet.of(LITHUANIAN, POLISH),
        "Żż" to EnumSet.of(POLISH, ROMANIAN),
        "Îî" to EnumSet.of(FRENCH, ROMANIAN),
        "Ññ" to EnumSet.of(BASQUE, SPANISH),
        "ŇňŤť" to EnumSet.of(CZECH, SLOVAK),
        "Ăă" to EnumSet.of(ROMANIAN, VIETNAMESE),
        "İıĞğ" to EnumSet.of(AZERBAIJANI, TURKISH),
        "ЈјЉљЊњ" to EnumSet.of(MACEDONIAN, SERBIAN),
        "ẸẹỌọ" to EnumSet.of(VIETNAMESE, YORUBA),
        "ÐðÞþ" to EnumSet.of(ICELANDIC, TURKISH),
        "Ûû" to EnumSet.of(FRENCH, HUNGARIAN),
        "Ōō" to EnumSet.of(MAORI, YORUBA),

        "ĀāĒēĪī" to EnumSet.of(LATVIAN, MAORI, YORUBA),
        "Şş" to EnumSet.of(AZERBAIJANI, ROMANIAN, TURKISH),
        "Ďď" to EnumSet.of(CZECH, ROMANIAN, SLOVAK),
        "Ćć" to EnumSet.of(BOSNIAN, CROATIAN, POLISH),
        "Đđ" to EnumSet.of(BOSNIAN, CROATIAN, VIETNAMESE),
        "Іі" to EnumSet.of(BELARUSIAN, KAZAKH, UKRAINIAN),
        "Ìì" to EnumSet.of(ITALIAN, VIETNAMESE, YORUBA),
        "Øø" to EnumSet.of(BOKMAL, DANISH, NYNORSK),

        "Ūū" to EnumSet.of(LATVIAN, LITHUANIAN, MAORI, YORUBA),
        "Ëë" to EnumSet.of(AFRIKAANS, ALBANIAN, DUTCH, FRENCH),
        "ÈèÙù" to EnumSet.of(FRENCH, ITALIAN, VIETNAMESE, YORUBA),
        "Êê" to EnumSet.of(AFRIKAANS, FRENCH, PORTUGUESE, VIETNAMESE),
        "Õõ" to EnumSet.of(ESTONIAN, HUNGARIAN, PORTUGUESE, VIETNAMESE),
        "Ôô" to EnumSet.of(FRENCH, PORTUGUESE, SLOVAK, VIETNAMESE),

        "ЁёЫыЭэ" to EnumSet.of(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN),
        "ЩщЪъ" to EnumSet.of(BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN),
        "Òò" to EnumSet.of(CATALAN, ITALIAN, VIETNAMESE, YORUBA),
        "Ææ" to EnumSet.of(BOKMAL, DANISH, ICELANDIC, NYNORSK),
        "Åå" to EnumSet.of(BOKMAL, DANISH, NYNORSK, SWEDISH),

        "Ýý" to EnumSet.of(CZECH, ICELANDIC, SLOVAK, TURKISH, VIETNAMESE),
        "Ää" to EnumSet.of(ESTONIAN, FINNISH, GERMAN, SLOVAK, SWEDISH),
        "Àà" to EnumSet.of(CATALAN, FRENCH, ITALIAN, PORTUGUESE, VIETNAMESE),
        "Ââ" to EnumSet.of(FRENCH, PORTUGUESE, ROMANIAN, TURKISH, VIETNAMESE),

        "Üü" to EnumSet.of(AZERBAIJANI, CATALAN, ESTONIAN, GERMAN, HUNGARIAN, SPANISH, TURKISH),
        "ČčŠšŽž" to EnumSet.of(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE),
        "Çç" to EnumSet.of(ALBANIAN, AZERBAIJANI, BASQUE, CATALAN, FRENCH, PORTUGUESE, TURKISH),

        "Öö" to EnumSet.of(AZERBAIJANI, ESTONIAN, FINNISH, GERMAN, HUNGARIAN, ICELANDIC, SWEDISH, TURKISH),

        "Óó" to
            EnumSet.of(CATALAN, HUNGARIAN, ICELANDIC, IRISH, POLISH, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA),
        "ÁáÍíÚú" to
            EnumSet.of(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA),

        "Éé" to EnumSet.of(
            CATALAN, CZECH, FRENCH, HUNGARIAN, ICELANDIC, IRISH,
            ITALIAN, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA
        )
    )

    fun isJapaneseAlphabet(char: Char): Boolean {
        val script = Character.UnicodeScript.of(char.code)
        return script == Character.UnicodeScript.HIRAGANA ||
            script == Character.UnicodeScript.KATAKANA ||
            script == Character.UnicodeScript.HAN
    }

    val LANGUAGES_SUPPORTING_LOGOGRAMS = EnumSet.of(CHINESE, JAPANESE, KOREAN)
    val MULTIPLE_WHITESPACE = Regex("\\s+")
    val NO_LETTER = Regex("^[^\\p{L}]+$")
    val NUMBERS = Regex("\\p{N}")
    val PUNCTUATION = Regex("\\p{P}")
}

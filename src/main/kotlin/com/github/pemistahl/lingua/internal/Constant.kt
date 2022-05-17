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
import com.github.pemistahl.lingua.internal.util.extension.enumSetOf

internal object Constant {

    val CHARS_TO_LANGUAGES_MAPPING = mapOf(
        "Ãã" to enumSetOf(PORTUGUESE, VIETNAMESE),
        "ĄąĘę" to enumSetOf(LITHUANIAN, POLISH),
        "Żż" to enumSetOf(POLISH, ROMANIAN),
        "Îî" to enumSetOf(FRENCH, ROMANIAN),
        "Ññ" to enumSetOf(BASQUE, SPANISH),
        "ŇňŤť" to enumSetOf(CZECH, SLOVAK),
        "Ăă" to enumSetOf(ROMANIAN, VIETNAMESE),
        "İıĞğ" to enumSetOf(AZERBAIJANI, TURKISH),
        "ЈјЉљЊњ" to enumSetOf(MACEDONIAN, SERBIAN),
        "ẸẹỌọ" to enumSetOf(VIETNAMESE, YORUBA),
        "ÐðÞþ" to enumSetOf(ICELANDIC, TURKISH),
        "Ûû" to enumSetOf(FRENCH, HUNGARIAN),
        "Ōō" to enumSetOf(MAORI, YORUBA),

        "ĀāĒēĪī" to enumSetOf(LATVIAN, MAORI, YORUBA),
        "Şş" to enumSetOf(AZERBAIJANI, ROMANIAN, TURKISH),
        "Ďď" to enumSetOf(CZECH, ROMANIAN, SLOVAK),
        "Ćć" to enumSetOf(BOSNIAN, CROATIAN, POLISH),
        "Đđ" to enumSetOf(BOSNIAN, CROATIAN, VIETNAMESE),
        "Іі" to enumSetOf(BELARUSIAN, KAZAKH, UKRAINIAN),
        "Ìì" to enumSetOf(ITALIAN, VIETNAMESE, YORUBA),
        "Øø" to enumSetOf(BOKMAL, DANISH, NYNORSK),

        "Ūū" to enumSetOf(LATVIAN, LITHUANIAN, MAORI, YORUBA),
        "Ëë" to enumSetOf(AFRIKAANS, ALBANIAN, DUTCH, FRENCH),
        "ÈèÙù" to enumSetOf(FRENCH, ITALIAN, VIETNAMESE, YORUBA),
        "Êê" to enumSetOf(AFRIKAANS, FRENCH, PORTUGUESE, VIETNAMESE),
        "Õõ" to enumSetOf(ESTONIAN, HUNGARIAN, PORTUGUESE, VIETNAMESE),
        "Ôô" to enumSetOf(FRENCH, PORTUGUESE, SLOVAK, VIETNAMESE),

        "ЁёЫыЭэ" to enumSetOf(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN),
        "ЩщЪъ" to enumSetOf(BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN),
        "Òò" to enumSetOf(CATALAN, ITALIAN, VIETNAMESE, YORUBA),
        "Ææ" to enumSetOf(BOKMAL, DANISH, ICELANDIC, NYNORSK),
        "Åå" to enumSetOf(BOKMAL, DANISH, NYNORSK, SWEDISH),

        "Ýý" to enumSetOf(CZECH, ICELANDIC, SLOVAK, TURKISH, VIETNAMESE),
        "Ää" to enumSetOf(ESTONIAN, FINNISH, GERMAN, SLOVAK, SWEDISH),
        "Àà" to enumSetOf(CATALAN, FRENCH, ITALIAN, PORTUGUESE, VIETNAMESE),
        "Ââ" to enumSetOf(FRENCH, PORTUGUESE, ROMANIAN, TURKISH, VIETNAMESE),

        "Üü" to enumSetOf(AZERBAIJANI, CATALAN, ESTONIAN, GERMAN, HUNGARIAN, SPANISH, TURKISH),
        "ČčŠšŽž" to enumSetOf(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE),
        "Çç" to enumSetOf(ALBANIAN, AZERBAIJANI, BASQUE, CATALAN, FRENCH, PORTUGUESE, TURKISH),

        "Öö" to enumSetOf(AZERBAIJANI, ESTONIAN, FINNISH, GERMAN, HUNGARIAN, ICELANDIC, SWEDISH, TURKISH),

        "Óó" to enumSetOf(
            CATALAN, HUNGARIAN, ICELANDIC, IRISH, POLISH,
            PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA
        ),
        "ÁáÍíÚú" to enumSetOf(
            CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN,
            PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA
        ),

        "Éé" to enumSetOf(
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

    val LANGUAGES_SUPPORTING_LOGOGRAMS = enumSetOf(CHINESE, JAPANESE, KOREAN)
    val MULTIPLE_WHITESPACE = Regex("\\s+")
    val NO_LETTER = Regex("^[^\\p{L}]+$")
    val NUMBERS = Regex("\\p{N}")
    val PUNCTUATION = Regex("\\p{P}")
}

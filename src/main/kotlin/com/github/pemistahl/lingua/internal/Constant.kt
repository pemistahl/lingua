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
import java.util.regex.PatternSyntaxException

internal object Constant {

    val CHARS_TO_LANGUAGES_MAPPING = hashMapOf(
        "Ãã" to setOf(PORTUGUESE, VIETNAMESE),
        "ĄąĘę" to setOf(LITHUANIAN, POLISH),
        "Żż" to setOf(POLISH, ROMANIAN),
        "Îî" to setOf(FRENCH, ROMANIAN),
        "Ññ" to setOf(BASQUE, SPANISH),
        "ŇňŤť" to setOf(CZECH, SLOVAK),
        "Ăă" to setOf(ROMANIAN, VIETNAMESE),
        "İıĞğ" to setOf(AZERBAIJANI, TURKISH),
        "ЈјЉљЊњ" to setOf(MACEDONIAN, SERBIAN),
        "ẸẹỌọ" to setOf(VIETNAMESE, YORUBA),
        "ÐðÞþ" to setOf(ICELANDIC, TURKISH),
        "Ûû" to setOf(FRENCH, HUNGARIAN),
        "Ōō" to setOf(MAORI, YORUBA),

        "ĀāĒēĪī" to setOf(LATVIAN, MAORI, YORUBA),
        "Şş" to setOf(AZERBAIJANI, ROMANIAN, TURKISH),
        "Ďď" to setOf(CZECH, ROMANIAN, SLOVAK),
        "Ćć" to setOf(BOSNIAN, CROATIAN, POLISH),
        "Đđ" to setOf(BOSNIAN, CROATIAN, VIETNAMESE),
        "Іі" to setOf(BELARUSIAN, KAZAKH, UKRAINIAN),
        "Ìì" to setOf(ITALIAN, VIETNAMESE, YORUBA),

        "Ūū" to setOf(LATVIAN, LITHUANIAN, MAORI, YORUBA),
        "Ëë" to setOf(AFRIKAANS, ALBANIAN, DUTCH, FRENCH),
        "ÈèÙù" to setOf(FRENCH, ITALIAN, VIETNAMESE, YORUBA),
        "Êê" to setOf(AFRIKAANS, FRENCH, PORTUGUESE, VIETNAMESE),
        "Õõ" to setOf(ESTONIAN, HUNGARIAN, PORTUGUESE, VIETNAMESE),
        "Ôô" to setOf(FRENCH, PORTUGUESE, SLOVAK, VIETNAMESE),
        "Øø" to setOf(BOKMAL, DANISH, NYNORSK),
        "ЁёЫыЭэ" to setOf(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN),
        "ЩщЪъ" to setOf(BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN),
        "Òò" to setOf(CATALAN, ITALIAN, VIETNAMESE, YORUBA),
        "Ââ" to setOf(PORTUGUESE, ROMANIAN, TURKISH, VIETNAMESE),

        "Ýý" to setOf(CZECH, ICELANDIC, SLOVAK, TURKISH, VIETNAMESE),
        "Ää" to setOf(ESTONIAN, FINNISH, GERMAN, SLOVAK, SWEDISH),
        "Àà" to setOf(CATALAN, FRENCH, ITALIAN, PORTUGUESE, VIETNAMESE),
        "Ææ" to setOf(BOKMAL, DANISH, ICELANDIC, NYNORSK),
        "Åå" to setOf(BOKMAL, DANISH, NYNORSK, SWEDISH),

        "Üü" to setOf(AZERBAIJANI, CATALAN, ESTONIAN, GERMAN, HUNGARIAN, SPANISH, TURKISH),
        "ČčŠšŽž" to setOf(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE),
        "Çç" to setOf(ALBANIAN, AZERBAIJANI, BASQUE, CATALAN, FRENCH, PORTUGUESE, TURKISH),

        "Öö" to setOf(AZERBAIJANI, ESTONIAN, FINNISH, GERMAN, HUNGARIAN, ICELANDIC, SWEDISH, TURKISH),

        "Óó" to setOf(CATALAN, HUNGARIAN, ICELANDIC, IRISH, POLISH, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA),
        "ÁáÍíÚú" to setOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA),

        "Éé" to setOf(
            CATALAN, CZECH, FRENCH, HUNGARIAN, ICELANDIC, IRISH,
            ITALIAN, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA
        )
    )
    val JAPANESE_CHARACTER_SET = try {
        Regex("^[\\p{Hiragana}\\p{Katakana}\\p{Han}]+$")
    } catch (e: PatternSyntaxException) {
        Regex("^[\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}]+$")
    }
    val LANGUAGES_SUPPORTING_LOGOGRAMS = setOf(CHINESE, JAPANESE, KOREAN)
    val MULTIPLE_WHITESPACE = Regex("\\s+")
    val NO_LETTER = Regex("^[^\\p{L}]+$")
    val NUMBERS = Regex("\\p{N}")
    val PUNCTUATION = Regex("\\p{P}")
}

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

import com.github.pemistahl.lingua.internal.Alphabet

enum class Language(
    val isoCode: String,
    internal val alphabets: Set<Alphabet>,
    internal val uniqueCharacters: String
) {
    AFRIKAANS  ("af", setOf(Alphabet.LATIN), ""),
    ALBANIAN   ("sq", setOf(Alphabet.LATIN), "Ëë"),
    ARABIC     ("ar", setOf(Alphabet.ARABIC), ""),
    BASQUE     ("eu", setOf(Alphabet.LATIN), ""),
    BELARUSIAN ("be", setOf(Alphabet.CYRILLIC), ""),
    BENGALI    ("bn", setOf(Alphabet.BENGALI), ""),
    BOKMAL     ("nb", setOf(Alphabet.LATIN), ""),
    BULGARIAN  ("bg", setOf(Alphabet.CYRILLIC), ""),
    CATALAN    ("ca", setOf(Alphabet.LATIN), "Ïï"),
    CHINESE    ("zh", setOf(Alphabet.HAN), ""),
    CROATIAN   ("hr", setOf(Alphabet.LATIN), ""),
    CZECH      ("cs", setOf(Alphabet.LATIN), "ĚěŘřŮů"),
    DANISH     ("da", setOf(Alphabet.LATIN), ""),
    DUTCH      ("nl", setOf(Alphabet.LATIN), ""),
    ENGLISH    ("en", setOf(Alphabet.LATIN), ""),
    ESTONIAN   ("et", setOf(Alphabet.LATIN), ""),
    FINNISH    ("fi", setOf(Alphabet.LATIN), ""),
    FRENCH     ("fr", setOf(Alphabet.LATIN), ""),
    GERMAN     ("de", setOf(Alphabet.LATIN), "ß"),
    GREEK      ("el", setOf(Alphabet.GREEK), ""),
    GUJARATI   ("gu", setOf(Alphabet.GUJARATI), ""),
    HEBREW     ("he", setOf(Alphabet.HEBREW), ""),
    HINDI      ("hi", setOf(Alphabet.DEVANAGARI), ""),
    HUNGARIAN  ("hu", setOf(Alphabet.LATIN), "ŐőŰű"),
    ICELANDIC  ("is", setOf(Alphabet.LATIN), ""),
    INDONESIAN ("id", setOf(Alphabet.LATIN), ""),
    IRISH      ("ga", setOf(Alphabet.LATIN), ""),
    ITALIAN    ("it", setOf(Alphabet.LATIN), ""),
    JAPANESE   ("ja", setOf(Alphabet.HIRAGANA, Alphabet.KATAKANA, Alphabet.HAN), ""),
    KOREAN     ("ko", setOf(Alphabet.HANGUL), ""),
    LATIN      ("la", setOf(Alphabet.LATIN), ""),
    LATVIAN    ("lv", setOf(Alphabet.LATIN), "ĀāĒēĢģĪīĶķĻļŅņ"),
    LITHUANIAN ("lt", setOf(Alphabet.LATIN), "ĖėĮįŲų"),
    MALAY      ("ms", setOf(Alphabet.LATIN), ""),
    NORWEGIAN  ("no", setOf(Alphabet.LATIN), ""),
    NYNORSK    ("nn", setOf(Alphabet.LATIN), ""),
    PERSIAN    ("fa", setOf(Alphabet.ARABIC), ""),
    POLISH     ("pl", setOf(Alphabet.LATIN), "ŁłŃńŚśŹź"),
    PORTUGUESE ("pt", setOf(Alphabet.LATIN), ""),
    PUNJABI    ("pa", setOf(Alphabet.GURMUKHI), ""),
    ROMANIAN   ("ro", setOf(Alphabet.LATIN), "Țţ"),
    RUSSIAN    ("ru", setOf(Alphabet.CYRILLIC), ""),
    SLOVAK     ("sk", setOf(Alphabet.LATIN), "ĹĺĽľŔŕ"),
    SLOVENE    ("sl", setOf(Alphabet.LATIN), ""),
    SOMALI     ("so", setOf(Alphabet.LATIN), ""),
    SPANISH    ("es", setOf(Alphabet.LATIN), "¿¡"),
    SWEDISH    ("sv", setOf(Alphabet.LATIN), ""),
    TAGALOG    ("tl", setOf(Alphabet.LATIN), ""),
    TAMIL      ("ta", setOf(Alphabet.TAMIL), ""),
    TELUGU     ("te", setOf(Alphabet.TELUGU), ""),
    THAI       ("th", setOf(Alphabet.THAI), ""),
    TURKISH    ("tr", setOf(Alphabet.LATIN), "İıĞğ"),
    URDU       ("ur", setOf(Alphabet.ARABIC), ""),
    VIETNAMESE ("vi", setOf(Alphabet.LATIN), "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếẸẹỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỌọỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ"),
    WELSH      ("cy", setOf(Alphabet.LATIN), ""),

    UNKNOWN    ("<unk>", setOf(Alphabet.NONE), "");

    companion object {
        @JvmStatic
        fun all() = filterOutLanguages(UNKNOWN, BOKMAL, NYNORSK)

        @JvmStatic
        fun allSpokenOnes() = filterOutLanguages(UNKNOWN, BOKMAL, LATIN, NYNORSK)

        @JvmStatic
        fun getByIsoCode(isoCode: String) = values().find {
            it.isoCode == isoCode
        } ?: throw IllegalArgumentException(
            "language with iso code '$isoCode' can not be found"
        )

        private fun filterOutLanguages(
            vararg languages: Language
        ) = values().filterNot { it in languages }.toTypedArray()
    }
}

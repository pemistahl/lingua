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
    internal val alphabet: Alphabet,
    internal val uniqueCharacters: String
) {
    AFRIKAANS  ("af", Alphabet.LATIN, ""),
    ALBANIAN   ("sq", Alphabet.LATIN, "Ëë"),
    ARABIC     ("ar", Alphabet.ARABIC, ""),
    BASQUE     ("eu", Alphabet.LATIN, ""),
    BELARUSIAN ("be", Alphabet.CYRILLIC, ""),
    BENGALI    ("bn", Alphabet.BENGALI, ""),
    BOKMAL     ("nb", Alphabet.LATIN, ""),
    BULGARIAN  ("bg", Alphabet.CYRILLIC, ""),
    CATALAN    ("ca", Alphabet.LATIN, "Ïï"),
    CHINESE    ("zh", Alphabet.CHINESE, ""),
    CROATIAN   ("hr", Alphabet.LATIN, ""),
    CZECH      ("cs", Alphabet.LATIN, "ĚěŘřŮů"),
    DANISH     ("da", Alphabet.LATIN, ""),
    DUTCH      ("nl", Alphabet.LATIN, ""),
    ENGLISH    ("en", Alphabet.LATIN, ""),
    ESTONIAN   ("et", Alphabet.LATIN, ""),
    FINNISH    ("fi", Alphabet.LATIN, ""),
    FRENCH     ("fr", Alphabet.LATIN, ""),
    GERMAN     ("de", Alphabet.LATIN, "ß"),
    GREEK      ("el", Alphabet.GREEK, ""),
    GUJARATI   ("gu", Alphabet.GUJARATI, ""),
    HEBREW     ("he", Alphabet.HEBREW, ""),
    HUNGARIAN  ("hu", Alphabet.LATIN, "ŐőŰű"),
    ICELANDIC  ("is", Alphabet.LATIN, ""),
    INDONESIAN ("id", Alphabet.LATIN, ""),
    IRISH      ("ga", Alphabet.LATIN, ""),
    ITALIAN    ("it", Alphabet.LATIN, ""),
    JAPANESE   ("ja", Alphabet.JAPANESE, ""),
    KOREAN     ("ko", Alphabet.KOREAN, ""),
    LATIN      ("la", Alphabet.LATIN, ""),
    LATVIAN    ("lv", Alphabet.LATIN, "ĀāĒēĢģĪīĶķĻļŅņ"),
    LITHUANIAN ("lt", Alphabet.LATIN, "ĖėĮįŲų"),
    MALAY      ("ms", Alphabet.LATIN, ""),
    NORWEGIAN  ("no", Alphabet.LATIN, ""),
    NYNORSK    ("nn", Alphabet.LATIN, ""),
    PERSIAN    ("fa", Alphabet.ARABIC, ""),
    POLISH     ("pl", Alphabet.LATIN, "ŁłŃńŚśŹź"),
    PORTUGUESE ("pt", Alphabet.LATIN, ""),
    ROMANIAN   ("ro", Alphabet.LATIN, "Țţ"),
    RUSSIAN    ("ru", Alphabet.CYRILLIC, ""),
    SLOVAK     ("sk", Alphabet.LATIN, "ĹĺĽľŔŕ"),
    SLOVENE    ("sl", Alphabet.LATIN, ""),
    SOMALI     ("so", Alphabet.LATIN, ""),
    SPANISH    ("es", Alphabet.LATIN, "¿¡"),
    SWEDISH    ("sv", Alphabet.LATIN, ""),
    TAGALOG    ("tl", Alphabet.LATIN, ""),
    TAMIL      ("ta", Alphabet.TAMIL, ""),
    TELUGU     ("te", Alphabet.TELUGU, ""),
    THAI       ("th", Alphabet.THAI, ""),
    TURKISH    ("tr", Alphabet.LATIN, "İıĞğ"),
    VIETNAMESE ("vi", Alphabet.LATIN, "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếẸẹỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỌọỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ"),
    WELSH      ("cy", Alphabet.LATIN, ""),

    UNKNOWN    ("<unk>", Alphabet.NONE, "");

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

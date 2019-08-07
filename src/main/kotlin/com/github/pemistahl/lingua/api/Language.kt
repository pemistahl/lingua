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
    internal val uniqueCharacters: String,
    internal var isExcludedFromDetection: Boolean
) {
    AFRIKAANS  ("af", Alphabet.LATIN, "", false),
    ALBANIAN   ("sq", Alphabet.LATIN, "Ëë", false),
    ARABIC     ("ar", Alphabet.ARABIC, "", false),
    BASQUE     ("eu", Alphabet.LATIN, "", false),
    BELARUSIAN ("be", Alphabet.CYRILLIC, "", false),
    BENGALI    ("bn", Alphabet.BENGALI, "", false),
    BOKMAL     ("nb", Alphabet.LATIN, "", false),
    BULGARIAN  ("bg", Alphabet.CYRILLIC, "", false),
    CATALAN    ("ca", Alphabet.LATIN, "Ïï", false),
    CHINESE    ("zh", Alphabet.CHINESE, "", false),
    CROATIAN   ("hr", Alphabet.LATIN, "", false),
    CZECH      ("cs", Alphabet.LATIN, "ĚěŘřŮů", false),
    DANISH     ("da", Alphabet.LATIN, "", false),
    DUTCH      ("nl", Alphabet.LATIN, "", false),
    ENGLISH    ("en", Alphabet.LATIN, "", false),
    ESTONIAN   ("et", Alphabet.LATIN, "", false),
    FINNISH    ("fi", Alphabet.LATIN, "", false),
    FRENCH     ("fr", Alphabet.LATIN, "", false),
    GERMAN     ("de", Alphabet.LATIN, "ß", false),
    GREEK      ("el", Alphabet.GREEK, "", false),
    HEBREW     ("he", Alphabet.HEBREW, "", false),
    HUNGARIAN  ("hu", Alphabet.LATIN, "ŐőŰű", false),
    ICELANDIC  ("is", Alphabet.LATIN, "", false),
    INDONESIAN ("id", Alphabet.LATIN, "", false),
    IRISH      ("ga", Alphabet.LATIN, "", false),
    ITALIAN    ("it", Alphabet.LATIN, "", false),
    JAPANESE   ("ja", Alphabet.JAPANESE, "", false),
    KOREAN     ("ko", Alphabet.KOREAN, "", false),
    LATIN      ("la", Alphabet.LATIN, "", false),
    LATVIAN    ("lv", Alphabet.LATIN, "ĀāĒēĢģĪīĶķĻļŅņ", false),
    LITHUANIAN ("lt", Alphabet.LATIN, "ĖėĮįŲų", false),
    MALAY      ("ms", Alphabet.LATIN, "", false),
    NORWEGIAN  ("no", Alphabet.LATIN, "", false),
    NYNORSK    ("nn", Alphabet.LATIN, "", false),
    PERSIAN    ("fa", Alphabet.ARABIC, "", false),
    POLISH     ("pl", Alphabet.LATIN, "ŁłŃńŚśŹź", false),
    PORTUGUESE ("pt", Alphabet.LATIN, "", false),
    ROMANIAN   ("ro", Alphabet.LATIN, "Țţ", false),
    RUSSIAN    ("ru", Alphabet.CYRILLIC, "", false),
    SLOVAK     ("sk", Alphabet.LATIN, "ĹĺĽľŔŕ", false),
    SLOVENE    ("sl", Alphabet.LATIN, "", false),
    SOMALI     ("so", Alphabet.LATIN, "", false),
    SPANISH    ("es", Alphabet.LATIN, "¿¡", false),
    SWEDISH    ("sv", Alphabet.LATIN, "", false),
    TAGALOG    ("tl", Alphabet.LATIN, "", false),
    TAMIL      ("ta", Alphabet.TAMIL, "", false),
    TELUGU     ("te", Alphabet.TELUGU, "", false),
    THAI       ("th", Alphabet.THAI, "", false),
    TURKISH    ("tr", Alphabet.LATIN, "İıĞğ", false),
    VIETNAMESE ("vi", Alphabet.LATIN, "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếẸẹỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỌọỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ", false),
    WELSH      ("cy", Alphabet.LATIN, "", false),

    UNKNOWN    ("<unk>", Alphabet.NONE, "", true);

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

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

enum class Language(
    val isoCode: String,
    internal val uniqueCharacters: String,
    internal val usesLatinAlphabet: Boolean,
    internal val usesCyrillicAlphabet: Boolean,
    internal val usesArabicAlphabet: Boolean,
    internal val usesChineseAlphabet: Boolean,
    internal var isExcludedFromDetection: Boolean
) {
    AFRIKAANS  ("af", "", true, false, false, false, false),
    ALBANIAN   ("sq", "Ëë", true, false, false, false, false),
    ARABIC     ("ar", "", false, false, true, false, false),
    BASQUE     ("eu", "", true, false, false, false, false),
    BELARUSIAN ("be", "", false, true, false, false, false),
    BOKMAL     ("nb", "", true, false, false, false, false),
    BULGARIAN  ("bg", "", false, true, false, false, false),
    CATALAN    ("ca", "Ïï", true, false, false, false, false),
    CHINESE    ("zh", "", false, false, false, true, false),
    CROATIAN   ("hr", "", true, false, false, false, false),
    CZECH      ("cs", "ĚěŘřŮů", true, false, false, false, false),
    DANISH     ("da", "", true, false, false, false, false),
    DUTCH      ("nl", "", true, false, false, false, false),
    ENGLISH    ("en", "", true, false, false, false, false),
    ESTONIAN   ("et", "", true, false, false, false, false),
    FINNISH    ("fi", "", true, false, false, false, false),
    FRENCH     ("fr", "", true, false, false, false, false),
    GERMAN     ("de", "ß", true, false, false, false, false),
    GREEK      ("el", "", false, false, false, false, false),
    HUNGARIAN  ("hu", "ŐőŰű", true, false, false, false, false),
    ICELANDIC  ("is", "", true, false, false, false, false),
    INDONESIAN ("id", "", true, false, false, false, false),
    IRISH      ("ga", "", true, false, false, false, false),
    ITALIAN    ("it", "", true, false, false, false, false),
    JAPANESE   ("ja", "", false, false, false, true, false),
    KOREAN     ("ko", "", false, false, false, false, false),
    LATIN      ("la", "", true, false, false, false, false),
    LATVIAN    ("lv", "ĀāĒēĢģĪīĶķĻļŅņ", true, false, false, false, false),
    LITHUANIAN ("lt", "ĖėĮįŲų", true, false, false, false, false),
    MALAY      ("ms", "", true, false, false, false, false),
    NORWEGIAN  ("no", "", true, false, false, false, false),
    NYNORSK    ("nn", "", true, false, false, false, false),
    PERSIAN    ("fa", "", false, false, true, false, false),
    POLISH     ("pl", "ŁłŃńŚśŹź", true, false, false, false, false),
    PORTUGUESE ("pt", "", true, false, false, false, false),
    ROMANIAN   ("ro", "Țţ", true, false, false, false, false),
    RUSSIAN    ("ru", "", false, true, false, false, false),
    SLOVAK     ("sk", "ĹĺĽľŔŕ", true, false, false, false, false),
    SLOVENE    ("sl", "", true, false, false, false, false),
    SOMALI     ("so", "", true, false, false, false, false),
    SPANISH    ("es", "¿¡", true, false, false, false, false),
    SWEDISH    ("sv", "", true, false, false, false, false),
    TAGALOG    ("tl", "", true, false, false, false, false),
    TAMIL      ("ta", "", false, false, false, false, false),
    THAI       ("th", "", false, false, false, false, false),
    TURKISH    ("tr", "İıĞğ", true, false, false, false, false),
    VIETNAMESE ("vi", "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếẸẹỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỌọỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ", true, false, false, false, false),
    WELSH      ("cy", "", true, false, false, false, false),

    UNKNOWN    ("<unk>", "", false, false, false, false, true);

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

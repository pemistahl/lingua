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
    val isoCode: IsoCode639_1,
    internal val alphabets: Set<Alphabet>,
    internal val uniqueCharacters: String
) {
    AFRIKAANS  (IsoCode639_1.AF, setOf(Alphabet.LATIN), ""),
    ALBANIAN   (IsoCode639_1.SQ, setOf(Alphabet.LATIN), "Ëë"),
    ARABIC     (IsoCode639_1.AR, setOf(Alphabet.ARABIC), ""),
    BASQUE     (IsoCode639_1.EU, setOf(Alphabet.LATIN), ""),
    BELARUSIAN (IsoCode639_1.BE, setOf(Alphabet.CYRILLIC), ""),
    BENGALI    (IsoCode639_1.BN, setOf(Alphabet.BENGALI), ""),
    BOKMAL     (IsoCode639_1.NB, setOf(Alphabet.LATIN), ""),
    BULGARIAN  (IsoCode639_1.BG, setOf(Alphabet.CYRILLIC), ""),
    CATALAN    (IsoCode639_1.CA, setOf(Alphabet.LATIN), "Ïï"),
    CHINESE    (IsoCode639_1.ZH, setOf(Alphabet.HAN), ""),
    CROATIAN   (IsoCode639_1.HR, setOf(Alphabet.LATIN), ""),
    CZECH      (IsoCode639_1.CS, setOf(Alphabet.LATIN), "ĚěŘřŮů"),
    DANISH     (IsoCode639_1.DA, setOf(Alphabet.LATIN), ""),
    DUTCH      (IsoCode639_1.NL, setOf(Alphabet.LATIN), ""),
    ENGLISH    (IsoCode639_1.EN, setOf(Alphabet.LATIN), ""),
    ESTONIAN   (IsoCode639_1.ET, setOf(Alphabet.LATIN), ""),
    FINNISH    (IsoCode639_1.FI, setOf(Alphabet.LATIN), ""),
    FRENCH     (IsoCode639_1.FR, setOf(Alphabet.LATIN), ""),
    GERMAN     (IsoCode639_1.DE, setOf(Alphabet.LATIN), "ß"),
    GREEK      (IsoCode639_1.EL, setOf(Alphabet.GREEK), ""),
    GUJARATI   (IsoCode639_1.GU, setOf(Alphabet.GUJARATI), ""),
    HEBREW     (IsoCode639_1.HE, setOf(Alphabet.HEBREW), ""),
    HINDI      (IsoCode639_1.HI, setOf(Alphabet.DEVANAGARI), ""),
    HUNGARIAN  (IsoCode639_1.HU, setOf(Alphabet.LATIN), "ŐőŰű"),
    ICELANDIC  (IsoCode639_1.IS, setOf(Alphabet.LATIN), ""),
    INDONESIAN (IsoCode639_1.ID, setOf(Alphabet.LATIN), ""),
    IRISH      (IsoCode639_1.GA, setOf(Alphabet.LATIN), ""),
    ITALIAN    (IsoCode639_1.IT, setOf(Alphabet.LATIN), ""),
    JAPANESE   (IsoCode639_1.JA, setOf(Alphabet.HIRAGANA, Alphabet.KATAKANA, Alphabet.HAN), ""),
    KOREAN     (IsoCode639_1.KO, setOf(Alphabet.HANGUL), ""),
    LATIN      (IsoCode639_1.LA, setOf(Alphabet.LATIN), ""),
    LATVIAN    (IsoCode639_1.LV, setOf(Alphabet.LATIN), "ĀāĒēĢģĪīĶķĻļŅņ"),
    LITHUANIAN (IsoCode639_1.LT, setOf(Alphabet.LATIN), "ĖėĮįŲų"),
    MALAY      (IsoCode639_1.MS, setOf(Alphabet.LATIN), ""),
    NORWEGIAN  (IsoCode639_1.NO, setOf(Alphabet.LATIN), ""),
    NYNORSK    (IsoCode639_1.NN, setOf(Alphabet.LATIN), ""),
    PERSIAN    (IsoCode639_1.FA, setOf(Alphabet.ARABIC), ""),
    POLISH     (IsoCode639_1.PL, setOf(Alphabet.LATIN), "ŁłŃńŚśŹź"),
    PORTUGUESE (IsoCode639_1.PT, setOf(Alphabet.LATIN), ""),
    PUNJABI    (IsoCode639_1.PA, setOf(Alphabet.GURMUKHI), ""),
    ROMANIAN   (IsoCode639_1.RO, setOf(Alphabet.LATIN), "Țţ"),
    RUSSIAN    (IsoCode639_1.RU, setOf(Alphabet.CYRILLIC), ""),
    SLOVAK     (IsoCode639_1.SK, setOf(Alphabet.LATIN), "ĹĺĽľŔŕ"),
    SLOVENE    (IsoCode639_1.SL, setOf(Alphabet.LATIN), ""),
    SOMALI     (IsoCode639_1.SO, setOf(Alphabet.LATIN), ""),
    SPANISH    (IsoCode639_1.ES, setOf(Alphabet.LATIN), "¿¡"),
    SWEDISH    (IsoCode639_1.SV, setOf(Alphabet.LATIN), ""),
    TAGALOG    (IsoCode639_1.TL, setOf(Alphabet.LATIN), ""),
    TAMIL      (IsoCode639_1.TA, setOf(Alphabet.TAMIL), ""),
    TELUGU     (IsoCode639_1.TE, setOf(Alphabet.TELUGU), ""),
    THAI       (IsoCode639_1.TH, setOf(Alphabet.THAI), ""),
    TURKISH    (IsoCode639_1.TR, setOf(Alphabet.LATIN), "İıĞğ"),
    URDU       (IsoCode639_1.UR, setOf(Alphabet.ARABIC), ""),
    VIETNAMESE (IsoCode639_1.VI, setOf(Alphabet.LATIN), "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếẸẹỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỌọỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ"),
    WELSH      (IsoCode639_1.CY, setOf(Alphabet.LATIN), ""),

    UNKNOWN    (IsoCode639_1.UNKNOWN, setOf(Alphabet.NONE), "");

    companion object {
        @JvmStatic
        fun all() = filterOutLanguages(UNKNOWN, BOKMAL, NYNORSK)

        @JvmStatic
        fun allSpokenOnes() = filterOutLanguages(UNKNOWN, BOKMAL, LATIN, NYNORSK)

        @JvmStatic
        fun getByIsoCode(isoCode: IsoCode639_1) = values().find { it.isoCode == isoCode }!!

        private fun filterOutLanguages(
            vararg languages: Language
        ) = values().filterNot { it in languages }
    }
}

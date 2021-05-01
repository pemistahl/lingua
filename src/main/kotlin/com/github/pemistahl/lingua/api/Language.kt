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

import com.github.pemistahl.lingua.api.IsoCode639_1.AF
import com.github.pemistahl.lingua.api.IsoCode639_1.AR
import com.github.pemistahl.lingua.api.IsoCode639_1.AZ
import com.github.pemistahl.lingua.api.IsoCode639_1.BE
import com.github.pemistahl.lingua.api.IsoCode639_1.BG
import com.github.pemistahl.lingua.api.IsoCode639_1.BN
import com.github.pemistahl.lingua.api.IsoCode639_1.BS
import com.github.pemistahl.lingua.api.IsoCode639_1.CA
import com.github.pemistahl.lingua.api.IsoCode639_1.CS
import com.github.pemistahl.lingua.api.IsoCode639_1.CY
import com.github.pemistahl.lingua.api.IsoCode639_1.DA
import com.github.pemistahl.lingua.api.IsoCode639_1.DE
import com.github.pemistahl.lingua.api.IsoCode639_1.EL
import com.github.pemistahl.lingua.api.IsoCode639_1.EN
import com.github.pemistahl.lingua.api.IsoCode639_1.EO
import com.github.pemistahl.lingua.api.IsoCode639_1.ES
import com.github.pemistahl.lingua.api.IsoCode639_1.ET
import com.github.pemistahl.lingua.api.IsoCode639_1.EU
import com.github.pemistahl.lingua.api.IsoCode639_1.FA
import com.github.pemistahl.lingua.api.IsoCode639_1.FI
import com.github.pemistahl.lingua.api.IsoCode639_1.FR
import com.github.pemistahl.lingua.api.IsoCode639_1.GA
import com.github.pemistahl.lingua.api.IsoCode639_1.GU
import com.github.pemistahl.lingua.api.IsoCode639_1.HE
import com.github.pemistahl.lingua.api.IsoCode639_1.HI
import com.github.pemistahl.lingua.api.IsoCode639_1.HR
import com.github.pemistahl.lingua.api.IsoCode639_1.HU
import com.github.pemistahl.lingua.api.IsoCode639_1.HY
import com.github.pemistahl.lingua.api.IsoCode639_1.ID
import com.github.pemistahl.lingua.api.IsoCode639_1.IS
import com.github.pemistahl.lingua.api.IsoCode639_1.IT
import com.github.pemistahl.lingua.api.IsoCode639_1.JA
import com.github.pemistahl.lingua.api.IsoCode639_1.KA
import com.github.pemistahl.lingua.api.IsoCode639_1.KK
import com.github.pemistahl.lingua.api.IsoCode639_1.KO
import com.github.pemistahl.lingua.api.IsoCode639_1.LA
import com.github.pemistahl.lingua.api.IsoCode639_1.LG
import com.github.pemistahl.lingua.api.IsoCode639_1.LT
import com.github.pemistahl.lingua.api.IsoCode639_1.LV
import com.github.pemistahl.lingua.api.IsoCode639_1.MK
import com.github.pemistahl.lingua.api.IsoCode639_1.MN
import com.github.pemistahl.lingua.api.IsoCode639_1.MR
import com.github.pemistahl.lingua.api.IsoCode639_1.MS
import com.github.pemistahl.lingua.api.IsoCode639_1.NB
import com.github.pemistahl.lingua.api.IsoCode639_1.NL
import com.github.pemistahl.lingua.api.IsoCode639_1.NN
import com.github.pemistahl.lingua.api.IsoCode639_1.PA
import com.github.pemistahl.lingua.api.IsoCode639_1.PL
import com.github.pemistahl.lingua.api.IsoCode639_1.PT
import com.github.pemistahl.lingua.api.IsoCode639_1.RO
import com.github.pemistahl.lingua.api.IsoCode639_1.RU
import com.github.pemistahl.lingua.api.IsoCode639_1.SK
import com.github.pemistahl.lingua.api.IsoCode639_1.SL
import com.github.pemistahl.lingua.api.IsoCode639_1.SN
import com.github.pemistahl.lingua.api.IsoCode639_1.SO
import com.github.pemistahl.lingua.api.IsoCode639_1.SQ
import com.github.pemistahl.lingua.api.IsoCode639_1.SR
import com.github.pemistahl.lingua.api.IsoCode639_1.ST
import com.github.pemistahl.lingua.api.IsoCode639_1.SV
import com.github.pemistahl.lingua.api.IsoCode639_1.SW
import com.github.pemistahl.lingua.api.IsoCode639_1.TA
import com.github.pemistahl.lingua.api.IsoCode639_1.TE
import com.github.pemistahl.lingua.api.IsoCode639_1.TH
import com.github.pemistahl.lingua.api.IsoCode639_1.TL
import com.github.pemistahl.lingua.api.IsoCode639_1.TN
import com.github.pemistahl.lingua.api.IsoCode639_1.TR
import com.github.pemistahl.lingua.api.IsoCode639_1.TS
import com.github.pemistahl.lingua.api.IsoCode639_1.UK
import com.github.pemistahl.lingua.api.IsoCode639_1.UR
import com.github.pemistahl.lingua.api.IsoCode639_1.VI
import com.github.pemistahl.lingua.api.IsoCode639_1.XH
import com.github.pemistahl.lingua.api.IsoCode639_1.YO
import com.github.pemistahl.lingua.api.IsoCode639_1.ZH
import com.github.pemistahl.lingua.api.IsoCode639_1.ZU
import com.github.pemistahl.lingua.api.IsoCode639_3.AFR
import com.github.pemistahl.lingua.api.IsoCode639_3.ARA
import com.github.pemistahl.lingua.api.IsoCode639_3.AZE
import com.github.pemistahl.lingua.api.IsoCode639_3.BEL
import com.github.pemistahl.lingua.api.IsoCode639_3.BEN
import com.github.pemistahl.lingua.api.IsoCode639_3.BOS
import com.github.pemistahl.lingua.api.IsoCode639_3.BUL
import com.github.pemistahl.lingua.api.IsoCode639_3.CAT
import com.github.pemistahl.lingua.api.IsoCode639_3.CES
import com.github.pemistahl.lingua.api.IsoCode639_3.CYM
import com.github.pemistahl.lingua.api.IsoCode639_3.DAN
import com.github.pemistahl.lingua.api.IsoCode639_3.DEU
import com.github.pemistahl.lingua.api.IsoCode639_3.ELL
import com.github.pemistahl.lingua.api.IsoCode639_3.ENG
import com.github.pemistahl.lingua.api.IsoCode639_3.EPO
import com.github.pemistahl.lingua.api.IsoCode639_3.EST
import com.github.pemistahl.lingua.api.IsoCode639_3.EUS
import com.github.pemistahl.lingua.api.IsoCode639_3.FAS
import com.github.pemistahl.lingua.api.IsoCode639_3.FIN
import com.github.pemistahl.lingua.api.IsoCode639_3.FRA
import com.github.pemistahl.lingua.api.IsoCode639_3.GLE
import com.github.pemistahl.lingua.api.IsoCode639_3.GUJ
import com.github.pemistahl.lingua.api.IsoCode639_3.HEB
import com.github.pemistahl.lingua.api.IsoCode639_3.HIN
import com.github.pemistahl.lingua.api.IsoCode639_3.HRV
import com.github.pemistahl.lingua.api.IsoCode639_3.HUN
import com.github.pemistahl.lingua.api.IsoCode639_3.HYE
import com.github.pemistahl.lingua.api.IsoCode639_3.IND
import com.github.pemistahl.lingua.api.IsoCode639_3.ISL
import com.github.pemistahl.lingua.api.IsoCode639_3.ITA
import com.github.pemistahl.lingua.api.IsoCode639_3.JPN
import com.github.pemistahl.lingua.api.IsoCode639_3.KAT
import com.github.pemistahl.lingua.api.IsoCode639_3.KAZ
import com.github.pemistahl.lingua.api.IsoCode639_3.KOR
import com.github.pemistahl.lingua.api.IsoCode639_3.LAT
import com.github.pemistahl.lingua.api.IsoCode639_3.LAV
import com.github.pemistahl.lingua.api.IsoCode639_3.LIT
import com.github.pemistahl.lingua.api.IsoCode639_3.LUG
import com.github.pemistahl.lingua.api.IsoCode639_3.MAR
import com.github.pemistahl.lingua.api.IsoCode639_3.MKD
import com.github.pemistahl.lingua.api.IsoCode639_3.MON
import com.github.pemistahl.lingua.api.IsoCode639_3.MSA
import com.github.pemistahl.lingua.api.IsoCode639_3.NLD
import com.github.pemistahl.lingua.api.IsoCode639_3.NNO
import com.github.pemistahl.lingua.api.IsoCode639_3.NOB
import com.github.pemistahl.lingua.api.IsoCode639_3.PAN
import com.github.pemistahl.lingua.api.IsoCode639_3.POL
import com.github.pemistahl.lingua.api.IsoCode639_3.POR
import com.github.pemistahl.lingua.api.IsoCode639_3.RON
import com.github.pemistahl.lingua.api.IsoCode639_3.RUS
import com.github.pemistahl.lingua.api.IsoCode639_3.SLK
import com.github.pemistahl.lingua.api.IsoCode639_3.SLV
import com.github.pemistahl.lingua.api.IsoCode639_3.SNA
import com.github.pemistahl.lingua.api.IsoCode639_3.SOM
import com.github.pemistahl.lingua.api.IsoCode639_3.SOT
import com.github.pemistahl.lingua.api.IsoCode639_3.SPA
import com.github.pemistahl.lingua.api.IsoCode639_3.SQI
import com.github.pemistahl.lingua.api.IsoCode639_3.SRP
import com.github.pemistahl.lingua.api.IsoCode639_3.SWA
import com.github.pemistahl.lingua.api.IsoCode639_3.SWE
import com.github.pemistahl.lingua.api.IsoCode639_3.TAM
import com.github.pemistahl.lingua.api.IsoCode639_3.TEL
import com.github.pemistahl.lingua.api.IsoCode639_3.TGL
import com.github.pemistahl.lingua.api.IsoCode639_3.THA
import com.github.pemistahl.lingua.api.IsoCode639_3.TSN
import com.github.pemistahl.lingua.api.IsoCode639_3.TSO
import com.github.pemistahl.lingua.api.IsoCode639_3.TUR
import com.github.pemistahl.lingua.api.IsoCode639_3.UKR
import com.github.pemistahl.lingua.api.IsoCode639_3.URD
import com.github.pemistahl.lingua.api.IsoCode639_3.VIE
import com.github.pemistahl.lingua.api.IsoCode639_3.XHO
import com.github.pemistahl.lingua.api.IsoCode639_3.YOR
import com.github.pemistahl.lingua.api.IsoCode639_3.ZHO
import com.github.pemistahl.lingua.api.IsoCode639_3.ZUL
import com.github.pemistahl.lingua.internal.Alphabet
import com.github.pemistahl.lingua.internal.Alphabet.CYRILLIC
import com.github.pemistahl.lingua.internal.Alphabet.DEVANAGARI
import com.github.pemistahl.lingua.internal.Alphabet.GURMUKHI
import com.github.pemistahl.lingua.internal.Alphabet.HAN
import com.github.pemistahl.lingua.internal.Alphabet.HANGUL
import com.github.pemistahl.lingua.internal.Alphabet.HIRAGANA
import com.github.pemistahl.lingua.internal.Alphabet.KATAKANA
import com.github.pemistahl.lingua.internal.Alphabet.NONE

/**
 * The supported detectable languages.
 */
enum class Language(
    val isoCode639_1: IsoCode639_1,
    val isoCode639_3: IsoCode639_3,
    internal val alphabets: Set<Alphabet>,
    internal val uniqueCharacters: String?
) {
    AFRIKAANS(AF, AFR, setOf(Alphabet.LATIN), null),
    ALBANIAN(SQ, SQI, setOf(Alphabet.LATIN), null),
    ARABIC(AR, ARA, setOf(Alphabet.ARABIC), null),
    ARMENIAN(HY, HYE, setOf(Alphabet.ARMENIAN), null),
    AZERBAIJANI(AZ, AZE, setOf(Alphabet.LATIN), "Əə"),
    BASQUE(EU, EUS, setOf(Alphabet.LATIN), null),
    BELARUSIAN(BE, BEL, setOf(CYRILLIC), null),
    BENGALI(BN, BEN, setOf(Alphabet.BENGALI), null),
    BOKMAL(NB, NOB, setOf(Alphabet.LATIN), null),
    BOSNIAN(BS, BOS, setOf(Alphabet.LATIN), null),
    BULGARIAN(BG, BUL, setOf(CYRILLIC), null),
    CATALAN(CA, CAT, setOf(Alphabet.LATIN), "Ïï"),
    CHINESE(ZH, ZHO, setOf(HAN), null),
    CROATIAN(HR, HRV, setOf(Alphabet.LATIN), null),
    CZECH(CS, CES, setOf(Alphabet.LATIN), "ĚěŘřŮů"),
    DANISH(DA, DAN, setOf(Alphabet.LATIN), null),
    DUTCH(NL, NLD, setOf(Alphabet.LATIN), null),
    ENGLISH(EN, ENG, setOf(Alphabet.LATIN), null),
    ESPERANTO(EO, EPO, setOf(Alphabet.LATIN), "ĈĉĜĝĤĥĴĵŜŝŬŭ"),
    ESTONIAN(ET, EST, setOf(Alphabet.LATIN), null),
    FINNISH(FI, FIN, setOf(Alphabet.LATIN), null),
    FRENCH(FR, FRA, setOf(Alphabet.LATIN), null),
    GANDA(LG, LUG, setOf(Alphabet.LATIN), null),
    GEORGIAN(KA, KAT, setOf(Alphabet.GEORGIAN), null),
    GERMAN(DE, DEU, setOf(Alphabet.LATIN), "ß"),
    GREEK(EL, ELL, setOf(Alphabet.GREEK), null),
    GUJARATI(GU, GUJ, setOf(Alphabet.GUJARATI), null),
    HEBREW(HE, HEB, setOf(Alphabet.HEBREW), null),
    HINDI(HI, HIN, setOf(DEVANAGARI), null),
    HUNGARIAN(HU, HUN, setOf(Alphabet.LATIN), "ŐőŰű"),
    ICELANDIC(IS, ISL, setOf(Alphabet.LATIN), null),
    INDONESIAN(ID, IND, setOf(Alphabet.LATIN), null),
    IRISH(GA, GLE, setOf(Alphabet.LATIN), null),
    ITALIAN(IT, ITA, setOf(Alphabet.LATIN), null),
    JAPANESE(JA, JPN, setOf(HIRAGANA, KATAKANA, HAN), null),
    KAZAKH(KK, KAZ, setOf(CYRILLIC), "ӘәҒғҚқҢңҰұ"),
    KOREAN(KO, KOR, setOf(HANGUL), null),
    LATIN(LA, LAT, setOf(Alphabet.LATIN), null),
    LATVIAN(LV, LAV, setOf(Alphabet.LATIN), "ĢģĶķĻļŅņ"),
    LITHUANIAN(LT, LIT, setOf(Alphabet.LATIN), "ĖėĮįŲų"),
    MACEDONIAN(MK, MKD, setOf(CYRILLIC), "ЃѓЅѕЌќЏџ"),
    MALAY(MS, MSA, setOf(Alphabet.LATIN), null),
    MARATHI(MR, MAR, setOf(DEVANAGARI), "ळ"),
    MONGOLIAN(MN, MON, setOf(CYRILLIC), "ӨөҮү"),
    NYNORSK(NN, NNO, setOf(Alphabet.LATIN), null),
    PERSIAN(FA, FAS, setOf(Alphabet.ARABIC), null),
    POLISH(PL, POL, setOf(Alphabet.LATIN), "ŁłŃńŚśŹź"),
    PORTUGUESE(PT, POR, setOf(Alphabet.LATIN), null),
    PUNJABI(PA, PAN, setOf(GURMUKHI), null),
    ROMANIAN(RO, RON, setOf(Alphabet.LATIN), "Țţ"),
    RUSSIAN(RU, RUS, setOf(CYRILLIC), null),
    SERBIAN(SR, SRP, setOf(CYRILLIC), "ЂђЋћ"),
    SHONA(SN, SNA, setOf(Alphabet.LATIN), null),
    SLOVAK(SK, SLK, setOf(Alphabet.LATIN), "ĹĺĽľŔŕ"),
    SLOVENE(SL, SLV, setOf(Alphabet.LATIN), null),
    SOMALI(SO, SOM, setOf(Alphabet.LATIN), null),
    SOTHO(ST, SOT, setOf(Alphabet.LATIN), null),
    SPANISH(ES, SPA, setOf(Alphabet.LATIN), "¿¡"),
    SWAHILI(SW, SWA, setOf(Alphabet.LATIN), null),
    SWEDISH(SV, SWE, setOf(Alphabet.LATIN), null),
    TAGALOG(TL, TGL, setOf(Alphabet.LATIN), null),
    TAMIL(TA, TAM, setOf(Alphabet.TAMIL), null),
    TELUGU(TE, TEL, setOf(Alphabet.TELUGU), null),
    THAI(TH, THA, setOf(Alphabet.THAI), null),
    TSONGA(TS, TSO, setOf(Alphabet.LATIN), null),
    TSWANA(TN, TSN, setOf(Alphabet.LATIN), null),
    TURKISH(TR, TUR, setOf(Alphabet.LATIN), null),
    UKRAINIAN(UK, UKR, setOf(CYRILLIC), "ҐґЄєЇї"),
    URDU(UR, URD, setOf(Alphabet.ARABIC), null),
    VIETNAMESE(
        VI,
        VIE,
        setOf(Alphabet.LATIN),
        "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ"
    ),
    WELSH(CY, CYM, setOf(Alphabet.LATIN), null),
    XHOSA(XH, XHO, setOf(Alphabet.LATIN), null),
    // TODO for YORUBA: "E̩e̩Ẹ́ẹ́É̩é̩Ẹ̀ẹ̀È̩è̩Ẹ̄ẹ̄Ē̩ē̩ŌōO̩o̩Ọ́ọ́Ó̩ó̩Ọ̀ọ̀Ò̩ò̩Ọ̄ọ̄Ō̩ō̩ṢṣS̩s̩"
    YORUBA(YO, YOR, setOf(Alphabet.LATIN), "ŌōṢṣ"),
    ZULU(ZU, ZUL, setOf(Alphabet.LATIN), null),

    /**
     * The imaginary unknown language.
     *
     * This value is returned if no language can be detected reliably.
     */
    UNKNOWN(IsoCode639_1.NONE, IsoCode639_3.NONE, setOf(NONE), null);

    companion object {
        /**
         * Returns a list of all built-in languages.
         */
        @JvmStatic
        fun all() = filterOutLanguages(UNKNOWN)

        /**
         * Returns a list of all built-in languages that are still spoken today.
         */
        @JvmStatic
        fun allSpokenOnes() = filterOutLanguages(UNKNOWN, LATIN)

        /**
         * Returns a list of all built-in languages supporting the Arabic script.
         */
        @JvmStatic
        fun allWithArabicScript() = values().filter { it.alphabets.contains(Alphabet.ARABIC) }

        /**
         * Returns a list of all built-in languages supporting the Cyrillic script.
         */
        @JvmStatic
        fun allWithCyrillicScript() = values().filter { it.alphabets.contains(CYRILLIC) }

        /**
         * Returns a list of all built-in languages supporting the Devanagari script.
         */
        @JvmStatic
        fun allWithDevanagariScript() = values().filter { it.alphabets.contains(DEVANAGARI) }

        /**
         * Returns a list of all built-in languages supporting the Latin script.
         */
        @JvmStatic
        fun allWithLatinScript() = values().filter { it.alphabets.contains(Alphabet.LATIN) }

        /**
         * Returns the language for the given ISO 639-1 code.
         */
        @JvmStatic
        fun getByIsoCode639_1(isoCode: IsoCode639_1) = values().find { it.isoCode639_1 == isoCode }!!

        /**
         * Returns the language for the given ISO 639-3 code.
         */
        @JvmStatic
        fun getByIsoCode639_3(isoCode: IsoCode639_3) = values().find { it.isoCode639_3 == isoCode }!!

        private fun filterOutLanguages(vararg languages: Language) = values().filterNot { it in languages }
    }
}

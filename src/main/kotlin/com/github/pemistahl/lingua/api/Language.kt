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
import com.github.pemistahl.lingua.api.IsoCode639_1.MI
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
import com.github.pemistahl.lingua.api.IsoCode639_3.MRI
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
import com.github.pemistahl.lingua.internal.Alphabet.LATIN
import com.github.pemistahl.lingua.internal.Alphabet.NONE
import java.util.EnumSet

/**
 * The supported detectable languages.
 */
enum class Language(
    val isoCode639_1: IsoCode639_1,
    val isoCode639_3: IsoCode639_3,
    internal val alphabets: Set<Alphabet>,
    internal val uniqueCharacters: String?
) {
    AFRIKAANS(AF, AFR, EnumSet.of(Alphabet.LATIN), null),
    ALBANIAN(SQ, SQI, EnumSet.of(Alphabet.LATIN), null),
    ARABIC(AR, ARA, EnumSet.of(Alphabet.ARABIC), null),
    ARMENIAN(HY, HYE, EnumSet.of(Alphabet.ARMENIAN), null),
    AZERBAIJANI(AZ, AZE, EnumSet.of(Alphabet.LATIN), "Əə"),
    BASQUE(EU, EUS, EnumSet.of(Alphabet.LATIN), null),
    BELARUSIAN(BE, BEL, EnumSet.of(CYRILLIC), null),
    BENGALI(BN, BEN, EnumSet.of(Alphabet.BENGALI), null),
    BOKMAL(NB, NOB, EnumSet.of(Alphabet.LATIN), null),
    BOSNIAN(BS, BOS, EnumSet.of(Alphabet.LATIN), null),
    BULGARIAN(BG, BUL, EnumSet.of(CYRILLIC), null),
    CATALAN(CA, CAT, EnumSet.of(Alphabet.LATIN), "Ïï"),
    CHINESE(ZH, ZHO, EnumSet.of(HAN), null),
    CROATIAN(HR, HRV, EnumSet.of(Alphabet.LATIN), null),
    CZECH(CS, CES, EnumSet.of(Alphabet.LATIN), "ĚěŘřŮů"),
    DANISH(DA, DAN, EnumSet.of(Alphabet.LATIN), null),
    DUTCH(NL, NLD, EnumSet.of(Alphabet.LATIN), null),
    ENGLISH(EN, ENG, EnumSet.of(Alphabet.LATIN), null),
    ESPERANTO(EO, EPO, EnumSet.of(Alphabet.LATIN), "ĈĉĜĝĤĥĴĵŜŝŬŭ"),
    ESTONIAN(ET, EST, EnumSet.of(Alphabet.LATIN), null),
    FINNISH(FI, FIN, EnumSet.of(Alphabet.LATIN), null),
    FRENCH(FR, FRA, EnumSet.of(Alphabet.LATIN), null),
    GANDA(LG, LUG, EnumSet.of(Alphabet.LATIN), null),
    GEORGIAN(KA, KAT, EnumSet.of(Alphabet.GEORGIAN), null),
    GERMAN(DE, DEU, EnumSet.of(Alphabet.LATIN), "ß"),
    GREEK(EL, ELL, EnumSet.of(Alphabet.GREEK), null),
    GUJARATI(GU, GUJ, EnumSet.of(Alphabet.GUJARATI), null),
    HEBREW(HE, HEB, EnumSet.of(Alphabet.HEBREW), null),
    HINDI(HI, HIN, EnumSet.of(DEVANAGARI), null),
    HUNGARIAN(HU, HUN, EnumSet.of(Alphabet.LATIN), "ŐőŰű"),
    ICELANDIC(IS, ISL, EnumSet.of(Alphabet.LATIN), null),
    INDONESIAN(ID, IND, EnumSet.of(Alphabet.LATIN), null),
    IRISH(GA, GLE, EnumSet.of(Alphabet.LATIN), null),
    ITALIAN(IT, ITA, EnumSet.of(Alphabet.LATIN), null),
    JAPANESE(JA, JPN, EnumSet.of(HIRAGANA, KATAKANA, HAN), null),
    KAZAKH(KK, KAZ, EnumSet.of(CYRILLIC), "ӘәҒғҚқҢңҰұ"),
    KOREAN(KO, KOR, EnumSet.of(HANGUL), null),
    LATIN(LA, LAT, EnumSet.of(Alphabet.LATIN), null),
    LATVIAN(LV, LAV, EnumSet.of(Alphabet.LATIN), "ĢģĶķĻļŅņ"),
    LITHUANIAN(LT, LIT, EnumSet.of(Alphabet.LATIN), "ĖėĮįŲų"),
    MACEDONIAN(MK, MKD, EnumSet.of(CYRILLIC), "ЃѓЅѕЌќЏџ"),
    MALAY(MS, MSA, EnumSet.of(Alphabet.LATIN), null),
    MAORI(MI, MRI, EnumSet.of(Alphabet.LATIN), null),
    MARATHI(MR, MAR, EnumSet.of(DEVANAGARI), "ळ"),
    MONGOLIAN(MN, MON, EnumSet.of(CYRILLIC), "ӨөҮү"),
    NYNORSK(NN, NNO, EnumSet.of(Alphabet.LATIN), null),
    PERSIAN(FA, FAS, EnumSet.of(Alphabet.ARABIC), null),
    POLISH(PL, POL, EnumSet.of(Alphabet.LATIN), "ŁłŃńŚśŹź"),
    PORTUGUESE(PT, POR, EnumSet.of(Alphabet.LATIN), null),
    PUNJABI(PA, PAN, EnumSet.of(GURMUKHI), null),
    ROMANIAN(RO, RON, EnumSet.of(Alphabet.LATIN), "Țţ"),
    RUSSIAN(RU, RUS, EnumSet.of(CYRILLIC), null),
    SERBIAN(SR, SRP, EnumSet.of(CYRILLIC), "ЂђЋћ"),
    SHONA(SN, SNA, EnumSet.of(Alphabet.LATIN), null),
    SLOVAK(SK, SLK, EnumSet.of(Alphabet.LATIN), "ĹĺĽľŔŕ"),
    SLOVENE(SL, SLV, EnumSet.of(Alphabet.LATIN), null),
    SOMALI(SO, SOM, EnumSet.of(Alphabet.LATIN), null),
    SOTHO(ST, SOT, EnumSet.of(Alphabet.LATIN), null),
    SPANISH(ES, SPA, EnumSet.of(Alphabet.LATIN), "¿¡"),
    SWAHILI(SW, SWA, EnumSet.of(Alphabet.LATIN), null),
    SWEDISH(SV, SWE, EnumSet.of(Alphabet.LATIN), null),
    TAGALOG(TL, TGL, EnumSet.of(Alphabet.LATIN), null),
    TAMIL(TA, TAM, EnumSet.of(Alphabet.TAMIL), null),
    TELUGU(TE, TEL, EnumSet.of(Alphabet.TELUGU), null),
    THAI(TH, THA, EnumSet.of(Alphabet.THAI), null),
    TSONGA(TS, TSO, EnumSet.of(Alphabet.LATIN), null),
    TSWANA(TN, TSN, EnumSet.of(Alphabet.LATIN), null),
    TURKISH(TR, TUR, EnumSet.of(Alphabet.LATIN), null),
    UKRAINIAN(UK, UKR, EnumSet.of(CYRILLIC), "ҐґЄєЇї"),
    URDU(UR, URD, EnumSet.of(Alphabet.ARABIC), null),
    VIETNAMESE(
        VI,
        VIE,
        EnumSet.of(Alphabet.LATIN),
        "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ"
    ),
    WELSH(CY, CYM, EnumSet.of(Alphabet.LATIN), null),
    XHOSA(XH, XHO, EnumSet.of(Alphabet.LATIN), null),
    // TODO for YORUBA: "E̩e̩Ẹ́ẹ́É̩é̩Ẹ̀ẹ̀È̩è̩Ẹ̄ẹ̄Ē̩ē̩ŌōO̩o̩Ọ́ọ́Ó̩ó̩Ọ̀ọ̀Ò̩ò̩Ọ̄ọ̄Ō̩ō̩ṢṣS̩s̩"
    YORUBA(YO, YOR, EnumSet.of(Alphabet.LATIN), "Ṣṣ"),
    ZULU(ZU, ZUL, EnumSet.of(Alphabet.LATIN), null),

    /**
     * The imaginary unknown language.
     *
     * This value is returned if no language can be detected reliably.
     */
    UNKNOWN(IsoCode639_1.NONE, IsoCode639_3.NONE, EnumSet.of(NONE), null);

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
        fun getByIsoCode639_1(isoCode: IsoCode639_1) = values().first { it.isoCode639_1 == isoCode }

        /**
         * Returns the language for the given ISO 639-3 code.
         */
        @JvmStatic
        fun getByIsoCode639_3(isoCode: IsoCode639_3) = values().first { it.isoCode639_3 == isoCode }

        private fun filterOutLanguages(vararg languages: Language) = values().filterNot { it in languages }
    }
}

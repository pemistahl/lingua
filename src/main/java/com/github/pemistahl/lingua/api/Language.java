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

package com.github.pemistahl.lingua.api;

import static com.github.pemistahl.lingua.api.IsoCode639_1.AF;
import static com.github.pemistahl.lingua.api.IsoCode639_1.AM;
import static com.github.pemistahl.lingua.api.IsoCode639_1.AR;
import static com.github.pemistahl.lingua.api.IsoCode639_1.AZ;
import static com.github.pemistahl.lingua.api.IsoCode639_1.BE;
import static com.github.pemistahl.lingua.api.IsoCode639_1.BG;
import static com.github.pemistahl.lingua.api.IsoCode639_1.BN;
import static com.github.pemistahl.lingua.api.IsoCode639_1.BS;
import static com.github.pemistahl.lingua.api.IsoCode639_1.CA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.CS;
import static com.github.pemistahl.lingua.api.IsoCode639_1.CY;
import static com.github.pemistahl.lingua.api.IsoCode639_1.DA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.DE;
import static com.github.pemistahl.lingua.api.IsoCode639_1.EL;
import static com.github.pemistahl.lingua.api.IsoCode639_1.EN;
import static com.github.pemistahl.lingua.api.IsoCode639_1.EO;
import static com.github.pemistahl.lingua.api.IsoCode639_1.ES;
import static com.github.pemistahl.lingua.api.IsoCode639_1.ET;
import static com.github.pemistahl.lingua.api.IsoCode639_1.EU;
import static com.github.pemistahl.lingua.api.IsoCode639_1.FA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.FI;
import static com.github.pemistahl.lingua.api.IsoCode639_1.FR;
import static com.github.pemistahl.lingua.api.IsoCode639_1.GA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.GU;
import static com.github.pemistahl.lingua.api.IsoCode639_1.HE;
import static com.github.pemistahl.lingua.api.IsoCode639_1.HI;
import static com.github.pemistahl.lingua.api.IsoCode639_1.HR;
import static com.github.pemistahl.lingua.api.IsoCode639_1.HU;
import static com.github.pemistahl.lingua.api.IsoCode639_1.HY;
import static com.github.pemistahl.lingua.api.IsoCode639_1.ID;
import static com.github.pemistahl.lingua.api.IsoCode639_1.IS;
import static com.github.pemistahl.lingua.api.IsoCode639_1.IT;
import static com.github.pemistahl.lingua.api.IsoCode639_1.JA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.KA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.KK;
import static com.github.pemistahl.lingua.api.IsoCode639_1.KO;
import static com.github.pemistahl.lingua.api.IsoCode639_1.LA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.LG;
import static com.github.pemistahl.lingua.api.IsoCode639_1.LT;
import static com.github.pemistahl.lingua.api.IsoCode639_1.LV;
import static com.github.pemistahl.lingua.api.IsoCode639_1.MI;
import static com.github.pemistahl.lingua.api.IsoCode639_1.MK;
import static com.github.pemistahl.lingua.api.IsoCode639_1.MN;
import static com.github.pemistahl.lingua.api.IsoCode639_1.MR;
import static com.github.pemistahl.lingua.api.IsoCode639_1.MS;
import static com.github.pemistahl.lingua.api.IsoCode639_1.NB;
import static com.github.pemistahl.lingua.api.IsoCode639_1.NL;
import static com.github.pemistahl.lingua.api.IsoCode639_1.NN;
import static com.github.pemistahl.lingua.api.IsoCode639_1.OM;
import static com.github.pemistahl.lingua.api.IsoCode639_1.PA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.PL;
import static com.github.pemistahl.lingua.api.IsoCode639_1.PT;
import static com.github.pemistahl.lingua.api.IsoCode639_1.RO;
import static com.github.pemistahl.lingua.api.IsoCode639_1.RU;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SI;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SK;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SL;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SN;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SO;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SQ;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SR;
import static com.github.pemistahl.lingua.api.IsoCode639_1.ST;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SV;
import static com.github.pemistahl.lingua.api.IsoCode639_1.SW;
import static com.github.pemistahl.lingua.api.IsoCode639_1.TA;
import static com.github.pemistahl.lingua.api.IsoCode639_1.TE;
import static com.github.pemistahl.lingua.api.IsoCode639_1.TH;
import static com.github.pemistahl.lingua.api.IsoCode639_1.TI;
import static com.github.pemistahl.lingua.api.IsoCode639_1.TL;
import static com.github.pemistahl.lingua.api.IsoCode639_1.TN;
import static com.github.pemistahl.lingua.api.IsoCode639_1.TR;
import static com.github.pemistahl.lingua.api.IsoCode639_1.TS;
import static com.github.pemistahl.lingua.api.IsoCode639_1.UK;
import static com.github.pemistahl.lingua.api.IsoCode639_1.UR;
import static com.github.pemistahl.lingua.api.IsoCode639_1.VI;
import static com.github.pemistahl.lingua.api.IsoCode639_1.XH;
import static com.github.pemistahl.lingua.api.IsoCode639_1.YO;
import static com.github.pemistahl.lingua.api.IsoCode639_1.ZH;
import static com.github.pemistahl.lingua.api.IsoCode639_1.ZU;
import static com.github.pemistahl.lingua.api.IsoCode639_3.AFR;
import static com.github.pemistahl.lingua.api.IsoCode639_3.AMH;
import static com.github.pemistahl.lingua.api.IsoCode639_3.ARA;
import static com.github.pemistahl.lingua.api.IsoCode639_3.AZE;
import static com.github.pemistahl.lingua.api.IsoCode639_3.BEL;
import static com.github.pemistahl.lingua.api.IsoCode639_3.BEN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.BOS;
import static com.github.pemistahl.lingua.api.IsoCode639_3.BUL;
import static com.github.pemistahl.lingua.api.IsoCode639_3.CAT;
import static com.github.pemistahl.lingua.api.IsoCode639_3.CES;
import static com.github.pemistahl.lingua.api.IsoCode639_3.CYM;
import static com.github.pemistahl.lingua.api.IsoCode639_3.DAN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.DEU;
import static com.github.pemistahl.lingua.api.IsoCode639_3.ELL;
import static com.github.pemistahl.lingua.api.IsoCode639_3.ENG;
import static com.github.pemistahl.lingua.api.IsoCode639_3.EPO;
import static com.github.pemistahl.lingua.api.IsoCode639_3.EST;
import static com.github.pemistahl.lingua.api.IsoCode639_3.EUS;
import static com.github.pemistahl.lingua.api.IsoCode639_3.FAS;
import static com.github.pemistahl.lingua.api.IsoCode639_3.FIN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.FRA;
import static com.github.pemistahl.lingua.api.IsoCode639_3.GLE;
import static com.github.pemistahl.lingua.api.IsoCode639_3.GUJ;
import static com.github.pemistahl.lingua.api.IsoCode639_3.HEB;
import static com.github.pemistahl.lingua.api.IsoCode639_3.HIN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.HRV;
import static com.github.pemistahl.lingua.api.IsoCode639_3.HUN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.HYE;
import static com.github.pemistahl.lingua.api.IsoCode639_3.IND;
import static com.github.pemistahl.lingua.api.IsoCode639_3.ISL;
import static com.github.pemistahl.lingua.api.IsoCode639_3.ITA;
import static com.github.pemistahl.lingua.api.IsoCode639_3.JPN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.KAT;
import static com.github.pemistahl.lingua.api.IsoCode639_3.KAZ;
import static com.github.pemistahl.lingua.api.IsoCode639_3.KOR;
import static com.github.pemistahl.lingua.api.IsoCode639_3.LAT;
import static com.github.pemistahl.lingua.api.IsoCode639_3.LAV;
import static com.github.pemistahl.lingua.api.IsoCode639_3.LIT;
import static com.github.pemistahl.lingua.api.IsoCode639_3.LUG;
import static com.github.pemistahl.lingua.api.IsoCode639_3.MAR;
import static com.github.pemistahl.lingua.api.IsoCode639_3.MKD;
import static com.github.pemistahl.lingua.api.IsoCode639_3.MON;
import static com.github.pemistahl.lingua.api.IsoCode639_3.MRI;
import static com.github.pemistahl.lingua.api.IsoCode639_3.MSA;
import static com.github.pemistahl.lingua.api.IsoCode639_3.NLD;
import static com.github.pemistahl.lingua.api.IsoCode639_3.NNO;
import static com.github.pemistahl.lingua.api.IsoCode639_3.NOB;
import static com.github.pemistahl.lingua.api.IsoCode639_3.ORM;
import static com.github.pemistahl.lingua.api.IsoCode639_3.PAN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.POL;
import static com.github.pemistahl.lingua.api.IsoCode639_3.POR;
import static com.github.pemistahl.lingua.api.IsoCode639_3.RON;
import static com.github.pemistahl.lingua.api.IsoCode639_3.RUS;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SIN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SLK;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SLV;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SNA;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SOM;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SOT;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SPA;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SQI;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SRP;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SWA;
import static com.github.pemistahl.lingua.api.IsoCode639_3.SWE;
import static com.github.pemistahl.lingua.api.IsoCode639_3.TAM;
import static com.github.pemistahl.lingua.api.IsoCode639_3.TEL;
import static com.github.pemistahl.lingua.api.IsoCode639_3.TGL;
import static com.github.pemistahl.lingua.api.IsoCode639_3.THA;
import static com.github.pemistahl.lingua.api.IsoCode639_3.TIR;
import static com.github.pemistahl.lingua.api.IsoCode639_3.TSN;
import static com.github.pemistahl.lingua.api.IsoCode639_3.TSO;
import static com.github.pemistahl.lingua.api.IsoCode639_3.TUR;
import static com.github.pemistahl.lingua.api.IsoCode639_3.UKR;
import static com.github.pemistahl.lingua.api.IsoCode639_3.URD;
import static com.github.pemistahl.lingua.api.IsoCode639_3.VIE;
import static com.github.pemistahl.lingua.api.IsoCode639_3.XHO;
import static com.github.pemistahl.lingua.api.IsoCode639_3.YOR;
import static com.github.pemistahl.lingua.api.IsoCode639_3.ZHO;
import static com.github.pemistahl.lingua.api.IsoCode639_3.ZUL;
import static com.github.pemistahl.lingua.internal.Alphabet.CYRILLIC;
import static com.github.pemistahl.lingua.internal.Alphabet.DEVANAGARI;
import static com.github.pemistahl.lingua.internal.Alphabet.GURMUKHI;
import static com.github.pemistahl.lingua.internal.Alphabet.HAN;
import static com.github.pemistahl.lingua.internal.Alphabet.HANGUL;
import static com.github.pemistahl.lingua.internal.Alphabet.HIRAGANA;
import static com.github.pemistahl.lingua.internal.Alphabet.KATAKANA;
import static com.github.pemistahl.lingua.internal.Alphabet.NONE;
import static com.github.pemistahl.lingua.internal.util.extension.EnumExtensions.enumSetOf;

import com.github.pemistahl.lingua.internal.Alphabet;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** The supported detectable languages. */
public enum Language {
  AFRIKAANS(AF, AFR, enumSetOf(Alphabet.LATIN), null),
  ALBANIAN(SQ, SQI, enumSetOf(Alphabet.LATIN), null),
  AMHARIC(AM, AMH, enumSetOf(Alphabet.ETHIOPIC), null),
  ARABIC(AR, ARA, enumSetOf(Alphabet.ARABIC), null),
  ARMENIAN(HY, HYE, enumSetOf(Alphabet.ARMENIAN), null),
  AZERBAIJANI(AZ, AZE, enumSetOf(Alphabet.LATIN), "Əə"),
  BASQUE(EU, EUS, enumSetOf(Alphabet.LATIN), null),
  BELARUSIAN(BE, BEL, enumSetOf(CYRILLIC), null),
  BENGALI(BN, BEN, enumSetOf(Alphabet.BENGALI), null),
  BOKMAL(NB, NOB, enumSetOf(Alphabet.LATIN), null),
  BOSNIAN(BS, BOS, enumSetOf(Alphabet.LATIN), null),
  BULGARIAN(BG, BUL, enumSetOf(CYRILLIC), null),
  CATALAN(CA, CAT, enumSetOf(Alphabet.LATIN), "Ïï"),
  CHINESE(ZH, ZHO, enumSetOf(HAN), null),
  CROATIAN(HR, HRV, enumSetOf(Alphabet.LATIN), null),
  CZECH(CS, CES, enumSetOf(Alphabet.LATIN), "ĚěŘřŮů"),
  DANISH(DA, DAN, enumSetOf(Alphabet.LATIN), null),
  DUTCH(NL, NLD, enumSetOf(Alphabet.LATIN), null),
  ENGLISH(EN, ENG, enumSetOf(Alphabet.LATIN), null),
  ESPERANTO(EO, EPO, enumSetOf(Alphabet.LATIN), "ĈĉĜĝĤĥĴĵŜŝŬŭ"),
  ESTONIAN(ET, EST, enumSetOf(Alphabet.LATIN), null),
  FINNISH(FI, FIN, enumSetOf(Alphabet.LATIN), null),
  FRENCH(FR, FRA, enumSetOf(Alphabet.LATIN), null),
  GANDA(LG, LUG, enumSetOf(Alphabet.LATIN), null),
  GEORGIAN(KA, KAT, enumSetOf(Alphabet.GEORGIAN), null),
  GERMAN(DE, DEU, enumSetOf(Alphabet.LATIN), "ß"),
  GREEK(EL, ELL, enumSetOf(Alphabet.GREEK), null),
  GUJARATI(GU, GUJ, enumSetOf(Alphabet.GUJARATI), null),
  HEBREW(HE, HEB, enumSetOf(Alphabet.HEBREW), null),
  HINDI(HI, HIN, enumSetOf(DEVANAGARI), null),
  HUNGARIAN(HU, HUN, enumSetOf(Alphabet.LATIN), "ŐőŰű"),
  ICELANDIC(IS, ISL, enumSetOf(Alphabet.LATIN), null),
  INDONESIAN(ID, IND, enumSetOf(Alphabet.LATIN), null),
  IRISH(GA, GLE, enumSetOf(Alphabet.LATIN), null),
  ITALIAN(IT, ITA, enumSetOf(Alphabet.LATIN), null),
  JAPANESE(JA, JPN, enumSetOf(HIRAGANA, KATAKANA, HAN), null),
  KAZAKH(KK, KAZ, enumSetOf(CYRILLIC), "ӘәҒғҚқҢңҰұ"),
  KOREAN(KO, KOR, enumSetOf(HANGUL), null),
  LATIN(LA, LAT, enumSetOf(Alphabet.LATIN), null),
  LATVIAN(LV, LAV, enumSetOf(Alphabet.LATIN), "ĢģĶķĻļŅņ"),
  LITHUANIAN(LT, LIT, enumSetOf(Alphabet.LATIN), "ĖėĮįŲų"),
  MACEDONIAN(MK, MKD, enumSetOf(CYRILLIC), "ЃѓЅѕЌќЏџ"),
  MALAY(MS, MSA, enumSetOf(Alphabet.LATIN), null),
  MAORI(MI, MRI, enumSetOf(Alphabet.LATIN), null),
  MARATHI(MR, MAR, enumSetOf(DEVANAGARI), "ळ"),
  MONGOLIAN(MN, MON, enumSetOf(CYRILLIC), "ӨөҮү"),
  NYNORSK(NN, NNO, enumSetOf(Alphabet.LATIN), null),
  OROMO(OM, ORM, enumSetOf(Alphabet.LATIN), null),
  PERSIAN(FA, FAS, enumSetOf(Alphabet.ARABIC), null),
  POLISH(PL, POL, enumSetOf(Alphabet.LATIN), "ŁłŃńŚśŹź"),
  PORTUGUESE(PT, POR, enumSetOf(Alphabet.LATIN), null),
  PUNJABI(PA, PAN, enumSetOf(GURMUKHI), null),
  ROMANIAN(RO, RON, enumSetOf(Alphabet.LATIN), "Țţ"),
  RUSSIAN(RU, RUS, enumSetOf(CYRILLIC), null),
  SERBIAN(SR, SRP, enumSetOf(CYRILLIC), "ЂђЋћ"),
  SHONA(SN, SNA, enumSetOf(Alphabet.LATIN), null),
  SINHALA(SI, SIN, enumSetOf(Alphabet.SINHALA), null),
  SLOVAK(SK, SLK, enumSetOf(Alphabet.LATIN), "ĹĺĽľŔŕ"),
  SLOVENE(SL, SLV, enumSetOf(Alphabet.LATIN), null),
  SOMALI(SO, SOM, enumSetOf(Alphabet.LATIN), null),
  SOTHO(ST, SOT, enumSetOf(Alphabet.LATIN), null),
  SPANISH(ES, SPA, enumSetOf(Alphabet.LATIN), "¿¡"),
  SWAHILI(SW, SWA, enumSetOf(Alphabet.LATIN), null),
  SWEDISH(SV, SWE, enumSetOf(Alphabet.LATIN), null),
  TAGALOG(TL, TGL, enumSetOf(Alphabet.LATIN), null),
  TAMIL(TA, TAM, enumSetOf(Alphabet.TAMIL), null),
  TELUGU(TE, TEL, enumSetOf(Alphabet.TELUGU), null),
  THAI(TH, THA, enumSetOf(Alphabet.THAI), null),
  TIGRINYA(TI, TIR, enumSetOf(Alphabet.ETHIOPIC), null),
  TSONGA(TS, TSO, enumSetOf(Alphabet.LATIN), null),
  TSWANA(TN, TSN, enumSetOf(Alphabet.LATIN), null),
  TURKISH(TR, TUR, enumSetOf(Alphabet.LATIN), null),
  UKRAINIAN(UK, UKR, enumSetOf(CYRILLIC), "ҐґЄєЇї"),
  URDU(UR, URD, enumSetOf(Alphabet.ARABIC), null),
  VIETNAMESE(
      VI,
      VIE,
      enumSetOf(Alphabet.LATIN),
      "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ"),
  WELSH(CY, CYM, enumSetOf(Alphabet.LATIN), null),
  XHOSA(XH, XHO, enumSetOf(Alphabet.LATIN), null),
  // TODO for YORUBA: "E̩e̩Ẹ́ẹ́É̩é̩Ẹ̀ẹ̀È̩è̩Ẹ̄ẹ̄Ē̩ē̩ŌōO̩o̩Ọ́ọ́Ó̩ó̩Ọ̀ọ̀Ò̩ò̩Ọ̄ọ̄Ō̩ō̩ṢṣS̩s̩"
  YORUBA(YO, YOR, enumSetOf(Alphabet.LATIN), "Ṣṣ"),
  ZULU(ZU, ZUL, enumSetOf(Alphabet.LATIN), null),

  /**
   * The imaginary unknown language.
   *
   * <p>This value is returned if no language can be detected reliably.
   */
  UNKNOWN(IsoCode639_1.NONE, IsoCode639_3.NONE, enumSetOf(NONE), null);

  private final IsoCode639_1 isoCode639_1;
  private final IsoCode639_3 isoCode639_3;
  private final EnumSet<Alphabet> alphabets;
  private final String uniqueCharacters;

  Language(
      final IsoCode639_1 isoCode639_1,
      final IsoCode639_3 isoCode639_3,
      final EnumSet<Alphabet> alphabets,
      final String uniqueCharacters) {
    this.isoCode639_1 = isoCode639_1;
    this.isoCode639_3 = isoCode639_3;
    this.alphabets = alphabets;
    this.uniqueCharacters = uniqueCharacters;
  }

  public IsoCode639_1 getIsoCode639_1() {
    return isoCode639_1;
  }

  public IsoCode639_3 getIsoCode639_3() {
    return isoCode639_3;
  }

  public EnumSet<Alphabet> getAlphabets() {
    // Copy to be safe
    return EnumSet.copyOf(this.alphabets);
  }

  public String getUniqueCharacters() {
    // Copy to be safe
    return Optional.ofNullable(uniqueCharacters).orElse("");
  }

  public static List<Language> all() {
    return filterOutLanguages(UNKNOWN);
  }

  public static List<Language> allSpokenOnes() {
    return filterOutLanguages(UNKNOWN, LATIN);
  }

  public static List<Language> allWithArabicScript() {
    return Arrays.stream(values())
        .filter(language -> language.alphabets.contains(Alphabet.ARABIC))
        .collect(Collectors.toList());
  }

  public static List<Language> allWithCyrillicScript() {
    return Arrays.stream(values())
        .filter(language -> language.alphabets.contains(CYRILLIC))
        .collect(Collectors.toList());
  }

  public static List<Language> allWithDevanagariScript() {
    return Arrays.stream(values())
        .filter(language -> language.alphabets.contains(DEVANAGARI))
        .collect(Collectors.toList());
  }

  public static List<Language> allWithEthiopicScript() {
    return Arrays.stream(values())
        .filter(language -> language.alphabets.contains(Alphabet.ETHIOPIC))
        .collect(Collectors.toList());
  }

  public static List<Language> allWithLatinScript() {
    return Arrays.stream(values())
        .filter(language -> language.alphabets.contains(Alphabet.LATIN))
        .collect(Collectors.toList());
  }

  public static Language getByIsoCode639_1(IsoCode639_1 isoCode) {
    return Arrays.stream(values())
        .filter(language -> language.isoCode639_1 == isoCode)
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException("No language found with ISO code 639-1: " + isoCode));
  }

  public static Language getByIsoCode639_3(IsoCode639_3 isoCode) {
    return Arrays.stream(values())
        .filter(language -> language.isoCode639_3 == isoCode)
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException("No language found with ISO code 639-3: " + isoCode));
  }

  private static List<Language> filterOutLanguages(Language... languages) {
    return Arrays.stream(values())
        .filter(language -> !Arrays.asList(languages).contains(language))
        .collect(Collectors.toList());
  }
}

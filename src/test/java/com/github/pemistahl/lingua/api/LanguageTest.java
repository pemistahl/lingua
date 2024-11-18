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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.github.pemistahl.lingua.internal.Alphabet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * This class contains various tests related to supported languages and their corresponding scripts.
 *
 * <p>Author: Peter M. Stahl <pemistahl@gmail.com><br>
 * Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
public class LanguageTest {

  /** Asserts that all supported languages are available. */
  @Test
  public void assertThatAllSupportedLanguagesAreAvailable() {
    assertThat(Language.all())
        .containsExactly(
            Language.AFRIKAANS,
            Language.ALBANIAN,
            Language.AMHARIC,
            Language.ARABIC,
            Language.ARMENIAN,
            Language.AZERBAIJANI,
            Language.BASQUE,
            Language.BELARUSIAN,
            Language.BENGALI,
            Language.BOKMAL,
            Language.BOSNIAN,
            Language.BULGARIAN,
            Language.CATALAN,
            Language.CHINESE,
            Language.CROATIAN,
            Language.CZECH,
            Language.DANISH,
            Language.DUTCH,
            Language.ENGLISH,
            Language.ESPERANTO,
            Language.ESTONIAN,
            Language.FINNISH,
            Language.FRENCH,
            Language.GANDA,
            Language.GEORGIAN,
            Language.GERMAN,
            Language.GREEK,
            Language.GUJARATI,
            Language.HEBREW,
            Language.HINDI,
            Language.HUNGARIAN,
            Language.ICELANDIC,
            Language.INDONESIAN,
            Language.IRISH,
            Language.ITALIAN,
            Language.JAPANESE,
            Language.KAZAKH,
            Language.KOREAN,
            Language.LATIN,
            Language.LATVIAN,
            Language.LITHUANIAN,
            Language.MACEDONIAN,
            Language.MALAY,
            Language.MAORI,
            Language.MARATHI,
            Language.MONGOLIAN,
            Language.NYNORSK,
            Language.OROMO,
            Language.PERSIAN,
            Language.POLISH,
            Language.PORTUGUESE,
            Language.PUNJABI,
            Language.ROMANIAN,
            Language.RUSSIAN,
            Language.SERBIAN,
            Language.SHONA,
            Language.SINHALA,
            Language.SLOVAK,
            Language.SLOVENE,
            Language.SOMALI,
            Language.SOTHO,
            Language.SPANISH,
            Language.SWAHILI,
            Language.SWEDISH,
            Language.TAGALOG,
            Language.TAMIL,
            Language.TELUGU,
            Language.THAI,
            Language.TIGRINYA,
            Language.TSONGA,
            Language.TSWANA,
            Language.TURKISH,
            Language.UKRAINIAN,
            Language.URDU,
            Language.VIETNAMESE,
            Language.WELSH,
            Language.XHOSA,
            Language.YORUBA,
            Language.ZULU);
  }

  /** Asserts that all supported spoken languages are available. */
  @Test
  public void assertThatAllSupportedSpokenLanguagesAreAvailable() {
    assertThat(Language.allSpokenOnes())
        .containsExactly(
            Language.AFRIKAANS,
            Language.ALBANIAN,
            Language.AMHARIC,
            Language.ARABIC,
            Language.ARMENIAN,
            Language.AZERBAIJANI,
            Language.BASQUE,
            Language.BELARUSIAN,
            Language.BENGALI,
            Language.BOKMAL,
            Language.BOSNIAN,
            Language.BULGARIAN,
            Language.CATALAN,
            Language.CHINESE,
            Language.CROATIAN,
            Language.CZECH,
            Language.DANISH,
            Language.DUTCH,
            Language.ENGLISH,
            Language.ESPERANTO,
            Language.ESTONIAN,
            Language.FINNISH,
            Language.FRENCH,
            Language.GANDA,
            Language.GEORGIAN,
            Language.GERMAN,
            Language.GREEK,
            Language.GUJARATI,
            Language.HEBREW,
            Language.HINDI,
            Language.HUNGARIAN,
            Language.ICELANDIC,
            Language.INDONESIAN,
            Language.IRISH,
            Language.ITALIAN,
            Language.JAPANESE,
            Language.KAZAKH,
            Language.KOREAN,
            Language.LATVIAN,
            Language.LITHUANIAN,
            Language.MACEDONIAN,
            Language.MALAY,
            Language.MAORI,
            Language.MARATHI,
            Language.MONGOLIAN,
            Language.NYNORSK,
            Language.OROMO,
            Language.PERSIAN,
            Language.POLISH,
            Language.PORTUGUESE,
            Language.PUNJABI,
            Language.ROMANIAN,
            Language.RUSSIAN,
            Language.SERBIAN,
            Language.SHONA,
            Language.SINHALA,
            Language.SLOVAK,
            Language.SLOVENE,
            Language.SOMALI,
            Language.SOTHO,
            Language.SPANISH,
            Language.SWAHILI,
            Language.SWEDISH,
            Language.TAGALOG,
            Language.TAMIL,
            Language.TELUGU,
            Language.THAI,
            Language.TIGRINYA,
            Language.TSONGA,
            Language.TSWANA,
            Language.TURKISH,
            Language.UKRAINIAN,
            Language.URDU,
            Language.VIETNAMESE,
            Language.WELSH,
            Language.XHOSA,
            Language.YORUBA,
            Language.ZULU);
  }

  /** Asserts that certain languages support Arabic script. */
  @Test
  public void assertThatCertainLanguagesSupportArabicScript() {
    assertThat(Language.allWithArabicScript())
        .containsExactly(Language.ARABIC, Language.PERSIAN, Language.URDU);
  }

  /** Asserts that certain languages support Cyrillic script. */
  @Test
  public void assertThatCertainLanguagesSupportCyrillicScript() {
    assertThat(Language.allWithCyrillicScript())
        .containsExactly(
            Language.BELARUSIAN,
            Language.BULGARIAN,
            Language.KAZAKH,
            Language.MACEDONIAN,
            Language.MONGOLIAN,
            Language.RUSSIAN,
            Language.SERBIAN,
            Language.UKRAINIAN);
  }

  /** Asserts that certain languages support Devanagari script. */
  @Test
  public void assertThatCertainLanguagesSupportDevanagariScript() {
    assertThat(Language.allWithDevanagariScript())
        .containsExactly(Language.HINDI, Language.MARATHI);
  }

  /** Asserts that certain languages support Ethiopic script. */
  @Test
  public void assertThatCertainLanguagesSupportEthiopicScript() {
    assertThat(Language.allWithEthiopicScript())
        .containsExactly(Language.AMHARIC, Language.TIGRINYA);
  }

  /** Asserts that certain languages support Latin script. */
  @Test
  public void assertThatCertainLanguagesSupportLatinScript() {
    assertThat(Language.allWithLatinScript())
        .containsExactly(
            Language.AFRIKAANS,
            Language.ALBANIAN,
            Language.AZERBAIJANI,
            Language.BASQUE,
            Language.BOKMAL,
            Language.BOSNIAN,
            Language.CATALAN,
            Language.CROATIAN,
            Language.CZECH,
            Language.DANISH,
            Language.DUTCH,
            Language.ENGLISH,
            Language.ESPERANTO,
            Language.ESTONIAN,
            Language.FINNISH,
            Language.FRENCH,
            Language.GANDA,
            Language.GERMAN,
            Language.HUNGARIAN,
            Language.ICELANDIC,
            Language.INDONESIAN,
            Language.IRISH,
            Language.ITALIAN,
            Language.LATIN,
            Language.LATVIAN,
            Language.LITHUANIAN,
            Language.MALAY,
            Language.MAORI,
            Language.NYNORSK,
            Language.OROMO,
            Language.POLISH,
            Language.PORTUGUESE,
            Language.ROMANIAN,
            Language.SHONA,
            Language.SLOVAK,
            Language.SLOVENE,
            Language.SOMALI,
            Language.SOTHO,
            Language.SPANISH,
            Language.SWAHILI,
            Language.SWEDISH,
            Language.TAGALOG,
            Language.TSONGA,
            Language.TSWANA,
            Language.TURKISH,
            Language.VIETNAMESE,
            Language.WELSH,
            Language.XHOSA,
            Language.YORUBA,
            Language.ZULU);
  }

  @ParameterizedTest
  @MethodSource("filteredLanguagesProvider")
  public void assertThatLanguagesSupportCorrectAlphabets(
      Alphabet alphabet, List<Language> expectedLanguages) {
    final List<Language> actualLanguages =
        Stream.of(Language.values())
            .filter(language -> language.getAlphabets().contains(alphabet))
            .collect(Collectors.toList());

    assertThat(actualLanguages)
        .as("alphabet '%s'", alphabet)
        .containsExactlyElementsOf(expectedLanguages);
  }

  /** Asserts that the correct language is returned for the given ISO 639-1 code. */
  @ParameterizedTest
  @CsvSource({
    "AF, AFRIKAANS",
    "SQ, ALBANIAN",
    "AM, AMHARIC",
    "AR, ARABIC",
    "HY, ARMENIAN",
    "AZ, AZERBAIJANI",
    "EU, BASQUE",
    "BE, BELARUSIAN",
    "BN, BENGALI",
    "NB, BOKMAL",
    "BS, BOSNIAN",
    "BG, BULGARIAN",
    "CA, CATALAN",
    "ZH, CHINESE",
    "HR, CROATIAN",
    "CS, CZECH",
    "DA, DANISH",
    "NL, DUTCH",
    "EN, ENGLISH",
    "EO, ESPERANTO",
    "ET, ESTONIAN",
    "FI, FINNISH",
    "FR, FRENCH",
    "LG, GANDA",
    "KA, GEORGIAN",
    "DE, GERMAN",
    "EL, GREEK",
    "GU, GUJARATI",
    "HE, HEBREW",
    "HI, HINDI",
    "HU, HUNGARIAN",
    "IS, ICELANDIC",
    "ID, INDONESIAN",
    "GA, IRISH",
    "IT, ITALIAN",
    "JA, JAPANESE",
    "KK, KAZAKH",
    "KO, KOREAN",
    "LA, LATIN",
    "LV, LATVIAN",
    "LT, LITHUANIAN",
    "MK, MACEDONIAN",
    "MS, MALAY",
    "MI, MAORI",
    "MR, MARATHI",
    "MN, MONGOLIAN",
    "NN, NYNORSK",
    "OM, OROMO",
    "FA, PERSIAN",
    "PL, POLISH",
    "PT, PORTUGUESE",
    "PA, PUNJABI",
    "RO, ROMANIAN",
    "RU, RUSSIAN",
    "SR, SERBIAN",
    "SN, SHONA",
    "SI, SINHALA",
    "SK, SLOVAK",
    "SL, SLOVENE",
    "SO, SOMALI",
    "ST, SOTHO",
    "ES, SPANISH",
    "SW, SWAHILI",
    "SV, SWEDISH",
    "TL, TAGALOG",
    "TA, TAMIL",
    "TE, TELUGU",
    "TH, THAI",
    "TI, TIGRINYA",
    "TS, TSONGA",
    "TN, TSWANA",
    "TR, TURKISH",
    "UK, UKRAINIAN",
    "UR, URDU",
    "VI, VIETNAMESE",
    "CY, WELSH",
    "XH, XHOSA",
    "YO, YORUBA",
    "ZU, ZULU"
  })
  public void assertThatLanguageIsReturnedForIso6391Code(
      String isoCode, Language expectedLanguage) {
    assertThat(Language.getByIsoCode639_1(IsoCode639_1.valueOf(isoCode)))
        .isEqualTo(expectedLanguage);
  }

  /**
   * Provides a filtered list of languages categorized by their alphabet script. This method is used
   * for parameterized tests where the alphabet and corresponding languages are passed as arguments.
   *
   * @return A stream of arguments containing an alphabet and a list of languages using it.
   */
  public static Stream<Arguments> filteredLanguagesProvider() {
    return Stream.of(
        arguments(Alphabet.ARABIC, List.of(Language.ARABIC, Language.PERSIAN, Language.URDU)),
        arguments(Alphabet.ARMENIAN, List.of(Language.ARMENIAN)),
        arguments(Alphabet.BENGALI, List.of(Language.BENGALI)),
        arguments(
            Alphabet.CYRILLIC,
            List.of(
                Language.BELARUSIAN,
                Language.BULGARIAN,
                Language.KAZAKH,
                Language.MACEDONIAN,
                Language.MONGOLIAN,
                Language.RUSSIAN,
                Language.SERBIAN,
                Language.UKRAINIAN)),
        arguments(Alphabet.DEVANAGARI, List.of(Language.HINDI, Language.MARATHI)),
        arguments(Alphabet.ETHIOPIC, List.of(Language.AMHARIC, Language.TIGRINYA)),
        arguments(Alphabet.GEORGIAN, List.of(Language.GEORGIAN)),
        arguments(Alphabet.GREEK, List.of(Language.GREEK)),
        arguments(Alphabet.GUJARATI, List.of(Language.GUJARATI)),
        arguments(Alphabet.GURMUKHI, List.of(Language.PUNJABI)),
        arguments(Alphabet.HAN, List.of(Language.CHINESE, Language.JAPANESE)),
        arguments(Alphabet.HANGUL, List.of(Language.KOREAN)),
        arguments(Alphabet.HEBREW, List.of(Language.HEBREW)),
        arguments(Alphabet.HIRAGANA, List.of(Language.JAPANESE)),
        arguments(Alphabet.KATAKANA, List.of(Language.JAPANESE)),
        arguments(
            Alphabet.LATIN,
            List.of(
                Language.AFRIKAANS,
                Language.ALBANIAN,
                Language.AZERBAIJANI,
                Language.BASQUE,
                Language.BOKMAL,
                Language.BOSNIAN,
                Language.CATALAN,
                Language.CROATIAN,
                Language.CZECH,
                Language.DANISH,
                Language.DUTCH,
                Language.ENGLISH,
                Language.ESPERANTO,
                Language.ESTONIAN,
                Language.FINNISH,
                Language.FRENCH,
                Language.GANDA,
                Language.GERMAN,
                Language.HUNGARIAN,
                Language.ICELANDIC,
                Language.INDONESIAN,
                Language.IRISH,
                Language.ITALIAN,
                Language.LATIN,
                Language.LATVIAN,
                Language.LITHUANIAN,
                Language.MALAY,
                Language.MAORI,
                Language.NYNORSK,
                Language.OROMO,
                Language.POLISH,
                Language.PORTUGUESE,
                Language.ROMANIAN,
                Language.SHONA,
                Language.SLOVAK,
                Language.SLOVENE,
                Language.SOMALI,
                Language.SOTHO,
                Language.SPANISH,
                Language.SWAHILI,
                Language.SWEDISH,
                Language.TAGALOG,
                Language.TSONGA,
                Language.TSWANA,
                Language.TURKISH,
                Language.VIETNAMESE,
                Language.WELSH,
                Language.XHOSA,
                Language.YORUBA,
                Language.ZULU)),
        arguments(Alphabet.SINHALA, List.of(Language.SINHALA)),
        arguments(Alphabet.TAMIL, List.of(Language.TAMIL)),
        arguments(Alphabet.TELUGU, List.of(Language.TELUGU)),
        arguments(Alphabet.THAI, List.of(Language.THAI)),
        arguments(Alphabet.NONE, List.of(Language.UNKNOWN)));
  }
}

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

package com.github.pemistahl.lingua.internal;

import com.github.pemistahl.lingua.api.Language;
import java.lang.Character.UnicodeScript;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Constant {

  public static final Map<String, Set<Language>> CHARS_TO_LANGUAGES_MAPPING;

  public static boolean isJapaneseAlphabet(char charValue) {
    UnicodeScript script = UnicodeScript.of(charValue);
    return script == UnicodeScript.HIRAGANA
        || script == UnicodeScript.KATAKANA
        || script == UnicodeScript.HAN;
  }

  public static final EnumSet<Language> LANGUAGES_SUPPORTING_LOGOGRAMS =
      EnumSet.of(Language.CHINESE, Language.JAPANESE, Language.KOREAN);

  public static final Pattern MULTIPLE_WHITESPACE = Pattern.compile("\\s+");
  public static final Pattern NO_LETTER = Pattern.compile("^[^\\p{L}]+$");
  public static final Pattern NUMBERS = Pattern.compile("\\p{N}");
  public static final Pattern PUNCTUATION = Pattern.compile("\\p{P}");

  static {
    CHARS_TO_LANGUAGES_MAPPING = new HashMap<>(50);
    CHARS_TO_LANGUAGES_MAPPING.put("Ãã", EnumSet.of(Language.PORTUGUESE, Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put("ĄąĘę", EnumSet.of(Language.LITHUANIAN, Language.POLISH));
    CHARS_TO_LANGUAGES_MAPPING.put("Żż", EnumSet.of(Language.POLISH, Language.ROMANIAN));
    CHARS_TO_LANGUAGES_MAPPING.put("Îî", EnumSet.of(Language.FRENCH, Language.ROMANIAN));
    CHARS_TO_LANGUAGES_MAPPING.put("Ññ", EnumSet.of(Language.BASQUE, Language.SPANISH));
    CHARS_TO_LANGUAGES_MAPPING.put("ŇňŤť", EnumSet.of(Language.CZECH, Language.SLOVAK));
    CHARS_TO_LANGUAGES_MAPPING.put("Ăă", EnumSet.of(Language.ROMANIAN, Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put("İıĞğ", EnumSet.of(Language.AZERBAIJANI, Language.TURKISH));
    CHARS_TO_LANGUAGES_MAPPING.put("ЈјЉљЊњ", EnumSet.of(Language.MACEDONIAN, Language.SERBIAN));
    CHARS_TO_LANGUAGES_MAPPING.put("ẸẹỌọ", EnumSet.of(Language.VIETNAMESE, Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put("ÐðÞþ", EnumSet.of(Language.ICELANDIC, Language.TURKISH));
    CHARS_TO_LANGUAGES_MAPPING.put("Ûû", EnumSet.of(Language.FRENCH, Language.HUNGARIAN));
    CHARS_TO_LANGUAGES_MAPPING.put("Ōō", EnumSet.of(Language.MAORI, Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "ĀāĒēĪī", EnumSet.of(Language.LATVIAN, Language.MAORI, Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Şş", EnumSet.of(Language.AZERBAIJANI, Language.ROMANIAN, Language.TURKISH));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ďď", EnumSet.of(Language.CZECH, Language.ROMANIAN, Language.SLOVAK));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ćć", EnumSet.of(Language.BOSNIAN, Language.CROATIAN, Language.POLISH));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Đđ", EnumSet.of(Language.BOSNIAN, Language.CROATIAN, Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Іі", EnumSet.of(Language.BELARUSIAN, Language.KAZAKH, Language.UKRAINIAN));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ìì", EnumSet.of(Language.ITALIAN, Language.VIETNAMESE, Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Øø", EnumSet.of(Language.BOKMAL, Language.DANISH, Language.NYNORSK));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ūū", EnumSet.of(Language.LATVIAN, Language.LITHUANIAN, Language.MAORI, Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ëë", EnumSet.of(Language.AFRIKAANS, Language.ALBANIAN, Language.DUTCH, Language.FRENCH));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "ÈèÙù",
        EnumSet.of(Language.FRENCH, Language.ITALIAN, Language.VIETNAMESE, Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Êê",
        EnumSet.of(Language.AFRIKAANS, Language.FRENCH, Language.PORTUGUESE, Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Õõ",
        EnumSet.of(
            Language.ESTONIAN, Language.HUNGARIAN, Language.PORTUGUESE, Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ôô",
        EnumSet.of(Language.FRENCH, Language.PORTUGUESE, Language.SLOVAK, Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "ЁёЫыЭэ",
        EnumSet.of(Language.BELARUSIAN, Language.KAZAKH, Language.MONGOLIAN, Language.RUSSIAN));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "ЩщЪъ",
        EnumSet.of(Language.BULGARIAN, Language.KAZAKH, Language.MONGOLIAN, Language.RUSSIAN));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Òò", EnumSet.of(Language.CATALAN, Language.ITALIAN, Language.VIETNAMESE, Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ææ", EnumSet.of(Language.BOKMAL, Language.DANISH, Language.ICELANDIC, Language.NYNORSK));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Åå", EnumSet.of(Language.BOKMAL, Language.DANISH, Language.NYNORSK, Language.SWEDISH));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ýý",
        EnumSet.of(
            Language.CZECH,
            Language.ICELANDIC,
            Language.SLOVAK,
            Language.TURKISH,
            Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ää",
        EnumSet.of(
            Language.ESTONIAN,
            Language.FINNISH,
            Language.GERMAN,
            Language.SLOVAK,
            Language.SWEDISH));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Àà",
        EnumSet.of(
            Language.CATALAN,
            Language.FRENCH,
            Language.ITALIAN,
            Language.PORTUGUESE,
            Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Ââ",
        EnumSet.of(
            Language.FRENCH,
            Language.PORTUGUESE,
            Language.ROMANIAN,
            Language.TURKISH,
            Language.VIETNAMESE));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Üü",
        EnumSet.of(
            Language.AZERBAIJANI,
            Language.CATALAN,
            Language.ESTONIAN,
            Language.GERMAN,
            Language.HUNGARIAN,
            Language.SPANISH,
            Language.TURKISH));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "ČčŠšŽž",
        EnumSet.of(
            Language.BOSNIAN,
            Language.CZECH,
            Language.CROATIAN,
            Language.LATVIAN,
            Language.LITHUANIAN,
            Language.SLOVAK,
            Language.SLOVENE));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Çç",
        EnumSet.of(
            Language.ALBANIAN,
            Language.AZERBAIJANI,
            Language.BASQUE,
            Language.CATALAN,
            Language.FRENCH,
            Language.PORTUGUESE,
            Language.TURKISH));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Öö",
        EnumSet.of(
            Language.AZERBAIJANI,
            Language.ESTONIAN,
            Language.FINNISH,
            Language.GERMAN,
            Language.HUNGARIAN,
            Language.ICELANDIC,
            Language.SWEDISH,
            Language.TURKISH));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Óó",
        EnumSet.of(
            Language.CATALAN,
            Language.HUNGARIAN,
            Language.ICELANDIC,
            Language.IRISH,
            Language.POLISH,
            Language.PORTUGUESE,
            Language.SLOVAK,
            Language.SPANISH,
            Language.VIETNAMESE,
            Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "ÁáÍíÚú",
        EnumSet.of(
            Language.CATALAN,
            Language.CZECH,
            Language.ICELANDIC,
            Language.IRISH,
            Language.HUNGARIAN,
            Language.PORTUGUESE,
            Language.SLOVAK,
            Language.SPANISH,
            Language.VIETNAMESE,
            Language.YORUBA));
    CHARS_TO_LANGUAGES_MAPPING.put(
        "Éé",
        EnumSet.of(
            Language.CATALAN,
            Language.CZECH,
            Language.FRENCH,
            Language.HUNGARIAN,
            Language.ICELANDIC,
            Language.IRISH,
            Language.ITALIAN,
            Language.PORTUGUESE,
            Language.SLOVAK,
            Language.SPANISH,
            Language.VIETNAMESE,
            Language.YORUBA));
  }
}

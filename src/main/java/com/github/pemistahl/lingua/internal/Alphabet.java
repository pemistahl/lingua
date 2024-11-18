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
import java.util.stream.Collectors;

public enum Alphabet {
  ARABIC,
  ARMENIAN,
  BENGALI,
  CYRILLIC,
  DEVANAGARI,
  ETHIOPIC,
  GEORGIAN,
  GREEK,
  GUJARATI,
  GURMUKHI,
  HAN,
  HANGUL,
  HEBREW,
  HIRAGANA,
  KATAKANA,
  LATIN,
  SINHALA,
  TAMIL,
  TELUGU,
  THAI,
  NONE;

  private UnicodeScript script;

  Alphabet() {
    try {
      this.script = UnicodeScript.forName(this.name());
    } catch (IllegalArgumentException e) {
      this.script = null;
    }
  }

  public boolean matches(char chr) {
    return UnicodeScript.of(chr) == this.script;
  }

  public boolean matches(CharSequence input) {
    return input.chars().allMatch(codePoint -> UnicodeScript.of(codePoint) == this.script);
  }

  private Set<Language> supportedLanguages() {
    return EnumSet.allOf(Language.class).stream()
        .filter(language -> language.getAlphabets().contains(this))
        .collect(Collectors.toSet());
  }

  public static Map<Alphabet, Language> allSupportingExactlyOneLanguage() {
    Map<Alphabet, Language> alphabets = new HashMap<>();
    for (Alphabet alphabet : values()) {
      if (alphabet != NONE) {
        Set<Language> supportedLanguages = alphabet.supportedLanguages();
        if (supportedLanguages.size() == 1) {
          alphabets.put(alphabet, supportedLanguages.iterator().next());
        }
      }
    }
    return alphabets;
  }
}

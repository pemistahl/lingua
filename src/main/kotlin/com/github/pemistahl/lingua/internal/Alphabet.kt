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

package com.github.pemistahl.lingua.internal

import com.github.pemistahl.lingua.api.Language
import java.lang.Character.UnicodeScript

internal enum class Alphabet {
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

    private val script: UnicodeScript? = try {
        UnicodeScript.forName(this.name)
    } catch (e: IllegalArgumentException) {
        null
    }

    fun matches(chr: Char): Boolean = UnicodeScript.of(chr.code) == this.script

    fun matches(input: CharSequence): Boolean = input.codePoints().allMatch { UnicodeScript.of(it) == this.script }

    private fun supportedLanguages(): Set<Language> {
        val languages = mutableSetOf<Language>()
        for (language in Language.values()) {
            if (language.alphabets.contains(this)) {
                languages.add(language)
            }
        }
        return languages
    }

    companion object {
        fun allSupportingExactlyOneLanguage(): Map<Alphabet, Language> {
            val alphabets = mutableMapOf<Alphabet, Language>()
            for (alphabet in values().filterNot { it == NONE }) {
                val supportedLanguages = alphabet.supportedLanguages()
                if (supportedLanguages.size == 1) {
                    alphabets[alphabet] = supportedLanguages.first()
                }
            }
            return alphabets
        }
    }
}

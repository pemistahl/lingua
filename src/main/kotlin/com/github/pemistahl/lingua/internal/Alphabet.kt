/*
 * Copyright © 2018-2020 Peter M. Stahl pemistahl@gmail.com
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
import com.github.pemistahl.lingua.internal.util.extension.asRegex

internal enum class Alphabet(private val regex: Regex) {
    ARABIC("Arabic".asRegex()),
    ARMENIAN("Armenian".asRegex()),
    BENGALI("Bengali".asRegex()),
    CYRILLIC("Cyrillic".asRegex()),
    DEVANAGARI("Devanagari".asRegex()),
    GEORGIAN("Georgian".asRegex()),
    GREEK("Greek".asRegex()),
    GUJARATI("Gujarati".asRegex()),
    GURMUKHI("Gurmukhi".asRegex()),
    HAN("Han".asRegex()),
    HANGUL("Hangul".asRegex()),
    HEBREW("Hebrew".asRegex()),
    HIRAGANA("Hiragana".asRegex()),
    KATAKANA("Katakana".asRegex()),
    LATIN("Latin".asRegex()),
    TAMIL("Tamil".asRegex()),
    TELUGU("Telugu".asRegex()),
    THAI("Thai".asRegex()),

    NONE(Regex(""));

    fun matches(input: CharSequence) = this.regex.matches(input)

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

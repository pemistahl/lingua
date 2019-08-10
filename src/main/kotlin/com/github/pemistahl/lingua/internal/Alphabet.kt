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

package com.github.pemistahl.lingua.internal

import com.github.pemistahl.lingua.internal.util.extension.asRegex

internal enum class Alphabet(private val regex: Regex) {
    ARABIC   ("Arabic".asRegex()),
    BENGALI  ("Bengali".asRegex()),
    CHINESE  ("Han".asRegex()),
    CYRILLIC ("Cyrillic".asRegex()),
    GREEK    ("Greek".asRegex()),
    GUJARATI ("Gujarati".asRegex()),
    GURMUKHI ("Gurmukhi".asRegex()),
    HEBREW   ("Hebrew".asRegex()),
    JAPANESE ("Hiragana, Katakana, Han".asRegex()),
    KOREAN   ("Hangul".asRegex()),
    LATIN    ("Latin".asRegex()),
    TAMIL    ("Tamil".asRegex()),
    TELUGU   ("Telugu".asRegex()),
    THAI     ("Thai".asRegex()),

    NONE     (Regex(""));

    fun matches(input: CharSequence) = this.regex.matches(input)
}

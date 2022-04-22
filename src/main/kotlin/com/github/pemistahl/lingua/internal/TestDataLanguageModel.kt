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

import com.github.pemistahl.lingua.internal.util.SubSequence

internal data class TestDataLanguageModel(val ngrams: Set<Ngram>) {

    companion object {
        /**
         * Match sequence of letter of n or more characters long.
         */
        private val LETTER_REGEX_MAP = List(5) { Regex("\\p{L}{${it + 1},}") }

        fun fromText(text: String, ngramLength: Int): TestDataLanguageModel {
            require(ngramLength in 1..5) {
                "ngram length $ngramLength is not in range 1..5"
            }
            val ngrams = LETTER_REGEX_MAP[ngramLength - 1].findAll(text).flatMapTo(mutableSetOf()) { match ->
                val word = match.value
                sequence {
                    for (i in 0..word.length - ngramLength) {
                        val textSlice = SubSequence(word, i, ngramLength)
                        yield(Ngram(textSlice))
                    }
                }
            }
            return TestDataLanguageModel(ngrams)
        }
    }
}

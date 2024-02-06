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

internal data class TestDataLanguageModel(val ngrams: Set<Ngram>) {
    companion object {
        private val LETTER_REGEX = Regex("\\p{L}+")

        fun fromText(
            text: String,
            ngramLength: Int,
        ): TestDataLanguageModel {
            require(ngramLength in 1..5) {
                "ngram length $ngramLength is not in range 1..5"
            }
            val ngrams = hashSetOf<Ngram>()
            for (i in 0..text.length - ngramLength) {
                val textSlice = text.substring(i, i + ngramLength)
                if (LETTER_REGEX.matches(textSlice)) {
                    val ngram = Ngram(textSlice)
                    ngrams.add(ngram)
                }
            }
            return TestDataLanguageModel(ngrams)
        }
    }
}

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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@Serializable
internal data class JsonLanguageModel(val language: Language, val ngrams: Map<Fraction, String>)

internal data class TrainingDataLanguageModel(
    val language: Language,
    val absoluteFrequencies: Map<Ngram, Int>,
    val relativeFrequencies: Map<Ngram, Fraction>
) {
    fun getRelativeFrequency(ngram: Ngram): Double = relativeFrequencies[ngram]?.toDouble() ?: 0.0

    fun toJson(): String {
        val ngrams = mutableMapOf<Fraction, MutableList<Ngram>>()
        for ((ngram, fraction) in relativeFrequencies) {
            ngrams.computeIfAbsent(fraction) { mutableListOf() }.add(ngram)
        }
        return JSON.stringify(
            JsonLanguageModel.serializer(),
            JsonLanguageModel(language, ngrams.mapValues {
                it.value.joinToString(separator = " ")
            }))
    }

    companion object {
        private val JSON = Json(JsonConfiguration.Stable)

        fun fromText(
            text: Sequence<String>,
            language: Language,
            ngramLength: Int,
            charClass: String,
            lowerNgramAbsoluteFrequencies: Map<Ngram, Int>
        ): TrainingDataLanguageModel {

            require(ngramLength in 1..5) {
                "ngram length $ngramLength is not in range 1..5"
            }

            val absoluteFrequencies = computeAbsoluteFrequencies(
                text,
                ngramLength,
                charClass
            )

            val relativeFrequencies = computeRelativeFrequencies(
                ngramLength,
                absoluteFrequencies,
                lowerNgramAbsoluteFrequencies
            )

            return TrainingDataLanguageModel(language, absoluteFrequencies, relativeFrequencies)
        }

        fun fromJson(json: String): TrainingDataLanguageModel {
            val jsonLanguageModel = JSON.parse(JsonLanguageModel.serializer(), json)
            val relativeFrequencies = mutableMapOf<Ngram, Fraction>()

            for ((fraction, ngrams) in jsonLanguageModel.ngrams) {
                for (ngram in ngrams.split(' ')) {
                    relativeFrequencies[Ngram(ngram)] = fraction
                }
            }

            return TrainingDataLanguageModel(
                language = jsonLanguageModel.language,
                absoluteFrequencies = emptyMap(),
                relativeFrequencies = relativeFrequencies
            )
        }

        private fun computeAbsoluteFrequencies(
            text: Sequence<String>,
            ngramLength: Int,
            charClass: String
        ): Map<Ngram, Int> {

            val absoluteFrequencies = hashMapOf<Ngram, Int>()
            val regex = Regex("""[\p{L}&&\p{$charClass}]+""")

            for (line in text) {
                val lowerCasedLine = line.toLowerCase()
                for (i in 0..lowerCasedLine.length - ngramLength) {
                    val textSlice = lowerCasedLine.slice(i until i + ngramLength)
                    if (regex.matches(textSlice)) {
                        val ngram = Ngram(textSlice)
                        absoluteFrequencies.merge(ngram, 1, Int::plus)
                    }
                }
            }

            return absoluteFrequencies
        }

        private fun computeRelativeFrequencies(
            ngramLength: Int,
            absoluteFrequencies: Map<Ngram, Int>,
            lowerNgramAbsoluteFrequencies: Map<Ngram, Int>
        ): Map<Ngram, Fraction> {

            val ngramProbabilities = hashMapOf<Ngram, Fraction>()
            val totalNgramFrequency = absoluteFrequencies.values.sum()

            for ((ngram, frequency) in absoluteFrequencies) {
                val denominator = if (ngramLength == 1 || lowerNgramAbsoluteFrequencies.isEmpty()) {
                    totalNgramFrequency
                } else {
                    lowerNgramAbsoluteFrequencies.getValue(Ngram(ngram.value.slice(0..ngramLength-2)))
                }
                ngramProbabilities[ngram] = Fraction(frequency, denominator)
            }

            return ngramProbabilities
        }
    }
}

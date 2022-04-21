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
import com.github.pemistahl.lingua.internal.util.extension.incrementCounter
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.TreeMap

@Serializable
internal data class JsonLanguageModel(val language: Language, val ngrams: Map<Fraction, String>)

internal data class TrainingDataLanguageModel(
    val language: Language,
    val absoluteFrequencies: Map<Ngram, Int>,
    val relativeFrequencies: Map<Ngram, Fraction>,
    val jsonRelativeFrequencies: RelativeFrequencies
) {
    fun getRelativeFrequency(ngram: Ngram): Float = jsonRelativeFrequencies[ngram.value]

    fun toJson(): String {
        val ngrams = mutableMapOf<Fraction, MutableList<Ngram>>()

        for ((ngram, fraction) in relativeFrequencies) {
            ngrams.computeIfAbsent(fraction) { mutableListOf() }.add(ngram)
        }

        val jsonLanguageModel = JsonLanguageModel(language, ngrams.mapValues { it.value.joinToString(separator = " ") })

        return Json.encodeToString(jsonLanguageModel)
    }

    companion object {
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

            return TrainingDataLanguageModel(
                language,
                absoluteFrequencies,
                relativeFrequencies,
                RelativeFrequencies.build(emptySequence())
            )
        }

        fun fromJson(language: Language, jsonLanguageModels: Sequence<JsonLanguageModel>): TrainingDataLanguageModel {
            val jsonDataSequence =
                sequence {
                    for (jsonLanguageModel in jsonLanguageModels) {
                        for ((fraction, ngrams) in jsonLanguageModel.ngrams) {
                            val fractionAsFloat = fraction.toFloat()
                            for (ngram in ngrams.split(' ')) {
                                yield(ngram to fractionAsFloat)
                            }
                        }
                    }
                }

            return TrainingDataLanguageModel(
                language = language,
                absoluteFrequencies = emptyMap(),
                relativeFrequencies = emptyMap(),
                jsonRelativeFrequencies = RelativeFrequencies.build(jsonDataSequence)
            )
        }

        private fun computeAbsoluteFrequencies(
            text: Sequence<String>,
            ngramLength: Int,
            charClass: String
        ): Map<Ngram, Int> {

            val absoluteFrequencies = mutableMapOf<Ngram, Int>()
            val regex = Regex("[$charClass]+")

            for (line in text) {
                val lowerCasedLine = line.lowercase()
                for (i in 0..lowerCasedLine.length - ngramLength) {
                    val textSlice = lowerCasedLine.substring(i, i + ngramLength)
                    if (regex.matches(textSlice)) {
                        val ngram = Ngram(textSlice)
                        absoluteFrequencies.incrementCounter(ngram)
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

            val ngramProbabilities = mutableMapOf<Ngram, Fraction>()
            val totalNgramFrequency = absoluteFrequencies.values.sum()

            for ((ngram, frequency) in absoluteFrequencies) {
                val denominator = if (ngramLength == 1 || lowerNgramAbsoluteFrequencies.isEmpty()) {
                    totalNgramFrequency
                } else {
                    lowerNgramAbsoluteFrequencies.getValue(Ngram(ngram.value.substring(0, ngramLength - 1)))
                }
                ngramProbabilities[ngram] = Fraction(frequency, denominator)
            }

            return ngramProbabilities
        }
    }

    /**
     * N-ary search tree.
     */
    internal class RelativeFrequencies {

        var frequency: Float = 0F

        private var childKeys = emptyKeys

        private var childValues = emptyValues

        operator fun get(ngram: String) = getImpl(ngram, depth = 0)

        private operator fun set(ngram: String, frequency: Float) = setImpl(ngram, frequency, depth = 0)

        private fun getImpl(ngram: String, depth: Int): Float {
            if (depth == ngram.length) return frequency
            val i = childKeys.binarySearch(ngram[depth])
            return if (i >= 0) childValues[i].getImpl(ngram, depth + 1) else 0F
        }

        private fun setImpl(ngram: String, frequency: Float, depth: Int) {
            if (depth == ngram.length) {
                this.frequency = frequency
                return
            }

            var i = childKeys.binarySearch(ngram[depth])
            // insert a new child
            if (i < 0) {
                i = -i - 1
                childKeys = CharArray(childKeys.size + 1) { idx ->
                    when {
                        idx < i -> childKeys[idx]
                        idx > i -> childKeys[idx - 1]
                        else -> ngram[depth]
                    }
                }
                childValues = Array(childValues.size + 1) { idx ->
                    when {
                        idx < i -> childValues[idx]
                        idx > i -> childValues[idx - 1]
                        else -> RelativeFrequencies()
                    }
                }
            }

            // set value
            childValues[i].setImpl(ngram, frequency, depth + 1)
        }

        companion object {

            private val emptyKeys = CharArray(0)

            private val emptyValues = emptyArray<RelativeFrequencies>()

            internal fun build(relativeFrequencies: Sequence<Pair<String, Float>>): RelativeFrequencies {
                val frequencies = RelativeFrequencies()
                for ((ngram, frequency) in relativeFrequencies) {
                    frequencies[ngram] = frequency
                }
                return frequencies
            }
        }
    }
}

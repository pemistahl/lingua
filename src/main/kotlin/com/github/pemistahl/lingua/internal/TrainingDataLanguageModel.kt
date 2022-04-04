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
import java.util.HashMap
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

        fun fromJson(json: String): TrainingDataLanguageModel {
            val jsonLanguageModel = Json.decodeFromString<JsonLanguageModel>(json)

            val jsonDataSequence = sequence {
                for ((fraction, ngrams) in jsonLanguageModel.ngrams) {
                    val fractionAsFloat = fraction.toFloat()
                    for (ngram in ngrams.split(' ')) {
                        yield(ngram to fractionAsFloat)
                    }
                }
            }

            return TrainingDataLanguageModel(
                language = jsonLanguageModel.language,
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

    internal class RelativeFrequencies private constructor(private val data: Map<Long, Entries>) {

        operator fun get(ngram: String): Float = data[computeHighHash(ngram)]?.get(ngram) ?: 0F

        private class Entries(private val chars: ByteArray, private val frequencies: FloatArray) {

            val size get() = frequencies.size

            operator fun get(ngram: String): Float {
                var low = 0
                var high = size - 1
                while (low <= high) {
                    if (low + 8 < high) {
                        // bisection search
                        val middle = (low + high) / 2
                        val diff = compareNgram(middle, ngram)
                        if (diff < 0) low = middle + 1
                        else if (diff > 0) high = middle - 1
                        else return frequencies[middle]
                    } else {
                        // linear search
                        for (i in low..high) {
                            if (compareNgram(i, ngram) == 0) return frequencies[i]
                            return 0F
                        }
                    }
                }
                return 0F
            }

            /**
             * Compare lower bits only.
             */
            private fun compareNgram(pos: Int, ngram: String): Int {
                val base = pos * ngram.length
                repeat(ngram.length) { i ->
                    val diff = chars[base + i].compareTo(ngram[i].code.and(0xFF))
                    if (diff != 0) return diff
                }
                return 0
            }
        }

        companion object {

            /**
             * Compare low bits of each character.
             * String length must be the same.
             */
            private object LowByteComparator : Comparator<String> {
                override fun compare(o1: String, o2: String): Int {
                    for (i in o1.indices) {
                        val res = o1[i].code.and(0xFF) - o2[i].code.and(0XFF)
                        if (res != 0) return res
                    }
                    return 0
                }
            }

            internal fun build(relativeFrequencies: Sequence<Pair<String, Float>>): RelativeFrequencies {
                val entryMap = LinkedHashMap<Long, MutableMap<String, Float>>()
                relativeFrequencies.forEach { (ngram, frequency) ->
                    val map = entryMap.computeIfAbsent(computeHighHash(ngram)) { TreeMap(LowByteComparator) }
                    map[ngram] = frequency
                }

                val data: Map<Long, Entries> = entryMap.entries.associateTo(HashMap()) { (highHash, map) ->
                    // flatten lower bytes
                    val chars = map.keys.flatMap { ngram -> ngram.map { (it.code and 0xFF).toByte() } }.toByteArray()
                    val float = map.values.toFloatArray()
                    highHash to Entries(chars, float)
                }

                return RelativeFrequencies(data)
            }

            /**
             * Compute the unique hash of a high bits of each character
             * Max ngram supported length: 7.
             */
            private fun computeHighHash(ngram: String): Long {
                var hash = ngram.length.toLong()
                ngram.forEach { c ->
                    hash = hash.shl(8) or c.code.shr(8).toLong()
                }
                return hash
            }
        }
    }
}

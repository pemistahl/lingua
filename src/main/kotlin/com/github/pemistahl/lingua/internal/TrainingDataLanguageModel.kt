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
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import it.unimi.dsi.fastutil.objects.Object2FloatMap
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap
import okio.buffer
import okio.source
import java.io.InputStream

internal class JsonLanguageModel(val language: Language, val ngrams: Map<Fraction, String>)

internal class TrainingDataLanguageModel(
    val language: Language,
    val absoluteFrequencies: Map<Ngram, Int>,
    val relativeFrequencies: Map<Ngram, Fraction>
) {
    fun toJson(): String {
        val ngrams = mutableMapOf<Fraction, MutableList<Ngram>>()
        for ((ngram, fraction) in relativeFrequencies) {
            ngrams.computeIfAbsent(fraction) { mutableListOf() }.add(ngram)
        }
        val jsonLanguageModel = JsonLanguageModel(language, ngrams.mapValues { it.value.joinToString(separator = " ") })
        return JSON_ADAPTER.toJson(jsonLanguageModel)
    }

    companion object {
        private const val LANGUAGE_NAME = "language"
        private const val NGRAMS_NAME = "ngrams"

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
                relativeFrequencies
            )
        }

        fun fromJson(json: InputStream): Object2FloatMap<String> {
            JsonReader.of(json.source().buffer()).use { reader ->
                val frequencies = Object2FloatOpenHashMap<String>()

                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        LANGUAGE_NAME -> reader.skipValue()
                        NGRAMS_NAME -> {
                            reader.beginObject()
                            while (reader.hasNext()) {
                                val (numerator, denominator) = reader.nextName().split('/')
                                val frequency = numerator.toFloat() / denominator.toInt()
                                val ngrams = reader.nextString().split(' ')
                                for (ngram in ngrams) {
                                    frequencies.put(ngram, frequency)
                                }
                            }
                            reader.endObject()
                        }
                        else -> throw AssertionError("Unexpected name in language model JSON")
                    }
                }
                reader.endObject()

                // Trim to reduce in-memory model size
                frequencies.trim()

                return frequencies
            }
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
}

private val JSON_ADAPTER = Moshi.Builder()
    .add(FractionAdapter())
    .addLast(KotlinJsonAdapterFactory())
    .build()
    .adapter(JsonLanguageModel::class.java)

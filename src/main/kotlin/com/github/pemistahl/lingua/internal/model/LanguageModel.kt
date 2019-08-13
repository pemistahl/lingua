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

package com.github.pemistahl.lingua.internal.model

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.internal.math.Fraction
import com.github.pemistahl.lingua.internal.util.extension.inverse
import com.github.pemistahl.lingua.internal.util.extension.over
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlin.reflect.KClass

internal class LanguageModel<T : Ngram, U : Ngram> {

    val ngramAbsoluteFrequencies: Map<T, Int>
    val ngrams: Set<T>

    internal val language: Language
    internal val ngramRelativeFrequencies: Map<T, Fraction>
    internal var jsonNgramRelativeFrequencies: Map<String, Double>

    internal constructor(
        language: Language,
        ngramRelativeFrequencies: Map<String, Double>
    ) {
        this.language = language
        this.ngrams = emptySet()
        this.ngramAbsoluteFrequencies = emptyMap()
        this.ngramRelativeFrequencies = emptyMap()
        this.jsonNgramRelativeFrequencies = ngramRelativeFrequencies
    }

    internal constructor(ngrams: Set<T>) {
        this.language = Language.UNKNOWN
        this.ngrams = ngrams
        this.ngramAbsoluteFrequencies = emptyMap()
        this.ngramRelativeFrequencies = emptyMap()
        this.jsonNgramRelativeFrequencies = emptyMap()
    }

    internal constructor(
        language: Language,
        ngramAbsoluteFrequencies: Map<T, Int>,
        ngramRelativeFrequencies: Map<T, Fraction>
    ) {
        this.language = language
        this.ngrams = emptySet()
        this.ngramAbsoluteFrequencies = ngramAbsoluteFrequencies
        this.ngramRelativeFrequencies = ngramRelativeFrequencies
        this.jsonNgramRelativeFrequencies = emptyMap()
    }

    fun <T : Ngram> getRelativeFrequency(ngram: T): Double = jsonNgramRelativeFrequencies[ngram.value] ?: 0.0

    fun toJson(): String {
        val languageModelDeserializer = lazy { registerLanguageModelDeserializer(useMapDBCache = false) }
        return languageModelDeserializer.value.toJson(
            mapOf("language" to language, "ngrams" to ngramRelativeFrequencies.inverse())
        )
    }

    internal companion object {

        private val LETTER_REGEX = Regex("\\p{L}+")

        fun <T : Ngram, U : Ngram> fromTrainingData(
            text: Sequence<String>,
            language: Language,
            ngramClass: KClass<T>,
            charClass: String,
            lowerNgramAbsoluteFrequencies: Map<U, Int>
        ): LanguageModel<T, U> {
            val ngramAbsoluteFrequencies = hashMapOf<T, Int>()
            val ngramLength = Ngram.getLength(ngramClass)
            val regex = Regex("""[\p{L}&&\p{$charClass}]+""")

            for (line in text) {
                val lowerCasedLine = line.toLowerCase()

                for (i in 0..lowerCasedLine.length - ngramLength) {
                    val textSlice = lowerCasedLine.slice(i until i + ngramLength)
                    if (regex.matches(textSlice)) {
                        val ngram = Ngram.getInstance(textSlice)
                        @Suppress("UNCHECKED_CAST")
                        ngramAbsoluteFrequencies.merge(ngram as T, 1, Int::plus)
                    }
                }
            }

            val ngramRelativeFrequencies = computeConditionalProbabilities(
                ngramLength,
                ngramAbsoluteFrequencies,
                lowerNgramAbsoluteFrequencies
            )
            return LanguageModel(language, ngramAbsoluteFrequencies, ngramRelativeFrequencies)
        }

        inline fun <reified T : Ngram> fromTestData(text: Sequence<String>): LanguageModel<T, T> {
            return fromTestData(text, T::class)
        }

        fun <T : Ngram> fromTestData(text: Sequence<String>, ngramClass: KClass<T>): LanguageModel<T, T> {
            if (ngramClass == Zerogram::class) {
                @Suppress("UNCHECKED_CAST")
                return LanguageModel(setOf(Zerogram as T))
            }

            val ngrams = hashSetOf<T>()
            val ngramLength = Ngram.getLength(ngramClass)

            for (line in text) {
                val lowerCasedLine = line.toLowerCase()

                for (i in 0..lowerCasedLine.length - ngramLength) {
                    val textSlice = lowerCasedLine.slice(i until i + ngramLength)
                    if (LETTER_REGEX.matches(textSlice)) {
                        val ngram = Ngram.getInstance(textSlice)
                        @Suppress("UNCHECKED_CAST")
                        ngrams.add(ngram as T)
                    }
                }
            }
            return LanguageModel(ngrams)
        }

        fun <T : Ngram> fromJson(json: JsonReader, ngramClass: KClass<T>, useMapDBCache: Boolean): LanguageModel<T, T> {
            val languageModelDeserializer = lazy { registerLanguageModelDeserializer(useMapDBCache) }
            val type = TypeToken.getParameterized(LanguageModel::class.java, ngramClass.java).type
            return languageModelDeserializer.value.fromJson(json, type)
        }

        private fun <T : Ngram, U : Ngram> computeConditionalProbabilities(
            ngramLength: Int,
            ngrams: Map<T, Int>,
            lowerNgramAbsoluteFrequencies: Map<U, Int>
        ): Map<T, Fraction> {
            val ngramProbabilities = hashMapOf<T, Fraction>()
            val totalNgramFrequency = ngrams.values.sum()

            for ((ngram, frequency) in ngrams) {
                @Suppress("UNCHECKED_CAST")
                val denominator = if (ngramLength == 1 || lowerNgramAbsoluteFrequencies.isEmpty())
                    totalNgramFrequency
                else if (ngramLength == 2)
                    lowerNgramAbsoluteFrequencies.getValue(Unigram(ngram.value[0].toString()) as U)
                else if (ngramLength == 3)
                    lowerNgramAbsoluteFrequencies.getValue(Bigram(ngram.value.slice(0..1)) as U)
                else if (ngramLength == 4)
                    lowerNgramAbsoluteFrequencies.getValue(Trigram(ngram.value.slice(0..2)) as U)
                else if (ngramLength == 5)
                    lowerNgramAbsoluteFrequencies.getValue(Quadrigram(ngram.value.slice(0..3)) as U)
                else totalNgramFrequency
                ngramProbabilities[ngram] = frequency over denominator
            }

            return ngramProbabilities
        }

        private fun registerLanguageModelDeserializer(useMapDBCache: Boolean): Gson {
            val unigramType = TypeToken.getParameterized(LanguageModel::class.java, Unigram::class.java).type
            val bigramType = TypeToken.getParameterized(LanguageModel::class.java, Bigram::class.java).type
            val trigramType = TypeToken.getParameterized(LanguageModel::class.java, Trigram::class.java).type
            val quadrigramType = TypeToken.getParameterized(LanguageModel::class.java, Quadrigram::class.java).type
            val fivegramType = TypeToken.getParameterized(LanguageModel::class.java, Fivegram::class.java).type

            return GsonBuilder()
                .registerTypeAdapter(
                    unigramType,
                    LanguageModelDeserializer<Unigram>(useMapDBCache)
                )
                .registerTypeAdapter(
                    bigramType,
                    LanguageModelDeserializer<Bigram>(useMapDBCache)
                )
                .registerTypeAdapter(
                    trigramType,
                    LanguageModelDeserializer<Trigram>(useMapDBCache)
                )
                .registerTypeAdapter(
                    quadrigramType,
                    LanguageModelDeserializer<Quadrigram>(useMapDBCache)
                )
                .registerTypeAdapter(
                    fivegramType,
                    LanguageModelDeserializer<Fivegram>(useMapDBCache)
                )
                .create()
        }
    }
}

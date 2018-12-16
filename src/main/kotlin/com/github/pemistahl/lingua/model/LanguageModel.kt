/*
 * Copyright 2018 Peter M. Stahl
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

package com.github.pemistahl.lingua.model

import com.github.pemistahl.lingua.math.Fraction
import com.github.pemistahl.lingua.util.extension.inverse
import com.github.pemistahl.lingua.util.extension.over
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

internal class LanguageModel<T : Ngram> {

    val language: Language
    val areNgramsLowerCase: Boolean
    val areNgramsPadded: Boolean
    val ngrams: Set<T>
        get() = if (ngramRelativeFrequencies.isNotEmpty()) {
            ngramRelativeFrequencies.keys
        } else {
            field
        }

    private val ngramRelativeFrequencies: Map<T, Fraction>

    private constructor(
        language: Language,
        areNgramsLowerCase: Boolean,
        areNgramsPadded: Boolean,
        ngramRelativeFrequencies: Map<T, Fraction>
    ) {
        this.language = language
        this.areNgramsLowerCase = areNgramsLowerCase
        this.areNgramsPadded = areNgramsPadded
        this.ngrams = emptySet()
        this.ngramRelativeFrequencies = ngramRelativeFrequencies
    }

    private constructor(
        linesOfText: Sequence<String>,
        ngramLength: Int,
        areNgramsLowerCase: Boolean = true,
        areNgramsPadded: Boolean = false
    ) {
        val ngrams = hashSetOf<T>()
        val padding = PAD_CHAR.repeat(ngramLength)

        for (line in linesOfText) {
            val lowerCasedLine = if (areNgramsLowerCase)
                line.toLowerCase()
            else line

            val ngramLine = if (areNgramsPadded)
                padding + lowerCasedLine + padding
            else lowerCasedLine

            for (i in 0..ngramLine.length - ngramLength) {
                val textSlice = ngramLine.slice(i until i + ngramLength)
                val ngram = getNgramInstance<T>(ngramLength, textSlice)
                ngrams.add(ngram)
            }
        }

        this.language = Language.UNKNOWN
        this.areNgramsLowerCase = areNgramsLowerCase
        this.areNgramsPadded = areNgramsPadded
        this.ngrams = ngrams
        this.ngramRelativeFrequencies = emptyMap()
    }

    private constructor(
        linesOfText: Sequence<String>,
        ngramLength: Int,
        language: Language,
        areNgramsLowerCase: Boolean = true,
        areNgramsPadded: Boolean = false
    ) {
        val ngramAbsoluteFrequencies = hashMapOf<T, Int>()
        val padding = PAD_CHAR.repeat(ngramLength)
        val regex = Regex("[\\p{L}]+")

        for (line in linesOfText) {
            val lowerCasedLine = if (areNgramsLowerCase)
                line.toLowerCase()
            else line

            val ngramLine = if (areNgramsPadded)
                padding + lowerCasedLine + padding
            else lowerCasedLine

            for (i in 0..ngramLine.length - ngramLength) {
                val textSlice = ngramLine.slice(i until i + ngramLength)
                if (regex.matches(textSlice)) {
                    val ngram = getNgramInstance<T>(ngramLength, textSlice)
                    ngramAbsoluteFrequencies.merge(ngram, 1, Int::plus)
                }
            }
        }

        val ngramRelativeFrequencies = computeConditionalProbabilities(ngramAbsoluteFrequencies)

        this.language = language
        this.areNgramsLowerCase = areNgramsLowerCase
        this.areNgramsPadded = areNgramsPadded
        this.ngrams = emptySet()
        this.ngramRelativeFrequencies = ngramRelativeFrequencies
    }

    fun getRelativeFrequency(ngram: T): Fraction? = ngramRelativeFrequencies[ngram]

    fun <T : Ngram> toJson(ngramClass: KClass<T>): String = getGson(ngramClass).toJson(
        mapOf(
            "language" to language,
            "areNgramsLowerCase" to areNgramsLowerCase,
            "areNgramsPadded" to areNgramsPadded,
            "ngrams" to ngramRelativeFrequencies.inverse()
        )
    )

    private fun computeConditionalProbabilities(
        ngrams: Map<T, Int>
    ): Map<T, Fraction>
    {
        val ngramProbabilities = hashMapOf<T, Fraction>()
        val totalNgramFrequency = ngrams.values.sum()
        for ((ngram, frequency) in ngrams) {
            ngramProbabilities[ngram] = frequency over totalNgramFrequency
        }

        return ngramProbabilities
    }

    internal companion object {

        inline fun <reified T : Ngram> fromTestData(
            text: Sequence<String>,
            areNgramsLowerCase: Boolean = true,
            areNgramsPadded: Boolean = false
        ) = LanguageModel<T>(
            text,
            getNgramLength<T>(),
            areNgramsLowerCase,
            areNgramsPadded
        )

        inline fun <reified T : Ngram> fromTrainingData(
            text: Sequence<String>,
            language: Language,
            areNgramsLowerCase: Boolean = true,
            areNgramsPadded: Boolean = false
        ) = LanguageModel<T>(
            text,
            getNgramLength<T>(),
            language,
            areNgramsLowerCase,
            areNgramsPadded
        )

        fun <T : Ngram> fromJson(json: JsonReader, ngramClass: KClass<T>): LanguageModel<T> {
            val type = TypeToken.getParameterized(LanguageModel::class.java, ngramClass.java).type
            return getGson(ngramClass).fromJson(json, type)
        }

        private inline fun <reified T : Ngram> getNgramLength(): Int =
            when (T::class) {
                Unigram::class -> 1
                Bigram::class -> 2
                Trigram::class -> 3
                Quadrigram::class -> 4
                Fivegram::class -> 5
                else -> throw IllegalArgumentException(
                    "unsupported ngram type: ${T::class.simpleName}"
                )
            }

        private fun <T: Ngram> getNgramInstance(ngramLength: Int, value: String): T {
            val ngram = when (ngramLength) {
                1 -> Unigram(value)
                2 -> Bigram(value)
                3 -> Trigram(value)
                4 -> Quadrigram(value)
                5 -> Fivegram(value)
                else -> throw IllegalArgumentException(
                    "unsupported ngram length: $ngramLength"
                )
            }

            @Suppress("UNCHECKED_CAST")
            return ngram as T
        }


        private fun <T : Ngram> getGson(ngramClass: KClass<T>): Gson {
            val type = TypeToken.getParameterized(LanguageModel::class.java, ngramClass.java).type
            return GsonBuilder()
                .registerTypeAdapter(
                    type,
                    LanguageModelDeserializer<T>()
                )
                .create()
        }

        private const val PAD_CHAR = "<PAD>"
    }

    private class LanguageModelDeserializer<T : Ngram> : JsonDeserializer<LanguageModel<T>> {
        override fun deserialize(
            json: JsonElement,
            type: Type,
            context: JsonDeserializationContext
        ): LanguageModel<T> {

            @Suppress("UNCHECKED_CAST")
            val ngramClass = (type as ParameterizedType).actualTypeArguments[0] as Class<T>

            val jsonObj = json.asJsonObject
            val language = Language.valueOf(jsonObj["language"].asString)
            val areNgramsLowerCase = jsonObj["areNgramsLowerCase"].asBoolean
            val areNgramsPadded = jsonObj["areNgramsPadded"].asBoolean
            val ngramsJsonObj = jsonObj["ngrams"].asJsonObject

            val ngramRelativeFrequencies = hashMapOf<T, Fraction>()
            for ((fractionLiteral, ngramsJsonElem) in ngramsJsonObj.entrySet()) {
                val fraction = Fraction(fractionLiteral)
                for (ngramJsonElem in ngramsJsonElem.asJsonArray) {
                    val ngram: T? = ngramClass.getConstructor(String::class.java).newInstance(ngramJsonElem.asString)
                    ngram?.let { ngramRelativeFrequencies[ngram] = fraction }
                }
            }

            return LanguageModel(
                language,
                areNgramsLowerCase,
                areNgramsPadded,
                ngramRelativeFrequencies
            )
        }
    }
}

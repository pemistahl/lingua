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
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.lang.reflect.Type
import kotlin.reflect.full.primaryConstructor

internal class LanguageModel {

    val language: Language?
    val areNgramsLowerCase: Boolean
    val areNgramsPadded: Boolean

    val unigrams: Set<Unigram>
        get() = if (unigramRelativeFrequencies.isNotEmpty()) {
            unigramRelativeFrequencies.keys
        } else {
            field
        }

    val bigrams: Set<Bigram>
        get() = if (bigramRelativeFrequencies.isNotEmpty()) {
            bigramRelativeFrequencies.keys
        } else {
            field
        }

    val trigrams: Set<Trigram>
        get() = if (trigramRelativeFrequencies.isNotEmpty()) {
            trigramRelativeFrequencies.keys
        } else {
            field
        }

    private val unigramRelativeFrequencies: Map<Unigram, Fraction>
    private val bigramRelativeFrequencies: Map<Bigram, Fraction>
    private val trigramRelativeFrequencies: Map<Trigram, Fraction>

    private constructor(
        language: Language,
        areNgramsLowerCase: Boolean,
        areNgramsPadded: Boolean,
        unigramRelativeFrequencies: Map<Unigram, Fraction>,
        bigramRelativeFrequencies: Map<Bigram, Fraction>,
        trigramRelativeFrequencies: Map<Trigram, Fraction>
    ) {
        this.language = language
        this.areNgramsLowerCase = areNgramsLowerCase
        this.areNgramsPadded = areNgramsPadded
        this.unigramRelativeFrequencies = unigramRelativeFrequencies
        this.bigramRelativeFrequencies = bigramRelativeFrequencies
        this.trigramRelativeFrequencies = trigramRelativeFrequencies
        this.unigrams = emptySet()
        this.bigrams = emptySet()
        this.trigrams = emptySet()
    }

    private constructor(
        linesOfText: Sequence<String>,
        areNgramsLowerCase: Boolean = true,
        areNgramsPadded: Boolean = false
    ) {
        val unigrams = hashSetOf<Unigram>()
        val bigrams = hashSetOf<Bigram>()
        val trigrams = hashSetOf<Trigram>()

        for (line in linesOfText) {
            val lowerCasedLine = if (areNgramsLowerCase)
                line.toLowerCase()
            else line

            val unigramLine = if (areNgramsPadded)
                UNIGRAM_PADDING + lowerCasedLine + UNIGRAM_PADDING
            else lowerCasedLine

            val bigramLine = if (areNgramsPadded)
                BIGRAM_PADDING + lowerCasedLine + BIGRAM_PADDING
            else lowerCasedLine

            val trigramLine = if (areNgramsPadded)
                TRIGRAM_PADDING + lowerCasedLine + TRIGRAM_PADDING
            else lowerCasedLine

            collectNgrams(unigrams, unigramLine)
            collectNgrams(bigrams, bigramLine)
            collectNgrams(trigrams, trigramLine)
        }

        this.language = null
        this.areNgramsLowerCase = areNgramsLowerCase
        this.areNgramsPadded = areNgramsPadded
        this.unigramRelativeFrequencies = emptyMap()
        this.bigramRelativeFrequencies = emptyMap()
        this.trigramRelativeFrequencies = emptyMap()
        this.unigrams = unigrams
        this.bigrams = bigrams
        this.trigrams = trigrams
    }

    fun getRelativeFrequency(ngram: Ngram): Fraction? = when (ngram) {
        is Unigram -> unigramRelativeFrequencies[ngram]
        is Bigram -> bigramRelativeFrequencies[ngram]
        is Trigram -> trigramRelativeFrequencies[ngram]
    }

    fun toJson(): String = gson.toJson(
        mapOf(
            "language" to language,
            "areNgramsLowerCase" to areNgramsLowerCase,
            "areNgramsPadded" to areNgramsPadded,
            "unigrams" to unigramRelativeFrequencies.inverse(),
            "bigrams" to bigramRelativeFrequencies.inverse(),
            "trigrams" to trigramRelativeFrequencies.inverse()
        )
    )

    private inline fun <reified T : Ngram> collectNgrams(
        ngrams: MutableSet<T>,
        text: String
    ) {
        val ngramLength = getNgramLength<T>()
        for (i in 0..text.length - ngramLength) {
            val textSlice = text.slice(i until i + ngramLength)
            val ngram = T::class.primaryConstructor?.call(textSlice)
            ngram?.let { ngrams.add(it) }
        }
    }

    private inline fun <reified T : Ngram> getNgramLength(): Int =
        when (T::class) {
            Unigram::class -> 1
            Bigram::class -> 2
            Trigram::class -> 3
            else -> throw IllegalArgumentException(
                "unsupported ngram type: ${T::class.simpleName}"
            )
        }

    internal companion object {
        fun fromTestData(
            text: Sequence<String>,
            areNgramsLowerCase: Boolean = true,
            areNgramsPadded: Boolean = false
        ) = LanguageModel(
            text,
            areNgramsLowerCase,
            areNgramsPadded
        )

        fun fromJson(json: JsonReader): LanguageModel {
            val type = object : TypeToken<LanguageModel>() {}.type
            return gson.fromJson<LanguageModel>(json, type)
        }

        private val gson = GsonBuilder()
            .registerTypeAdapter(
                LanguageModel::class.java,
                LanguageModelDeserializer()
            )
            .create()

        private const val PAD_CHAR = "<PAD>"
        private const val UNIGRAM_PADDING = PAD_CHAR
        private const val BIGRAM_PADDING = "$PAD_CHAR$PAD_CHAR"
        private const val TRIGRAM_PADDING = "$PAD_CHAR$PAD_CHAR$PAD_CHAR"
    }

    private class LanguageModelDeserializer : JsonDeserializer<LanguageModel> {
        override fun deserialize(
            json: JsonElement,
            type: Type,
            context: JsonDeserializationContext
        ): LanguageModel {

            val jsonObj = json.asJsonObject

            val language = Language.valueOf(jsonObj["language"].asString)
            val areNgramsLowerCase = jsonObj["areNgramsLowerCase"].asBoolean
            val areNgramsPadded = jsonObj["areNgramsPadded"].asBoolean

            val unigramsJsonObj = jsonObj["unigrams"].asJsonObject
            val bigramsJsonObj = jsonObj["bigrams"].asJsonObject
            val trigramsJsonObj = jsonObj["trigrams"].asJsonObject

            val unigramRelativeFrequencies =
                restoreNgramFrequencies<Unigram>(unigramsJsonObj)
            val bigramRelativeFrequencies =
                restoreNgramFrequencies<Bigram>(bigramsJsonObj)
            val trigramRelativeFrequencies =
                restoreNgramFrequencies<Trigram>(trigramsJsonObj)

            return LanguageModel(
                language,
                areNgramsLowerCase,
                areNgramsPadded,
                unigramRelativeFrequencies,
                bigramRelativeFrequencies,
                trigramRelativeFrequencies
            )
        }

        private inline fun <reified T : Ngram> restoreNgramFrequencies(
            jsonObj: JsonObject
        ): Map<T, Fraction> {
            val relativeFrequencies = hashMapOf<T, Fraction>()
            for ((fractionLiteral, ngramsJsonElem) in jsonObj.entrySet()) {
                val fraction = Fraction(fractionLiteral)
                for (ngramJsonElem in ngramsJsonElem.asJsonArray) {
                    val ngram =
                        T::class.primaryConstructor?.call(ngramJsonElem.asString)
                    ngram?.let { relativeFrequencies[ngram] = fraction }
                }
            }
            return relativeFrequencies
        }
    }
}

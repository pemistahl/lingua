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
import org.mapdb.Serializer
import org.mapdb.SortedTableMap
import org.mapdb.volume.MappedFileVol
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.reflect.KClass

internal class LanguageModel<T : Ngram, U : Ngram> {

    val language: Language
    val areNgramsLowerCase: Boolean
    val areNgramsPadded: Boolean
    val ngramAbsoluteFrequencies: Map<T, Int>
    val ngrams: Set<T>
        get() = if (ngramRelativeFrequencies.isNotEmpty()) {
            ngramRelativeFrequencies.keys
        } else {
            field
        }

    private val ngramRelativeFrequencies: Map<T, Fraction>
    private val jsonNgramRelativeFrequencies: Map<String, Double>

    private constructor(
        language: Language,
        areNgramsLowerCase: Boolean,
        areNgramsPadded: Boolean,
        ngramRelativeFrequencies: Map<String, Double>
    ) {
        this.language = language
        this.areNgramsLowerCase = areNgramsLowerCase
        this.areNgramsPadded = areNgramsPadded
        this.ngrams = emptySet()
        this.ngramAbsoluteFrequencies = emptyMap()
        this.ngramRelativeFrequencies = emptyMap()
        this.jsonNgramRelativeFrequencies = ngramRelativeFrequencies
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
        this.ngramAbsoluteFrequencies = emptyMap()
        this.ngramRelativeFrequencies = emptyMap()
        this.jsonNgramRelativeFrequencies = emptyMap()
    }

    private constructor(
        linesOfText: Sequence<String>,
        ngramLength: Int,
        language: Language,
        lowerNgramAbsoluteFrequencies: Map<U, Int>,
        areNgramsLowerCase: Boolean = true,
        areNgramsPadded: Boolean = false
    ) {
        val ngramAbsoluteFrequencies = hashMapOf<T, Int>()
        val padding = PAD_CHAR.repeat(ngramLength)

        // Match only cyrillic letters
        //val regex = Regex("[\\p{L}&&\\p{IsCyrillic}]+")

        // Match only arabic letters
        //val regex = Regex("[\\p{L}&&\\p{IsArabic}]+")

        // Match only letters of latin alphabet
        val regex = Regex("[\\p{L}&&\\p{IsLatin}]+")

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

        val ngramRelativeFrequencies = computeConditionalProbabilities(
            ngramLength,
            ngramAbsoluteFrequencies,
            lowerNgramAbsoluteFrequencies
        )

        this.language = language
        this.areNgramsLowerCase = areNgramsLowerCase
        this.areNgramsPadded = areNgramsPadded
        this.ngrams = emptySet()
        this.ngramAbsoluteFrequencies = ngramAbsoluteFrequencies
        this.ngramRelativeFrequencies = ngramRelativeFrequencies
        this.jsonNgramRelativeFrequencies = emptyMap()
    }

    fun getRelativeFrequency(ngram: T): Double? = jsonNgramRelativeFrequencies[ngram.value]

    fun <T : Ngram> toJson(ngramClass: KClass<T>): String = getGson(ngramClass).toJson(
        mapOf(
            "language" to language,
            "areNgramsLowerCase" to areNgramsLowerCase,
            "areNgramsPadded" to areNgramsPadded,
            "ngrams" to ngramRelativeFrequencies.inverse()
        )
    )

    private fun computeConditionalProbabilities(
        ngramLength: Int,
        ngrams: Map<T, Int>,
        lowerNgramAbsoluteFrequencies: Map<U, Int>
    ): Map<T, Fraction>
    {
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
            else if (ngramLength == 6)
                lowerNgramAbsoluteFrequencies.getValue(Fivegram(ngram.value.slice(0..4)) as U)
            else totalNgramFrequency
            ngramProbabilities[ngram] = frequency over denominator
        }

        return ngramProbabilities
    }

    internal companion object {

        inline fun <reified T : Ngram> fromTestData(
            text: Sequence<String>,
            areNgramsLowerCase: Boolean = true,
            areNgramsPadded: Boolean = false
        ) = LanguageModel<T, T>(
            text,
            getNgramLength<T>(),
            areNgramsLowerCase,
            areNgramsPadded
        )

        inline fun <reified T : Ngram, U : Ngram> fromTrainingData(
            text: Sequence<String>,
            language: Language,
            lowerNgramAbsoluteFrequencies: Map<U, Int>,
            areNgramsLowerCase: Boolean = true,
            areNgramsPadded: Boolean = false
        ) = LanguageModel<T, U>(
            text,
            getNgramLength<T>(),
            language,
            lowerNgramAbsoluteFrequencies,
            areNgramsLowerCase,
            areNgramsPadded
        )

        fun <T : Ngram> fromJson(json: JsonReader, ngramClass: KClass<T>): LanguageModel<T, T> {
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
                Sixgram::class -> 6
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
                6 -> Sixgram(value)
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

    private class LanguageModelDeserializer<T : Ngram> : JsonDeserializer<LanguageModel<T, T>> {
        override fun deserialize(
            json: JsonElement,
            type: Type,
            context: JsonDeserializationContext
        ): LanguageModel<T, T> {

            val jsonObj = json.asJsonObject
            val language = Language.valueOf(jsonObj["language"].asString)
            val areNgramsLowerCase = jsonObj["areNgramsLowerCase"].asBoolean
            val areNgramsPadded = jsonObj["areNgramsPadded"].asBoolean

            @Suppress("UNCHECKED_CAST")
            val ngramClass = (type as ParameterizedType).actualTypeArguments[0] as Class<T>

            val userHomeDirectory = System.getProperty("user.home")
            val mapdbDirectoryPath = Paths.get(userHomeDirectory, "lingua-mapdb-files", language.isoCode)
            val mapdbFileName = "${ngramClass.simpleName}s_$language.mapdb"
            val mapdbFilePath = Paths.get(mapdbDirectoryPath.toString(), mapdbFileName)

            if (!Files.isDirectory(mapdbDirectoryPath)) {
                Files.createDirectories(mapdbDirectoryPath)
            }

            lateinit var ngramRelativeFrequencies: SortedTableMap<String, Double>

            if (Files.exists(mapdbFilePath)) {
                val volume = MappedFileVol.FACTORY.makeVolume(mapdbFilePath.toString(), true)
                ngramRelativeFrequencies = SortedTableMap.open(volume, Serializer.STRING, Serializer.DOUBLE)
            }
            else {
                val volume = MappedFileVol.FACTORY.makeVolume(mapdbFilePath.toString(), false)
                val sink: SortedTableMap.Sink<String, Double> = SortedTableMap
                    .create(volume, Serializer.STRING, Serializer.DOUBLE)
                    .pageSize(1*1024) // page size of 1KB
                    .nodeSize(1)
                    .createFromSink()

                val ngramsJsonObj = jsonObj["ngrams"].asJsonObject
                val tempMap = hashMapOf<String, Double>()

                for ((fractionLiteral, ngramsJsonElem) in ngramsJsonObj.entrySet()) {
                    val fractionParts = fractionLiteral.split('/').map { it.toInt() }
                    val probability = fractionParts[0].toDouble() / fractionParts[1]
                    for (ngramJsonElem in ngramsJsonElem.asString.split(' ')) {
                        tempMap[ngramJsonElem] = probability
                    }
                }

                for ((key, value) in tempMap.toList().sortedBy { it.first }) {
                    sink.put(key, value)
                }

                ngramRelativeFrequencies = sink.create()
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

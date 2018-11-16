package com.github.pemistahl.lingua.model

import com.github.pemistahl.lingua.util.extension.Fraction
import com.github.pemistahl.lingua.util.extension.inverse
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import org.apache.commons.math3.fraction.Fraction
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
            val preprocessedLineForUnigrams =
                preprocess<Unigram>(line, areNgramsLowerCase, areNgramsPadded)
            val preprocessedLineForBigrams =
                preprocess<Bigram>(line, areNgramsLowerCase, areNgramsPadded)
            val preprocessedLineForTrigrams =
                preprocess<Trigram>(line, areNgramsLowerCase, areNgramsPadded)

            collectNgrams(unigrams, preprocessedLineForUnigrams)
            collectNgrams(bigrams, preprocessedLineForBigrams)
            collectNgrams(trigrams, preprocessedLineForTrigrams)
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

    private inline fun <reified T : Ngram> preprocess(
        text: String,
        areNgramsLowerCase: Boolean,
        areNgramsPadded: Boolean
    ): String {
        val padding = "<PAD>".repeat(getNgramLength<T>() - 1)
        return when {
            areNgramsPadded && areNgramsLowerCase -> padding + text.toLowerCase() + padding
            areNgramsPadded -> padding + text + padding
            areNgramsLowerCase -> text.toLowerCase()
            else -> text
        }
    }

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

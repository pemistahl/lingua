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

package com.github.pemistahl.lingua.api

import com.github.pemistahl.lingua.api.Language.ALBANIAN
import com.github.pemistahl.lingua.api.Language.BASQUE
import com.github.pemistahl.lingua.api.Language.BENGALI
import com.github.pemistahl.lingua.api.Language.BOKMAL
import com.github.pemistahl.lingua.api.Language.CATALAN
import com.github.pemistahl.lingua.api.Language.CHINESE
import com.github.pemistahl.lingua.api.Language.CROATIAN
import com.github.pemistahl.lingua.api.Language.CZECH
import com.github.pemistahl.lingua.api.Language.DANISH
import com.github.pemistahl.lingua.api.Language.ESTONIAN
import com.github.pemistahl.lingua.api.Language.FINNISH
import com.github.pemistahl.lingua.api.Language.FRENCH
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.GREEK
import com.github.pemistahl.lingua.api.Language.GUJARATI
import com.github.pemistahl.lingua.api.Language.HEBREW
import com.github.pemistahl.lingua.api.Language.HUNGARIAN
import com.github.pemistahl.lingua.api.Language.ICELANDIC
import com.github.pemistahl.lingua.api.Language.IRISH
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.JAPANESE
import com.github.pemistahl.lingua.api.Language.KOREAN
import com.github.pemistahl.lingua.api.Language.LATVIAN
import com.github.pemistahl.lingua.api.Language.LITHUANIAN
import com.github.pemistahl.lingua.api.Language.NORWEGIAN
import com.github.pemistahl.lingua.api.Language.NYNORSK
import com.github.pemistahl.lingua.api.Language.POLISH
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.PUNJABI
import com.github.pemistahl.lingua.api.Language.ROMANIAN
import com.github.pemistahl.lingua.api.Language.SLOVAK
import com.github.pemistahl.lingua.api.Language.SLOVENE
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.TAMIL
import com.github.pemistahl.lingua.api.Language.TELUGU
import com.github.pemistahl.lingua.api.Language.THAI
import com.github.pemistahl.lingua.api.Language.TURKISH
import com.github.pemistahl.lingua.api.Language.UNKNOWN
import com.github.pemistahl.lingua.api.Language.VIETNAMESE
import com.github.pemistahl.lingua.internal.Alphabet
import com.github.pemistahl.lingua.internal.model.Bigram
import com.github.pemistahl.lingua.internal.model.Fivegram
import com.github.pemistahl.lingua.internal.model.LanguageModel
import com.github.pemistahl.lingua.internal.model.Ngram
import com.github.pemistahl.lingua.internal.model.Quadrigram
import com.github.pemistahl.lingua.internal.model.Trigram
import com.github.pemistahl.lingua.internal.model.Unigram
import com.github.pemistahl.lingua.internal.util.extension.containsAnyOf
import com.github.pemistahl.lingua.internal.util.readJsonResource
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.reflect.KClass

class LanguageDetector internal constructor(
    internal val languages: MutableSet<Language>,
    internal val minimumRelativeDistance: Double,
    internal val isCachedByMapDB: Boolean,
    internal val numberOfLoadedLanguages: Int = languages.size
) {
    private val languagesWithUniqueCharacters = languages.filter { it.uniqueCharacters.isNotEmpty() }.asSequence()

    internal val unigramLanguageModels = loadLanguageModels(Unigram::class)
    internal val bigramLanguageModels = loadLanguageModels(Bigram::class)
    internal val trigramLanguageModels = loadLanguageModels(Trigram::class)
    internal val quadrigramLanguageModels = loadLanguageModels(Quadrigram::class)
    internal val fivegramLanguageModels = loadLanguageModels(Fivegram::class)

    fun detectLanguagesOf(texts: Iterable<String>): List<Language> = texts.map { detectLanguageOf(it) }

    fun detectLanguageOf(text: String): Language {
        val trimmedText = text
            .trim()
            .toLowerCase()
            .replace(PUNCTUATION, "")
            .replace(NUMBERS, "")
            .replace(MULTIPLE_WHITESPACE, " ")

        if (trimmedText.isEmpty() || NO_LETTER.matches(trimmedText)) return UNKNOWN

        val words = if (trimmedText.contains(' ')) trimmedText.split(" ") else listOf(trimmedText)

        val languageDetectedByRules = detectLanguageWithRules(words)
        if (languageDetectedByRules != UNKNOWN) return languageDetectedByRules

        val languagesSequence = filterLanguagesByRules(words)

        val textSequence = trimmedText.lineSequence()
        val allProbabilities = mutableListOf<Map<Language, Double>>()
        val unigramCountsOfInputText = mutableMapOf<Language, Int>()

        if (trimmedText.length >= 1) {
            val unigramLanguageModel = LanguageModel.fromTestData(textSequence, Unigram::class)
            addNgramProbabilities(allProbabilities, languagesSequence, unigramLanguageModel)
            countUnigramsOfInputText(unigramCountsOfInputText, unigramLanguageModel, languagesSequence)
        }
        if (trimmedText.length >= 2) {
            addNgramProbabilities(allProbabilities, languagesSequence, LanguageModel.fromTestData(textSequence, Bigram::class))
        }
        if (trimmedText.length >= 3) {
            addNgramProbabilities(allProbabilities, languagesSequence, LanguageModel.fromTestData(textSequence, Trigram::class))
        }
        if (trimmedText.length >= 4) {
            addNgramProbabilities(allProbabilities, languagesSequence, LanguageModel.fromTestData(textSequence, Quadrigram::class))
        }
        if (trimmedText.length >= 5) {
            addNgramProbabilities(allProbabilities, languagesSequence, LanguageModel.fromTestData(textSequence, Fivegram::class))
        }

        return getMostLikelyLanguage(allProbabilities, unigramCountsOfInputText, languagesSequence)
    }

    internal fun addLanguageModel(language: Language) {
        languages.add(language)
        if (!unigramLanguageModels.containsKey(language)) {
            unigramLanguageModels[language] = lazy { loadLanguageModel(language, Unigram::class) }
            bigramLanguageModels[language] = lazy { loadLanguageModel(language, Bigram::class) }
            trigramLanguageModels[language] = lazy { loadLanguageModel(language, Trigram::class) }
            quadrigramLanguageModels[language] = lazy { loadLanguageModel(language, Quadrigram::class) }
            fivegramLanguageModels[language] = lazy { loadLanguageModel(language, Fivegram::class) }
        }
    }

    internal fun removeLanguageModel(language: Language) {
        if (languages.contains(language)) {
            languages.remove(language)
        }
    }

    internal fun countUnigramsOfInputText(
        unigramCounts: MutableMap<Language, Int>,
        unigramLanguageModel: LanguageModel<Unigram, Unigram>,
        languagesSequence: Sequence<Language>
    ) {
        for (language in languagesSequence) {
            for (unigram in unigramLanguageModel.ngrams) {
                val probability = lookUpNgramProbability(language, unigram)
                if (probability > 0) {
                    unigramCounts.merge(language, 1, Int::plus)
                }
            }
        }
    }

    internal fun <T : Ngram> addNgramProbabilities(
        probabilities: MutableList<Map<Language, Double>>,
        languagesSequence: Sequence<Language>,
        testDataModel: LanguageModel<T, T>
    ) {
        val ngramProbabilities = computeLanguageProbabilities(testDataModel, languagesSequence)
        if (!ngramProbabilities.containsValue(0.0)) {
            probabilities.add(ngramProbabilities)
        }
    }

    internal fun getMostLikelyLanguage(
        probabilities: List<Map<Language, Double>>,
        unigramCountsOfInputText: Map<Language, Int>,
        languagesSequence: Sequence<Language>
    ): Language {
        val summedUpProbabilities = hashMapOf<Language, Double>()
        for (language in languagesSequence) {
            summedUpProbabilities[language] = probabilities.sumByDouble { it[language] ?: 0.0 }

            if (unigramCountsOfInputText.containsKey(language)) {
                summedUpProbabilities[language] = summedUpProbabilities.getValue(language) / unigramCountsOfInputText.getValue(language)
            }
        }

        val filteredProbabilities = summedUpProbabilities.asSequence().filter { it.value != 0.0 }

        return when {
            filteredProbabilities.none() -> UNKNOWN
            filteredProbabilities.singleOrNull() != null -> filteredProbabilities.first().key
            else -> {
                val mostLikelyLanguage = filteredProbabilities.maxBy { it.value }!!
                val secondMostLikelyLanguage = filteredProbabilities.filterNot { it.key == mostLikelyLanguage.key }.maxBy { it.value }!!
                when {
                    mostLikelyLanguage.value == secondMostLikelyLanguage.value -> UNKNOWN
                    1.0 - (mostLikelyLanguage.value / secondMostLikelyLanguage.value) >= minimumRelativeDistance -> mostLikelyLanguage.key
                    else -> UNKNOWN
                }
            }
        }
    }

    internal fun detectLanguageWithRules(words: List<String>): Language {
        val languageCharCounts = mutableMapOf<Language, Int>()
        val minimalRequiredCharCount = ceil(words.joinToString(separator = "").length / 2.0)

        for (word in words) {
            when {
                Alphabet.BENGALI.matches(word) -> languageCharCounts.addCharCount(word, BENGALI)
                Alphabet.CHINESE.matches(word) -> languageCharCounts.addCharCount(word, CHINESE)
                Alphabet.GREEK.matches(word) -> languageCharCounts.addCharCount(word, GREEK)
                Alphabet.GUJARATI.matches(word) -> languageCharCounts.addCharCount(word, GUJARATI)
                Alphabet.GURMUKHI.matches(word) -> languageCharCounts.addCharCount(word, PUNJABI)
                Alphabet.HEBREW.matches(word) -> languageCharCounts.addCharCount(word, HEBREW)
                Alphabet.JAPANESE.matches(word) -> languageCharCounts.addCharCount(word, JAPANESE)
                Alphabet.KOREAN.matches(word) -> languageCharCounts.addCharCount(word, KOREAN)
                Alphabet.TAMIL.matches(word) -> languageCharCounts.addCharCount(word, TAMIL)
                Alphabet.TELUGU.matches(word) -> languageCharCounts.addCharCount(word, TELUGU)
                Alphabet.THAI.matches(word) -> languageCharCounts.addCharCount(word, THAI)
                Alphabet.LATIN.matches(word) -> languagesWithUniqueCharacters.filter {
                    word.containsAnyOf(it.uniqueCharacters)
                }.forEach {
                    languageCharCounts.addCharCount(word, it)
                }
            }
        }

        val languagesWithMinimumRequiredCharCountExist = languageCharCounts
            .asSequence()
            .filter { it.value >= minimalRequiredCharCount }
            .count() > 0

        return if (languagesWithMinimumRequiredCharCountExist)
            languageCharCounts.toList().sortedByDescending { it.second }.first().first
        else
            UNKNOWN
    }

    internal fun filterLanguagesByRules(words: List<String>): Sequence<Language> {
        for (word in words) {
            if (Alphabet.CYRILLIC.matches(word)) {
                return languages.asSequence().filter { it.alphabet == Alphabet.CYRILLIC }
            } else if (Alphabet.ARABIC.matches(word)) {
                return languages.asSequence().filter { it.alphabet == Alphabet.ARABIC }
            } else if (Alphabet.CHINESE.matches(word) || Alphabet.JAPANESE.matches(word)) {
                return languages.asSequence().filter { it.alphabet == Alphabet.CHINESE }
            } else if (Alphabet.LATIN.matches(word)) {
                val temp = if (languages.contains(NORWEGIAN)) {
                    languages.asSequence().filter { it.alphabet == Alphabet.LATIN && it !in setOf(BOKMAL, NYNORSK) }
                } else if (languages.contains(BOKMAL) || languages.contains(NYNORSK)) {
                    languages.asSequence().filter { it.alphabet == Alphabet.LATIN && it != NORWEGIAN }
                } else {
                    languages.asSequence().filter { it.alphabet == Alphabet.LATIN }
                }

                val languagesSubset = mutableSetOf<Language>()
                for ((characters, languages) in CHARS_TO_LANGUAGES_MAPPING) {
                    if (word.containsAnyOf(characters)) {
                        languagesSubset.addAll(languages)
                    }
                }
                return if (languagesSubset.isNotEmpty()) temp.filter { it in languagesSubset } else temp
            }
        }
        return languages.asSequence()
    }

    internal fun <T : Ngram> computeLanguageProbabilities(
        testDataModel: LanguageModel<T, T>,
        languagesSequence: Sequence<Language>
    ): Map<Language, Double> {
        val probabilities = hashMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = computeSumOfNgramProbabilities(language, testDataModel.ngrams)
        }
        return probabilities
    }

    internal fun <T : Ngram> computeSumOfNgramProbabilities(
        language: Language,
        ngrams: Set<T>
    ): Double {
        val probabilities = mutableListOf<Double>()

        for (ngram in ngrams) {
            for (elem in ngram.rangeOfLowerOrderNgrams()) {
                val probability = lookUpNgramProbability(language, elem)
                if (probability > 0) {
                    probabilities.add(probability)
                    break
                }
            }
        }
        return probabilities.sumByDouble { ln(it) }
    }

    internal fun <T : Ngram> lookUpNgramProbability(
        language: Language,
        ngram: T
    ): Double {
        val languageModels = when (ngram.length) {
            5 -> fivegramLanguageModels
            4 -> quadrigramLanguageModels
            3 -> trigramLanguageModels
            2 -> bigramLanguageModels
            1 -> unigramLanguageModels
            0 -> throw IllegalArgumentException("Zerogram detected")
            else -> throw IllegalArgumentException("unsupported ngram type detected: $ngram")
        }

        return languageModels.getValue(language).value?.getRelativeFrequency(ngram) ?: 0.0
    }

    internal fun <T : Ngram> loadLanguageModel(
        language: Language,
        ngramClass: KClass<T>
    ): LanguageModel<T, T>? {
        val fileName = "${ngramClass.simpleName?.toLowerCase()}s.json"
        val jsonResource = readJsonResource(
            "/language-models/${language.isoCode}/$fileName"
        )
        return if (jsonResource != null)
            LanguageModel.fromJson(jsonResource, ngramClass, isCachedByMapDB)
        else
            null
    }

    internal fun <T : Ngram> loadLanguageModels(ngramClass: KClass<T>): MutableMap<Language, Lazy<LanguageModel<T,T>?>> {
        val languageModels = hashMapOf<Language, Lazy<LanguageModel<T, T>?>>()
        for (language in languages) {
            languageModels[language] = lazy { loadLanguageModel(language, ngramClass) }
        }
        return languageModels
    }

    private fun MutableMap<Language, Int>.addCharCount(word: String, language: Language) {
        this.merge(language, word.length, Int::plus)
    }

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is LanguageDetector -> false
        languages != other.languages -> false
        minimumRelativeDistance != other.minimumRelativeDistance -> false
        isCachedByMapDB != other.isCachedByMapDB -> false
        else -> true
    }

    override fun hashCode() = 31 * languages.hashCode() + minimumRelativeDistance.hashCode() + isCachedByMapDB.hashCode()

    internal companion object {
        private val NO_LETTER = Regex("^[^\\p{L}]+$")
        private val PUNCTUATION = Regex("\\p{P}")
        private val NUMBERS = Regex("\\p{N}")
        private val MULTIPLE_WHITESPACE = Regex("\\s+")

        private val CHARS_TO_LANGUAGES_MAPPING = mapOf(
            "Ćć" to setOf(CROATIAN, POLISH),
            "Đđ" to setOf(CROATIAN, VIETNAMESE),
            "Ãã" to setOf(PORTUGUESE, VIETNAMESE),
            "ĄąĘę" to setOf(LITHUANIAN, POLISH),
            "Ūū" to setOf(LATVIAN, LITHUANIAN),
            "Şş" to setOf(ROMANIAN, TURKISH),
            "Żż" to setOf(POLISH, ROMANIAN),
            "Îî" to setOf(FRENCH, ROMANIAN),
            "Ìì" to setOf(ITALIAN, VIETNAMESE),
            "Ññ" to setOf(BASQUE, SPANISH),
            "ŇňŤť" to setOf(CZECH, SLOVAK),
            "Ăă" to setOf(ROMANIAN, VIETNAMESE),

            "Ďď" to setOf(CZECH, ROMANIAN, SLOVAK),
            "ÐðÞþ" to setOf(ICELANDIC, LATVIAN, TURKISH),
            "Ûû" to setOf(FRENCH, HUNGARIAN, LATVIAN),
            "Êê" to setOf(FRENCH, PORTUGUESE, VIETNAMESE),
            "ÈèÙù" to setOf(FRENCH, ITALIAN, VIETNAMESE),

            "Õõ" to setOf(ESTONIAN, HUNGARIAN, PORTUGUESE, VIETNAMESE),
            "Òò" to setOf(CATALAN, ITALIAN, LATVIAN, VIETNAMESE),
            "Ôô" to setOf(FRENCH, PORTUGUESE, SLOVAK, VIETNAMESE),
            "Øø" to setOf(BOKMAL, DANISH, NORWEGIAN, NYNORSK),

            "Ýý" to setOf(CZECH, ICELANDIC, SLOVAK, TURKISH, VIETNAMESE),
            "Ää" to setOf(ESTONIAN, FINNISH, GERMAN, SLOVAK, SWEDISH),
            "Ââ" to setOf(LATVIAN, PORTUGUESE, ROMANIAN, TURKISH, VIETNAMESE),
            "Àà" to setOf(CATALAN, FRENCH, ITALIAN, PORTUGUESE, VIETNAMESE),
            "Üü" to setOf(CATALAN, ESTONIAN, GERMAN, HUNGARIAN, TURKISH),
            "Ææ" to setOf(BOKMAL, DANISH, ICELANDIC, NORWEGIAN, NYNORSK),
            "Åå" to setOf(BOKMAL, DANISH, NORWEGIAN, NYNORSK, SWEDISH),

            "ČčŠšŽž" to setOf(CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE),

            "Çç" to setOf(ALBANIAN, BASQUE, CATALAN, FRENCH, LATVIAN, PORTUGUESE, TURKISH),
            "Öö" to setOf(ESTONIAN, FINNISH, GERMAN, HUNGARIAN, ICELANDIC, SWEDISH, TURKISH),

            "ÁáÍíÚú" to setOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, VIETNAMESE),
            "Óó" to setOf(CATALAN, HUNGARIAN, ICELANDIC, IRISH, POLISH, PORTUGUESE, SLOVAK, VIETNAMESE),

            "Éé" to setOf(CATALAN, CZECH, FRENCH, HUNGARIAN, ICELANDIC, IRISH, ITALIAN, PORTUGUESE, SLOVAK, VIETNAMESE)
        )
    }
}

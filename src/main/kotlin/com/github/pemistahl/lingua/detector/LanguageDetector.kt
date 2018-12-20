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

package com.github.pemistahl.lingua.detector

import com.github.pemistahl.lingua.math.Fraction
import com.github.pemistahl.lingua.model.Bigram
import com.github.pemistahl.lingua.model.Fivegram
import com.github.pemistahl.lingua.model.Language
import com.github.pemistahl.lingua.model.LanguageModel
import com.github.pemistahl.lingua.model.Ngram
import com.github.pemistahl.lingua.model.Quadrigram
import com.github.pemistahl.lingua.model.Trigram
import com.github.pemistahl.lingua.model.Unigram
import com.github.pemistahl.lingua.util.extension.asJsonResource
import kotlin.reflect.KClass

class LanguageDetector private constructor(
    internal val languages: MutableSet<Language>,
    val numberOfLoadedLanguages: Int = languages.size
) {
    internal var languagesSequence = languages.asSequence()

    private val unigramLanguageModels = loadLanguageModels(languages, Unigram::class)
    private val bigramLanguageModels = loadLanguageModels(languages, Bigram::class)
    private val trigramLanguageModels = loadLanguageModels(languages, Trigram::class)

    private val quadrigramLanguageModels by lazy { loadLanguageModels(languages, Quadrigram::class) }
    private val fivegramLanguageModels by lazy { loadLanguageModels(languages, Fivegram::class) }

    fun detectLanguageOf(text: String): Language {
        val trimmedText = text.trim().toLowerCase()
        val textSequence = trimmedText.lineSequence()

        val languageDetectedByRules = detectLanguageWithRules(trimmedText)
        if (languageDetectedByRules != Language.UNKNOWN) return languageDetectedByRules

        languagesSequence = languagesSequence.filterNot { it.isExcludedFromDetection }

        val unigramTestDataModel = LanguageModel.fromTestData<Unigram>(textSequence)
        val bigramTestDataModel = LanguageModel.fromTestData<Bigram>(textSequence)
        val trigramTestDataModel = LanguageModel.fromTestData<Trigram>(textSequence)

        val unigramProbabilities = computeUnigramProbabilities(unigramTestDataModel)
        val bigramProbabilities = computeBigramProbabilities(bigramTestDataModel)
        val trigramProbabilities = computeTrigramProbabilities(trigramTestDataModel)

        val allProbabilities = mutableListOf(unigramProbabilities, bigramProbabilities, trigramProbabilities)

        if (trimmedText.length in 4..60) {
            val quadrigramTestDataModel = LanguageModel.fromTestData<Quadrigram>(textSequence)
            val quadrigramProbabilities = computeQuadrigramProbabilities(quadrigramTestDataModel)
            allProbabilities.add(quadrigramProbabilities)
        }
        if (trimmedText.length in 5..60) {
            val fivegramTestDataModel = LanguageModel.fromTestData<Fivegram>(textSequence)
            val fivegramProbabilities = computeFivegramProbabilities(fivegramTestDataModel)
            allProbabilities.add(fivegramProbabilities)
        }

        val summedUpProbabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            summedUpProbabilities[language] = allProbabilities.sumByDouble { it.getValue(language) }
        }

        languagesSequence = languages.asSequence()
        languagesSequence.forEach { it.isExcludedFromDetection = false }

        return getMostLikelyLanguage(summedUpProbabilities)
    }

    internal fun addLanguageModel(language: Language) {
        if (!unigramLanguageModels.containsKey(language)) {
            languages.add(language)
            languagesSequence = languages.asSequence()
            unigramLanguageModels[language] = loadLanguageModel(language, Unigram::class)
            bigramLanguageModels[language] = loadLanguageModel(language, Bigram::class)
            trigramLanguageModels[language] = loadLanguageModel(language, Trigram::class)
            quadrigramLanguageModels[language] = loadLanguageModel(language, Quadrigram::class)
            fivegramLanguageModels[language] = loadLanguageModel(language, Fivegram::class)
        }
    }

    internal fun removeLanguageModel(language: Language) {
        if (languages.contains(language)) {
            languages.remove(language)
            languagesSequence = languages.asSequence()
        }
    }

    private fun getMostLikelyLanguage(probabilities: Map<Language, Double>): Language {
        return probabilities.maxBy { (_, value) -> value }!!.key
    }

    private fun detectLanguageWithRules(text: String): Language {
        if (text.isEmpty() || NO_LETTER_REGEX.matches(text)) return Language.UNKNOWN
        val splitText = if (text.contains(' ')) text.split(" ") else listOf(text)

        for (word in splitText) {
            if (word.contains(ENGLISH_GRAPHS) || word.endsWith("ing")) return Language.ENGLISH
            if (word.contains(FRENCH_UNIQUE_CHARS)) return Language.FRENCH
            if (word.contains(GERMAN_UNIQUE_CHARS) || word.endsWith("ung")) return Language.GERMAN
            if (word.contains(ITALIAN_GRAPHS)) return Language.ITALIAN
            if (word.contains(PORTUGUESE_UNIQUE_CHARS)) return Language.PORTUGUESE
            if (word.contains(SPANISH_UNIQUE_CHARS)) return Language.SPANISH

            if (word.contains("ç")) {
                languagesSequence.filterNot {
                    it == Language.FRENCH || it == Language.PORTUGUESE
                }.forEach { it.isExcludedFromDetection = true }
                break
            }

            if (word.endsWith("ed")) {
                languagesSequence.filterNot {
                    it == Language.ENGLISH || it == Language.SPANISH
                }.forEach { it.isExcludedFromDetection = true }
                break
            }

            if (word.contains("sch")) {
                languagesSequence.filterNot {
                    it == Language.ENGLISH || it == Language.GERMAN || it == Language.ITALIAN
                }.forEach { it.isExcludedFromDetection = true }
                break
            }
        }

        return Language.UNKNOWN
    }

    private fun computeUnigramProbabilities(
        unigramTestDataModel: LanguageModel<Unigram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = unigramTestDataModel.ngrams.map { unigram ->
                lookUpNgramProbabilities(
                    language,
                    unigram,
                    ::lookUpUnigramProbability
                )
            }.sum()
        }
        return probabilities
    }

    private fun computeBigramProbabilities(
        bigramTestDataModel: LanguageModel<Bigram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = bigramTestDataModel.ngrams.map { bigram ->
                lookUpNgramProbabilities(
                    language,
                    bigram,
                    ::lookUpBigramProbability,
                    ::lookUpUnigramProbability
                )
            }.sum()
        }
        return probabilities
    }

    private fun computeTrigramProbabilities(
        trigramTestDataModel: LanguageModel<Trigram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = trigramTestDataModel.ngrams.map { trigram ->
                lookUpNgramProbabilities(
                    language,
                    trigram,
                    ::lookUpTrigramProbability,
                    ::lookUpBigramProbability,
                    ::lookUpUnigramProbability
                )
            }.sum()
        }
        return probabilities
    }

    private fun computeQuadrigramProbabilities(
        quadrigramTestDataModel: LanguageModel<Quadrigram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = quadrigramTestDataModel.ngrams.map { quadrigram ->
                lookUpNgramProbabilities(
                    language,
                    quadrigram,
                    ::lookUpQuadrigramProbability,
                    ::lookUpTrigramProbability,
                    ::lookUpBigramProbability,
                    ::lookUpUnigramProbability
                )
            }.sum()
        }
        return probabilities
    }

    private fun computeFivegramProbabilities(
        fivegramTestDataModel: LanguageModel<Fivegram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = fivegramTestDataModel.ngrams.map { fivegram ->
                lookUpNgramProbabilities(
                    language,
                    fivegram,
                    ::lookUpFivegramProbability,
                    ::lookUpQuadrigramProbability,
                    ::lookUpTrigramProbability,
                    ::lookUpBigramProbability,
                    ::lookUpUnigramProbability
                )
            }.sum()
        }
        return probabilities
    }

    private fun lookUpNgramProbabilities(
        language: Language,
        ngram: Ngram,
        vararg lookUpFunctions: (Language, Ngram) -> Fraction?
    ): Double {
        for (lookUpFunction in lookUpFunctions) {
            val probability = lookUpFunction(language, ngram)
            if (probability != null) return probability.log()
        }
        return 0.0
    }

    private fun <T : Ngram> lookUpNgramProbability(
        languageModels: Map<Language, LanguageModel<T>>,
        language: Language,
        ngram: T
    ): Fraction? = languageModels.getValue(language).getRelativeFrequency(ngram)

    private fun lookUpUnigramProbability(language: Language, ngram: Ngram): Fraction? {
        val unigram = if (ngram is Unigram) ngram else Unigram(ngram.value[0].toString())
        return lookUpNgramProbability(unigramLanguageModels, language, unigram)
    }

    private fun lookUpBigramProbability(language: Language, ngram: Ngram): Fraction? {
        val bigram = if (ngram is Bigram) ngram else Bigram(ngram.value.slice(0..1))
        return lookUpNgramProbability(bigramLanguageModels, language, bigram)
    }

    private fun lookUpTrigramProbability(language: Language, ngram: Ngram): Fraction? {
        val trigram = if (ngram is Trigram) ngram else Trigram(ngram.value.slice(0..2))
        return lookUpNgramProbability(trigramLanguageModels, language, trigram)
    }

    private fun lookUpQuadrigramProbability(language: Language, ngram: Ngram): Fraction? {
        val quadrigram = if (ngram is Quadrigram) ngram else Quadrigram(ngram.value.slice(0..3))
        return lookUpNgramProbability(quadrigramLanguageModels, language, quadrigram)
    }

    private fun lookUpFivegramProbability(language: Language, ngram: Ngram): Fraction? {
        val fivegram = if (ngram is Fivegram) ngram else Fivegram(ngram.value.slice(0..4))
        return lookUpNgramProbability(fivegramLanguageModels, language, fivegram)
    }

    companion object {
        @JvmStatic
        fun fromAllBuiltInLanguages() = LanguageDetector(
            Language.values().toSet().minus(Language.UNKNOWN).toMutableSet()
        )

        @JvmStatic
        fun fromAllBuiltInSpokenLanguages() = LanguageDetector(
            Language.values().toSet().minus(arrayOf(Language.UNKNOWN, Language.LATIN)).toMutableSet()
        )

        @JvmStatic
        fun fromAllBuiltInLanguagesWithout(language: Language, vararg languages: Language): LanguageDetector {
            val languagesToLoad = Language.values().toSet().minus(arrayOf(Language.UNKNOWN, language, *languages)).toMutableSet()
            require(languagesToLoad.size > 1) { MISSING_LANGUAGE_MESSAGE }
            return LanguageDetector(languagesToLoad)
        }

        @JvmStatic
        fun fromLanguages(language: Language, vararg languages: Language): LanguageDetector {
            require(languages.isNotEmpty()) { MISSING_LANGUAGE_MESSAGE }
            return LanguageDetector(arrayOf(language, *languages).toMutableSet())
        }

        @JvmStatic
        fun supportedLanguages() = Language.values().toSet().minus(Language.UNKNOWN)

        private fun <T : Ngram> loadLanguageModel(
            language: Language,
            ngramClass: KClass<T>
        ): LanguageModel<T> {
            var languageModel: LanguageModel<T>? = null
            val fileName = "${ngramClass.simpleName!!.toLowerCase()}s.json"
            "/language-models/${language.isoCode}/$fileName".asJsonResource { jsonReader ->
                languageModel = LanguageModel.fromJson(jsonReader, ngramClass)
            }
            return languageModel!!
        }

        private fun <T : Ngram> loadLanguageModels(
            languages: Set<Language>,
            ngramClass: KClass<T>
        ): MutableMap<Language, LanguageModel<T>> {
            val languageModels = hashMapOf<Language, LanguageModel<T>>()
            for (language in languages) {
                languageModels[language] = loadLanguageModel(language, ngramClass)
            }
            return languageModels
        }

        private const val MISSING_LANGUAGE_MESSAGE = "LanguageDetector needs at least 2 languages to choose from"
        private val NO_LETTER_REGEX = Regex("^[^\\p{L}]+$")
        private val ENGLISH_GRAPHS = Regex("[ao]ugh")
        private val FRENCH_UNIQUE_CHARS = Regex("[ÆæŒœŸÿ]+")
        private val GERMAN_UNIQUE_CHARS = Regex("[AÖÜäöüß]+")
        private val ITALIAN_GRAPHS = Regex("cch|ggh|zz")
        private val PORTUGUESE_UNIQUE_CHARS = Regex("[ãõ]+")
        private val SPANISH_UNIQUE_CHARS = Regex("[ñ¿¡]+")
    }
}

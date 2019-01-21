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

import com.github.pemistahl.lingua.api.Language.*
import com.github.pemistahl.lingua.internal.model.Bigram
import com.github.pemistahl.lingua.internal.model.Fivegram
import com.github.pemistahl.lingua.internal.model.LanguageModel
import com.github.pemistahl.lingua.internal.model.Ngram
import com.github.pemistahl.lingua.internal.model.Quadrigram
import com.github.pemistahl.lingua.internal.model.Trigram
import com.github.pemistahl.lingua.internal.model.Unigram
import com.github.pemistahl.lingua.internal.util.extension.asJsonResource
import kotlin.reflect.KClass

class LanguageDetector internal constructor(
    internal val languages: MutableSet<Language>,
    private val isCachedByMapDB: Boolean,
    internal val numberOfLoadedLanguages: Int = languages.size
) {
    private var languagesSequence = languages.asSequence()

    private lateinit var unigramLanguageModels: MutableMap<Language, LanguageModel<Unigram, Unigram>>
    private lateinit var bigramLanguageModels: MutableMap<Language, LanguageModel<Bigram, Bigram>>
    private lateinit var trigramLanguageModels: MutableMap<Language, LanguageModel<Trigram, Trigram>>
    private lateinit var quadrigramLanguageModels: MutableMap<Language, LanguageModel<Quadrigram, Quadrigram>>
    private lateinit var fivegramLanguageModels: MutableMap<Language, LanguageModel<Fivegram, Fivegram>>

    fun detectLanguagesOf(texts: Iterable<String>): List<Language> = texts.map { detectLanguageOf(it) }

    fun detectLanguageOf(text: String): Language {
        val trimmedText = text.trim().toLowerCase()
        if (trimmedText.isEmpty() || NO_LETTER.matches(trimmedText)) return Language.UNKNOWN

        val languageDetectedByRules = detectLanguageWithRules(trimmedText)
        if (languageDetectedByRules != Language.UNKNOWN) return languageDetectedByRules

        languagesSequence = languagesSequence.filterNot { it.isExcludedFromDetection }

        val textSequence = trimmedText.lineSequence()
        val unigramTestDataModel = LanguageModel.fromTestData<Unigram>(textSequence)
        val bigramTestDataModel = LanguageModel.fromTestData<Bigram>(textSequence)
        val trigramTestDataModel = LanguageModel.fromTestData<Trigram>(textSequence)

        val unigramProbabilities = computeUnigramProbabilities(unigramTestDataModel)
        val bigramProbabilities = computeBigramProbabilities(bigramTestDataModel)
        val trigramProbabilities = computeTrigramProbabilities(trigramTestDataModel)

        val allProbabilities = mutableListOf(unigramProbabilities, bigramProbabilities, trigramProbabilities)

        if (trimmedText.length in 4..50) {
            val quadrigramTestDataModel = LanguageModel.fromTestData<Quadrigram>(textSequence)
            val quadrigramProbabilities = computeQuadrigramProbabilities(quadrigramTestDataModel)
            if (!quadrigramProbabilities.containsValue(0.0)) {
                allProbabilities.add(quadrigramProbabilities)
            }
        }
        if (trimmedText.length in 5..50) {
            val fivegramTestDataModel = LanguageModel.fromTestData<Fivegram>(textSequence)
            val fivegramProbabilities = computeFivegramProbabilities(fivegramTestDataModel)
            if (!fivegramProbabilities.containsValue(0.0)) {
                allProbabilities.add(fivegramProbabilities)
            }
        }

        val summedUpProbabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            summedUpProbabilities[language] = allProbabilities.sumByDouble { it.getOrDefault(language, 0.0) }
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
        return probabilities
            .asSequence()
            .filter { it.value != 0.0 }
            .maxBy { (_, value) -> value }!!.key
    }

    internal fun detectLanguageWithRules(text: String): Language {
        val splitText = if (text.contains(' ')) text.split(" ") else listOf(text)

        for (word in splitText) {
            if (word.contains('ß')) return GERMAN

            if (LATIN_ALPHABET.matches(word)) {
                filterLanguages(Language::hasLatinAlphabet)

                if (word.contains(UMLAUTS)) filterLanguages(Language::hasUmlauts)
                if (word.contains(LITHUANIAN_OR_POLISH_CHARACTERS)) filterLanguages {
                    it in arrayOf(LITHUANIAN, POLISH)
                }
                else if (word.contains(LATVIAN_OR_LITHUANIAN_CHARACTERS)) filterLanguages {
                    it in arrayOf(LATVIAN, LITHUANIAN)
                }
                if (word.contains(LATVIAN_CHARACTERS)) return LATVIAN
                if (word.contains(LITHUANIAN_CHARACTERS)) return LITHUANIAN
                break
            }
            else if (CYRILLIC_ALPHABET.matches(word)) {
                filterLanguages(Language::hasCyrillicAlphabet)
                break
            }
            else if (ARABIC_ALPHABET.matches(word)) {
                filterLanguages(Language::hasArabicAlphabet)
                break
            }
        }

        return Language.UNKNOWN
    }

    private fun filterLanguages(func: (Language) -> Boolean) {
        return languagesSequence.filterNot(func).forEach { it.isExcludedFromDetection = true }
    }

    private fun computeUnigramProbabilities(
        unigramTestDataModel: LanguageModel<Unigram, Unigram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = lookUpNgramProbabilities(
                language,
                unigramTestDataModel.ngrams,
                ::lookUpUnigramProbabilities
            )
        }
        return probabilities
    }

    private fun computeBigramProbabilities(
        bigramTestDataModel: LanguageModel<Bigram, Bigram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = lookUpNgramProbabilities(
                language,
                bigramTestDataModel.ngrams,
                ::lookUpBigramProbabilities,
                ::lookUpUnigramProbabilities
            )
        }
        return probabilities
    }

    private fun computeTrigramProbabilities(
        trigramTestDataModel: LanguageModel<Trigram, Trigram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = lookUpNgramProbabilities(
                language,
                trigramTestDataModel.ngrams,
                ::lookUpTrigramProbabilities,
                ::lookUpBigramProbabilities,
                ::lookUpUnigramProbabilities
            )
        }
        return probabilities
    }

    private fun computeQuadrigramProbabilities(
        quadrigramTestDataModel: LanguageModel<Quadrigram, Quadrigram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = lookUpNgramProbabilities(
                language,
                quadrigramTestDataModel.ngrams,
                ::lookUpQuadrigramProbabilities,
                ::lookUpTrigramProbabilities,
                ::lookUpBigramProbabilities,
                ::lookUpUnigramProbabilities
            )
        }
        return probabilities
    }

    private fun computeFivegramProbabilities(
        fivegramTestDataModel: LanguageModel<Fivegram, Fivegram>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = lookUpNgramProbabilities(
                language,
                fivegramTestDataModel.ngrams,
                ::lookUpFivegramProbabilities,
                ::lookUpQuadrigramProbabilities,
                ::lookUpTrigramProbabilities,
                ::lookUpBigramProbabilities,
                ::lookUpUnigramProbabilities
            )
        }
        return probabilities
    }

    private fun <T : Ngram> lookUpNgramProbabilities(
        language: Language,
        ngrams: Set<T>,
        vararg lookUpFunctions: (Language, List<T>) -> MutableList<Double?>
    ): Double {
        val ngramsList = ngrams.toMutableList()
        val probabilities = lookUpFunctions[0](language, ngramsList)

        if (lookUpFunctions.size > 1) {
            for (i in 1 until lookUpFunctions.size) {
                val indices = mutableListOf<Int>()
                for ((idx, fraction) in probabilities.withIndex()) {
                    if (fraction == null) indices.add(idx)
                }
                val probs = lookUpFunctions[i](language, ngramsList.filterIndexed {
                        idx, _ -> indices.contains(idx)
                })
                for ((c, idx) in indices.withIndex()) {
                    probabilities[idx] = probs[c]
                }
            }
        }

        return probabilities.asSequence().filterNotNull().sumByDouble { Math.log(it) }
    }

    private fun <T : Ngram> lookUpNgramProbabilities(
        languageModels: Map<Language, LanguageModel<T, T>>,
        language: Language,
        ngrams: List<T>
    ): MutableList<Double?> = ngrams.map { languageModels.getValue(language).getRelativeFrequency(it) }.toMutableList()

    private fun <T : Ngram> lookUpUnigramProbabilities(language: Language, ngrams: List<T>): MutableList<Double?> {
        val unigrams = ngrams.map { if (it is Unigram) it else Unigram(it.value[0].toString()) }
        return lookUpNgramProbabilities(unigramLanguageModels, language, unigrams)
    }

    private fun <T : Ngram> lookUpBigramProbabilities(language: Language, ngrams: List<T>): MutableList<Double?> {
        val bigrams = ngrams.map { if (it is Bigram) it else Bigram(it.value.slice(0..1)) }
        return lookUpNgramProbabilities(bigramLanguageModels, language, bigrams)
    }

    private fun <T : Ngram> lookUpTrigramProbabilities(language: Language, ngrams: List<T>): MutableList<Double?> {
        val trigrams = ngrams.map { if (it is Trigram) it else Trigram(it.value.slice(0..2)) }
        return lookUpNgramProbabilities(trigramLanguageModels, language, trigrams)
    }

    private fun <T : Ngram> lookUpQuadrigramProbabilities(language: Language, ngrams: List<T>): MutableList<Double?> {
        val quadrigrams = ngrams.map {
            if (it is Quadrigram) it else Quadrigram(it.value.slice(0..3))
        }.toMutableList()
        return lookUpNgramProbabilities(quadrigramLanguageModels, language, quadrigrams)
    }

    private fun <T : Ngram> lookUpFivegramProbabilities(language: Language, ngrams: List<T>): MutableList<Double?> {
        val fivegrams = ngrams.map {
            if (it is Fivegram) it else Fivegram(it.value.slice(0..4))
        }.toMutableList()

        return lookUpNgramProbabilities(fivegramLanguageModels, language, fivegrams)
    }

    private fun <T : Ngram> loadLanguageModel(
        language: Language,
        ngramClass: KClass<T>
    ): LanguageModel<T, T> {
        var languageModel: LanguageModel<T, T>? = null
        val fileName = "${ngramClass.simpleName!!.toLowerCase()}s.json"
        "/language-models/${language.isoCode}/$fileName".asJsonResource { jsonReader ->
            languageModel = LanguageModel.fromJson(jsonReader, ngramClass, isCachedByMapDB)
        }
        return languageModel!!
    }

    private fun <T : Ngram> loadLanguageModels(
        languages: Set<Language>,
        ngramClass: KClass<T>
    ): MutableMap<Language, LanguageModel<T, T>> {
        val languageModels = hashMapOf<Language, LanguageModel<T, T>>()
        for (language in languages) {
            languageModels[language] = loadLanguageModel(language, ngramClass)
        }
        return languageModels
    }

    internal fun loadAllLanguageModels() {
        unigramLanguageModels = loadLanguageModels(languages, Unigram::class)
        bigramLanguageModels = loadLanguageModels(languages, Bigram::class)
        trigramLanguageModels = loadLanguageModels(languages, Trigram::class)
        quadrigramLanguageModels = loadLanguageModels(languages, Quadrigram::class)
        fivegramLanguageModels = loadLanguageModels(languages, Fivegram::class)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LanguageDetector

        if (languages != other.languages) return false
        if (isCachedByMapDB != other.isCachedByMapDB) return false

        return true
    }

    override fun hashCode(): Int {
        var result = languages.hashCode()
        result = 31 * result + isCachedByMapDB.hashCode()
        return result
    }

    internal companion object {
        private val NO_LETTER = Regex("^[^\\p{L}]+$")
        private val LATIN_ALPHABET = Regex("^[\\p{IsLatin}]+$")
        private val CYRILLIC_ALPHABET = Regex("^[\\p{IsCyrillic}]+$")
        private val ARABIC_ALPHABET = Regex("^[\\p{IsArabic}]+$")
        private val UMLAUTS = Regex("[ÄÖÜäöü]+")
        private val LATVIAN_CHARACTERS = Regex("[ĀĒĢĪĶĻŅāēģīķļņ]+")
        private val LITHUANIAN_CHARACTERS = Regex("[ĖĮŲėįų]+")
        private val LITHUANIAN_OR_POLISH_CHARACTERS = Regex("[ĄĘąę]+")
        private val LATVIAN_OR_LITHUANIAN_CHARACTERS = Regex("[Ūū]+")
    }
}

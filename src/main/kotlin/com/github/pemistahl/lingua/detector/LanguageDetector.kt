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

package com.github.pemistahl.lingua.detector

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

class LanguageDetector internal constructor(
    val languages: MutableSet<Language>,
    val isCachedByMapDB: Boolean,
    val numberOfLoadedLanguages: Int = languages.size
) {
    private var languagesSequence = languages.asSequence()

    private val unigramLanguageModels = loadLanguageModels(languages, Unigram::class)
    private val bigramLanguageModels = loadLanguageModels(languages, Bigram::class)
    private val trigramLanguageModels = loadLanguageModels(languages, Trigram::class)
    private val quadrigramLanguageModels = loadLanguageModels(languages, Quadrigram::class)
    private val fivegramLanguageModels = loadLanguageModels(languages, Fivegram::class)

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

        /*
        for (prob in allProbabilities) {
            println(prob)
        }
        */

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
        //println("TOTAL: $probabilities")
        return probabilities
            .asSequence()
            .filter { it.value != 0.0 }
            .maxBy { (_, value) -> value }!!.key
    }

    private fun detectLanguageWithRules(text: String): Language {
        if (text.isEmpty() || NO_LETTER_REGEX.matches(text)) return Language.UNKNOWN
        val splitText = if (text.contains(' ')) text.split(" ") else listOf(text)

        for (word in splitText) {
            if (word.contains('ß')) return Language.GERMAN

            if (LATIN_ALPHABET_REGEX.matches(word)) {
                languagesSequence.filterNot {
                    it.hasLatinAlphabet
                }.forEach {
                    it.isExcludedFromDetection = true
                }
                break
            }

            else if (CYRILLIC_ALPHABET_REGEX.matches(word)) {
                languagesSequence.filterNot {
                    it.hasCyrillicAlphabet
                }.forEach {
                    it.isExcludedFromDetection = true
                }
                break
            }

            else if (ARABIC_ALPHABET_REGEX.matches(word)) {
                languagesSequence.filterNot {
                    it.hasArabicAlphabet
                }.forEach {
                    it.isExcludedFromDetection = true
                }
                break
            }

            /*
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
            */
        }

        return Language.UNKNOWN
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
                    probabilities.set(idx, probs.get(c))
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

    internal companion object {
        private val NO_LETTER_REGEX = Regex("^[^\\p{L}]+$")
        private val LATIN_ALPHABET_REGEX = Regex("^[\\p{IsLatin}]+$")
        private val CYRILLIC_ALPHABET_REGEX = Regex("^[\\p{IsCyrillic}]+$")
        private val ARABIC_ALPHABET_REGEX = Regex("^[\\p{IsArabic}]+$")
        /*
        private val ENGLISH_GRAPHS = Regex("[ao]ugh")
        private val FRENCH_UNIQUE_CHARS = Regex("[ÆæŒœŸÿ]+")
        private val GERMAN_UNIQUE_CHARS = Regex("[AÖÜäöüß]+")
        private val ITALIAN_GRAPHS = Regex("cch|ggh|zz")
        private val PORTUGUESE_UNIQUE_CHARS = Regex("[ãõ]+")
        private val SPANISH_UNIQUE_CHARS = Regex("[ñ¿¡]+")
        */
    }
}

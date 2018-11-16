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

import com.github.pemistahl.lingua.model.Language
import com.github.pemistahl.lingua.model.LanguageModel
import com.github.pemistahl.lingua.model.Ngram
import com.github.pemistahl.lingua.util.extension.asJsonResource

class LanguageDetector private constructor(
    private val languageModels: List<LanguageModel>,
    val supportedLanguages: Int = languageModels.size
) {
    fun detectLanguageFrom(text: String): Language? {
        val trimmedText = text.trim()
        if (trimmedText.isEmpty()) return null
        val testDataModel = LanguageModel.fromTestData(trimmedText.lineSequence())
        val unigramProbabilities = computeLogProbabilities(testDataModel.unigrams)
        val bigramProbabilities = computeLogProbabilities(testDataModel.bigrams)
        val trigramProbabilities = computeLogProbabilities(testDataModel.trigrams)

        val totalProbabilities =
            computeTotalLogProbabilities(unigramProbabilities, bigramProbabilities, trigramProbabilities)

        return getMostLikelyLanguage(totalProbabilities)
    }

    private fun computeTotalLogProbabilities(vararg probabilities: Map<Language, Double>): Map<Language, Double> {
        val totalLogProbabilities = mutableMapOf<Language, Double>()

        languageModels.map { it.language as Language }.forEach { language ->
            totalLogProbabilities[language] = probabilities.sumByDouble { it.getValue(language) }
        }
        return totalLogProbabilities
    }

    private fun computeLogProbabilities(ngrams: Set<Ngram>): Map<Language, Double> {
        val logProbabilities = mutableMapOf<Language, Double>()
        val totalNumberOfNgrams = ngrams.size
        val unseenNgramProbability = Math.log(1.0) - Math.log(totalNumberOfNgrams.toDouble())

        languageModels.forEach { model ->
            logProbabilities[model.language!!] = ngrams.map { model.getRelativeFrequency(it) }
                .map { when (it) {
                    null -> unseenNgramProbability
                    else -> {
                        val numerator = it.numerator.toDouble()
                        val denominator = it.denominator.toDouble()
                        Math.log(numerator + 1) - Math.log(denominator + totalNumberOfNgrams)
                    }
                }}.sum()
        }
        return logProbabilities
    }

    private fun getMostLikelyLanguage(probabilities: Map<Language, Double>): Language {
        return probabilities.toList().sortedByDescending { (_, value) -> value }.first().first
    }

    companion object {
        @JvmStatic
        fun fromAllBuiltInLanguages() =
            LanguageDetector(loadLanguageModels(Language.values()))

        @JvmStatic
        fun fromAllBuiltInLanguagesWithout(vararg languages: Language) =
            LanguageDetector(loadLanguageModels(Language.values().toSet().minus(languages).toTypedArray()))

        @JvmStatic
        fun fromLanguages(vararg languages: Language) =
            LanguageDetector(loadLanguageModels(languages))

        private fun loadLanguageModels(languages: Array<out Language>): List<LanguageModel> {
            val languageModels = mutableListOf<LanguageModel>()
            for (language in languages) {
                "/language-models/${language.isoCode}.json".asJsonResource {
                    languageModels.add(LanguageModel.fromJson(it))
                }
            }
            return languageModels
        }
    }
}

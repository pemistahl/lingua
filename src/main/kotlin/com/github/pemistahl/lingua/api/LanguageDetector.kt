/*
 * Copyright © 2018-today Peter M. Stahl pemistahl@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pemistahl.lingua.api

import com.github.pemistahl.lingua.api.Language.CHINESE
import com.github.pemistahl.lingua.api.Language.JAPANESE
import com.github.pemistahl.lingua.api.Language.UNKNOWN
import com.github.pemistahl.lingua.internal.Alphabet
import com.github.pemistahl.lingua.internal.Constant.CHARS_TO_LANGUAGES_MAPPING
import com.github.pemistahl.lingua.internal.Constant.MULTIPLE_WHITESPACE
import com.github.pemistahl.lingua.internal.Constant.NO_LETTER
import com.github.pemistahl.lingua.internal.Constant.NUMBERS
import com.github.pemistahl.lingua.internal.Constant.PUNCTUATION
import com.github.pemistahl.lingua.internal.Constant.isJapaneseAlphabet
import com.github.pemistahl.lingua.internal.JsonLanguageModel
import com.github.pemistahl.lingua.internal.Ngram
import com.github.pemistahl.lingua.internal.TestDataLanguageModel
import com.github.pemistahl.lingua.internal.TrainingDataLanguageModel
import com.github.pemistahl.lingua.internal.util.extension.containsAnyOf
import com.github.pemistahl.lingua.internal.util.extension.incrementCounter
import com.github.pemistahl.lingua.internal.util.extension.isLogogram
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.SortedMap
import java.util.TreeMap
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.ln

/**
 * Detects the language of given input text.
 */
class LanguageDetector internal constructor(
    internal val languages: MutableSet<Language>,
    internal val minimumRelativeDistance: Double,
    isEveryLanguageModelPreloaded: Boolean,
    internal val numberOfLoadedLanguages: Int = languages.size,
    internal val languageModels: MutableMap<Language, TrainingDataLanguageModel> = mutableMapOf()
) {
    internal val threadPool = createThreadPool()

    private val languagesWithUniqueCharacters = languages.filterNot { it.uniqueCharacters.isNullOrBlank() }.asSequence()
    private val oneLanguageAlphabets = Alphabet.allSupportingExactlyOneLanguage().filterValues {
        it in languages
    }

    init {
        if (isEveryLanguageModelPreloaded) {
            preloadLanguageModels()
        }
    }

    /**
     * Detects the language of given input text.
     *
     * @param text The input text to detect the language for.
     * @return The identified language or [Language.UNKNOWN].
     * @throws IllegalStateException If [destroy] has been invoked before on this instance of [LanguageDetector].
     */
    fun detectLanguageOf(text: String): Language {
        val confidenceValues = computeLanguageConfidenceValues(text)

        if (confidenceValues.isEmpty()) return UNKNOWN

        val mostLikelyLanguage = confidenceValues.firstKey()
        if (confidenceValues.size == 1) return mostLikelyLanguage

        val mostLikelyLanguageProbability = confidenceValues.getValue(mostLikelyLanguage)
        val secondMostLikelyLanguageProbability = confidenceValues.values.elementAt(1)

        return when {
            mostLikelyLanguageProbability == secondMostLikelyLanguageProbability -> UNKNOWN
            (mostLikelyLanguageProbability - secondMostLikelyLanguageProbability) < minimumRelativeDistance -> UNKNOWN
            else -> mostLikelyLanguage
        }
    }

    /**
     * Computes confidence values for every language considered possible for the given input text.
     *
     * The values that this method computes are part of a **relative** confidence metric, not of an absolute one.
     * Each value is a number between 0.0 and 1.0. The most likely language is always returned with value 1.0.
     * All other languages get values assigned which are lower than 1.0, denoting how less likely those languages
     * are in comparison to the most likely language.
     *
     * The map returned by this method does not necessarily contain all languages which the calling instance of
     * [LanguageDetector] was built from. If the rule-based engine decides that a specific language is truly impossible,
     * then it will not be part of the returned map. Likewise, if no ngram probabilities can be found within the
     * detector's languages for the given input text, the returned map will be empty. The confidence value for
     * each language not being part of the returned map is assumed to be 0.0.
     *
     * @param text The input text to detect the language for.
     * @return A map of all possible languages, sorted by their confidence value in descending order.
     * @throws IllegalStateException If [destroy] has been invoked before on this instance of [LanguageDetector].
     */
    fun computeLanguageConfidenceValues(text: String): SortedMap<Language, Double> {
        if (threadPool.isShutdown) {
            throw IllegalStateException(
                "This LanguageDetector instance has been destroyed and cannot be reused"
            )
        }
        val values = TreeMap<Language, Double>()
        val cleanedUpText = cleanUpInputText(text)

        if (cleanedUpText.isEmpty() || NO_LETTER.matches(cleanedUpText)) return values

        val words = splitTextIntoWords(cleanedUpText)
        val languageDetectedByRules = detectLanguageWithRules(words)

        if (languageDetectedByRules != UNKNOWN) {
            values[languageDetectedByRules] = 1.0
            return values
        }

        val filteredLanguages = filterLanguagesByRules(words)

        if (filteredLanguages.size == 1) {
            val filteredLanguage = filteredLanguages.iterator().next()
            values[filteredLanguage] = 1.0
            return values
        }

        val ngramSizeRange = if (cleanedUpText.length >= 120) (3..3) else (1..5)
        val tasks = ngramSizeRange.filter { i -> cleanedUpText.length >= i }.map { i ->
            Callable {
                val testDataModel = TestDataLanguageModel.fromText(cleanedUpText, ngramLength = i)
                val probabilities = computeLanguageProbabilities(testDataModel, filteredLanguages)

                val unigramCounts =
                    if (i != 1 || probabilities.isEmpty()) {
                        emptyMap()
                    } else {
                        countUnigramsOfInputText(
                            testDataModel,
                            filteredLanguages.filterTo(mutableSetOf()) { languages.contains(it) }
                        )
                    }

                Pair(probabilities, unigramCounts)
            }
        }

        val allProbabilitiesAndUnigramCounts = threadPool.invokeAll(tasks).map { it.get() }
        val allProbabilities = allProbabilitiesAndUnigramCounts.map { (probabilities, _) -> probabilities }
        val unigramCounts = allProbabilitiesAndUnigramCounts[0].second
        val summedUpProbabilities = sumUpProbabilities(allProbabilities, unigramCounts, filteredLanguages)
        val highestProbability = summedUpProbabilities.maxByOrNull { it.value }?.value ?: return sortedMapOf()
        val confidenceValues = summedUpProbabilities.mapValues { highestProbability / it.value }
        val sortedByConfidenceValue = compareByDescending<Language> { language -> confidenceValues[language] }
        val sortedByConfidenceValueThenByLanguage = sortedByConfidenceValue.thenBy { language -> language }

        return confidenceValues.toSortedMap(sortedByConfidenceValueThenByLanguage)
    }

    /**
     * Destroys this [LanguageDetector] instance and frees associated resources.
     *
     * This will be useful if the library is used within a web application inside
     * an application server. By calling this method prior to undeploying the
     * web application, the language models are removed and memory is freed.
     * The internal thread pool used for parallel processing is shut down as well.
     * This prevents exceptions such as [OutOfMemoryError] when the web application
     * is redeployed multiple times.
     */
    fun destroy() {
        threadPool.shutdown()
        if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
            threadPool.shutdownNow()
        }

        languageModels.clear()
    }

    internal fun cleanUpInputText(text: String): String {
        return text.trim().lowercase()
            .replace(PUNCTUATION, "")
            .replace(NUMBERS, "")
            .replace(MULTIPLE_WHITESPACE, " ")
    }

    internal fun splitTextIntoWords(text: String): List<String> {
        val words = mutableListOf<String>()
        var nextWordStart = 0
        for (i in text.indices) {
            val char = text[i]

            if (char == ' ') {
                if (nextWordStart != i) {
                    words.add(text.substring(nextWordStart, i))
                }
                nextWordStart = i + 1
            } else if (char.isLogogram()) {
                if (nextWordStart != i) {
                    words.add(text.substring(nextWordStart, i))
                }

                words.add(text[i].toString())
                nextWordStart = i + 1
            }
        }

        if (nextWordStart != text.length) {
            words.add(text.substring(nextWordStart, text.length))
        }
        return words
    }

    internal fun countUnigramsOfInputText(
        unigramLanguageModel: TestDataLanguageModel,
        filteredLanguages: Set<Language>
    ): Map<Language, Int> {
        val unigramCounts = mutableMapOf<Language, Int>()
        for (language in filteredLanguages) {
            for (unigram in unigramLanguageModel.ngrams) {
                val probability = lookUpNgramProbability(language, unigram)
                if (probability > 0) {
                    unigramCounts.incrementCounter(language)
                }
            }
        }
        return unigramCounts
    }

    internal fun sumUpProbabilities(
        probabilities: List<Map<Language, Double>>,
        unigramCountsOfInputText: Map<Language, Int>,
        filteredLanguages: Set<Language>
    ): Map<Language, Double> {
        val summedUpProbabilities = mutableMapOf<Language, Double>()
        for (language in filteredLanguages) {
            summedUpProbabilities[language] = probabilities.sumOf { it[language] ?: 0.0 }

            if (unigramCountsOfInputText.containsKey(language)) {
                summedUpProbabilities[language] = summedUpProbabilities.getValue(language) /
                    unigramCountsOfInputText.getValue(language)
            }
        }
        return summedUpProbabilities.filter { it.value != 0.0 }
    }

    internal fun detectLanguageWithRules(words: List<String>): Language {
        val totalLanguageCounts = mutableMapOf<Language, Int>()

        for (word in words) {
            val wordLanguageCounts = mutableMapOf<Language, Int>()

            for (character in word) {
                var isMatch = false
                for ((alphabet, language) in oneLanguageAlphabets) {
                    if (alphabet.matches(character)) {
                        wordLanguageCounts.incrementCounter(language)
                        isMatch = true
                        break
                    }
                }
                if (!isMatch) {
                    when {
                        Alphabet.HAN.matches(character) -> wordLanguageCounts.incrementCounter(CHINESE)
                        isJapaneseAlphabet(character) -> wordLanguageCounts.incrementCounter(JAPANESE)
                        Alphabet.LATIN.matches(character) ||
                            Alphabet.CYRILLIC.matches(character) ||
                            Alphabet.DEVANAGARI.matches(character) ->
                            languagesWithUniqueCharacters.filter {
                                it.uniqueCharacters?.contains(character) ?: false
                            }.forEach {
                                wordLanguageCounts.incrementCounter(it)
                            }
                    }
                }
            }

            if (wordLanguageCounts.isEmpty()) {
                totalLanguageCounts.incrementCounter(UNKNOWN)
            } else if (wordLanguageCounts.size == 1) {
                val language = wordLanguageCounts.keys.first()
                if (language in languages) {
                    totalLanguageCounts.incrementCounter(language)
                } else {
                    totalLanguageCounts.incrementCounter(UNKNOWN)
                }
            } else {
                val sortedWordLanguageCounts = wordLanguageCounts.toList().sortedByDescending { it.second }
                val (mostFrequentLanguage, firstCharCount) = sortedWordLanguageCounts[0]
                val (_, secondCharCount) = sortedWordLanguageCounts[1]

                if (firstCharCount > secondCharCount && mostFrequentLanguage in languages) {
                    totalLanguageCounts.incrementCounter(mostFrequentLanguage)
                } else {
                    totalLanguageCounts.incrementCounter(UNKNOWN)
                }
            }
        }

        val unknownLanguageCount = totalLanguageCounts[UNKNOWN] ?: 0
        if (unknownLanguageCount < (0.5 * words.size)) {
            totalLanguageCounts.remove(UNKNOWN)
        }

        if (totalLanguageCounts.isEmpty()) {
            return UNKNOWN
        }
        if (totalLanguageCounts.size == 1) {
            return totalLanguageCounts.keys.first()
        }
        if (totalLanguageCounts.size == 2 &&
            totalLanguageCounts.containsKey(CHINESE) &&
            totalLanguageCounts.containsKey(JAPANESE)
        ) {
            return JAPANESE
        }
        val sortedTotalLanguageCounts = totalLanguageCounts.toList().sortedByDescending { it.second }
        val (mostFrequentLanguage, firstCharCount) = sortedTotalLanguageCounts[0]
        val (_, secondCharCount) = sortedTotalLanguageCounts[1]

        return when {
            firstCharCount == secondCharCount -> UNKNOWN
            else -> mostFrequentLanguage
        }
    }

    internal fun filterLanguagesByRules(words: List<String>): Set<Language> {
        val detectedAlphabets = mutableMapOf<Alphabet, Int>()

        for (word in words) {
            for (alphabet in Alphabet.values()) {
                if (alphabet.matches(word)) {
                    detectedAlphabets.incrementCounter(alphabet)
                    break
                }
            }
        }

        if (detectedAlphabets.isEmpty()) {
            return languages
        }

        if (detectedAlphabets.size > 1) {
            val distinctAlphabets = mutableSetOf<Int>()
            for (count in detectedAlphabets.values) {
                distinctAlphabets.add(count)
            }
            if (distinctAlphabets.size == 1) {
                return languages
            }
        }

        val mostFrequentAlphabet = detectedAlphabets.entries.maxByOrNull { it.value }!!.key
        val filteredLanguages = languages.filter { it.alphabets.contains(mostFrequentAlphabet) }
        val languageCounts = mutableMapOf<Language, Int>()

        for (word in words) {
            for ((characters, languages) in CHARS_TO_LANGUAGES_MAPPING) {
                if (word.containsAnyOf(characters)) {
                    for (language in languages) {
                        languageCounts.incrementCounter(language)
                    }
                    break
                }
            }
        }

        val languagesSubset = languageCounts.filterValues { it >= words.size / 2.0 }.keys

        return if (languagesSubset.isNotEmpty()) {
            filteredLanguages.filter { it in languagesSubset }.toSet()
        } else {
            filteredLanguages.toSet()
        }
    }

    internal fun computeLanguageProbabilities(
        testDataModel: TestDataLanguageModel,
        filteredLanguages: Set<Language>
    ): Map<Language, Double> {
        val probabilities = mutableMapOf<Language, Double>()
        for (language in filteredLanguages) {
            probabilities[language] = computeSumOfNgramProbabilities(language, testDataModel.ngrams)
        }
        return probabilities.filter { it.value < 0.0 }
    }

    internal fun computeSumOfNgramProbabilities(
        language: Language,
        ngrams: Set<Ngram>
    ): Double {
        var probabilitiesSum = 0.0

        for (ngram in ngrams) {
            for (elem in ngram.rangeOfLowerOrderNgrams()) {
                val probability = lookUpNgramProbability(language, elem)
                if (probability > 0) {
                    probabilitiesSum += ln(probability.toDouble())
                    break
                }
            }
        }
        return probabilitiesSum
    }

    internal fun lookUpNgramProbability(
        language: Language,
        ngram: Ngram
    ): Float {
        require(ngram.length > 0) { "Zerogram detected" }
        require(ngram.length <= 5) { "unsupported ngram length detected: ${ngram.length}" }
        return loadLanguageModels(languageModels, language).getRelativeFrequency(ngram)
    }

    private fun loadLanguageModels(
        languageModels: MutableMap<Language, TrainingDataLanguageModel>,
        language: Language,
        builderCache: TrainingDataLanguageModel.BuilderCache? = null
    ): TrainingDataLanguageModel =
        languageModels.computeIfAbsent(language) {
            loadLanguageModel(language, builderCache ?: TrainingDataLanguageModel.BuilderCache())
        }

    private fun loadLanguageModel(
        language: Language,
        builderCache: TrainingDataLanguageModel.BuilderCache
    ): TrainingDataLanguageModel {
        val jsonLanguageModels: Sequence<JsonLanguageModel> = (1..5).asSequence().map { ngramLength ->
            val fileName = "${Ngram.getNgramNameByLength(ngramLength)}s.json"
            val filePath = "/language-models/${language.isoCode639_1}/$fileName"
            Json.decodeFromString(Language::class.java.getResourceAsStream(filePath).reader().use { it.readText() })
        }
        return TrainingDataLanguageModel.fromJson(language, jsonLanguageModels, builderCache)
    }

    private fun preloadLanguageModels() {
        val builderCache = TrainingDataLanguageModel.BuilderCache()
        for (language in languages) {
            loadLanguageModels(languageModels, language, builderCache)
        }
    }

    private fun createThreadPool(): ExecutorService {
        val cpus = Runtime.getRuntime().availableProcessors()
        val threadPool = ThreadPoolExecutor(cpus, cpus, 60L, TimeUnit.SECONDS, LinkedBlockingQueue())
        threadPool.allowCoreThreadTimeOut(true)
        return threadPool
    }

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is LanguageDetector -> false
        languages != other.languages -> false
        minimumRelativeDistance != other.minimumRelativeDistance -> false
        else -> true
    }

    override fun hashCode() = 31 * languages.hashCode() + minimumRelativeDistance.hashCode()
}

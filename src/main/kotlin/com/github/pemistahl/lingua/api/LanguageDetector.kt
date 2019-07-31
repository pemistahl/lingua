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
import com.github.pemistahl.lingua.internal.util.extension.containsAnyOf
import com.github.pemistahl.lingua.internal.util.readJsonResource
import java.util.regex.PatternSyntaxException
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.reflect.KClass

class LanguageDetector internal constructor(
    internal val languages: MutableSet<Language>,
    internal val isCachedByMapDB: Boolean,
    internal val numberOfLoadedLanguages: Int = languages.size
) {
    private var languagesSequence = languages.asSequence()
    private val languagesWithUniqueCharacters = languages.filter { it.uniqueCharacters.isNotEmpty() }.asSequence()

    internal val unigramLanguageModels = loadLanguageModels(Unigram::class)
    internal val bigramLanguageModels = loadLanguageModels(Bigram::class)
    internal val trigramLanguageModels = loadLanguageModels(Trigram::class)
    internal val quadrigramLanguageModels = loadLanguageModels(Quadrigram::class)
    internal val fivegramLanguageModels = loadLanguageModels(Fivegram::class)

    fun detectLanguagesOf(texts: Iterable<String>): List<Language> = texts.map { detectLanguageOf(it) }

    fun detectLanguageOf(text: String): Language {
        resetLanguageFilter()

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

        filterLanguagesByRules(words)

        val textSequence = trimmedText.lineSequence()
        val allProbabilities = mutableListOf<Map<Language, Double>>()
        val unigramCountsOfInputText = mutableMapOf<Language, Int>()

        if (trimmedText.length >= 1) {
            val unigramLanguageModel = LanguageModel.fromTestData(textSequence, Unigram::class)
            addNgramProbabilities(allProbabilities, unigramLanguageModel)
            countUnigramsOfInputText(unigramCountsOfInputText, unigramLanguageModel)
        }
        if (trimmedText.length >= 2) {
            addNgramProbabilities(allProbabilities, LanguageModel.fromTestData(textSequence, Bigram::class))
        }
        if (trimmedText.length >= 3) {
            addNgramProbabilities(allProbabilities, LanguageModel.fromTestData(textSequence, Trigram::class))
        }
        if (trimmedText.length >= 4) {
            addNgramProbabilities(allProbabilities, LanguageModel.fromTestData(textSequence, Quadrigram::class))
        }
        if (trimmedText.length >= 5) {
            addNgramProbabilities(allProbabilities, LanguageModel.fromTestData(textSequence, Fivegram::class))
        }

        return getMostLikelyLanguage(allProbabilities, unigramCountsOfInputText)
    }

    internal fun resetLanguageFilter() {
        languagesSequence = languages.asSequence()
        languagesSequence.forEach { it.isExcludedFromDetection = false }
    }

    internal fun applyLanguageFilter(func: (Language) -> Boolean) {
        languagesSequence.filterNot(func).forEach { it.isExcludedFromDetection = true }
        languagesSequence = languagesSequence.filterNot { it.isExcludedFromDetection }
    }

    internal fun addLanguageModel(language: Language) {
        languages.add(language)
        if (!unigramLanguageModels.containsKey(language)) {
            languagesSequence = languages.asSequence()
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
            languagesSequence = languages.asSequence()
        }
    }

    internal fun countUnigramsOfInputText(
        unigramCounts: MutableMap<Language, Int>,
        unigramLanguageModel: LanguageModel<Unigram, Unigram>
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
        testDataModel: LanguageModel<T, T>
    ) {
        val ngramProbabilities = computeLanguageProbabilities(testDataModel)
        if (!ngramProbabilities.containsValue(0.0)) {
            probabilities.add(ngramProbabilities)
        }
    }

    internal fun getMostLikelyLanguage(
        probabilities: List<Map<Language, Double>>,
        unigramCountsOfInputText: Map<Language, Int>
    ): Language {
        val summedUpProbabilities = hashMapOf<Language, Double>()
        for (language in languagesSequence) {
            summedUpProbabilities[language] = probabilities.sumByDouble { it[language] ?: 0.0 }

            if (unigramCountsOfInputText.containsKey(language)) {
                summedUpProbabilities[language] = summedUpProbabilities.getValue(language) / unigramCountsOfInputText.getValue(language)
            }
        }

        val filteredProbabilities = summedUpProbabilities.asSequence().filter { it.value != 0.0 }
        return if (filteredProbabilities.none()) UNKNOWN
        else filteredProbabilities.maxBy {
                (_, value) -> value
        }?.key ?: throw IllegalArgumentException(
            "most likely language can not be determined due to some internal error"
        )
    }

    internal fun detectLanguageWithRules(words: List<String>): Language {
        val languageCharCounts = mutableMapOf<Language, Int>()
        val minimalRequiredCharCount = ceil(words.joinToString(separator = "").length / 2.0)

        for (word in words) {
            when {
                GREEK_ALPHABET.matches(word) -> languageCharCounts.addCharCount(word, GREEK)
                CHINESE_ALPHABET.matches(word) -> languageCharCounts.addCharCount(word, CHINESE)
                JAPANESE_ALPHABET.matches(word) -> languageCharCounts.addCharCount(word, JAPANESE)
                KOREAN_ALPHABET.matches(word) -> languageCharCounts.addCharCount(word, KOREAN)
                THAI_ALPHABET.matches(word) -> languageCharCounts.addCharCount(word, THAI)
                TAMIL_ALPHABET.matches(word) -> languageCharCounts.addCharCount(word, TAMIL)
                TELUGU_ALPHABET.matches(word) -> languageCharCounts.addCharCount(word, TELUGU)
                LATIN_ALPHABET.matches(word) -> languagesWithUniqueCharacters.filter {
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

    internal fun filterLanguagesByRules(words: List<String>) {
        for (word in words) {
            if (CYRILLIC_ALPHABET.matches(word)) {
                applyLanguageFilter(Language::usesCyrillicAlphabet)
                break
            }
            else if (ARABIC_ALPHABET.matches(word)) {
                applyLanguageFilter(Language::usesArabicAlphabet)
                break
            }
            else if (CHINESE_ALPHABET.matches(word) || JAPANESE_ALPHABET.matches(word)) {
                applyLanguageFilter(Language::usesChineseAlphabet)
                break
            }
            else if (LATIN_ALPHABET.matches(word)) {
                if (languages.contains(NORWEGIAN)) {
                    applyLanguageFilter { it.usesLatinAlphabet && it !in setOf(BOKMAL, NYNORSK) }
                }
                else if (languages.contains(BOKMAL) || languages.contains(NYNORSK)) {
                    applyLanguageFilter { it.usesLatinAlphabet && it != NORWEGIAN }
                }
                else {
                    applyLanguageFilter(Language::usesLatinAlphabet)
                }

                val languagesSubset = mutableSetOf<Language>()
                for ((characters, languages) in CHARS_TO_LANGUAGES_MAPPING) {
                    if (word.containsAnyOf(characters)) {
                        languagesSubset.addAll(languages)
                    }
                }
                if (languagesSubset.isNotEmpty()) applyLanguageFilter { it in languagesSubset }
                break
            }
        }
    }

    internal fun <T : Ngram> computeLanguageProbabilities(
        testDataModel: LanguageModel<T, T>
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
        for (language in languagesSequence) {
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
        isCachedByMapDB != other.isCachedByMapDB -> false
        else -> true
    }

    override fun hashCode() = 31 * languages.hashCode() + isCachedByMapDB.hashCode()

    internal companion object {
        private val NO_LETTER = Regex("^[^\\p{L}]+$")
        private val PUNCTUATION = Regex("\\p{P}")
        private val NUMBERS = Regex("\\p{N}")
        private val MULTIPLE_WHITESPACE = Regex("\\s+")

        private val LATIN_ALPHABET = "Latin".asRegex()
        private val GREEK_ALPHABET = "Greek".asRegex()
        private val CYRILLIC_ALPHABET = "Cyrillic".asRegex()
        private val ARABIC_ALPHABET = "Arabic".asRegex()
        private val CHINESE_ALPHABET = "Han".asRegex()
        private val KOREAN_ALPHABET = "Hangul".asRegex()
        private val JAPANESE_ALPHABET = "Hiragana, Katakana, Han".asRegex()
        private val THAI_ALPHABET = "Thai".asRegex()
        private val TAMIL_ALPHABET = "Tamil".asRegex()
        private val TELUGU_ALPHABET = "Telugu".asRegex()

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

        private fun String.asRegex(): Regex {
            val splitRegex = Regex("""\s*,\s*""")
            val charClasses = this.split(splitRegex)
            val charClassesWithoutPrefix = charClasses.joinToString(separator = "") { "\\p{$it}" }
            val charClassesWithPrefix = charClasses.joinToString(separator = "") { "\\p{Is$it}" }

            return try {
                // Android only supports character classes without Is- prefix
                Regex("^[$charClassesWithoutPrefix]+$")
            } catch (e: PatternSyntaxException) {
                Regex("^[$charClassesWithPrefix]+$")
            }
        }
    }
}

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

import com.github.pemistahl.lingua.api.Language.BASQUE
import com.github.pemistahl.lingua.api.Language.BOKMAL
import com.github.pemistahl.lingua.api.Language.CATALAN
import com.github.pemistahl.lingua.api.Language.CROATIAN
import com.github.pemistahl.lingua.api.Language.CZECH
import com.github.pemistahl.lingua.api.Language.DANISH
import com.github.pemistahl.lingua.api.Language.ESTONIAN
import com.github.pemistahl.lingua.api.Language.FINNISH
import com.github.pemistahl.lingua.api.Language.FRENCH
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.GREEK
import com.github.pemistahl.lingua.api.Language.HUNGARIAN
import com.github.pemistahl.lingua.api.Language.ICELANDIC
import com.github.pemistahl.lingua.api.Language.IRISH
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.LATVIAN
import com.github.pemistahl.lingua.api.Language.LITHUANIAN
import com.github.pemistahl.lingua.api.Language.NORWEGIAN
import com.github.pemistahl.lingua.api.Language.NYNORSK
import com.github.pemistahl.lingua.api.Language.POLISH
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.ROMANIAN
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.TURKISH
import com.github.pemistahl.lingua.api.Language.UNKNOWN
import com.github.pemistahl.lingua.api.Language.VIETNAMESE
import com.github.pemistahl.lingua.internal.model.Bigram
import com.github.pemistahl.lingua.internal.model.Fivegram
import com.github.pemistahl.lingua.internal.model.LanguageModel
import com.github.pemistahl.lingua.internal.model.Ngram
import com.github.pemistahl.lingua.internal.model.Quadrigram
import com.github.pemistahl.lingua.internal.model.Trigram
import com.github.pemistahl.lingua.internal.model.Unigram
import com.github.pemistahl.lingua.internal.util.extension.asJsonResource
import com.github.pemistahl.lingua.internal.util.extension.containsAnyOf
import java.util.regex.PatternSyntaxException
import kotlin.reflect.KClass

class LanguageDetector internal constructor(
    internal val languages: MutableSet<Language>,
    internal val isCachedByMapDB: Boolean,
    internal val numberOfLoadedLanguages: Int = languages.size
) {
    private var languagesSequence = languages.asSequence()

    private lateinit var unigramLanguageModels: MutableMap<Language, Lazy<LanguageModel<Unigram, Unigram>>>
    private lateinit var bigramLanguageModels: MutableMap<Language, Lazy<LanguageModel<Bigram, Bigram>>>
    private lateinit var trigramLanguageModels: MutableMap<Language, Lazy<LanguageModel<Trigram, Trigram>>>
    private lateinit var quadrigramLanguageModels: MutableMap<Language, Lazy<LanguageModel<Quadrigram, Quadrigram>>>
    private lateinit var fivegramLanguageModels: MutableMap<Language, Lazy<LanguageModel<Fivegram, Fivegram>>>

    fun detectLanguagesOf(texts: Iterable<String>): List<Language> = texts.map { detectLanguageOf(it) }

    fun detectLanguageOf(text: String): Language {
        val trimmedText = text.trim().toLowerCase()
        if (trimmedText.isEmpty() || NO_LETTER.matches(trimmedText)) return UNKNOWN

        val words = if (text.contains(' ')) text.split(" ") else listOf(text)

        val languageDetectedByRules = detectLanguageWithRules(words)
        if (languageDetectedByRules != UNKNOWN) return languageDetectedByRules

        filterLanguagesByRules(words)

        languagesSequence = languagesSequence.filterNot { it.isExcludedFromDetection }

        val textSequence = trimmedText.lineSequence()
        val allProbabilities = mutableListOf<Map<Language, Double>>()

        if (trimmedText.length >= 1) {
            val unigramTestDataModel = LanguageModel.fromTestData<Unigram>(textSequence)
            val unigramProbabilities = computeLanguageProbabilities(unigramTestDataModel)
            if (!unigramProbabilities.containsValue(0.0)) {
                allProbabilities.add(unigramProbabilities)
            }
        }
        if (trimmedText.length >= 2) {
            val bigramTestDataModel = LanguageModel.fromTestData<Bigram>(textSequence)
            val bigramProbabilities = computeLanguageProbabilities(bigramTestDataModel)
            if (!bigramProbabilities.containsValue(0.0)) {
                allProbabilities.add(bigramProbabilities)
            }
        }
        if (trimmedText.length >= 3) {
            val trigramTestDataModel = LanguageModel.fromTestData<Trigram>(textSequence)
            val trigramProbabilities = computeLanguageProbabilities(trigramTestDataModel)
            if (!trigramProbabilities.containsValue(0.0)) {
                allProbabilities.add(trigramProbabilities)
            }
        }
        if (trimmedText.length >= 4) {
            val quadrigramTestDataModel = LanguageModel.fromTestData<Quadrigram>(textSequence)
            val quadrigramProbabilities = computeLanguageProbabilities(quadrigramTestDataModel)
            if (!quadrigramProbabilities.containsValue(0.0)) {
                allProbabilities.add(quadrigramProbabilities)
            }
        }
        if (trimmedText.length >= 5) {
            val fivegramTestDataModel = LanguageModel.fromTestData<Fivegram>(textSequence)
            if (trimmedText.length <= 30) filterLanguagesByUniqueFivegrams(fivegramTestDataModel)
            val fivegramProbabilities = computeLanguageProbabilities(fivegramTestDataModel)
            if (!fivegramProbabilities.containsValue(0.0)) {
                allProbabilities.add(fivegramProbabilities)
            }
        }

        val summedUpProbabilities = hashMapOf<Language, Double>()
        for (language in languagesSequence) {
            summedUpProbabilities[language] = allProbabilities.sumByDouble { it.getOrDefault(language, 0.0) }
        }

        languagesSequence = languages.asSequence()
        languagesSequence.forEach { it.isExcludedFromDetection = false }

        return getMostLikelyLanguage(summedUpProbabilities)
    }

    internal fun addLanguageModel(language: Language) {
        languages.add(language)
        if (!unigramLanguageModels.containsKey(language)) {
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
        val filteredProbabilities = probabilities.asSequence().filter { it.value != 0.0 }
        return if (filteredProbabilities.none()) UNKNOWN
        else filteredProbabilities.maxBy {
                (_, value) -> value
        }?.key ?: throw IllegalArgumentException(
            """
            most likely language can not be determined.
            are there invalid probability values?
            probabilities: $probabilities
            """.trimIndent()
        )
    }

    internal fun detectLanguageWithRules(words: List<String>): Language {
        for (word in words) {
            if (GREEK_ALPHABET.matches(word)) return GREEK
            else if (LATIN_ALPHABET.matches(word)) {
                for ((characters, language) in CHARS_TO_SINGLE_LANGUAGE_MAPPING) {
                    if (word.containsAnyOf(characters)) return language
                }
            }
        }
        return UNKNOWN
    }

    internal fun filterLanguagesByRules(words: List<String>) {
        for (word in words) {
            if (CYRILLIC_ALPHABET.matches(word)) {
                filterLanguages(Language::hasCyrillicAlphabet)
                break
            }
            else if (ARABIC_ALPHABET.matches(word)) {
                filterLanguages(Language::hasArabicAlphabet)
                break
            }
            else if (LATIN_ALPHABET.matches(word)) {
                filterLanguages(Language::hasLatinAlphabet)

                if (languages.contains(BOKMAL) || languages.contains(NYNORSK)) {
                    filterLanguages { it != NORWEGIAN }
                }
                val languagesSubset = mutableSetOf<Language>()
                for ((characters, languages) in CHARS_TO_LANGUAGES_MAPPING) {
                    if (word.containsAnyOf(characters)) {
                        languagesSubset.addAll(languages)
                    }
                }
                if (languagesSubset.isNotEmpty()) filterLanguages { it in languagesSubset }
                break
            }
        }
    }

    private fun filterLanguagesByUniqueFivegrams(fivegramTestDataModel: LanguageModel<Fivegram, Fivegram>) {
        val ngramInLanguageCounts = mutableMapOf<Fivegram, MutableList<Language>>()
        for (fivegram in fivegramTestDataModel.ngrams) {
            val supportedLanguages = mutableListOf<Language>()
            for (language in languagesSequence) {
                if (fivegramLanguageModels.getValue(language).value.getRelativeFrequency(fivegram) != null) {
                    supportedLanguages.add(language)
                }
            }
            ngramInLanguageCounts[fivegram] = supportedLanguages
        }

        val languagesWithUniqueNgrams = mutableSetOf<Language>()
        for ((_, languageList) in ngramInLanguageCounts) {
            if (languageList.size == 1) {
                languagesWithUniqueNgrams.add(languageList[0])
            }
        }

        if (languagesWithUniqueNgrams.isNotEmpty()) filterLanguages {
            it in languagesWithUniqueNgrams
        }
    }

    private fun filterLanguages(func: (Language) -> Boolean) {
        return languagesSequence.filterNot(func).forEach { it.isExcludedFromDetection = true }
    }

    private fun <T : Ngram> computeLanguageProbabilities(
        testDataModel: LanguageModel<T, T>
    ): Map<Language, Double> {
        val probabilities = hashMapOf<Language, Double>()
        for (language in languagesSequence) {
            probabilities[language] = computeSumOfNgramProbabilities(language, testDataModel.ngrams)
        }
        return probabilities
    }

    private fun <T : Ngram> computeSumOfNgramProbabilities(
        language: Language,
        ngrams: Set<T>
    ): Double {
        val probabilities = mutableListOf<Double>()

        for (ngram in ngrams) {
            for (elem in ngram.rangeOfLowerOrderNgrams()) {
                val probability = lookUpNgramProbability(language, elem)
                if (probability != null) {
                    probabilities.add(probability)
                    break
                }
            }
        }
        return probabilities.sumByDouble { Math.log(it) }
    }

    private fun <T : Ngram> lookUpNgramProbability(
        language: Language,
        ngram: T
    ): Double? {
        val languageModels = when (ngram.length) {
            5 -> fivegramLanguageModels
            4 -> quadrigramLanguageModels
            3 -> trigramLanguageModels
            2 -> bigramLanguageModels
            1 -> unigramLanguageModels
            0 -> throw IllegalArgumentException("Zerogram detected")
            else -> throw IllegalArgumentException("unsupported ngram type detected: $ngram")
        }

        return languageModels.getValue(language).value.getRelativeFrequency(ngram)
    }

    private fun <T : Ngram> loadLanguageModel(
        language: Language,
        ngramClass: KClass<T>
    ): Lazy<LanguageModel<T, T>> {
        var languageModel: Lazy<LanguageModel<T, T>>? = null
        val fileName = "${ngramClass.simpleName!!.toLowerCase()}s.json"
        "/language-models/${language.isoCode}/$fileName".asJsonResource { jsonReader ->
            languageModel = lazy { LanguageModel.fromJson(jsonReader, ngramClass, isCachedByMapDB) }
        }
        return languageModel!!
    }

    private fun <T : Ngram> loadLanguageModels(ngramClass: KClass<T>): MutableMap<Language, Lazy<LanguageModel<T, T>>> {
        val languageModels = hashMapOf<Language, Lazy<LanguageModel<T, T>>>()
        for (language in languagesSequence) {
            languageModels[language] = loadLanguageModel(language, ngramClass)
        }
        return languageModels
    }

    internal fun loadAllLanguageModels() {
        unigramLanguageModels = loadLanguageModels(Unigram::class)
        bigramLanguageModels = loadLanguageModels(Bigram::class)
        trigramLanguageModels = loadLanguageModels(Trigram::class)
        quadrigramLanguageModels = loadLanguageModels(Quadrigram::class)
        fivegramLanguageModels = loadLanguageModels(Fivegram::class)
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

        // Android only supports character classes without Is- prefix
        private val LATIN_ALPHABET = try {
            Regex("^[\\p{Latin}]+$")
        } catch (e: PatternSyntaxException) {
            Regex("^[\\p{IsLatin}]+$")
        }

        private val GREEK_ALPHABET = try {
            Regex("^[\\p{Greek}]+$")
        } catch (e: PatternSyntaxException) {
            Regex("^[\\p{IsGreek}]+$")
        }

        private val CYRILLIC_ALPHABET = try {
            Regex("^[\\p{Cyrillic}]+$")
        } catch (e: PatternSyntaxException) {
            Regex("^[\\p{IsCyrillic}]+$")
        }

        private val ARABIC_ALPHABET = try {
            Regex("^[\\p{Arabic}]+$")
        } catch (e: PatternSyntaxException) {
            Regex("^[\\p{IsArabic}]+$")
        }

        private val CHARS_TO_SINGLE_LANGUAGE_MAPPING = mapOf(
            "Ïï" to CATALAN,
            "ĚěŇňŘřŤťŮů" to CZECH,
            "ß" to GERMAN,
            "ŐőŰű" to HUNGARIAN,
            "ĀāĒēĢģĪīĶķĻļŅņ" to LATVIAN,
            "ĖėĮįŲų" to LITHUANIAN,
            "ŁłŃńŚśŹź" to POLISH,
            "Țţ" to ROMANIAN,
            "¿¡" to SPANISH,
            "İıĞğ" to TURKISH,
            """
            ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậ
            ỀềẺẻỂểẼẽỄễẾếẸẹỆệ
            ỈỉĨĩỊị
            ƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỌọỘộỢợ
            ƯưỪừỦủỬửŨũỮữỨứỤụỰự
            ỲỳỶỷỸỹỴỵ
            """.trimIndent() to VIETNAMESE
        )

        private val CHARS_TO_LANGUAGES_MAPPING = mapOf(
            "Ćć" to setOf(CROATIAN, POLISH),
            "Ďď" to setOf(CZECH, ROMANIAN),
            "Đđ" to setOf(CROATIAN, VIETNAMESE),
            "Ãã" to setOf(PORTUGUESE, VIETNAMESE),
            "ĄąĘę" to setOf(LITHUANIAN, POLISH),
            "Ūū" to setOf(LATVIAN, LITHUANIAN),
            "Şş" to setOf(ROMANIAN, TURKISH),
            "Żż" to setOf(POLISH, ROMANIAN),
            "Îî" to setOf(FRENCH, ROMANIAN),
            "Ìì" to setOf(ITALIAN, VIETNAMESE),
            "Ññ" to setOf(BASQUE, SPANISH),

            "ÐðÞþ" to setOf(ICELANDIC, LATVIAN, TURKISH),
            "Ăă" to setOf(CZECH, ROMANIAN, VIETNAMESE),
            "Ûû" to setOf(FRENCH, HUNGARIAN, LATVIAN),
            "ÊêÔô" to setOf(FRENCH, PORTUGUESE, VIETNAMESE),
            "ÈèÙù" to setOf(FRENCH, ITALIAN, VIETNAMESE),

            "Õõ" to setOf(ESTONIAN, HUNGARIAN, PORTUGUESE, VIETNAMESE),
            "Òò" to setOf(CATALAN, ITALIAN, LATVIAN, VIETNAMESE),
            "Øø" to setOf(BOKMAL, DANISH, NORWEGIAN, NYNORSK),
            "Ýý" to setOf(CZECH, ICELANDIC, TURKISH, VIETNAMESE),
            "ČčŠšŽž" to setOf(CZECH, CROATIAN, LATVIAN, LITHUANIAN),
            "Ää" to setOf(ESTONIAN, FINNISH, GERMAN, SWEDISH),

            "Ââ" to setOf(LATVIAN, PORTUGUESE, ROMANIAN, TURKISH, VIETNAMESE),
            "Àà" to setOf(CATALAN, FRENCH, ITALIAN, PORTUGUESE, VIETNAMESE),
            "Üü" to setOf(CATALAN, ESTONIAN, GERMAN, HUNGARIAN, TURKISH),
            "Ææ" to setOf(BOKMAL, DANISH, ICELANDIC, NORWEGIAN, NYNORSK),
            "Åå" to setOf(BOKMAL, DANISH, NORWEGIAN, NYNORSK, SWEDISH),

            "Çç" to setOf(BASQUE, CATALAN, FRENCH, LATVIAN, PORTUGUESE, TURKISH),

            "ÁáÍíÚú" to setOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, VIETNAMESE),
            "Óó" to setOf(CATALAN, HUNGARIAN, ICELANDIC, IRISH, POLISH, PORTUGUESE, VIETNAMESE),
            "Öö" to setOf(ESTONIAN, FINNISH, GERMAN, HUNGARIAN, ICELANDIC, SWEDISH, TURKISH),

            "Éé" to setOf(CATALAN, CZECH, FRENCH, HUNGARIAN, ICELANDIC, IRISH, ITALIAN, PORTUGUESE, VIETNAMESE)
        )
    }
}

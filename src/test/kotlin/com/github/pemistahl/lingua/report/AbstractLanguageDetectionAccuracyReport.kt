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

package com.github.pemistahl.lingua.report

import com.github.pemistahl.lingua.detector.LanguageDetector
import com.github.pemistahl.lingua.model.Language
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.LINGUA
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.OPTIMAIZE
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.TIKA
import com.google.common.base.Optional
import com.optimaize.langdetect.LanguageDetectorBuilder
import com.optimaize.langdetect.i18n.LdLocale
import com.optimaize.langdetect.ngram.NgramExtractors
import com.optimaize.langdetect.profiles.LanguageProfileReader
import com.optimaize.langdetect.text.CommonTextObjectFactories
import org.apache.tika.langdetect.OptimaizeLangDetector
import org.apache.tika.language.detect.LanguageResult
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import java.util.logging.Logger

@TestInstance(Lifecycle.PER_CLASS)
abstract class AbstractLanguageDetectionAccuracyReport(
    private val language: Language,
    private val implementationToUse: LanguageDetectorImplementation
) {
    protected val logger: Logger = Logger.getLogger(this::class.qualifiedName)

    private val singleWordsStatistics = mutableMapOf<Language, Int>()
    private val wordPairsStatistics = mutableMapOf<Language, Int>()
    private val sentencesStatistics = mutableMapOf<Language, Int>()

    private var wordCount = 0
    private var wordPairCount = 0
    private var sentenceCount = 0

    private var wordLengthCount = 0
    private var wordPairLengthCount = 0
    private var sentenceLengthCount = 0

    abstract fun `assert that single words are identified correctly`(singleWord: String)

    abstract fun `assert that word pairs are identified correctly`(wordPair: String)

    abstract fun `assert that entire sentences are identified correctly`(sentence: String)

    protected fun statisticsReport(): String {
        val singleWordsAccuracyValues = mapCountsToAccuracy(singleWordsStatistics)
        val wordPairsAccuracyValues = mapCountsToAccuracy(wordPairsStatistics)
        val sentencesAccuracyValues = mapCountsToAccuracy(sentencesStatistics)

        val (singleWordAccuracy, singleWordAccuracyReport) = getReportData(singleWordsAccuracyValues, wordCount, wordLengthCount, "single words")
        val (wordPairAccuracy, wordPairAccuracyReport) = getReportData(wordPairsAccuracyValues, wordPairCount, wordPairLengthCount, "word pairs")
        val (sentenceAccuracy, sentenceAccuracyReport) = getReportData(sentencesAccuracyValues, sentenceCount, sentenceLengthCount, "sentences")

        val averageAccuracy = if (singleWordAccuracy != null && wordPairAccuracy != null && sentenceAccuracy != null)
            (singleWordAccuracy + wordPairAccuracy + sentenceAccuracy) / 3
        else
            null

        val averageAccuracyReport = if (averageAccuracy != null)
            ">>> Accuracy on average: ${formatAccuracy(averageAccuracy)}"
        else ""

        val reportParts = arrayOf(
            averageAccuracyReport,
            singleWordAccuracyReport,
            wordPairAccuracyReport,
            sentenceAccuracyReport
        )
        val newlines = System.lineSeparator().repeat(2)
        var report = "$newlines##### $language #####"
        for (reportPart in reportParts)
            if (reportPart.isNotEmpty())
                report += "$newlines$reportPart"

        return report
    }

    protected fun computeSingleWordStatistics(singleWord: String) {
        computeStatistics(singleWordsStatistics, singleWord)
        wordCount++
        wordLengthCount += singleWord.length
    }

    protected fun computeWordPairStatistics(wordPair: String) {
        computeStatistics(wordPairsStatistics, wordPair)
        wordPairCount++
        wordPairLengthCount += wordPair.length
    }

    protected fun computeSentenceStatistics(sentence: String) {
        computeStatistics(sentencesStatistics, sentence)
        sentenceCount++
        sentenceLengthCount += sentence.length
    }

    private fun computeStatistics(statistics: MutableMap<Language, Int>, element: String) {
        val detectedLanguage = when (implementationToUse) {
            LINGUA -> {
                if (language == Language.LATIN) linguaDetector.addLanguageModel(Language.LATIN)
                linguaDetector.detectLanguageOf(element)
            }
            OPTIMAIZE -> mapLocaleToLanguage(optimaizeDetector.detect(textObjectFactory.forText(element)))
            TIKA -> {
                tikaDetector.addText(element)
                val detectedLanguage = mapLanguageResultToLanguage(tikaDetector.detect())
                tikaDetector.reset()
                detectedLanguage
            }
        }
        statistics.merge(detectedLanguage, 1, Int::plus)
    }

    private fun computeAccuracy(languageCount: Int, totalLanguagesCount: Int) =
        languageCount.toDouble() / totalLanguagesCount * 100

    private fun formatAccuracy(accuracy: Double) = "%.2f".format(accuracy) + "%"

    private fun formatStatistics(statistics: Map<Language, Double>, language: Language): String {
        return statistics
            .filterNot { it.key == language }
            .toList()
            .sortedByDescending { it.second }
            .joinToString { "${it.first}: ${formatAccuracy(it.second)}" }
    }

    private fun mapCountsToAccuracy(statistics: Map<Language, Int>): Map<Language, Double> {
        return statistics.mapValues { languageCount -> computeAccuracy(languageCount.value, statistics.values.sum()) }
    }

    private fun getReportData(statistics: Map<Language, Double>, count: Int, length: Int, description: String): Pair<Double?, String> {
        return if (statistics.isNotEmpty()) {
            Pair(
                statistics.getValue(language),
                """
                >> Detection of $count $description (average length: ${Math.round(length.toDouble() / count)} chars)
                Accuracy: ${formatAccuracy(statistics.getValue(language))}
                Erroneously classified as ${formatStatistics(statistics, language)}
                """.trimIndent()
            )
        } else {
            Pair(null, "")
        }
    }

    private fun mapLocaleToLanguage(locale: Optional<LdLocale>): Language {
        return if (!locale.isPresent) { Language.UNKNOWN }
        else when (locale.get().language) {
            "da" -> Language.DANISH
            "de" -> Language.GERMAN
            "en" -> Language.ENGLISH
            "es" -> Language.SPANISH
            "fr" -> Language.FRENCH
            "it" -> Language.ITALIAN
            "nl" -> Language.DUTCH
            "pt" -> Language.PORTUGUESE
            "sv" -> Language.SWEDISH
            else -> Language.UNKNOWN
        }
    }

    private fun mapLanguageResultToLanguage(result: LanguageResult): Language {
        return if (result.isUnknown) { Language.UNKNOWN }
        else when (result.language) {
            "da" -> Language.DANISH
            "de" -> Language.GERMAN
            "en" -> Language.ENGLISH
            "es" -> Language.SPANISH
            "fr" -> Language.FRENCH
            "it" -> Language.ITALIAN
            "nl" -> Language.DUTCH
            "pt" -> Language.PORTUGUESE
            "sv" -> Language.SWEDISH
            else -> Language.UNKNOWN
        }
    }

    companion object {
        private val languageIsoCodesToTest = listOf("da", "de", "en", "es", "fr", "it", "nl", "pt", "sv")

        internal val linguaDetector by lazy { LanguageDetector.fromAllBuiltInSpokenLanguages() }

        private val textObjectFactory by lazy { CommonTextObjectFactories.forDetectingShortCleanText() }
        private val optimaizeDetector by lazy {
            val languageLocales = languageIsoCodesToTest.map { LdLocale.fromString(it) }
            val languageProfiles = LanguageProfileReader().readBuiltIn(languageLocales)
            LanguageDetectorBuilder
                .create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .build()
        }
        private val tikaDetector by lazy { OptimaizeLangDetector().loadModels(languageIsoCodesToTest.toSet()) }

        const val CSV_FILE_ENCODING = "UTF-8"
        const val CSV_FILE_DELIMITER = '|'
    }
}

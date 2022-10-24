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

package com.github.pemistahl.lingua.report

import com.github.pemistahl.lingua.api.IsoCode639_1
import com.github.pemistahl.lingua.api.IsoCode639_1.AM
import com.github.pemistahl.lingua.api.IsoCode639_1.AZ
import com.github.pemistahl.lingua.api.IsoCode639_1.BS
import com.github.pemistahl.lingua.api.IsoCode639_1.EO
import com.github.pemistahl.lingua.api.IsoCode639_1.HY
import com.github.pemistahl.lingua.api.IsoCode639_1.KA
import com.github.pemistahl.lingua.api.IsoCode639_1.KK
import com.github.pemistahl.lingua.api.IsoCode639_1.LA
import com.github.pemistahl.lingua.api.IsoCode639_1.LG
import com.github.pemistahl.lingua.api.IsoCode639_1.MI
import com.github.pemistahl.lingua.api.IsoCode639_1.MN
import com.github.pemistahl.lingua.api.IsoCode639_1.NB
import com.github.pemistahl.lingua.api.IsoCode639_1.NN
import com.github.pemistahl.lingua.api.IsoCode639_1.OM
import com.github.pemistahl.lingua.api.IsoCode639_1.SI
import com.github.pemistahl.lingua.api.IsoCode639_1.SN
import com.github.pemistahl.lingua.api.IsoCode639_1.ST
import com.github.pemistahl.lingua.api.IsoCode639_1.TI
import com.github.pemistahl.lingua.api.IsoCode639_1.TN
import com.github.pemistahl.lingua.api.IsoCode639_1.TS
import com.github.pemistahl.lingua.api.IsoCode639_1.XH
import com.github.pemistahl.lingua.api.IsoCode639_1.YO
import com.github.pemistahl.lingua.api.IsoCode639_1.ZU
import com.github.pemistahl.lingua.api.IsoCode639_3
import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.Language.CHINESE
import com.github.pemistahl.lingua.api.Language.UNKNOWN
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.LINGUA
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.OPENNLP
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.OPTIMAIZE
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.TIKA
import com.google.common.base.Optional
import com.optimaize.langdetect.i18n.LdLocale
import com.optimaize.langdetect.ngram.NgramExtractors
import com.optimaize.langdetect.profiles.LanguageProfileReader
import com.optimaize.langdetect.text.CommonTextObjectFactories
import opennlp.tools.langdetect.LanguageDetectorME
import opennlp.tools.langdetect.LanguageDetectorModel
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector
import org.apache.tika.language.detect.LanguageResult
import org.junit.jupiter.api.AfterAll
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Locale
import kotlin.math.roundToInt

abstract class AbstractLanguageDetectionAccuracyReport(
    private val language: Language,
    private val implementationToUse: LanguageDetectorImplementation
) {
    private val singleWordsStatistics = mutableMapOf<Language, Pair<Int, Int>>()
    private val wordPairsStatistics = mutableMapOf<Language, Pair<Int, Int>>()
    private val sentencesStatistics = mutableMapOf<Language, Pair<Int, Int>>()

    private var wordCount = 0
    private var wordPairCount = 0
    private var sentenceCount = 0

    private var wordLengthCount = 0
    private var wordPairLengthCount = 0
    private var sentenceLengthCount = 0

    abstract fun `assert that single words are identified correctly`(singleWord: String)

    abstract fun `assert that word pairs are identified correctly`(wordPair: String)

    abstract fun `assert that entire sentences are identified correctly`(sentence: String)

    @AfterAll
    fun afterAll() {
        val projectRootPath = Paths.get("").toAbsolutePath().toString()
        val accuracyReportsDirectoryName = "accuracy-reports"
        val detectorDirectoryName = implementationToUse.name.lowercase()
        val languageReportFileName = "${language.name.lowercase().replaceFirstChar { it.titlecase() }}.txt"
        val accuracyReportsDirectoryPath = Paths.get(
            projectRootPath,
            accuracyReportsDirectoryName,
            detectorDirectoryName
        )
        val accuracyReportFilePath = Paths.get(
            projectRootPath,
            accuracyReportsDirectoryName,
            detectorDirectoryName,
            languageReportFileName
        )

        println(statisticsReport())

        Files.createDirectories(accuracyReportsDirectoryPath)
        accuracyReportFilePath.toFile().bufferedWriter().use { writer ->
            writer.write(statisticsReport())
        }
    }

    private fun statisticsReport(): String {
        val singleWordsAccuracyValues = mapCountsToAccuracies(singleWordsStatistics)
        val wordPairsAccuracyValues = mapCountsToAccuracies(wordPairsStatistics)
        val sentencesAccuracyValues = mapCountsToAccuracies(sentencesStatistics)

        val (singleWordAccuracies, singleWordAccuracyReport) = getReportData(
            singleWordsAccuracyValues,
            wordCount,
            wordLengthCount,
            "single words"
        )
        val (wordPairAccuracies, wordPairAccuracyReport) = getReportData(
            wordPairsAccuracyValues,
            wordPairCount,
            wordPairLengthCount,
            "word pairs"
        )
        val (sentenceAccuracies, sentenceAccuracyReport) = getReportData(
            sentencesAccuracyValues,
            sentenceCount,
            sentenceLengthCount,
            "sentences"
        )

        val averageAccuracyInLowAccuracyMode =
            (singleWordAccuracies.first + wordPairAccuracies.first + sentenceAccuracies.first) / 3
        val averageAccuracyInHighAccuracyMode =
            (singleWordAccuracies.second + wordPairAccuracies.second + sentenceAccuracies.second) / 3

        val averageAccuracyReport = if (implementationToUse == LINGUA) {
            ">>> Accuracy on average: ${formatAccuracy(averageAccuracyInLowAccuracyMode)} | " +
                formatAccuracy(averageAccuracyInHighAccuracyMode)
        } else {
            ">>> Accuracy on average: ${formatAccuracy(averageAccuracyInHighAccuracyMode)}"
        }

        val reportParts = arrayOf(
            averageAccuracyReport,
            singleWordAccuracyReport,
            wordPairAccuracyReport,
            sentenceAccuracyReport
        )
        val newlines = "\n".repeat(2)
        var report = "##### $language #####"

        if (implementationToUse == LINGUA) {
            val legend = "Legend: 'low accuracy mode | high accuracy mode'"
            report += "$newlines$legend"
        }

        for (reportPart in reportParts)
            if (reportPart.isNotEmpty())
                report += "$newlines$reportPart"

        report += "$newlines>> Exact values:"

        if (implementationToUse == LINGUA) {
            report += " $averageAccuracyInLowAccuracyMode ${singleWordAccuracies.first} " +
                "${wordPairAccuracies.first} ${sentenceAccuracies.first}"
            report += " $averageAccuracyInHighAccuracyMode ${singleWordAccuracies.second} " +
                "${wordPairAccuracies.second} ${sentenceAccuracies.second}"
        } else {
            report += " $averageAccuracyInHighAccuracyMode ${singleWordAccuracies.second} " +
                "${wordPairAccuracies.second} ${sentenceAccuracies.second}"
        }

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

    private fun computeStatistics(statistics: MutableMap<Language, Pair<Int, Int>>, element: String) {
        val detectedLanguages = when (implementationToUse) {
            LINGUA -> {
                val languageInLowAccuracyMode = linguaDetectorWithLowAccuracy.detectLanguageOf(element)
                val languageInHighAccuracyMode = linguaDetectorWithHighAccuracy.detectLanguageOf(element)
                languageInLowAccuracyMode to languageInHighAccuracyMode
            }
            OPTIMAIZE -> null to mapLocaleToLanguage(optimaizeDetector.detect(textObjectFactory.forText(element)))
            TIKA -> {
                tikaDetector.addText(element)
                val detectedLanguage = mapLanguageResultToLanguage(tikaDetector.detect())
                tikaDetector.reset()
                null to detectedLanguage
            }
            OPENNLP -> {
                val detectedLanguage = opennlpDetector.predictLanguage(element)
                val isoCode = try {
                    val isoCode = detectedLanguage.lang.uppercase()
                    IsoCode639_3.valueOf(mapOpenNlpIsoCodeToLinguaIsoCode(isoCode))
                } catch (e: IllegalArgumentException) {
                    IsoCode639_3.NONE
                }
                null to Language.getByIsoCode639_3(isoCode)
            }
        }
        val languageInLowAccuracyMode = detectedLanguages.first
        val languageInHighAccuracyMode = detectedLanguages.second

        if (languageInLowAccuracyMode != null) {
            var languageCounts = statistics[languageInLowAccuracyMode] ?: (0 to 0)
            languageCounts = languageCounts.first + 1 to languageCounts.second
            statistics[languageInLowAccuracyMode] = languageCounts
        }

        var languageCounts = statistics[languageInHighAccuracyMode] ?: (0 to 0)
        languageCounts = languageCounts.first to languageCounts.second + 1
        statistics[languageInHighAccuracyMode] = languageCounts
    }

    private fun computeAccuracy(languageCount: Int, totalLanguagesCount: Int) =
        languageCount.toDouble() / totalLanguagesCount * 100

    private fun formatAccuracy(accuracy: Double) = "%.2f".format(Locale.US, accuracy) + "%"

    private fun formatStatistics(statistics: Map<Language, Pair<Double, Double>>, language: Language): String {
        val sortedEntries = statistics
            .filterNot { it.key == language }
            .toList()
            .sortedByDescending { it.second.second }

        return if (implementationToUse == LINGUA) {
            sortedEntries.joinToString {
                "${it.first}: ${formatAccuracy(it.second.first)} | ${formatAccuracy(it.second.second)}"
            }
        } else {
            sortedEntries.joinToString {
                "${it.first}: ${formatAccuracy(it.second.second)}"
            }
        }
    }

    private fun mapCountsToAccuracies(statistics: Map<Language, Pair<Int, Int>>): Map<Language, Pair<Double, Double>> {
        val sumOfCountsInLowAccuracyMode = statistics.values.sumOf { it.first }
        val sumOfCountsInHighAccuracyMode = statistics.values.sumOf { it.second }

        return statistics.mapValues { languageCount ->
            val lowAccuracy = computeAccuracy(languageCount.value.first, sumOfCountsInLowAccuracyMode)
            val highAccuracy = computeAccuracy(languageCount.value.second, sumOfCountsInHighAccuracyMode)
            lowAccuracy to highAccuracy
        }
    }

    private fun getReportData(
        statistics: Map<Language, Pair<Double, Double>>,
        count: Int,
        length: Int,
        description: String
    ): Pair<Pair<Double, Double>, String> {
        val accuracies = statistics[language] ?: (0.0 to 0.0)
        var report = ">> Detection of $count $description (average length: " +
            "${(length.toDouble() / count).roundToInt()} chars)\n"

        report += if (implementationToUse == LINGUA) {
            "Accuracy: ${formatAccuracy(accuracies.first)} | ${formatAccuracy(accuracies.second)}\n"
        } else {
            "Accuracy: ${formatAccuracy(accuracies.second)}\n"
        }

        report += "Erroneously classified as ${formatStatistics(statistics, language)}"

        return accuracies to report
    }

    private fun mapOpenNlpIsoCodeToLinguaIsoCode(isoCode: String): String {
        return when (isoCode) {
            "CMN", "NAN" -> "ZHO" // map Mandarin and Min Nan to Chinese
            "LVS" -> "LAV" // map Standard Latvian to Latvian
            "NDS" -> "DEU" // map Low German to German
            "PES" -> "FAS" // map Iranian Persian to Persian
            "PNB" -> "PAN" // map Western Punjabi to Punjabi
            else -> isoCode
        }
    }

    private fun mapLocaleToLanguage(locale: Optional<LdLocale>): Language {
        return when {
            !locale.isPresent -> UNKNOWN
            locale.get().language.startsWith("zh") -> CHINESE
            else -> Language.getByIsoCode639_1(IsoCode639_1.valueOf(locale.get().language.uppercase()))
        }
    }

    private fun mapLanguageResultToLanguage(result: LanguageResult): Language {
        return when {
            result.isUnknown -> UNKNOWN
            result.language.startsWith("zh") -> CHINESE
            else -> Language.getByIsoCode639_1(IsoCode639_1.valueOf(result.language.uppercase()))
        }
    }

    companion object {
        private val languageIsoCodesToTest = Language.values().toSet().minus(arrayOf(UNKNOWN)).map {
            it.isoCode639_1
        }.toTypedArray()

        private val filteredIsoCodesForTikaAndOptimaize = languageIsoCodesToTest.filterNot {
            it in setOf(AM, AZ, BS, EO, HY, KA, KK, LA, LG, MI, MN, NB, NN, OM, SI, SN, ST, TI, TN, TS, XH, YO, ZU)
        }.map { it.toString() }

        internal val linguaDetectorWithLowAccuracy by lazy {
            LanguageDetectorBuilder
                .fromIsoCodes639_1(*languageIsoCodesToTest)
                .withLowAccuracyMode()
                .build()
        }

        internal val linguaDetectorWithHighAccuracy by lazy {
            LanguageDetectorBuilder
                .fromIsoCodes639_1(*languageIsoCodesToTest)
                .build()
        }

        private val textObjectFactory by lazy {
            CommonTextObjectFactories.forDetectingShortCleanText()
        }

        private val optimaizeDetector by lazy {
            val languageLocales = filteredIsoCodesForTikaAndOptimaize.map {
                when (it) {
                    "zh" -> LdLocale.fromString("$it-CN")
                    else -> LdLocale.fromString(it)
                }
            }
            val languageProfiles = LanguageProfileReader().readBuiltIn(languageLocales.toSet())
            com.optimaize.langdetect.LanguageDetectorBuilder
                .create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .build()
        }

        private val tikaDetector by lazy {
            OptimaizeLangDetector().loadModels(
                filteredIsoCodesForTikaAndOptimaize.map {
                    when (it) {
                        "zh" -> "$it-CN"
                        else -> it
                    }
                }.toSet()
            )
        }

        private val opennlpDetector by lazy {
            val languageModel = LanguageDetectorModel(
                this::class.java.getResourceAsStream("/opennlp-training-data/langdetect-183.bin")
            )
            LanguageDetectorME(languageModel)
        }

        const val CSV_FILE_ENCODING = "UTF-8"
        const val CSV_FILE_DELIMITER = '|'
    }
}

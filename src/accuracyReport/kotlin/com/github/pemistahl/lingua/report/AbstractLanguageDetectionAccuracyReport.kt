/*
 * Copyright © 2018-2020 Peter M. Stahl pemistahl@gmail.com
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
import com.github.pemistahl.lingua.api.IsoCode639_1.AZ
import com.github.pemistahl.lingua.api.IsoCode639_1.BS
import com.github.pemistahl.lingua.api.IsoCode639_1.EO
import com.github.pemistahl.lingua.api.IsoCode639_1.HY
import com.github.pemistahl.lingua.api.IsoCode639_1.KA
import com.github.pemistahl.lingua.api.IsoCode639_1.KK
import com.github.pemistahl.lingua.api.IsoCode639_1.LA
import com.github.pemistahl.lingua.api.IsoCode639_1.LG
import com.github.pemistahl.lingua.api.IsoCode639_1.MN
import com.github.pemistahl.lingua.api.IsoCode639_1.NB
import com.github.pemistahl.lingua.api.IsoCode639_1.NN
import com.github.pemistahl.lingua.api.IsoCode639_1.SN
import com.github.pemistahl.lingua.api.IsoCode639_1.ST
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
import org.apache.tika.langdetect.OptimaizeLangDetector
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

    @AfterAll
    fun afterAll() {
        val projectRootPath = Paths.get("").toAbsolutePath().toString()
        val accuracyReportsDirectoryName = "accuracy-reports"
        val detectorDirectoryName = implementationToUse.name.toLowerCase()
        val languageReportFileName = "${language.name.toLowerCase().capitalize()}.txt"
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
        val singleWordsAccuracyValues = mapCountsToAccuracy(singleWordsStatistics)
        val wordPairsAccuracyValues = mapCountsToAccuracy(wordPairsStatistics)
        val sentencesAccuracyValues = mapCountsToAccuracy(sentencesStatistics)

        val (singleWordAccuracy, singleWordAccuracyReport) = getReportData(
            singleWordsAccuracyValues,
            wordCount,
            wordLengthCount,
            "single words"
        )
        val (wordPairAccuracy, wordPairAccuracyReport) = getReportData(
            wordPairsAccuracyValues,
            wordPairCount,
            wordPairLengthCount,
            "word pairs"
        )
        val (sentenceAccuracy, sentenceAccuracyReport) = getReportData(
            sentencesAccuracyValues,
            sentenceCount,
            sentenceLengthCount,
            "sentences"
        )

        val averageAccuracy = (singleWordAccuracy + wordPairAccuracy + sentenceAccuracy) / 3
        val averageAccuracyReport = ">>> Accuracy on average: ${formatAccuracy(averageAccuracy)}"

        val reportParts = arrayOf(
            averageAccuracyReport,
            singleWordAccuracyReport,
            wordPairAccuracyReport,
            sentenceAccuracyReport
        )
        val newlines = System.lineSeparator().repeat(2)
        var report = "##### $language #####"
        for (reportPart in reportParts)
            if (reportPart.isNotEmpty())
                report += "$newlines$reportPart"

        report += newlines
        report += ">> Exact values: $averageAccuracy $singleWordAccuracy $wordPairAccuracy $sentenceAccuracy"

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
            LINGUA -> linguaDetector.detectLanguageOf(element)
            OPTIMAIZE -> mapLocaleToLanguage(optimaizeDetector.detect(textObjectFactory.forText(element)))
            TIKA -> {
                tikaDetector.addText(element)
                val detectedLanguage = mapLanguageResultToLanguage(tikaDetector.detect())
                tikaDetector.reset()
                detectedLanguage
            }
            OPENNLP -> {
                val detectedLanguage = opennlpDetector.predictLanguage(element)
                val isoCode = try {
                    val isoCode = detectedLanguage.lang.toUpperCase()
                    IsoCode639_3.valueOf(mapOpenNlpIsoCodeToLinguaIsoCode(isoCode))
                } catch (e: IllegalArgumentException) {
                    IsoCode639_3.NONE
                }
                Language.getByIsoCode639_3(isoCode)
            }
        }
        statistics[detectedLanguage] = statistics.getOrDefault(detectedLanguage, 0) + 1
    }

    private fun computeAccuracy(languageCount: Int, totalLanguagesCount: Int) =
        languageCount.toDouble() / totalLanguagesCount * 100

    private fun formatAccuracy(accuracy: Double) = "%.2f".format(Locale.US, accuracy) + "%"

    private fun formatStatistics(statistics: Map<Language, Double>, language: Language): String {
        return statistics
            .filterNot { it.key == language }
            .toList()
            .sortedByDescending { it.second }
            .joinToString { "${it.first}: ${formatAccuracy(it.second)}" }
    }

    private fun mapCountsToAccuracy(statistics: Map<Language, Int>): Map<Language, Double> {
        return statistics.mapValues { languageCount ->
            computeAccuracy(languageCount.value, statistics.values.sum())
        }
    }

    private fun getReportData(
        statistics: Map<Language, Double>,
        count: Int,
        length: Int,
        description: String
    ): Pair<Double, String> {
        val accuracy = statistics[language] ?: 0.0

        return Pair(
            accuracy,
            """
            >> Detection of $count $description (average length: ${(length.toDouble() / count).roundToInt()} chars)
            Accuracy: ${formatAccuracy(accuracy)}
            Erroneously classified as ${formatStatistics(statistics, language)}
            """.trimIndent()
        )
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
            else -> Language.getByIsoCode639_1(IsoCode639_1.valueOf(locale.get().language.toUpperCase()))
        }
    }

    private fun mapLanguageResultToLanguage(result: LanguageResult): Language {
        return when {
            result.isUnknown -> UNKNOWN
            result.language.startsWith("zh") -> CHINESE
            else -> Language.getByIsoCode639_1(IsoCode639_1.valueOf(result.language.toUpperCase()))
        }
    }

    companion object {
        private val languageIsoCodesToTest = Language.values().toSet().minus(arrayOf(UNKNOWN)).map {
            it.isoCode639_1
        }.toTypedArray()

        private val filteredIsoCodesForTikaAndOptimaize = languageIsoCodesToTest.filterNot {
            it in setOf(AZ, BS, EO, HY, KA, KK, LA, LG, MN, NB, NN, SN, ST, TN, TS, XH, YO, ZU)
        }.map { it.toString() }

        internal val linguaDetector by lazy {
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

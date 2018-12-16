package com.github.pemistahl.lingua.detector.report

import com.github.pemistahl.lingua.detector.LanguageDetector
import com.github.pemistahl.lingua.model.Language
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import java.util.logging.Logger

@TestInstance(Lifecycle.PER_CLASS)
abstract class AbstractLanguageDetectionAccuracyReport(
    private val language: Language
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
        val detectedLanguage = detector.detectLanguageOf(element)
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

    companion object {
        private val detector = LanguageDetector.fromAllBuiltInLanguages()

        const val CSV_FILE_ENCODING = "UTF-8"
        const val CSV_FILE_DELIMITER = '|'
    }
}

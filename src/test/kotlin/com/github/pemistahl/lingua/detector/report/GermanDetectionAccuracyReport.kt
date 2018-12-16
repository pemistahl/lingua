package com.github.pemistahl.lingua.detector.report

import com.github.pemistahl.lingua.model.Language
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource

class GermanDetectionAccuracyReport : AbstractLanguageDetectionAccuracyReport(Language.GERMAN) {

    @ParameterizedTest
    @CsvFileSource(resources = ["/language-testdata/single-words/de.txt"], delimiter = CSV_FILE_DELIMITER, encoding = CSV_FILE_ENCODING)
    @DisplayName("single word detection")
    override fun `assert that single words are identified correctly`(singleWord: String) {
        computeSingleWordStatistics(singleWord)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/language-testdata/word-pairs/de.txt"], delimiter = CSV_FILE_DELIMITER, encoding = CSV_FILE_ENCODING)
    @DisplayName("word pair detection")
    override fun `assert that word pairs are identified correctly`(wordPair: String) {
        computeWordPairStatistics(wordPair)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/language-testdata/sentences/de.txt"], delimiter = CSV_FILE_DELIMITER, encoding = CSV_FILE_ENCODING)
    @DisplayName("sentence detection")
    override fun `assert that entire sentences are identified correctly`(sentence: String) {
        computeSentenceStatistics(sentence)
    }

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}

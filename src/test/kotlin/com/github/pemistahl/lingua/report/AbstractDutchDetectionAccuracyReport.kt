package com.github.pemistahl.lingua.report

import com.github.pemistahl.lingua.model.Language
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource

abstract class AbstractDutchDetectionAccuracyReport(
    implementationToUse: LanguageDetectorImplementation
) : AbstractLanguageDetectionAccuracyReport(Language.DUTCH, implementationToUse) {

    @ParameterizedTest
    @CsvFileSource(resources = ["/language-testdata/single-words/nl.txt"], delimiter = CSV_FILE_DELIMITER, encoding = CSV_FILE_ENCODING)
    @DisplayName("single word detection")
    override fun `assert that single words are identified correctly`(singleWord: String) {
        computeSingleWordStatistics(singleWord)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/language-testdata/word-pairs/nl.txt"], delimiter = CSV_FILE_DELIMITER, encoding = CSV_FILE_ENCODING)
    @DisplayName("word pair detection")
    override fun `assert that word pairs are identified correctly`(wordPair: String) {
        computeWordPairStatistics(wordPair)
    }

    @ParameterizedTest
    @CsvFileSource(resources = ["/language-testdata/sentences/nl.txt"], delimiter = CSV_FILE_DELIMITER, encoding = CSV_FILE_ENCODING)
    @DisplayName("sentence detection")
    override fun `assert that entire sentences are identified correctly`(sentence: String) {
        computeSentenceStatistics(sentence)
    }
}

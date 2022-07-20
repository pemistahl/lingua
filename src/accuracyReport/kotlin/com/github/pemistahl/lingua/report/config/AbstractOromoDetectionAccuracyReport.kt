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

package com.github.pemistahl.lingua.report.config

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.report.AbstractLanguageDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource

abstract class AbstractOromoDetectionAccuracyReport(
    implementationToUse: LanguageDetectorImplementation
) : AbstractLanguageDetectionAccuracyReport(Language.OROMO, implementationToUse) {

    @ParameterizedTest
    @CsvFileSource(
        resources = ["/language-testdata/single-words/om.txt"],
        delimiter = CSV_FILE_DELIMITER,
        encoding = CSV_FILE_ENCODING
    )
    @DisplayName("single word detection")
    override fun `assert that single words are identified correctly`(singleWord: String) {
        computeSingleWordStatistics(singleWord)
    }

    @ParameterizedTest
    @CsvFileSource(
        resources = ["/language-testdata/word-pairs/om.txt"],
        delimiter = CSV_FILE_DELIMITER,
        encoding = CSV_FILE_ENCODING
    )
    @DisplayName("word pair detection")
    override fun `assert that word pairs are identified correctly`(wordPair: String) {
        computeWordPairStatistics(wordPair)
    }

    @ParameterizedTest
    @CsvFileSource(
        resources = ["/language-testdata/sentences/om.txt"],
        delimiter = CSV_FILE_DELIMITER,
        encoding = CSV_FILE_ENCODING
    )
    @DisplayName("sentence detection")
    override fun `assert that entire sentences are identified correctly`(sentence: String) {
        computeSentenceStatistics(sentence)
    }
}

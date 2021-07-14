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

package com.github.pemistahl.lingua.api.io

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.internal.Constant.MULTIPLE_WHITESPACE
import com.github.pemistahl.lingua.internal.Constant.NUMBERS
import com.github.pemistahl.lingua.internal.Constant.PUNCTUATION
import com.github.pemistahl.lingua.internal.io.FilesWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

object TestDataFilesWriter : FilesWriter() {

    /**
     * Creates test data files for accuracy report generation and writes them to a directory.
     *
     * @param inputFilePath The path to a txt file used for test data creation.
     * @param inputFileCharset The encoding of [inputFilePath]. Defaults to [Charsets.UTF_8].
     * @param outputDirectoryPath The directory where the test data files are to be written.
     * @param language The language for which to create test data.
     * @param charClass A regex character class as supported by [java.util.regex.Pattern]
     * to restrict the set of characters that the test data files are built from. Defaults to `\p{L}`.
     * @param maximumLines The maximum number of lines each test data files should have.
     */
    @JvmStatic
    fun createAndWriteTestDataFiles(
        inputFilePath: Path,
        inputFileCharset: Charset = Charsets.UTF_8,
        outputDirectoryPath: Path,
        language: Language,
        charClass: String = "\\p{L}",
        maximumLines: Int
    ) {
        checkInputFilePath(inputFilePath)
        checkOutputDirectoryPath(outputDirectoryPath)

        createAndWriteSentencesFile(
            inputFilePath,
            inputFileCharset,
            outputDirectoryPath,
            language,
            maximumLines
        )
        val singleWords = createAndWriteSingleWordsFile(
            inputFilePath,
            inputFileCharset,
            outputDirectoryPath,
            language,
            charClass,
            maximumLines
        )
        createAndWriteWordPairsFile(singleWords, outputDirectoryPath, language, maximumLines)
    }

    private fun createAndWriteSentencesFile(
        inputFilePath: Path,
        inputFileCharset: Charset,
        outputDirectoryPath: Path,
        language: Language,
        maximumLines: Int
    ) {
        val fileName = "${language.isoCode639_1}.txt"
        val sentencesDirectoryPath = outputDirectoryPath.resolve("sentences")
        val sentencesFilePath = sentencesDirectoryPath.resolve(fileName)
        var lineCounter = 0

        if (!Files.isDirectory(sentencesDirectoryPath)) {
            Files.createDirectory(sentencesDirectoryPath)
        }
        if (Files.isRegularFile(sentencesFilePath)) {
            Files.delete(sentencesFilePath)
        }

        inputFilePath.toFile().bufferedReader(charset = inputFileCharset).useLines { lines ->
            sentencesFilePath.toFile().bufferedWriter().use { writer ->
                for (line in lines) {
                    if (lineCounter < maximumLines) {
                        writer.write(line.replace(MULTIPLE_WHITESPACE, " ").replace("\"", ""))
                        writer.newLine()
                        lineCounter++
                    }
                }
            }
        }
    }

    private fun createAndWriteSingleWordsFile(
        inputFilePath: Path,
        inputFileCharset: Charset,
        outputDirectoryPath: Path,
        language: Language,
        charClass: String,
        maximumLines: Int
    ): List<String> {
        val fileName = "${language.isoCode639_1}.txt"
        val singleWordsDirectoryPath = outputDirectoryPath.resolve("single-words")
        val singleWordsFilePath = singleWordsDirectoryPath.resolve(fileName)
        val wordRegex = Regex("[$charClass]{5,}")

        val words = mutableListOf<String>()
        var lineCounter = 0

        if (!Files.isDirectory(singleWordsDirectoryPath)) {
            Files.createDirectory(singleWordsDirectoryPath)
        }
        if (Files.isRegularFile(singleWordsFilePath)) {
            Files.delete(singleWordsFilePath)
        }

        inputFilePath.toFile().bufferedReader(charset = inputFileCharset).useLines { lines ->
            for (line in lines) {
                val singleWords = line
                    .replace(PUNCTUATION, "")
                    .replace(NUMBERS, "")
                    .replace(MULTIPLE_WHITESPACE, " ")
                    .replace("\"", "")
                    .split(' ')
                    .map { it.trim().lowercase() }
                    .filter { wordRegex.matches(it) }

                words.addAll(singleWords)
            }
        }

        singleWordsFilePath.toFile().bufferedWriter().use { writer ->
            for (word in words) {
                if (lineCounter < maximumLines) {
                    writer.write(word)
                    writer.newLine()
                    lineCounter++
                } else {
                    break
                }
            }
        }

        return words
    }

    private fun createAndWriteWordPairsFile(
        words: List<String>,
        outputDirectoryPath: Path,
        language: Language,
        maximumLines: Int
    ) {
        val fileName = "${language.isoCode639_1}.txt"
        val wordPairsDirectoryPath = outputDirectoryPath.resolve("word-pairs")
        val wordPairsFilePath = wordPairsDirectoryPath.resolve(fileName)
        val wordPairs = mutableSetOf<String>()
        var lineCounter = 0

        if (!Files.isDirectory(wordPairsDirectoryPath)) {
            Files.createDirectory(wordPairsDirectoryPath)
        }
        if (Files.isRegularFile(wordPairsFilePath)) {
            Files.delete(wordPairsFilePath)
        }

        for (i in 0..(words.size - 2) step 2) {
            wordPairs.add(words.slice(i..i + 1).joinToString(" "))
        }

        wordPairsFilePath.toFile().bufferedWriter().use { writer ->
            for (wordPair in wordPairs) {
                if (lineCounter < maximumLines) {
                    writer.write(wordPair)
                    writer.newLine()
                    lineCounter++
                } else {
                    break
                }
            }
        }
    }
}

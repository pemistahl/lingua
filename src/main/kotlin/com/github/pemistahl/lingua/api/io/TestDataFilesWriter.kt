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

package com.github.pemistahl.lingua.api.io

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

object TestDataFilesWriter : FilesWriter() {

    private val PUNCTUATION = Regex("\\p{P}")
    private val NUMBERS = Regex("\\p{N}")
    private val MULTIPLE_WHITESPACE = Regex("\\s+")

    @JvmStatic
    fun createAndWriteTestDataFiles(
        inputFilePath: Path,
        inputFileCharset: Charset = Charsets.UTF_8,
        outputDirectoryPath: Path,
        fileName: String,
        charClass: String?,
        maximumLines: Int
    ) {
        checkInputFilePath(inputFilePath)
        checkOutputDirectoryPath(outputDirectoryPath)

        createAndWriteSentencesFile(inputFilePath, inputFileCharset, outputDirectoryPath, fileName, maximumLines)
        val singleWords = createAndWriteSingleWordsFile(inputFilePath, inputFileCharset, outputDirectoryPath, fileName, charClass, maximumLines)
        createAndWriteWordPairsFile(singleWords, outputDirectoryPath, fileName, maximumLines)
    }

    private fun createAndWriteSentencesFile(
        inputFilePath: Path,
        inputFileCharset: Charset,
        outputDirectoryPath: Path,
        fileName: String,
        maximumLines: Int
    ) {
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
        fileName: String,
        charClass: String?,
        maximumLines: Int
    ): List<String> {
        val singleWordsDirectoryPath = outputDirectoryPath.resolve("single-words")
        val singleWordsFilePath = singleWordsDirectoryPath.resolve(fileName)

        val wordRegex = if (charClass != null) {
            Regex("""[\p{L}&&\p{$charClass}]{5,}""")
        } else {
            Regex("""[\p{L}]{5,}""")
        }

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
                    .map { it.trim().toLowerCase() }
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
                }
            }
        }

        return words
    }

    private fun createAndWriteWordPairsFile(
        words: List<String>,
        outputDirectoryPath: Path,
        fileName: String,
        maximumLines: Int
    ) {
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

        for (i in 0..(words.size-2)) {
            wordPairs.add(words.slice(i..i+1).joinToString(" "))
        }

        wordPairsFilePath.toFile().bufferedWriter().use { writer ->
            for (wordPair in wordPairs) {
                if (lineCounter < maximumLines) {
                    writer.write(wordPair)
                    writer.newLine()
                    lineCounter++
                }
            }
        }
    }
}

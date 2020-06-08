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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors.toList

class TestDataFilesWriterTest {

    private lateinit var inputFilePath: Path

    private val text = """
        There are many attributes associated with good software.
        Some of these can be mutually contradictory, and different customers and participants may have different priorities.
        Weinberg provides an example of how different goals can have a dramatic effect on both effort required and efficiency.
        Furthermore, he notes that programmers will generally aim to achieve any explicit goals which may be set, probably at the expense of any other quality attributes.
        Sommerville has identified four generalised attributes which are not concerned with what a program does, but how well the program does it:
        Maintainability, Dependability, Efficiency, Usability
    """.trimIndent()

    private val expectedSentencesFileContent = """
        There are many attributes associated with good software.
        Some of these can be mutually contradictory, and different customers and participants may have different priorities.
        Weinberg provides an example of how different goals can have a dramatic effect on both effort required and efficiency.
        Furthermore, he notes that programmers will generally aim to achieve any explicit goals which may be set, probably at the expense of any other quality attributes.

    """.trimIndent()

    private val expectedSingleWordsFileContent = """
        there
        attributes
        associated
        software

    """.trimIndent()

    private val expectedWordPairsFileContent = """
        there attributes
        attributes associated
        associated software
        software these

    """.trimIndent()

    @BeforeEach
    fun beforeEach() {
        inputFilePath = Files.createTempFile(null, null)
        inputFilePath.toFile().writeText(text)
    }

    @AfterEach
    fun afterEach() {
        inputFilePath.toFile().delete()
    }

    @Test
    fun createAndWriteTestDataFiles(@TempDir outputDirectoryPath: Path) {
        TestDataFilesWriter.createAndWriteTestDataFiles(
            inputFilePath = inputFilePath,
            outputDirectoryPath = outputDirectoryPath,
            fileName = "en.txt",
            charClass = "IsLatin",
            maximumLines = 4
        )

        val subDirectoryPaths = retrieveAndSortSubdirectories(outputDirectoryPath)

        assertThat(subDirectoryPaths.size).`as`("number of subdirectories").isEqualTo(3)

        val (sentencesDirectory, singleWordsDirectory, wordPairsDirectory) = subDirectoryPaths

        testSubDirectory(sentencesDirectory, "sentences", expectedSentencesFileContent)
        testSubDirectory(singleWordsDirectory, "single-words", expectedSingleWordsFileContent)
        testSubDirectory(wordPairsDirectory, "word-pairs", expectedWordPairsFileContent)
    }

    private fun retrieveAndSortSubdirectories(outputDirectoryPath: Path): List<Path> {
        return Files.list(outputDirectoryPath).sorted { first, second ->
            first.fileName.compareTo(second.fileName)
        }.collect(toList())
    }

    private fun testSubDirectory(
       subDirectoryPath: Path,
       expectedDirectoryName: String,
       expectedFileContent: String
    ) {
        assertThat(subDirectoryPath).`as`("directory check").isDirectory()

        val directoryName = subDirectoryPath.fileName.toString()
        assertThat(directoryName).`as`("directory name").isEqualTo(expectedDirectoryName)

        val directoryContent = Files.list(subDirectoryPath).collect(toList())
        assertThat(directoryContent.size).`as`("number of files").isEqualTo(1)

        val testDataFilePath = directoryContent[0]
        assertThat(testDataFilePath).`as`("regular file check").isRegularFile()

        val testDataFileName = testDataFilePath.fileName.toString()
        assertThat(testDataFileName).`as`("file name").isEqualTo("en.txt")

        val testDataFileContent = testDataFilePath.toFile().readText()
        assertThat(testDataFileContent).`as`("file content").isEqualTo(expectedFileContent)
    }
}

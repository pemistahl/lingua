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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS.WINDOWS
import org.junit.jupiter.api.io.TempDir
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.NotDirectoryException
import java.nio.file.Path
import java.util.stream.Collectors.toList
import kotlin.io.path.Path

class TestDataFilesWriterTest {

    private lateinit var inputFilePath: Path

    private val text =
        """
        There are many attributes associated with good software.
        Some of these can be mutually contradictory, and different customers and participants may have different priorities.
        Weinberg provides an example of how different goals can have a dramatic effect on both effort required and efficiency.
        Furthermore, he notes that programmers will generally aim to achieve any explicit goals which may be set, probably at the expense of any other quality attributes.
        Sommerville has identified four generalised attributes which are not concerned with what a program does, but how well the program does it:
        Maintainability, Dependability, Efficiency, Usability
        """.trimIndent()

    private val expectedSentencesFileContent =
        """
        There are many attributes associated with good software.
        Some of these can be mutually contradictory, and different customers and participants may have different priorities.
        Weinberg provides an example of how different goals can have a dramatic effect on both effort required and efficiency.
        Furthermore, he notes that programmers will generally aim to achieve any explicit goals which may be set, probably at the expense of any other quality attributes.

        """.trimIndent()

    private val expectedSingleWordsFileContent =
        """
        there
        attributes
        associated
        software

        """.trimIndent()

    private val expectedWordPairsFileContent =
        """
        there attributes
        associated software
        these mutually
        contradictory different

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
    @DisabledOnOs(WINDOWS) // TempDir cannot be deleted on Windows
    fun createAndWriteTestDataFiles(@TempDir outputDirectoryPath: Path) {
        TestDataFilesWriter.createAndWriteTestDataFiles(
            inputFilePath = inputFilePath,
            outputDirectoryPath = outputDirectoryPath,
            language = Language.ENGLISH,
            charClass = "\\p{L}&&\\p{IsLatin}",
            maximumLines = 4
        )

        val subDirectoryPaths = retrieveAndSortSubdirectories(outputDirectoryPath)

        assertThat(subDirectoryPaths.size).`as`("number of subdirectories").isEqualTo(3)

        val (sentencesDirectory, singleWordsDirectory, wordPairsDirectory) = subDirectoryPaths

        testSubDirectory(sentencesDirectory, "sentences", expectedSentencesFileContent)
        testSubDirectory(singleWordsDirectory, "single-words", expectedSingleWordsFileContent)
        testSubDirectory(wordPairsDirectory, "word-pairs", expectedWordPairsFileContent)
    }

    @Test
    fun `assert that relative input file path throws exception`() {
        val relativeInputFilePath = Path("some/relative/path/file.txt")
        val exception = assertThrows<IllegalArgumentException> {
            TestDataFilesWriter.createAndWriteTestDataFiles(
                inputFilePath = relativeInputFilePath,
                outputDirectoryPath = Path("/some/output/directory"),
                language = Language.ENGLISH,
                maximumLines = 4
            )
        }
        assertThat(exception.message).isEqualTo(
            "Input file path '$relativeInputFilePath' is not absolute"
        )
    }

    @Test
    fun `assert that non-existing input file throws exception`() {
        val nonExistingInputFilePath = Path("/some/non-existing/path/file.txt").toAbsolutePath()
        val exception = assertThrows<java.nio.file.NoSuchFileException> {
            TestDataFilesWriter.createAndWriteTestDataFiles(
                inputFilePath = nonExistingInputFilePath,
                outputDirectoryPath = Path("/some/output/directory"),
                language = Language.ENGLISH,
                maximumLines = 4
            )
        }
        assertThat(exception.message).isEqualTo(
            "Input file '$nonExistingInputFilePath' does not exist"
        )
    }

    @Test
    @DisabledOnOs(WINDOWS) // TempDir cannot be deleted on Windows
    fun `assert that directory as input file throws exception`(
        @TempDir inputFilePath: Path
    ) {
        val exception = assertThrows<FileNotFoundException> {
            TestDataFilesWriter.createAndWriteTestDataFiles(
                inputFilePath = inputFilePath,
                outputDirectoryPath = Path("/some/output/directory"),
                language = Language.ENGLISH,
                maximumLines = 4
            )
        }
        assertThat(exception.message).isEqualTo(
            "Input file path '$inputFilePath' does not represent a regular file"
        )
    }

    @Test
    fun `assert that relative output directory path throws exception`() {
        val relativeOutputDirectoryPath = Path("some/relative/path")
        val exception = assertThrows<IllegalArgumentException> {
            TestDataFilesWriter.createAndWriteTestDataFiles(
                inputFilePath = inputFilePath,
                outputDirectoryPath = relativeOutputDirectoryPath,
                language = Language.ENGLISH,
                maximumLines = 4
            )
        }
        assertThat(exception.message).isEqualTo(
            "Output directory path '$relativeOutputDirectoryPath' is not absolute"
        )
    }

    @Test
    fun `assert that non-existing output directory path throws exception`() {
        val nonExistingOutputDirectoryPath = Path("/some/non-existing/directory").toAbsolutePath()
        val exception = assertThrows<NotDirectoryException> {
            TestDataFilesWriter.createAndWriteTestDataFiles(
                inputFilePath = inputFilePath,
                outputDirectoryPath = nonExistingOutputDirectoryPath,
                language = Language.ENGLISH,
                maximumLines = 4
            )
        }
        assertThat(exception.message).isEqualTo(
            "Output directory '$nonExistingOutputDirectoryPath' does not exist"
        )
    }

    @Test
    fun `assert that file as output directory throws exception`() {
        val exception = assertThrows<NotDirectoryException> {
            TestDataFilesWriter.createAndWriteTestDataFiles(
                inputFilePath = inputFilePath,
                outputDirectoryPath = inputFilePath,
                language = Language.ENGLISH,
                maximumLines = 4
            )
        }
        assertThat(exception.message).isEqualTo(
            "Output directory path '$inputFilePath' does not represent a directory"
        )
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
        assertThat(testDataFileName).`as`("file name").isEqualTo("eng.txt")

        val testDataFileContent = testDataFilePath.toFile().readText()
        assertThat(testDataFileContent.replace("\r\n", "\n")).`as`("file content")
            .isEqualTo(expectedFileContent.replace("\r\n", "\n"))
    }
}

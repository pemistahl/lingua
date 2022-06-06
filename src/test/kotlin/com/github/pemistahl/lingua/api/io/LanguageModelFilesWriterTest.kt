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

class LanguageModelFilesWriterTest {

    private lateinit var inputFilePath: Path

    private val text =
        """
        These sentences are intended for testing purposes.
        Do not use them in production!
        By the way, they consist of 23 words in total.
        """.lowercase().trimIndent()

    private val expectedUnigramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "13/100":"t",
                "1/25":"h",
                "7/50":"e",
                "1/10":"s n o",
                "3/100":"c a p u y",
                "1/20":"r d",
                "3/50":"i",
                "1/50":"f w",
                "1/100":"g m b l"
            }
        }
        """.minify()

    private val expectedBigramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "4/13":"th",
                "1/1":"he by",
                "2/7":"es",
                "2/5":"se",
                "3/14":"en",
                "1/5":"nt re de or st rp do ot ro du on rd ds",
                "3/13":"te",
                "1/10":"nc nd ng os no od ns si of",
                "1/3":"ce ar pu ur po us pr uc ct ay co al",
                "2/3":"in",
                "1/14":"ed em ey",
                "1/2":"fo wa wo",
                "2/13":"ti",
                "1/6":"io is",
                "1/13":"to ta"
            }
        }
        """.minify()

    private val expectedTrigramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/1":"the nte nce ces are nde ded for pur urp rpo pos ose not use pro rod odu duc uct cti ion way con nsi sis ist wor rds tot tal",
                "1/4":"hes ese sen int est ing ses hem hey",
                "1/3":"ent enc end tes",
                "2/3":"ten",
                "1/2":"sti tin tio ons ord ota"
            }
        }
        """.minify()

    private val expectedQuadrigramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/4":"thes them they",
                "1/1":"hese sent ente nten ence nces inte ende nded test esti stin ting purp urpo rpos pose oses prod rodu oduc duct ucti ctio tion cons onsi nsis sist word ords tota otal",
                "1/2":"tenc tend"
            }
        }
        """.minify()

    private val expectedFivegramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/1":"these sente enten tence ences inten tende ended testi estin sting purpo urpos rpose poses produ roduc oduct ducti uctio ction consi onsis nsist words total",
                "1/2":"ntenc ntend"
            }
        }
        """.minify()

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
    fun createAndWriteLanguageModelFiles(@TempDir outputDirectoryPath: Path) {
        LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
            inputFilePath = inputFilePath,
            outputDirectoryPath = outputDirectoryPath,
            language = Language.ENGLISH,
            charClass = "\\p{L}&&\\p{IsLatin}"
        )

        val modelFilePaths = retrieveAndSortModelFiles(outputDirectoryPath)

        assertThat(modelFilePaths.size).`as`("number of language model files").isEqualTo(5)

        val (bigrams, fivegrams, quadrigrams, trigrams, unigrams) = modelFilePaths

        testModelFile(unigrams, "unigrams.json", expectedUnigramLanguageModel)
        testModelFile(bigrams, "bigrams.json", expectedBigramLanguageModel)
        testModelFile(trigrams, "trigrams.json", expectedTrigramLanguageModel)
        testModelFile(quadrigrams, "quadrigrams.json", expectedQuadrigramLanguageModel)
        testModelFile(fivegrams, "fivegrams.json", expectedFivegramLanguageModel)
    }

    @Test
    fun `assert that relative input file path throws exception`() {
        val relativeInputFilePath = Path("some/relative/path/file.txt")
        val exception = assertThrows<IllegalArgumentException> {
            LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
                inputFilePath = relativeInputFilePath,
                outputDirectoryPath = Path("/some/output/directory"),
                language = Language.ENGLISH
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
            LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
                inputFilePath = nonExistingInputFilePath,
                outputDirectoryPath = Path("/some/output/directory"),
                language = Language.ENGLISH
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
            LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
                inputFilePath = inputFilePath,
                outputDirectoryPath = Path("/some/output/directory"),
                language = Language.ENGLISH
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
            LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
                inputFilePath = inputFilePath,
                outputDirectoryPath = relativeOutputDirectoryPath,
                language = Language.ENGLISH
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
            LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
                inputFilePath = inputFilePath,
                outputDirectoryPath = nonExistingOutputDirectoryPath,
                language = Language.ENGLISH
            )
        }
        assertThat(exception.message).isEqualTo(
            "Output directory '$nonExistingOutputDirectoryPath' does not exist"
        )
    }

    @Test
    fun `assert that file as output directory throws exception`() {
        val exception = assertThrows<NotDirectoryException> {
            LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
                inputFilePath = inputFilePath,
                outputDirectoryPath = inputFilePath,
                language = Language.ENGLISH
            )
        }
        assertThat(exception.message).isEqualTo(
            "Output directory path '$inputFilePath' does not represent a directory"
        )
    }

    private fun retrieveAndSortModelFiles(outputDirectoryPath: Path): List<Path> {
        return Files.list(outputDirectoryPath).sorted { first, second ->
            first.fileName.compareTo(second.fileName)
        }.collect(toList())
    }

    private fun testModelFile(
        modelFilePath: Path,
        expectedFileName: String,
        expectedModelContent: String
    ) {
        assertThat(modelFilePath).`as`("regular file check").isRegularFile()

        val modelFileName = modelFilePath.fileName.toString()
        val modelContent = modelFilePath.toFile().readText()

        assertThat(modelFileName).`as`("model file name").isEqualTo(expectedFileName)
        assertThat(modelContent).`as`("model content").isEqualTo(expectedModelContent)
    }

    private fun String.minify() = this.replace(Regex("\n\\s*"), "")
}

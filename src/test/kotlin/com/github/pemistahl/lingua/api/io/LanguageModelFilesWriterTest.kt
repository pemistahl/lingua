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
import org.junit.jupiter.api.condition.DisabledOnOs
import org.junit.jupiter.api.condition.OS.WINDOWS
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors.toList

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
                "3/100":"a c p u y",
                "1/100":"b g l m",
                "1/20":"d r",
                "7/50":"e",
                "1/50":"f w",
                "1/25":"h",
                "3/50":"i",
                "1/10":"n o s",
                "13/100":"t"
            }
        }
        """.minify()

    private val expectedBigramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/5":"de do ds du rd re ro rp nt on st or ot",
                "1/3":"pr pu uc ur us al ar ay ce co ct po",
                "1/14":"ed em ey",
                "2/3":"in",
                "1/6":"io is",
                "3/14":"en",
                "2/7":"es",
                "1/10":"nc nd ng no ns od si of os",
                "1/2":"fo wa wo",
                "2/5":"se",
                "1/1":"by he",
                "1/13":"ta to",
                "3/13":"te",
                "4/13":"th",
                "2/13":"ti"
            }
        }
        """.minify()

    private val expectedTrigramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/1":"ose rds ded con use ion ist pur cti wor tal uct pro odu nsi rod for ces nce not are pos tot sis nte nde way the rpo urp duc",
                "1/4":"est hem hes hey sen ses ing int ese",
                "1/2":"tin tio ota sti ord ons",
                "2/3":"ten",
                "1/3":"tes end enc ent"
            }
        }
        """.minify()

    private val expectedQuadrigramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/1":"onsi sist ende ords esti nces oduc rpos ting nten nsis tota cons tion prod ence test otal pose nded oses inte urpo sent duct stin ente ucti purp ctio rodu word hese",
                "1/2":"tenc tend",
                "1/4":"thes they them"
            }
        }
        """.minify()

    private val expectedFivegramLanguageModel =
        """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/1":"testi sente ences tende these ducti onsis total uctio enten poses ction produ inten nsist words sting tence purpo estin roduc urpos ended rpose oduct consi",
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

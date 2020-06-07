package com.github.pemistahl.lingua.api

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import java.util.stream.Collectors.toList

class LanguageModelFilesWriterTest {

    private lateinit var inputFilePath: Path

    private val replacementRegex = Regex("\n\\s*")

    private val text = """
        These sentences are intended for testing purposes.
        Do not use them in production!
        By the way, they consist of 23 words in total.
    """.toLowerCase().trimIndent()

    private val expectedUnigramLanguageModel = """
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
    """.replace(replacementRegex, "")

    private val expectedBigramLanguageModel = """
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
    """.replace(replacementRegex, "")

    private val expectedTrigramLanguageModel = """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/1":"rds ose ded con use ion ist pur cti wor tal uct pro odu nsi rod for ces nce not pos are tot sis nte way nde rpo the urp duc",
                "1/4":"est hem hes hey sen ses ing int ese",
                "1/2":"tin tio ota sti ord ons",
                "2/3":"ten",
                "1/3":"tes end enc ent"
            }
        }
    """.replace(replacementRegex, "")

    private val expectedQuadrigramLanguageModel = """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/1":"onsi sist ende ords esti oduc nces rpos ting nsis nten tota cons tion prod otal test ence pose oses nded inte urpo duct sent stin ucti ente purp ctio rodu word hese",
                "1/2":"tenc tend",
                "1/4":"thes they them"
            }
        }
    """.replace(replacementRegex, "")

    private val expectedFivegramLanguageModel = """
        {
            "language":"ENGLISH",
            "ngrams":{
                "1/1":"testi sente ences tende ducti these onsis total uctio enten poses ction produ inten nsist words sting purpo tence estin roduc urpos rpose ended oduct consi",
                "1/2":"ntenc ntend"
            }
        }
    """.replace(replacementRegex, "")

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
    fun createAndWriteLanguageModelFiles(@TempDir outputDirectoryPath: Path) {
        LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
            inputFilePath = inputFilePath,
            outputDirectoryPath = outputDirectoryPath,
            language = Language.ENGLISH,
            charClass = "IsLatin"
        )

        val modelFilePaths = Files.list(outputDirectoryPath).sorted { first, second ->
            first.fileName.compareTo(second.fileName)
        }.collect(toList())

        assertThat(modelFilePaths.size).`as`("number of language model files").isEqualTo(5)

        testModelFile(modelFilePaths[4], "unigrams.json", expectedUnigramLanguageModel)
        testModelFile(modelFilePaths[0], "bigrams.json", expectedBigramLanguageModel)
        testModelFile(modelFilePaths[3], "trigrams.json", expectedTrigramLanguageModel)
        testModelFile(modelFilePaths[2], "quadrigrams.json", expectedQuadrigramLanguageModel)
        testModelFile(modelFilePaths[1], "fivegrams.json", expectedFivegramLanguageModel)
    }

    private fun testModelFile(
        modelFilePath: Path,
        expectedFileName: String,
        expectedModelContent: String
    ) {
        val modelFileName = modelFilePath.fileName.toString()
        val modelContent = modelFilePath.toFile().readText()

        assertThat(modelFileName).`as`("model file name").isEqualTo(expectedFileName)
        assertThat(modelContent).`as`("model content").isEqualTo(expectedModelContent)
    }
}

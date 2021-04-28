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
import com.github.pemistahl.lingua.internal.Ngram
import com.github.pemistahl.lingua.internal.TrainingDataLanguageModel
import com.github.pemistahl.lingua.internal.io.FilesWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

object LanguageModelFilesWriter : FilesWriter() {

    /**
     * Creates language model files and writes them to a directory.
     *
     * @param inputFilePath The path to a txt file used for language model creation.
     * @param inputFileCharset The encoding of [inputFilePath]. Defaults to [Charsets.UTF_8].
     * @param outputDirectoryPath The directory where the language model files are to be written.
     * @param language The language for which to create language models.
     * @param charClass A regex character class as supported by [java.util.regex.Pattern]
     * to restrict the set of characters that the language models are built from. Defaults to `\p{L}`.
     */
    @JvmStatic
    fun createAndWriteLanguageModelFiles(
        inputFilePath: Path,
        inputFileCharset: Charset = Charsets.UTF_8,
        outputDirectoryPath: Path,
        language: Language,
        charClass: String = "\\p{L}"
    ) {
        checkInputFilePath(inputFilePath)
        checkOutputDirectoryPath(outputDirectoryPath)

        val unigramModel = createLanguageModel(
            inputFilePath,
            inputFileCharset,
            language,
            1,
            charClass,
            emptyMap()
        )
        val bigramModel = createLanguageModel(
            inputFilePath,
            inputFileCharset,
            language,
            2,
            charClass,
            unigramModel.absoluteFrequencies
        )
        val trigramModel = createLanguageModel(
            inputFilePath,
            inputFileCharset,
            language,
            3,
            charClass,
            bigramModel.absoluteFrequencies
        )
        val quadrigramModel = createLanguageModel(
            inputFilePath,
            inputFileCharset,
            language,
            4,
            charClass,
            trigramModel.absoluteFrequencies
        )
        val fivegramModel = createLanguageModel(
            inputFilePath,
            inputFileCharset,
            language,
            5,
            charClass,
            quadrigramModel.absoluteFrequencies
        )

        writeLanguageModel(unigramModel, outputDirectoryPath, "unigrams.json")
        writeLanguageModel(bigramModel, outputDirectoryPath, "bigrams.json")
        writeLanguageModel(trigramModel, outputDirectoryPath, "trigrams.json")
        writeLanguageModel(quadrigramModel, outputDirectoryPath, "quadrigrams.json")
        writeLanguageModel(fivegramModel, outputDirectoryPath, "fivegrams.json")
    }

    private fun createLanguageModel(
        inputFilePath: Path,
        inputFileCharset: Charset,
        language: Language,
        ngramLength: Int,
        charClass: String,
        lowerNgramAbsoluteFrequencies: Map<Ngram, Int>
    ): TrainingDataLanguageModel {

        lateinit var model: TrainingDataLanguageModel
        inputFilePath.toFile().bufferedReader(charset = inputFileCharset).useLines { lines ->
            model = TrainingDataLanguageModel.fromText(
                text = lines,
                language = language,
                ngramLength = ngramLength,
                charClass = charClass,
                lowerNgramAbsoluteFrequencies = lowerNgramAbsoluteFrequencies
            )
        }
        return model
    }

    private fun writeLanguageModel(
        model: TrainingDataLanguageModel,
        outputDirectoryPath: Path,
        fileName: String
    ) {
        val modelFilePath = outputDirectoryPath.resolve(fileName)

        if (Files.isRegularFile(modelFilePath)) {
            Files.delete(modelFilePath)
        }

        modelFilePath.toFile().bufferedWriter().use { writer ->
            writer.write(model.toJson())
        }
    }
}

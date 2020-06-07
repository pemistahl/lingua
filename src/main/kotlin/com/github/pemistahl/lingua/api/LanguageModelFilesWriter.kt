package com.github.pemistahl.lingua.api

import com.github.pemistahl.lingua.internal.Ngram
import com.github.pemistahl.lingua.internal.TrainingDataLanguageModel
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.NotDirectoryException
import java.nio.file.Path

object LanguageModelFilesWriter {

    @JvmStatic
    fun createAndWriteLanguageModelFiles(
        inputFilePath: Path,
        outputDirectoryPath: Path,
        language: Language,
        charClass: String?
    ) {
        checkInputFilePath(inputFilePath)
        checkOutputDirectoryPath(outputDirectoryPath)

        val unigramModel = createLanguageModel(inputFilePath, language, 1, charClass, emptyMap())
        val bigramModel = createLanguageModel(inputFilePath, language, 2, charClass, unigramModel.absoluteFrequencies)
        val trigramModel = createLanguageModel(inputFilePath, language, 3, charClass, bigramModel.absoluteFrequencies)
        val quadrigramModel = createLanguageModel(inputFilePath, language, 4, charClass, trigramModel.absoluteFrequencies)
        val fivegramModel = createLanguageModel(inputFilePath, language, 5, charClass, quadrigramModel.absoluteFrequencies)

        writeLanguageModel(unigramModel, outputDirectoryPath, "unigrams.json")
        writeLanguageModel(bigramModel, outputDirectoryPath, "bigrams.json")
        writeLanguageModel(trigramModel, outputDirectoryPath, "trigrams.json")
        writeLanguageModel(quadrigramModel, outputDirectoryPath, "quadrigrams.json")
        writeLanguageModel(fivegramModel, outputDirectoryPath, "fivegrams.json")
    }

    private fun checkInputFilePath(inputFilePath: Path) {
        if (!inputFilePath.isAbsolute()) {
            throw IllegalArgumentException("Input file path '$inputFilePath' is not absolute")
        }
        if (!Files.exists(inputFilePath)) {
            throw NoSuchFileException("Input file '$inputFilePath' does not exist")
        }
        if (!Files.isRegularFile(inputFilePath)) {
            throw FileNotFoundException("Input file path '$inputFilePath' does not represent a regular file")
        }
    }

    private fun checkOutputDirectoryPath(outputDirectoryPath: Path) {
        if (!outputDirectoryPath.isAbsolute()) {
            throw IllegalArgumentException("Output directory path '$outputDirectoryPath' is not absolute")
        }
        if (!Files.exists(outputDirectoryPath)) {
            throw NotDirectoryException("Output directory '$outputDirectoryPath' does not exist")
        }
        if (!Files.isDirectory(outputDirectoryPath)) {
            throw NotDirectoryException("Output directory path '$outputDirectoryPath' does not represent a directory")
        }
    }

    private fun createLanguageModel(
        inputFilePath: Path,
        language: Language,
        ngramLength: Int,
        charClass: String?,
        lowerNgramAbsoluteFrequencies: Map<Ngram, Int>
    ): TrainingDataLanguageModel {

        lateinit var model: TrainingDataLanguageModel
        inputFilePath.toFile().bufferedReader().useLines { lines ->
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
        outputDirectoryPath.resolve(fileName).toFile().bufferedWriter().use { writer ->
            writer.write(model.toJson())
        }
    }
}

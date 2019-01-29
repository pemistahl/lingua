/*
 * Copyright 2018-2019 Peter M. Stahl pemistahl@googlemail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pemistahl.lingua.internal.util

import com.github.pemistahl.lingua.internal.model.Bigram
import com.github.pemistahl.lingua.internal.model.Fivegram
import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.internal.model.LanguageModel
import com.github.pemistahl.lingua.internal.model.Quadrigram
import com.github.pemistahl.lingua.internal.model.Sixgram
import com.github.pemistahl.lingua.internal.model.Trigram
import com.github.pemistahl.lingua.internal.model.Unigram
import com.github.pemistahl.lingua.internal.util.extension.asLineSequenceResource
import java.io.File
import kotlin.random.Random

internal fun writeTestDataFiles(inputPath: String, outputPath: String, isoCode: String, charClass: String) {
    val wordRegex = Regex("""[\p{L}&&\p{$charClass}]{5,}""")
    val whitespaceRegex = Regex("""\s+""")
    val words = mutableSetOf<String>()
    val wordPairs = mutableSetOf<String>()

    var lineCounter = 0
    val randomLineNumbers = (0 until 1100).map { Random.nextInt(0, 9900) }

    println("Stripping line numbers from each line...")
    File("$outputPath/sentences/$isoCode.txt").bufferedWriter().use { writer ->
        inputPath.asLineSequenceResource { lines ->
            for ((idx, line) in lines.withIndex()) {
                if (randomLineNumbers.contains(idx) && lineCounter < 1000) {
                    writer.write(line.split("\t")[1].replace(whitespaceRegex, " ").replace("\"", ""))
                    writer.newLine()
                    lineCounter++
                }
            }
        }
    }
    println("Done.\n")

    lineCounter = 0

    println("Creating words and word pairs...")
    inputPath.asLineSequenceResource { lines ->
        for ((idx, line) in lines.withIndex()) {
            if (randomLineNumbers.contains(idx) && lineCounter < 1000) {
                val singleWords = line
                    .split("\t")[1]
                    .replace(whitespaceRegex, " ")
                    .replace("\"", "")
                    .split(' ')
                    .map { it.trim().toLowerCase() }
                    .filter { wordRegex.matches(it) }

                words.addAll(singleWords)

                for (i in 0..(singleWords.size-2)) {
                    wordPairs.add(
                        singleWords.slice(i..i+1).joinToString(" ")
                    )
                }

                lineCounter++
            }
        }
    }

    lineCounter = 0

    File("$outputPath/single-words/$isoCode.txt").bufferedWriter().use { writer ->
        for (word in words.shuffled()) {
            if (lineCounter < 1000) {
                writer.write(word)
                writer.newLine()
                lineCounter++
            }

        }
    }

    lineCounter = 0

    File("$outputPath/word-pairs/$isoCode.txt").bufferedWriter().use { writer ->
        for (wordPair in wordPairs.shuffled()) {
            if (lineCounter < 1000) {
                writer.write(wordPair)
                writer.newLine()
                lineCounter++
            }

        }
    }
    println("Done.\n")
}

internal fun writeLanguageModelsFromLeipzigCorpusFile(
    inputPath: String,
    outputPath: String,
    language: Language,
    charClass: String
) {
    println("Writing unigrams...")
    lateinit var unigramModel: LanguageModel<Unigram, Unigram>
    inputPath.asLineSequenceResource { lines ->
        unigramModel = LanguageModel.fromTrainingData(lines, language, charClass, emptyMap())
        File("$outputPath/unigrams.json").bufferedWriter().use { writer ->
            writer.write(unigramModel.toJson(Unigram::class))
        }
    }
    println("Done.\n")

    println("Writing bigrams...")
    lateinit var bigramModel: LanguageModel<Bigram, Unigram>
    inputPath.asLineSequenceResource { lines ->
        bigramModel = LanguageModel.fromTrainingData(lines, language, charClass, unigramModel.ngramAbsoluteFrequencies)
        File("$outputPath/bigrams.json").bufferedWriter().use { writer ->
            writer.write(bigramModel.toJson(Bigram::class))
        }
    }
    println("Done.\n")

    println("Writing trigrams...")
    lateinit var trigramModel: LanguageModel<Trigram, Bigram>
    inputPath.asLineSequenceResource { lines ->
        trigramModel = LanguageModel.fromTrainingData(lines, language, charClass, bigramModel.ngramAbsoluteFrequencies)
        File("$outputPath/trigrams.json").bufferedWriter().use { writer ->
            writer.write(trigramModel.toJson(Trigram::class))
        }
    }
    println("Done.\n")

    println("Writing quadrigrams...")
    lateinit var quadrigramModel: LanguageModel<Quadrigram, Trigram>
    inputPath.asLineSequenceResource { lines ->
        quadrigramModel = LanguageModel.fromTrainingData(lines, language, charClass, trigramModel.ngramAbsoluteFrequencies)
        File("$outputPath/quadrigrams.json").bufferedWriter().use { writer ->
            writer.write(quadrigramModel.toJson(Quadrigram::class))
        }
    }
    println("Done.\n")

    println("Writing fivegrams...")
    lateinit var fivegramModel: LanguageModel<Fivegram, Quadrigram>
    inputPath.asLineSequenceResource { lines ->
        fivegramModel = LanguageModel.fromTrainingData(lines, language, charClass, quadrigramModel.ngramAbsoluteFrequencies)
        File("$outputPath/fivegrams.json").bufferedWriter().use { writer ->
            writer.write(fivegramModel.toJson(Fivegram::class))
        }
    }
    println("Done.\n")

    println("Writing sixgrams...")
    lateinit var sixgramModel: LanguageModel<Sixgram, Fivegram>
    inputPath.asLineSequenceResource { lines ->
        sixgramModel = LanguageModel.fromTrainingData(lines, language, charClass, fivegramModel.ngramAbsoluteFrequencies)
        File("$outputPath/sixgrams.json").bufferedWriter().use { writer ->
            writer.write(sixgramModel.toJson(Sixgram::class))
        }
    }


    println("Done.")
}

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

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.Language.UNKNOWN
import com.github.pemistahl.lingua.internal.model.Bigram
import com.github.pemistahl.lingua.internal.model.Fivegram
import com.github.pemistahl.lingua.internal.model.LanguageModel
import com.github.pemistahl.lingua.internal.model.Ngram
import com.github.pemistahl.lingua.internal.model.Quadrigram
import com.github.pemistahl.lingua.internal.model.Trigram
import com.github.pemistahl.lingua.internal.model.Unigram
import com.github.pemistahl.lingua.internal.model.Zerogram
import com.github.pemistahl.lingua.internal.util.extension.asLineSequenceResource
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import kotlin.random.Random

internal fun writeTestDataFiles(inputPath: String, outputPath: String, isoCode: String, charClass: String) {
    val wordRegex = Regex("""[\p{L}&&\p{$charClass}]{5,}""")
    //val punctuation = Regex("""\p{P}+""")
    //val numbers = Regex("""\p{N}+""")
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
        unigramModel = LanguageModel.fromTrainingData(lines, language, Unigram::class, charClass, emptyMap())
        File("$outputPath/unigrams.json").bufferedWriter().use { writer ->
            writer.write(unigramModel.toJson())
        }
    }
    println("Done.\n")

    println("Writing bigrams...")
    lateinit var bigramModel: LanguageModel<Bigram, Unigram>
    inputPath.asLineSequenceResource { lines ->
        bigramModel = LanguageModel.fromTrainingData(lines, language, Bigram::class, charClass, unigramModel.ngramAbsoluteFrequencies)
        File("$outputPath/bigrams.json").bufferedWriter().use { writer ->
            writer.write(bigramModel.toJson())
        }
    }
    println("Done.\n")

    println("Writing trigrams...")
    lateinit var trigramModel: LanguageModel<Trigram, Bigram>
    inputPath.asLineSequenceResource { lines ->
        trigramModel = LanguageModel.fromTrainingData(lines, language, Trigram::class, charClass, bigramModel.ngramAbsoluteFrequencies)
        File("$outputPath/trigrams.json").bufferedWriter().use { writer ->
            writer.write(trigramModel.toJson())
        }
    }
    println("Done.\n")

    println("Writing quadrigrams...")
    lateinit var quadrigramModel: LanguageModel<Quadrigram, Trigram>
    inputPath.asLineSequenceResource { lines ->
        quadrigramModel = LanguageModel.fromTrainingData(lines, language, Quadrigram::class, charClass, trigramModel.ngramAbsoluteFrequencies)
        File("$outputPath/quadrigrams.json").bufferedWriter().use { writer ->
            writer.write(quadrigramModel.toJson())
        }
    }
    println("Done.\n")

    println("Writing fivegrams...")
    lateinit var fivegramModel: LanguageModel<Fivegram, Quadrigram>
    inputPath.asLineSequenceResource { lines ->
        fivegramModel = LanguageModel.fromTrainingData(lines, language, Fivegram::class, charClass, quadrigramModel.ngramAbsoluteFrequencies)
        File("$outputPath/fivegrams.json").bufferedWriter().use { writer ->
            writer.write(fivegramModel.toJson())
        }
    }

    println("Done.")
}

internal fun writeUniqueNgramFiles(outputPath: String) {
    val ngramSubclasses = Ngram::class.sealedSubclasses.filterNot { it == Zerogram::class }
    val ngramType = TypeToken.getParameterized(Set::class.java, String::class.java).type
    val gson = GsonBuilder()
        .registerTypeAdapter(ngramType, NgramsDeserializer())
        .create()

    for (ngramSubclass in ngramSubclasses) {
        val inputJsonFileName = "${ngramSubclass.simpleName?.toLowerCase()}s.json"
        val outputJsonFileName = "unique${ngramSubclass.simpleName}s.json"
        val uniqueNgrams = mutableMapOf<String, Language>()

        for (language in Language.values().filter { it != UNKNOWN }) {
            val jsonResource = readJsonResource(
                "/language-models/${language.isoCode}/$inputJsonFileName"
            )
            val ngrams: Set<String> = gson.fromJson(jsonResource, ngramType)

            for (ngram in ngrams) {
                if (uniqueNgrams.containsKey(ngram)) {
                    uniqueNgrams.remove(ngram)
                    continue
                }
                uniqueNgrams[ngram] = language
            }
        }

        val languageToUniqueNgrams = mutableMapOf<Language, MutableSet<String>>()
        for ((ngram, language) in uniqueNgrams) {
            if (!languageToUniqueNgrams.containsKey(language)) {
                languageToUniqueNgrams[language] = mutableSetOf()
            }
            languageToUniqueNgrams[language]?.add(ngram)
        }

        File("$outputPath/$outputJsonFileName").bufferedWriter().use { writer ->
            writer.write(gson.toJson(languageToUniqueNgrams))
        }
    }

    println("Done.")
}

private class NgramsDeserializer : JsonDeserializer<Set<String>> {
    override fun deserialize(
        json: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ): Set<String> {

        val ngrams = mutableSetOf<String>()

        val jsonObj = json.asJsonObject
        val ngramsJsonObj = jsonObj["ngrams"].asJsonObject

        for ((_, ngramsJsonElem) in ngramsJsonObj.entrySet()) {
            for (ngramJsonElem in ngramsJsonElem.asString.split(' ')) {
                ngrams.add(ngramJsonElem)
            }
        }
        return ngrams
    }
}

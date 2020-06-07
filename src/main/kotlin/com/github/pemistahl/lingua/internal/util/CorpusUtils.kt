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

package com.github.pemistahl.lingua.internal.util

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.internal.TrainingDataLanguageModel
import com.github.pemistahl.lingua.internal.util.extension.asLineSequenceResource
import java.io.File
import kotlin.random.Random

internal fun writeTestDataFiles(inputPath: String, outputPath: String, isoCode: String, charClass: String) {
    val wordRegex = Regex("""[\p{$charClass}]{5,}""")
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

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

package com.github.pemistahl.lingua

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.Language.*
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder.Companion.fromAllBuiltInLanguages
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder.Companion.fromLanguages
import com.github.pemistahl.lingua.internal.util.writeLanguageModelsFromLeipzigCorpusFile
import com.github.pemistahl.lingua.internal.util.writeTestDataFiles
import java.io.Console
import java.util.Scanner

fun main() {
    //runApp()

    /*
    writeLanguageModelsFromLeipzigCorpusFile(
        inputPath = "/training-data/bn/bn_1M.txt",
        outputPath = "C:/Users/pstahl/Documents/language-models",
        language = BENGALI,
        charClass = "IsBengali"
    )
    */

    writeTestDataFiles(
        inputPath = "/training-data/bn/bn_10K.txt",
        outputPath = "C:/Users/pstahl/Documents/language-testdata",
        isoCode = "bn",
        charClass = "IsBengali"
    )
}

private fun runApp() {

    val supportedLanguages = Language.all().count()

    println(
        """
        This is Lingua.
        Select the language models to load.

        1: Afrikaans, Dutch
        2: Arabic, Persian
        3: Basque, Catalan, Spanish
        4: Belarusian, Bulgarian, Russian
        5: Bokmal, Nynorsk
        6: Croatian, Romanian
        7: Czech, Polish, Slovak, Slovene
        8: Danish, Icelandic, Norwegian, Swedish
        9: English, Dutch, German
        10: English, Irish, Welsh
        11: Estonian, Latvian, Lithuanian
        12: Finnish, Hungarian
        13: French, Italian, Spanish, Portuguese
        14: Indonesian, Malay, Tagalog
        15: all $supportedLanguages supported languages

        Type a number and press <Enter>.
        Type :quit to exit.

        """.trimIndent()
    )

    val console: Console? = System.console()
    val scanner by lazy { Scanner(System.`in`, "UTF-8") }
    var number: Int? = null

    while (true) {
        print("> ")
        val input = console?.readLine()?.trim() ?: scanner.nextLine().trim()
        if (input == ":quit") break
        if (input.isEmpty()) { number = 1; break }

        number = try {
            input.toInt()
        } catch (e: NumberFormatException) {
            println("This is not a valid number. Try again.\n")
            continue
        }

        if (number !in 1..15) {
            println("This selection is out of range.\nEnter a number between 1 and 15.\n")
            number = null
            continue
        }

        break
    }

    if (number == null) {
        println("Bye! Ciao! Tschüss! Salut!")
        return
    }

    println("Loading language models...")

    val detectorBuilder = when (number) {
        1 -> fromLanguages(AFRIKAANS, DUTCH)
        2 -> fromLanguages(ARABIC, PERSIAN)
        3 -> fromLanguages(BASQUE, CATALAN, SPANISH)
        4 -> fromLanguages(BELARUSIAN, BULGARIAN, RUSSIAN)
        5 -> fromLanguages(BOKMAL, NYNORSK)
        6 -> fromLanguages(CROATIAN, ROMANIAN)
        7 -> fromLanguages(CZECH, POLISH, SLOVAK, SLOVENE)
        8 -> fromLanguages(DANISH, ICELANDIC, NORWEGIAN, SWEDISH)
        9 -> fromLanguages(ENGLISH, DUTCH, GERMAN)
        10 -> fromLanguages(ENGLISH, IRISH, WELSH)
        11 -> fromLanguages(ESTONIAN, LATVIAN, LITHUANIAN)
        12 -> fromLanguages(FINNISH, HUNGARIAN)
        13 -> fromLanguages(FRENCH, ITALIAN, SPANISH, PORTUGUESE)
        14 -> fromLanguages(INDONESIAN, MALAY, TAGALOG)
        15 -> fromAllBuiltInLanguages()
        else -> throw IllegalArgumentException("option '$number' is not supported")
    }

    val detector = detectorBuilder.build()

    println(
        """
        Done. ${detector.numberOfLoadedLanguages} language models loaded lazily.

        Type some text and press <Enter> to detect its language.
        Type :quit to exit.

        """.trimIndent()
    )

    while (true) {
        print("> ")
        val text = console?.readLine()?.trim() ?: scanner.nextLine().trim()
        if (text == ":quit") break
        if (text.isEmpty()) continue
        println(detector.detectLanguageOf(text))
    }

    println("Bye! Ciao! Tschüss! Salut!")
}

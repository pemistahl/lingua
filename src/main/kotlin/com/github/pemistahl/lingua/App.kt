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

import com.github.pemistahl.lingua.api.IsoCode639_1
import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.api.Language.AFRIKAANS
import com.github.pemistahl.lingua.api.Language.ARABIC
import com.github.pemistahl.lingua.api.Language.BASQUE
import com.github.pemistahl.lingua.api.Language.BELARUSIAN
import com.github.pemistahl.lingua.api.Language.BENGALI
import com.github.pemistahl.lingua.api.Language.BOKMAL
import com.github.pemistahl.lingua.api.Language.BULGARIAN
import com.github.pemistahl.lingua.api.Language.CATALAN
import com.github.pemistahl.lingua.api.Language.CHINESE
import com.github.pemistahl.lingua.api.Language.CROATIAN
import com.github.pemistahl.lingua.api.Language.CZECH
import com.github.pemistahl.lingua.api.Language.DANISH
import com.github.pemistahl.lingua.api.Language.DUTCH
import com.github.pemistahl.lingua.api.Language.ENGLISH
import com.github.pemistahl.lingua.api.Language.ESTONIAN
import com.github.pemistahl.lingua.api.Language.FINNISH
import com.github.pemistahl.lingua.api.Language.FRENCH
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.GUJARATI
import com.github.pemistahl.lingua.api.Language.HINDI
import com.github.pemistahl.lingua.api.Language.HUNGARIAN
import com.github.pemistahl.lingua.api.Language.ICELANDIC
import com.github.pemistahl.lingua.api.Language.INDONESIAN
import com.github.pemistahl.lingua.api.Language.IRISH
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.JAPANESE
import com.github.pemistahl.lingua.api.Language.KOREAN
import com.github.pemistahl.lingua.api.Language.LATVIAN
import com.github.pemistahl.lingua.api.Language.LITHUANIAN
import com.github.pemistahl.lingua.api.Language.MALAY
import com.github.pemistahl.lingua.api.Language.NORWEGIAN
import com.github.pemistahl.lingua.api.Language.NYNORSK
import com.github.pemistahl.lingua.api.Language.PERSIAN
import com.github.pemistahl.lingua.api.Language.POLISH
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.PUNJABI
import com.github.pemistahl.lingua.api.Language.ROMANIAN
import com.github.pemistahl.lingua.api.Language.RUSSIAN
import com.github.pemistahl.lingua.api.Language.SLOVAK
import com.github.pemistahl.lingua.api.Language.SLOVENE
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.TAGALOG
import com.github.pemistahl.lingua.api.Language.TAMIL
import com.github.pemistahl.lingua.api.Language.TELUGU
import com.github.pemistahl.lingua.api.Language.THAI
import com.github.pemistahl.lingua.api.Language.URDU
import com.github.pemistahl.lingua.api.Language.WELSH
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder.Companion.fromAllBuiltInLanguages
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder.Companion.fromIsoCodes
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder.Companion.fromLanguages
import java.io.Console
import java.util.*

fun main() {
    runApp()
}

private fun runApp() {

    val supportedLanguages = Language.all().count()

    println(
        """
        This is Lingua.
        Select the language models to load.

        ----
        1: enter language iso codes manually
        ----
        2: all $supportedLanguages supported languages
        ----
        3: Afrikaans, Dutch
        4: Arabic, Persian
        5: Basque, Catalan, Spanish
        6: Belarusian, Bulgarian, Russian
        7: Bokmal, Nynorsk
        8: Croatian, Romanian
        9: Czech, Polish, Slovak, Slovene
        10: Danish, Icelandic, Norwegian, Swedish
        11: English, Dutch, German
        12: English, Irish, Welsh
        13: Estonian, Latvian, Lithuanian
        14: Finnish, Hungarian
        15: French, Italian, Spanish, Portuguese
        16: Indonesian, Malay, Tagalog
        17: Chinese, Japanese, Korean, Thai
        18: Bengali, Gujarati, Hindi, Punjabi, Urdu, Tamil, Telugu

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
        if (input.isEmpty()) continue

        number = try {
            input.toInt()
        } catch (e: NumberFormatException) {
            println("This is not a valid number. Try again.\n")
            continue
        }

        if (number !in 1..18) {
            println("This selection is out of range.\nEnter a number between 1 and 18.\n")
            number = null
            continue
        }

        break
    }

    if (number == null) {
        println("Bye! Ciao! Tschüss! Salut!")
        return
    }

    var detectorBuilder: LanguageDetectorBuilder? = null

    if (number == 1) {
        val isoCodesList = mutableListOf<String>()

        while (true) {
            println(
                """
                List some language iso codes separated by spaces and press <Enter>.
                Type :quit to exit.
                
                """.trimIndent()
            )

            while (true) {
                print("> ")
                val isoCodes = console?.readLine()?.trim() ?: scanner.nextLine().trim()
                if (isoCodes == ":quit") break
                if (isoCodes.isEmpty()) continue
                isoCodesList.addAll(isoCodes.split(Regex("\\s+")))
                break
            }

            if (isoCodesList.isNotEmpty()) {
                val isoCodesArray = isoCodesList.map { IsoCode639_1.valueOf(it.toUpperCase()) }.toTypedArray()
                val isoCodesLength = isoCodesArray.size

                try {
                    detectorBuilder = fromIsoCodes(isoCodesArray[0], *isoCodesArray.sliceArray(1 until isoCodesLength))
                    println("Loading language models...")
                    break
                } catch (e: IllegalArgumentException) {
                    isoCodesList.clear()
                    println("At least one iso code you've entered is not supported. Try again.\n")
                }
            }
            else {
                break
            }
        }
    }
    else {
        println("Loading language models...")

        detectorBuilder = when (number) {
            2 -> fromAllBuiltInLanguages()
            3 -> fromLanguages(AFRIKAANS, DUTCH)
            4 -> fromLanguages(ARABIC, PERSIAN)
            5 -> fromLanguages(BASQUE, CATALAN, SPANISH)
            6 -> fromLanguages(BELARUSIAN, BULGARIAN, RUSSIAN)
            7 -> fromLanguages(BOKMAL, NYNORSK)
            8 -> fromLanguages(CROATIAN, ROMANIAN)
            9 -> fromLanguages(CZECH, POLISH, SLOVAK, SLOVENE)
            10 -> fromLanguages(DANISH, ICELANDIC, NORWEGIAN, SWEDISH)
            11 -> fromLanguages(ENGLISH, DUTCH, GERMAN)
            12 -> fromLanguages(ENGLISH, IRISH, WELSH)
            13 -> fromLanguages(ESTONIAN, LATVIAN, LITHUANIAN)
            14 -> fromLanguages(FINNISH, HUNGARIAN)
            15 -> fromLanguages(FRENCH, ITALIAN, SPANISH, PORTUGUESE)
            16 -> fromLanguages(INDONESIAN, MALAY, TAGALOG)
            17 -> fromLanguages(CHINESE, JAPANESE, KOREAN, THAI)
            18 -> fromLanguages(BENGALI, GUJARATI, HINDI, PUNJABI, URDU, TAMIL, TELUGU)
            else -> throw IllegalArgumentException("option '$number' is not supported")
        }
    }

    if (detectorBuilder != null) {
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
    }

    println("Bye! Ciao! Tschüss! Salut!")
}

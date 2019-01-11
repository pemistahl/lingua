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

import com.github.pemistahl.lingua.detector.LanguageDetectorBuilder
import com.github.pemistahl.lingua.detector.LanguageDetectorBuilder.Companion.fromAllBuiltInLanguages
import com.github.pemistahl.lingua.detector.LanguageDetectorBuilder.Companion.fromLanguages
import com.github.pemistahl.lingua.model.Language.ARABIC
import com.github.pemistahl.lingua.model.Language.BELARUSIAN
import com.github.pemistahl.lingua.model.Language.BULGARIAN
import com.github.pemistahl.lingua.model.Language.CROATIAN
import com.github.pemistahl.lingua.model.Language.CZECH
import com.github.pemistahl.lingua.model.Language.DANISH
import com.github.pemistahl.lingua.model.Language.DUTCH
import com.github.pemistahl.lingua.model.Language.ENGLISH
import com.github.pemistahl.lingua.model.Language.ESTONIAN
import com.github.pemistahl.lingua.model.Language.FINNISH
import com.github.pemistahl.lingua.model.Language.FRENCH
import com.github.pemistahl.lingua.model.Language.GERMAN
import com.github.pemistahl.lingua.model.Language.HUNGARIAN
import com.github.pemistahl.lingua.model.Language.ITALIAN
import com.github.pemistahl.lingua.model.Language.LATVIAN
import com.github.pemistahl.lingua.model.Language.LITHUANIAN
import com.github.pemistahl.lingua.model.Language.PERSIAN
import com.github.pemistahl.lingua.model.Language.POLISH
import com.github.pemistahl.lingua.model.Language.PORTUGUESE
import com.github.pemistahl.lingua.model.Language.ROMANIAN
import com.github.pemistahl.lingua.model.Language.RUSSIAN
import com.github.pemistahl.lingua.model.Language.SPANISH
import com.github.pemistahl.lingua.model.Language.SWEDISH
import java.io.Console
import java.util.Scanner
import kotlin.math.roundToInt

fun main() {
    runApp()
}

private fun runApp() {

    val supportedLanguages = LanguageDetectorBuilder.supportedLanguages().count()

    println(
        """
        This is Lingua.
        Select the language models to load.

        1: all $supportedLanguages supported languages
        2: French, Italian, Spanish, Portuguese
        3: Dutch, English, German
        4: Belarusian, Bulgarian, Russian
        5: Danish, Finnish, Swedish
        6: Estonian, Latvian, Lithuanian
        7: Czech, Polish
        8: Croatian, Hungarian, Romanian
        9: Arabic, Persian

        Type a number (default: 1) and press <Enter>.
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

        if (number !in 1..9) {
            println("This selection is out of range.\nEnter a number between 1 and 9.\n")
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
        1 -> fromAllBuiltInLanguages()
        2 -> fromLanguages(FRENCH, ITALIAN, SPANISH, PORTUGUESE)
        3 -> fromLanguages(DUTCH, ENGLISH, GERMAN)
        4 -> fromLanguages(BELARUSIAN, BULGARIAN, RUSSIAN)
        5 -> fromLanguages(DANISH, FINNISH, SWEDISH)
        6 -> fromLanguages(ESTONIAN, LATVIAN, LITHUANIAN)
        7 -> fromLanguages(CZECH, POLISH)
        8 -> fromLanguages(CROATIAN, HUNGARIAN, ROMANIAN)
        9 -> fromLanguages(ARABIC, PERSIAN)
        else -> fromAllBuiltInLanguages()
    }

    val startTime = System.currentTimeMillis()
    val detector = detectorBuilder.build()
    val endTime = System.currentTimeMillis()
    val seconds = ((endTime - startTime) / 1000.0).roundToInt()

    println(
        """
        Done. ${detector.numberOfLoadedLanguages} language models loaded in $seconds seconds.

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

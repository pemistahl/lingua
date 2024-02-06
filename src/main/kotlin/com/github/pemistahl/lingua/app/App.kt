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

package com.github.pemistahl.lingua.app

import com.github.pemistahl.lingua.api.IsoCode639_1
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder.Companion.fromAllLanguages
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder.Companion.fromIsoCodes639_1
import java.io.Console
import java.util.Scanner

fun main() {
    runApp()
}

private fun runApp() {
    println(
        """
        This is Lingua.
        Select the language models to load.

        1: enter language iso codes manually
        2: all supported languages

        Type a number and press <Enter>.
        Type :quit to exit.

        """.trimIndent(),
    )

    val console: Console? = System.console()
    val scanner by lazy { Scanner(System.`in`, "UTF-8") }
    var number: Int? = null

    while (true) {
        print("> ")
        val input = console?.readLine()?.trim() ?: scanner.nextLine().trim()
        if (input == ":quit") break
        if (input.isEmpty()) continue

        number =
            try {
                input.toInt()
            } catch (e: NumberFormatException) {
                println("This is not a valid number. Try again.\n")
                continue
            }

        if (number !in 1..2) {
            println("This selection is out of range.\nEnter number 1 or 2.\n")
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
                List some language iso 639-1 codes separated by spaces and press <Enter>.
                Type :quit to exit.

                """.trimIndent(),
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
                val isoCodes = mutableListOf<IsoCode639_1>()

                for (isoCode in isoCodesList) {
                    try {
                        isoCodes.add(IsoCode639_1.valueOf(isoCode.uppercase()))
                    } catch (e: IllegalArgumentException) {
                        isoCodes.clear()
                        println("Iso code '$isoCode' is not supported. Try again.\n")
                        break
                    }
                }

                if (isoCodes.size < isoCodesList.size) {
                    isoCodesList.clear()
                    continue
                }

                try {
                    detectorBuilder = fromIsoCodes639_1(*isoCodes.toTypedArray())
                    println("Loading language models...")
                    break
                } catch (e: IllegalArgumentException) {
                    isoCodesList.clear()
                    println("At least one iso code you've entered is not supported. Try again.\n")
                }
            } else {
                break
            }
        }
    } else {
        println("Loading language models...")
        detectorBuilder = fromAllLanguages()
    }

    if (detectorBuilder != null) {
        val detector = detectorBuilder.build()

        println(
            """
            Done. ${detector.numberOfLoadedLanguages} language models loaded lazily.

            Type some text and press <Enter> to detect its language.
            Type :quit to exit.

            """.trimIndent(),
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

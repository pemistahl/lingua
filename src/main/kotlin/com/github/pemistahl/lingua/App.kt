/*
 * Copyright 2018 Peter M. Stahl
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

import com.github.pemistahl.lingua.detector.LanguageDetector
import java.io.Console
import java.util.Scanner

fun main() {
    runApp()
}

private fun runApp() {
    println(
        """
        This is Lingua.
        Loading language models...
        """.trimIndent()
    )

    val detector = LanguageDetector.fromAllBuiltInLanguages()

    println(
        """
        Done. ${detector.numberOfLoadedLanguages} language models loaded.

        Type some text and press <Enter> to detect its language.
        Type :quit to exit.

        """.trimIndent()
    )

    val console: Console? = System.console()
    val scanner by lazy { Scanner(System.`in`, "UTF-8") }

    while (true) {
        print("> ")
        val text = console?.readLine()?.trim() ?: scanner.nextLine().trim()
        if (text == ":quit") break
        if (text.isEmpty()) continue
        println(detector.detectLanguageOf(text))
    }

    println("Bye! Ciao! Tschüss! Salut!")
}

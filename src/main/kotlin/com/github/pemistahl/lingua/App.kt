package com.github.pemistahl.lingua

import com.github.pemistahl.lingua.detector.LanguageDetector

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
        Done. ${detector.supportedLanguages} language models loaded.

        Type some text and press <Enter> to detect its language.
        Type :quit to exit.

        """.trimIndent()
    )

    while (true) {
        print("> ")
        val text = readLine().toString().trim()
        if (text == ":quit") break
        if (text.isEmpty()) continue
        println(detector.detectLanguageFrom(text))
    }

    println("Bye! Ciao! Tschüss! Salut!")
}

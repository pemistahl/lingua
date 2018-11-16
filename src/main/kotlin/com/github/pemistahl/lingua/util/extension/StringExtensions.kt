package com.github.pemistahl.lingua.util.extension

import com.github.pemistahl.lingua.detector.LanguageDetector
import com.google.gson.stream.JsonReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.nio.charset.Charset

internal fun String.asJsonResource(
    charset: Charset = Charsets.UTF_8,
    operation: (JsonReader) -> Unit
) {
    val inputStream =
        LanguageDetector::class.java.getResourceAsStream(this)
            ?: throw FileNotFoundException("the file '$this' could not be found")

    operation(JsonReader(InputStreamReader(inputStream, charset)))
}

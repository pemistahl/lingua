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

package com.github.pemistahl.lingua.internal.util.extension

import com.github.pemistahl.lingua.api.LanguageDetector
import java.io.FileNotFoundException
import java.nio.charset.Charset
import java.util.regex.PatternSyntaxException

internal fun String.asLineSequenceResource(
    charset: Charset = Charsets.UTF_8,
    operation: (Sequence<String>) -> Unit
) {
    val inputStream =
        LanguageDetector::class.java.getResourceAsStream(this)
            ?: throw FileNotFoundException("the file '$this' could not be found")

    inputStream.bufferedReader(charset).useLines(operation)
}

internal fun String.containsAnyOf(characters: String): Boolean {
    for (c in characters) {
        if (this.contains(c)) return true
    }
    return false
}

internal fun String.asRegex(): Regex {
    val splitRegex = Regex("""\s*,\s*""")
    val charClasses = this.split(splitRegex)
    val charClassesWithoutPrefix = charClasses.joinToString(separator = "") { "\\p{$it}" }
    val charClassesWithPrefix = charClasses.joinToString(separator = "") { "\\p{Is$it}" }

    return try {
        // Android only supports character classes without Is- prefix
        Regex("^[$charClassesWithoutPrefix]+$")
    } catch (e: PatternSyntaxException) {
        Regex("^[$charClassesWithPrefix]+$")
    }
}

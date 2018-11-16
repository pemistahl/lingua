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

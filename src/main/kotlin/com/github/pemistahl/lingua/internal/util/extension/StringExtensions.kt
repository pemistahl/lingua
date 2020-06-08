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

package com.github.pemistahl.lingua.internal.util.extension

import java.util.regex.PatternSyntaxException

internal fun String.containsAnyOf(characters: String): Boolean {
    for (c in characters) {
        if (this.contains(c)) return true
    }
    return false
}

internal fun String.asRegex() = try {
    // Android only supports character classes without Is- prefix
    Regex("\\p{$this}+")
} catch (e: PatternSyntaxException) {
    Regex("\\p{Is$this}+")
}

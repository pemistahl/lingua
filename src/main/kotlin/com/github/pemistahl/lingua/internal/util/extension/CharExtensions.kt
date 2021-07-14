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

package com.github.pemistahl.lingua.internal.util.extension

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.internal.Constant.LANGUAGES_SUPPORTING_LOGOGRAMS

// Cache set of scripts here to avoid evaluating it every time for isLogogram()
private val scriptsWithLogograms = LANGUAGES_SUPPORTING_LOGOGRAMS.asSequence()
    .flatMap(Language::alphabets)
    .toSet()

fun Char.isLogogram(): Boolean {
    return if (this.isWhitespace()) {
        false
    } else {
        scriptsWithLogograms.any { it.matches(this) }
    }
}

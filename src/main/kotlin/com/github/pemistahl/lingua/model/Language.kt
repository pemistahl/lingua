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

package com.github.pemistahl.lingua.model

enum class Language(val isoCode: String, internal var isExcludedFromDetection: Boolean) {
    ENGLISH    ("en", false),
    FRENCH     ("fr", false),
    GERMAN     ("de", false),
    ITALIAN    ("it", false),
    LATIN      ("la", false),
    PORTUGUESE ("pt", false),
    SPANISH    ("es", false),

    UNKNOWN    ("<unk>", true)
}
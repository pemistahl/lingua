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

package com.github.pemistahl.lingua.model

enum class Language(val isoCode: String, internal var isExcludedFromDetection: Boolean) {

    ARABIC     ("ar", false),
    BELARUSIAN ("be", false),
    BULGARIAN  ("bg", false),
    CROATIAN   ("hr", false),
    CZECH      ("cs", false),
    DANISH     ("da", false),
    DUTCH      ("nl", false),
    ENGLISH    ("en", false),
    ESTONIAN   ("et", false),
    FINNISH    ("fi", false),
    FRENCH     ("fr", false),
    GERMAN     ("de", false),
    HUNGARIAN  ("hu", false),
    ITALIAN    ("it", false),
    LATIN      ("la", false),
    LATVIAN    ("lv", false),
    LITHUANIAN ("lt", false),
    PERSIAN    ("fa", false),
    POLISH     ("pl", false),
    PORTUGUESE ("pt", false),
    ROMANIAN   ("ro", false),
    RUSSIAN    ("ru", false),
    SPANISH    ("es", false),
    SWEDISH    ("sv", false),
    TURKISH    ("tr", false),

    UNKNOWN    ("<unk>", true)
}

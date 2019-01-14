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

package com.github.pemistahl.lingua.api

enum class Language(
    val isoCode: String,
    internal val hasLatinAlphabet: Boolean,
    internal val hasCyrillicAlphabet: Boolean,
    internal val hasArabicAlphabet: Boolean,
    internal var isExcludedFromDetection: Boolean
) {

    ARABIC     ("ar", false, false, true, false),
    BELARUSIAN ("be", false, true, false, false),
    BULGARIAN  ("bg", false, true, false, false),
    CROATIAN   ("hr", true, false, false, false),
    CZECH      ("cs", true, false, false, false),
    DANISH     ("da", true, false, false, false),
    DUTCH      ("nl", true, false, false, false),
    ENGLISH    ("en", true, false, false, false),
    ESTONIAN   ("et", true, false, false, false),
    FINNISH    ("fi", true, false, false, false),
    FRENCH     ("fr", true, false, false, false),
    GERMAN     ("de", true, false, false, false),
    HUNGARIAN  ("hu", true, false, false, false),
    ITALIAN    ("it", true, false, false, false),
    LATIN      ("la", true, false, false, false),
    LATVIAN    ("lv", true, false, false, false),
    LITHUANIAN ("lt", true, false, false, false),
    PERSIAN    ("fa", false, false, true, false),
    POLISH     ("pl", true, false, false, false),
    PORTUGUESE ("pt", true, false, false, false),
    ROMANIAN   ("ro", true, false, false, false),
    RUSSIAN    ("ru", false, true, false, false),
    SPANISH    ("es", true, false, false, false),
    SWEDISH    ("sv", true, false, false, false),
    TURKISH    ("tr", true, false, false, false),

    UNKNOWN    ("<unk>", false, false, false, true);

    companion object {
        fun getByIsoCode(isoCode: String): Language {
            for (language in Language.values()) {
                if (isoCode == language.isoCode) {
                    return language
                }
            }
            return UNKNOWN
        }

        internal fun getIsoCodesForTests(): List<String> {
            return Language.values().toSet().minus(arrayOf(
                LATIN,
                UNKNOWN
            )).map { it.isoCode }
        }
    }
}

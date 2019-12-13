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

class LanguageDetectorBuilder private constructor(
    internal val languages: List<Language>,
    internal var minimumRelativeDistance: Double = 0.0,
    internal var useMapDBCache: Boolean = false
) {
    fun build() = LanguageDetector(languages.toMutableSet(), minimumRelativeDistance, useMapDBCache)

    fun withMinimumRelativeDistance(distance: Double): LanguageDetectorBuilder {
        require(distance in 0.0..0.99) { "minimum relative distance must lie in between 0.0 and 0.99" }
        this.minimumRelativeDistance = distance
        return this
    }

    fun withMapDBCache(): LanguageDetectorBuilder {
        this.useMapDBCache = true
        return this
    }

    companion object {
        @JvmStatic
        fun fromAllBuiltInLanguages() = LanguageDetectorBuilder(Language.all())

        @JvmStatic
        fun fromAllBuiltInSpokenLanguages() = LanguageDetectorBuilder(Language.allSpokenOnes())

        @JvmStatic
        fun fromAllBuiltInLanguagesWithout(language: Language, vararg languages: Language): LanguageDetectorBuilder {
            val languagesToLoad = Language.values().toMutableList()
            languagesToLoad.removeAll(arrayOf(Language.UNKNOWN, language, *languages))
            require(languagesToLoad.size > 1) { MISSING_LANGUAGE_MESSAGE }
            return LanguageDetectorBuilder(languagesToLoad)
        }

        @JvmStatic
        fun fromLanguages(vararg languages: Language): LanguageDetectorBuilder {
            require(languages.size >= 2) { MISSING_LANGUAGE_MESSAGE }
            return LanguageDetectorBuilder(languages.toList())
        }

        @JvmStatic
        fun fromIsoCodes(vararg isoCodes: String): LanguageDetectorBuilder {
            require(isoCodes.size >= 2) { MISSING_LANGUAGE_MESSAGE }
            val languages = isoCodes.map { Language.getByIsoCode(it) }
            return LanguageDetectorBuilder(languages)
        }

        private const val MISSING_LANGUAGE_MESSAGE = "LanguageDetector needs at least 2 languages to choose from"
    }
}

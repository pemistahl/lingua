/*
 * Copyright © 2018-2019 Peter M. Stahl pemistahl@gmail.com
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

package com.github.pemistahl.lingua.api

class LanguageDetectorBuilder private constructor(
    internal val languages: List<Language>,
    internal var minimumRelativeDistance: Double = 0.0
) {
    fun build() = LanguageDetector(languages.toMutableSet(), minimumRelativeDistance)

    fun withMinimumRelativeDistance(distance: Double): LanguageDetectorBuilder {
        require(distance in 0.0..0.99) { "minimum relative distance must lie in between 0.0 and 0.99" }
        this.minimumRelativeDistance = distance
        return this
    }

    companion object {
        @JvmStatic
        fun fromAllBuiltInLanguages() = LanguageDetectorBuilder(Language.all())

        @JvmStatic
        fun fromAllBuiltInSpokenLanguages() = LanguageDetectorBuilder(Language.allSpokenOnes())

        @JvmStatic
        fun fromAllBuiltInLanguagesWithout(vararg languages: Language): LanguageDetectorBuilder {
            val languagesToLoad = Language.values().toMutableList()
            languagesToLoad.removeAll(arrayOf(Language.UNKNOWN, *languages))
            require(languagesToLoad.size >= 2) { MISSING_LANGUAGE_MESSAGE }
            return LanguageDetectorBuilder(languagesToLoad)
        }

        @JvmStatic
        fun fromLanguages(vararg languages: Language): LanguageDetectorBuilder {
            require(languages.size >= 2) { MISSING_LANGUAGE_MESSAGE }
            return LanguageDetectorBuilder(listOf(*languages))
        }

        @JvmStatic
        fun fromIsoCodes639_1(vararg isoCodes: IsoCode639_1): LanguageDetectorBuilder {
            require(isoCodes.size >= 2) { MISSING_LANGUAGE_MESSAGE }
            val languages = isoCodes.map { Language.getByIsoCode639_1(it) }
            return LanguageDetectorBuilder(languages)
        }

        @JvmStatic
        fun fromIsoCodes639_3(vararg isoCodes: IsoCode639_3): LanguageDetectorBuilder {
            require(isoCodes.size >= 2) { MISSING_LANGUAGE_MESSAGE }
            val languages = isoCodes.map { Language.getByIsoCode639_3(it) }
            return LanguageDetectorBuilder(languages)
        }

        private const val MISSING_LANGUAGE_MESSAGE = "LanguageDetector needs at least 2 languages to choose from"
    }
}

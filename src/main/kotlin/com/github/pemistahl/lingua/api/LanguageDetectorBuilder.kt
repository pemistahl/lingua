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
    internal val languages: Set<Language>,
    internal var useMapDBCache: Boolean
) {
    fun build(): LanguageDetector {
        val detector = instantiateLanguageDetector()
        detector.loadAllLanguageModels()
        return detector
    }

    fun withMapDBCache(): LanguageDetectorBuilder {
        this.useMapDBCache = true
        return this
    }

    internal fun instantiateLanguageDetector() = LanguageDetector(languages.toMutableSet(), useMapDBCache)

    companion object {
        @JvmStatic
        fun fromAllBuiltInLanguages(): LanguageDetectorBuilder {
            val languagesToLoad = Language.values().toMutableSet()
            languagesToLoad.remove(Language.UNKNOWN)
            return LanguageDetectorBuilder(languagesToLoad, false)
        }

        @JvmStatic
        fun fromAllBuiltInSpokenLanguages(): LanguageDetectorBuilder {
            val languagesToLoad = Language.values().toMutableSet()
            languagesToLoad.removeAll(arrayOf(
                Language.UNKNOWN,
                Language.LATIN
            ))
            return LanguageDetectorBuilder(languagesToLoad, false)
        }

        @JvmStatic
        fun fromAllBuiltInLanguagesWithout(language: Language, vararg languages: Language): LanguageDetectorBuilder {
            val languagesToLoad = Language.values().toMutableSet()
            languagesToLoad.removeAll(arrayOf(Language.UNKNOWN, language, *languages))
            require(languagesToLoad.size > 1) { MISSING_LANGUAGE_MESSAGE }
            return LanguageDetectorBuilder(languagesToLoad, false)
        }

        @JvmStatic
        fun fromLanguages(language: Language, vararg languages: Language): LanguageDetectorBuilder {
            require(languages.isNotEmpty()) { MISSING_LANGUAGE_MESSAGE }
            return LanguageDetectorBuilder(setOf(language, *languages), false)
        }

        @JvmStatic
        fun fromIsoCodes(isoCode: String, vararg isoCodes: String): LanguageDetectorBuilder {
            require(isoCodes.isNotEmpty()) { MISSING_LANGUAGE_MESSAGE }
            val languages = isoCodes.map { Language.getByIsoCode(it) }.toMutableSet()
            languages.add(Language.getByIsoCode(isoCode))
            return LanguageDetectorBuilder(languages, false)
        }

        @JvmStatic
        fun supportedLanguages() = Language.values().toSet().minus(Language.UNKNOWN)

        private const val MISSING_LANGUAGE_MESSAGE = "LanguageDetector needs at least 2 languages to choose from"
    }
}

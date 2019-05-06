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

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class LanguageDetectorBuilderTest {

    private val errorMessage = "LanguageDetector needs at least 2 languages to choose from"

    @Test
    fun `assert that LanguageDetector can be built from all languages`() {
        val builder = LanguageDetectorBuilder.fromAllBuiltInLanguages()

        assertThat(builder.languages).isEqualTo(Language.all())
        assertThat(builder.useMapDBCache).isFalse()
        assertThat(builder.build()).isEqualTo(
            LanguageDetector(Language.all().toMutableSet(), isCachedByMapDB = false)
        )
        assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
        builder.useMapDBCache = false
        assertThat(builder.build()).isEqualTo(
            LanguageDetector(Language.all().toMutableSet(), isCachedByMapDB = true)
        )
    }

    @Test
    fun `assert that LanguageDetector can be built from spoken languages`() {
        val builder = LanguageDetectorBuilder.fromAllBuiltInSpokenLanguages()

        assertThat(builder.languages).isEqualTo(Language.allSpokenOnes())
        assertThat(builder.useMapDBCache).isFalse()
        assertThat(builder.build()).isEqualTo(
            LanguageDetector(Language.allSpokenOnes().toMutableSet(), isCachedByMapDB = false)
        )
        assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
        builder.useMapDBCache = false
        assertThat(builder.build()).isEqualTo(
            LanguageDetector(Language.allSpokenOnes().toMutableSet(), isCachedByMapDB = true)
        )
    }

    @Test
    fun `assert that LanguageDetector can be built from blacklist`() {
        run {
            val builder = LanguageDetectorBuilder.fromAllBuiltInLanguagesWithout(
                Language.TURKISH, Language.ROMANIAN
            )
            val expectedLanguages = Language.values().toSet().minus(
                arrayOf(Language.TURKISH, Language.ROMANIAN, Language.UNKNOWN)
            )
            assertThat(builder.languages).isEqualTo(expectedLanguages.toTypedArray())
            assertThat(builder.useMapDBCache).isFalse()
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), isCachedByMapDB = false)
            )
            assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
            builder.useMapDBCache = false
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), isCachedByMapDB = true)
            )
        }
        run {
            val languages = Language.values().toSet().minus(arrayOf(Language.GERMAN, Language.ENGLISH)).toTypedArray()
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromAllBuiltInLanguagesWithout(Language.GERMAN, *languages)
            }.withMessage(errorMessage)
        }
    }

    @Test
    fun `assert that LanguageDetector can be built from whitelist`() {
        run {
            val builder = LanguageDetectorBuilder.fromLanguages(Language.GERMAN, Language.ENGLISH)
            val expectedLanguages = setOf(Language.GERMAN, Language.ENGLISH)

            assertThat(builder.languages).isEqualTo(expectedLanguages.toTypedArray())
            assertThat(builder.useMapDBCache).isFalse()
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), isCachedByMapDB = false)
            )
            assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
            builder.useMapDBCache = false
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), isCachedByMapDB = true)
            )
        }
        run {
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromLanguages(Language.GERMAN)
            }.withMessage(errorMessage)
        }
    }

    @Test
    fun `assert that LanguageDetector can be built from iso codes`() {
        run {
            val builder = LanguageDetectorBuilder.fromIsoCodes("de", "sv")
            val expectedLanguages = setOf(Language.GERMAN, Language.SWEDISH)

            assertThat(builder.languages).isEqualTo(expectedLanguages.toTypedArray())
            assertThat(builder.useMapDBCache).isFalse()
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), isCachedByMapDB = false)
            )
            assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
            builder.useMapDBCache = false
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), isCachedByMapDB = true)
            )
        }
        run {
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromIsoCodes("en")
            }.withMessage(errorMessage)
        }
    }
}

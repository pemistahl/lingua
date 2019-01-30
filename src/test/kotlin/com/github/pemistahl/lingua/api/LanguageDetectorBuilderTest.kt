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

import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.spyk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class LanguageDetectorBuilderTest {

    @MockK
    lateinit var detector: LanguageDetector

    private val errorMessage = "LanguageDetector needs at least 2 languages to choose from"

    @Test
    fun `assert that LanguageDetector can be built from all languages`() {
        val builder = spyk(LanguageDetectorBuilder.fromAllBuiltInLanguages())

        every { detector.languages } returns Language.values().toSet().minus(Language.UNKNOWN).toMutableSet()
        every { detector.isCachedByMapDB } returns false
        every { detector.loadAllLanguageModels() } just Runs
        every { builder.instantiateLanguageDetector() } returns detector

        assertThat(builder.languages).isEqualTo(Language.values().toSet().minus(Language.UNKNOWN))
        assertThat(builder.useMapDBCache).isFalse()
        assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
        assertThat(builder.build()).isEqualTo(detector)
    }

    @Test
    fun `assert that LanguageDetector can be built from spoken languages`() {
        val builder = spyk(LanguageDetectorBuilder.fromAllBuiltInSpokenLanguages())

        every { detector.languages } returns Language.values().toSet().minus(
            arrayOf(Language.LATIN, Language.UNKNOWN)
        ).toMutableSet()
        every { detector.isCachedByMapDB } returns false
        every { detector.loadAllLanguageModels() } just Runs
        every { builder.instantiateLanguageDetector() } returns detector

        assertThat(builder.languages).isEqualTo(
            Language.values().toSet().minus(arrayOf(Language.LATIN, Language.UNKNOWN))
        )
        assertThat(builder.useMapDBCache).isFalse()
        assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
        assertThat(builder.build()).isEqualTo(detector)
    }

    @Test
    fun `assert that LanguageDetector can be built from blacklist`() {
        run {
            val builder = spyk(LanguageDetectorBuilder.fromAllBuiltInLanguagesWithout(
                Language.TURKISH, Language.ROMANIAN
            ))

            every { detector.languages } returns Language.values().toSet().minus(
                arrayOf(Language.TURKISH, Language.ROMANIAN, Language.UNKNOWN)
            ).toMutableSet()
            every { detector.isCachedByMapDB } returns false
            every { detector.loadAllLanguageModels() } just Runs
            every { builder.instantiateLanguageDetector() } returns detector

            assertThat(builder.languages).isEqualTo(Language.values().toSet().minus(
                arrayOf(Language.TURKISH, Language.ROMANIAN, Language.UNKNOWN))
            )
            assertThat(builder.useMapDBCache).isFalse()
            assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
            assertThat(builder.build()).isEqualTo(detector)
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
            val builder = spyk(LanguageDetectorBuilder.fromLanguages(Language.GERMAN, Language.ENGLISH))

            every { detector.languages } returns mutableSetOf(Language.GERMAN, Language.ENGLISH)
            every { detector.isCachedByMapDB } returns false
            every { detector.loadAllLanguageModels() } just Runs
            every { builder.instantiateLanguageDetector() } returns detector

            assertThat(builder.languages).isEqualTo(setOf(Language.GERMAN, Language.ENGLISH))
            assertThat(builder.useMapDBCache).isFalse()
            assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
            assertThat(builder.build()).isEqualTo(detector)
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
            val builder = spyk(LanguageDetectorBuilder.fromIsoCodes("de", "sv"))

            every { detector.languages } returns mutableSetOf(Language.GERMAN, Language.SWEDISH)
            every { detector.isCachedByMapDB } returns false
            every { detector.loadAllLanguageModels() } just Runs
            every { builder.instantiateLanguageDetector() } returns detector

            assertThat(builder.languages).isEqualTo(setOf(Language.GERMAN, Language.SWEDISH))
            assertThat(builder.useMapDBCache).isFalse()
            assertThat(builder.withMapDBCache().useMapDBCache).isTrue()
            assertThat(builder.build()).isEqualTo(detector)
        }
        run {
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromIsoCodes("en")
            }.withMessage(errorMessage)
        }
    }

    @Test
    fun `assert that LanguageDetectorBuilder supports all built-in languages`() {
        assertThat(LanguageDetectorBuilder.supportedLanguages()).isEqualTo(
            Language.values().toSet().minus(Language.UNKNOWN)
        )
    }
}

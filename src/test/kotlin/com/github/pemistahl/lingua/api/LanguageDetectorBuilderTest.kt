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

package com.github.pemistahl.lingua.api

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class LanguageDetectorBuilderTest {

    private val minimumLanguagesErrorMessage = "LanguageDetector needs at least 2 languages to choose from"

    @Test
    fun `assert that LanguageDetector can be built from all languages`() {
        val builder = LanguageDetectorBuilder.fromAllLanguages()

        assertThat(builder.languages).isEqualTo(Language.all())
        assertThat(builder.minimumRelativeDistance).isEqualTo(0.0)
        assertThat(builder.build()).isEqualTo(
            LanguageDetector(Language.all().toMutableSet(), minimumRelativeDistance = 0.0)
        )

        assertThat(builder.withMinimumRelativeDistance(0.2).minimumRelativeDistance).isEqualTo(0.2)
        assertThat(builder.build()).isEqualTo(
            LanguageDetector(Language.all().toMutableSet(), minimumRelativeDistance = 0.2)
        )
    }

    @Test
    fun `assert that LanguageDetector can be built from spoken languages`() {
        val builder = LanguageDetectorBuilder.fromAllSpokenLanguages()

        assertThat(builder.languages).isEqualTo(Language.allSpokenOnes())
        assertThat(builder.minimumRelativeDistance).isEqualTo(0.0)
        assertThat(builder.build()).isEqualTo(
            LanguageDetector(Language.allSpokenOnes().toMutableSet(), minimumRelativeDistance = 0.0)
        )

        assertThat(builder.withMinimumRelativeDistance(0.2).minimumRelativeDistance).isEqualTo(0.2)
        assertThat(builder.build()).isEqualTo(
            LanguageDetector(Language.allSpokenOnes().toMutableSet(), minimumRelativeDistance = 0.2)
        )
    }

    @Test
    fun `assert that LanguageDetector can be built from all languages with Arabic script`() {
        val builder = LanguageDetectorBuilder.fromAllLanguagesWithArabicScript()
        assertThat(builder.languages).isEqualTo(Language.allWithArabicScript())
    }

    @Test
    fun `assert that LanguageDetector can be built from all languages with Cyrillic script`() {
        val builder = LanguageDetectorBuilder.fromAllLanguagesWithCyrillicScript()
        assertThat(builder.languages).isEqualTo(Language.allWithCyrillicScript())
    }

    @Test
    fun `assert that LanguageDetector can be built from all languages with Devanagari script`() {
        val builder = LanguageDetectorBuilder.fromAllLanguagesWithDevanagariScript()
        assertThat(builder.languages).isEqualTo(Language.allWithDevanagariScript())
    }

    @Test
    fun `assert that LanguageDetector can be built from all languages with Latin script`() {
        val builder = LanguageDetectorBuilder.fromAllLanguagesWithLatinScript()
        assertThat(builder.languages).isEqualTo(Language.allWithLatinScript())
    }

    @Test
    fun `assert that LanguageDetector can be built from blacklist`() {
        run {
            val builder = LanguageDetectorBuilder.fromAllLanguagesWithout(
                Language.TURKISH,
                Language.ROMANIAN
            )
            val expectedLanguages = Language.values().toSet().minus(
                arrayOf(Language.TURKISH, Language.ROMANIAN, Language.UNKNOWN)
            ).toList()

            assertThat(builder.languages).isEqualTo(expectedLanguages)
            assertThat(builder.minimumRelativeDistance).isEqualTo(0.0)
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), minimumRelativeDistance = 0.0)
            )

            assertThat(builder.withMinimumRelativeDistance(0.2).minimumRelativeDistance).isEqualTo(0.2)
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), minimumRelativeDistance = 0.2)
            )
        }
        run {
            val languages = Language.values().toSet().minus(arrayOf(Language.GERMAN, Language.ENGLISH)).toTypedArray()
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromAllLanguagesWithout(Language.GERMAN, *languages)
            }.withMessage(minimumLanguagesErrorMessage)
        }
    }

    @Test
    fun `assert that LanguageDetector can be built from whitelist`() {
        run {
            val builder = LanguageDetectorBuilder.fromLanguages(Language.GERMAN, Language.ENGLISH)
            val expectedLanguages = listOf(Language.GERMAN, Language.ENGLISH)

            assertThat(builder.languages).isEqualTo(expectedLanguages)
            assertThat(builder.minimumRelativeDistance).isEqualTo(0.0)
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), minimumRelativeDistance = 0.0)
            )

            assertThat(builder.withMinimumRelativeDistance(0.2).minimumRelativeDistance).isEqualTo(0.2)
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), minimumRelativeDistance = 0.2)
            )
        }
        run {
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromLanguages(Language.GERMAN)
            }.withMessage(minimumLanguagesErrorMessage)
        }
    }

    @Test
    fun `assert that LanguageDetector can be built from iso codes`() {
        run {
            val builder = LanguageDetectorBuilder.fromIsoCodes639_1(IsoCode639_1.DE, IsoCode639_1.SV)
            val expectedLanguages = listOf(Language.GERMAN, Language.SWEDISH)

            assertThat(builder.languages).isEqualTo(expectedLanguages)
            assertThat(builder.minimumRelativeDistance).isEqualTo(0.0)
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), minimumRelativeDistance = 0.0)
            )

            assertThat(builder.withMinimumRelativeDistance(0.2).minimumRelativeDistance).isEqualTo(0.2)
            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), minimumRelativeDistance = 0.2)
            )

            assertThat(builder.build()).isEqualTo(
                LanguageDetector(expectedLanguages.toMutableSet(), minimumRelativeDistance = 0.2)
            )
        }
        run {
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromIsoCodes639_1(IsoCode639_1.EN)
            }.withMessage(minimumLanguagesErrorMessage)
        }
    }

    @Test
    fun `assert that LanguageDetector can not be built from invalid minimum relative distance value`() {
        val errorMessage = "minimum relative distance must lie in between 0.0 and 0.99"
        run {
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromAllLanguages().withMinimumRelativeDistance(-2.3)
            }.withMessage(errorMessage)
        }
        run {
            assertThatIllegalArgumentException().isThrownBy {
                LanguageDetectorBuilder.fromAllLanguages().withMinimumRelativeDistance(1.7)
            }.withMessage(errorMessage)
        }
    }
}

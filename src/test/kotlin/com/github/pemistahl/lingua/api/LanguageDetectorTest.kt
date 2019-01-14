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

import com.github.pemistahl.lingua.api.Language.ENGLISH
import com.github.pemistahl.lingua.api.Language.FRENCH
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.LATIN
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.internal.model.LanguageModel
import com.github.pemistahl.lingua.internal.model.Unigram
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KClass

@ExtendWith(MockKExtension::class)
@Disabled
class LanguageDetectorTest {

    @MockK internal lateinit var englishLanguageModel: LanguageModel<Unigram, Unigram>
    @MockK internal lateinit var frenchLanguageModel: LanguageModel<Unigram, Unigram>
    @MockK internal lateinit var germanLanguageModel: LanguageModel<Unigram, Unigram>
    @MockK internal lateinit var italianLanguageModel: LanguageModel<Unigram, Unigram>
    @MockK internal lateinit var latinLanguageModel: LanguageModel<Unigram, Unigram>
    @MockK internal lateinit var portugueseLanguageModel: LanguageModel<Unigram, Unigram>
    @MockK internal lateinit var spanishLanguageModel: LanguageModel<Unigram, Unigram>

    @BeforeAll
    fun beforeAll() {
        every { englishLanguageModel.language } returns ENGLISH
        every { frenchLanguageModel.language } returns FRENCH
        every { germanLanguageModel.language } returns GERMAN
        every { italianLanguageModel.language } returns ITALIAN
        every { latinLanguageModel.language } returns LATIN
        every { portugueseLanguageModel.language } returns PORTUGUESE
        every { spanishLanguageModel.language } returns SPANISH

        mockkObject(LanguageDetector)
    }

    @Test
    fun `assert that each and every built-in language model is loaded properly`() {
        every { LanguageDetector["loadLanguageModels"](any<Set<Language>>(), any<KClass<Unigram>>()) } returns mapOf(
            ENGLISH to englishLanguageModel,
            FRENCH to frenchLanguageModel,
            GERMAN to germanLanguageModel,
            ITALIAN to italianLanguageModel,
            LATIN to latinLanguageModel,
            PORTUGUESE to portugueseLanguageModel,
            SPANISH to spanishLanguageModel
        )
        val detector = LanguageDetectorBuilder.fromAllBuiltInLanguages().build()
        assertEquals(7, detector.numberOfLoadedLanguages)
        assertEquals(setOf(ENGLISH, FRENCH, GERMAN, ITALIAN, LATIN, PORTUGUESE, SPANISH), detector.languages)
    }

    @Test
    fun `assert that all built-in spoken language models are loaded properly`() {
        every { LanguageDetector["loadLanguageModels"](any<Set<Language>>(), any<KClass<Unigram>>()) } returns mapOf(
            ENGLISH to englishLanguageModel,
            FRENCH to frenchLanguageModel,
            GERMAN to germanLanguageModel,
            ITALIAN to italianLanguageModel,
            PORTUGUESE to portugueseLanguageModel,
            SPANISH to spanishLanguageModel
        )
        val detector = LanguageDetectorBuilder.fromAllBuiltInSpokenLanguages().build()
        assertEquals(6, detector.numberOfLoadedLanguages)
        assertEquals(
            setOf(ENGLISH, FRENCH, GERMAN, ITALIAN, PORTUGUESE, SPANISH),
            detector.languages
        )
    }

    @Test
    fun `assert that selected language models are loaded properly`() {
        every { LanguageDetector["loadLanguageModels"](any<Set<Language>>(), any<KClass<Unigram>>()) } returns mapOf(
            GERMAN to germanLanguageModel,
            LATIN to latinLanguageModel
        )
        val detector = LanguageDetectorBuilder.fromLanguages(LATIN, GERMAN).build()
        assertEquals(2, detector.numberOfLoadedLanguages)
        assertEquals(setOf(LATIN, GERMAN), detector.languages)
    }

    @Test
    fun `assert that LanguageDetector can not be built from only one language`() {
        val expectedMessage = "LanguageDetector needs at least 2 languages to choose from"
        run {
            val exception = assertThrows(IllegalArgumentException::class.java) {
                LanguageDetectorBuilder.fromLanguages(GERMAN).build()
            }
            assertEquals(expectedMessage, exception.message)
        }
        run {
            val exception = assertThrows(IllegalArgumentException::class.java) {
                LanguageDetectorBuilder
                    .fromAllBuiltInLanguagesWithout(ENGLISH, FRENCH, GERMAN, ITALIAN, LATIN, PORTUGUESE)
                    .build()
            }
            assertEquals(expectedMessage, exception.message)
        }
    }

    @Test
    fun `assert that excluded language models are not loaded`() {
        every { LanguageDetector["loadLanguageModels"](any<Set<Language>>(), any<KClass<Unigram>>()) } returns mapOf(
            ENGLISH to englishLanguageModel,
            GERMAN to germanLanguageModel,
            ITALIAN to italianLanguageModel,
            SPANISH to spanishLanguageModel
        )
        val detector = LanguageDetectorBuilder.fromAllBuiltInLanguagesWithout(FRENCH, LATIN, PORTUGUESE).build()
        assertEquals(4, detector.numberOfLoadedLanguages)
        assertEquals(
            setOf(ENGLISH, GERMAN, ITALIAN, SPANISH),
            detector.languages
        )
    }
}

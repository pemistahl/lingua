package com.github.pemistahl.lingua.detector

import com.github.pemistahl.lingua.model.Language
import com.github.pemistahl.lingua.model.Language.ENGLISH
import com.github.pemistahl.lingua.model.Language.FRENCH
import com.github.pemistahl.lingua.model.Language.GERMAN
import com.github.pemistahl.lingua.model.Language.ITALIAN
import com.github.pemistahl.lingua.model.Language.LATIN
import com.github.pemistahl.lingua.model.Language.PORTUGUESE
import com.github.pemistahl.lingua.model.Language.SPANISH
import com.github.pemistahl.lingua.model.LanguageModel
import com.github.pemistahl.lingua.model.Unigram
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KClass

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class LanguageDetectorTest {

    @MockK internal lateinit var englishLanguageModel: LanguageModel<Unigram>
    @MockK internal lateinit var frenchLanguageModel: LanguageModel<Unigram>
    @MockK internal lateinit var germanLanguageModel: LanguageModel<Unigram>
    @MockK internal lateinit var italianLanguageModel: LanguageModel<Unigram>
    @MockK internal lateinit var latinLanguageModel: LanguageModel<Unigram>
    @MockK internal lateinit var portugueseLanguageModel: LanguageModel<Unigram>
    @MockK internal lateinit var spanishLanguageModel: LanguageModel<Unigram>

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
        every { LanguageDetector["loadLanguageModels"](any<Set<Language>>(), any<KClass<Unigram>>()) } returns listOf(
            englishLanguageModel, frenchLanguageModel, germanLanguageModel,
            italianLanguageModel, latinLanguageModel, portugueseLanguageModel,
            spanishLanguageModel
        )
        val detector = LanguageDetector.fromAllBuiltInLanguages()
        assertEquals(7, detector.numberOfLoadedLanguages)
        assertEquals(setOf(ENGLISH, FRENCH, GERMAN, ITALIAN, LATIN, PORTUGUESE, SPANISH), detector.languages)
    }

    @Test
    fun `assert that all built-in spoken language models are loaded properly`() {
        every { LanguageDetector["loadLanguageModels"](any<Set<Language>>(), any<KClass<Unigram>>()) } returns listOf(
            englishLanguageModel, frenchLanguageModel, germanLanguageModel,
            italianLanguageModel, portugueseLanguageModel, spanishLanguageModel
        )
        val detector = LanguageDetector.fromAllBuiltInSpokenLanguages()
        assertEquals(6, detector.numberOfLoadedLanguages)
        assertEquals(
            setOf(ENGLISH, FRENCH, GERMAN, ITALIAN, PORTUGUESE, SPANISH),
            detector.languages
        )
    }

    @Test
    fun `assert that selected language models are loaded properly`() {
        every { LanguageDetector["loadLanguageModels"](any<Set<Language>>(), any<KClass<Unigram>>()) } returns listOf(
            latinLanguageModel, germanLanguageModel
        )
        val detector = LanguageDetector.fromLanguages(LATIN, GERMAN)
        assertEquals(2, detector.numberOfLoadedLanguages)
        assertEquals(setOf(LATIN, GERMAN), detector.languages)
    }

    @Test
    fun `assert that LanguageDetector can not be built from only one language`() {
        val expectedMessage = "LanguageDetector needs at least 2 languages to choose from"
        run {
            val exception = assertThrows(IllegalArgumentException::class.java) {
                LanguageDetector.fromLanguages(GERMAN)
            }
            assertEquals(expectedMessage, exception.message)
        }
        run {
            val exception = assertThrows(IllegalArgumentException::class.java) {
                LanguageDetector.fromAllBuiltInLanguagesWithout(ENGLISH, FRENCH, GERMAN, ITALIAN, LATIN, PORTUGUESE)
            }
            assertEquals(expectedMessage, exception.message)
        }
    }

    @Test
    fun `assert that excluded language models are not loaded`() {
        every { LanguageDetector["loadLanguageModels"](any<Set<Language>>(), any<KClass<Unigram>>()) } returns listOf(
            englishLanguageModel, germanLanguageModel, italianLanguageModel, spanishLanguageModel
        )
        val detector = LanguageDetector.fromAllBuiltInLanguagesWithout(FRENCH, LATIN, PORTUGUESE)
        assertEquals(4, detector.numberOfLoadedLanguages)
        assertEquals(
            setOf(ENGLISH, GERMAN, ITALIAN, SPANISH),
            detector.languages
        )
    }
}

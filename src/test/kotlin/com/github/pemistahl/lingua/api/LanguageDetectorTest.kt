/*
 * Copyright © 2018-today Peter M. Stahl pemistahl@gmail.com
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

import com.github.pemistahl.lingua.api.Language.AFRIKAANS
import com.github.pemistahl.lingua.api.Language.ALBANIAN
import com.github.pemistahl.lingua.api.Language.ARABIC
import com.github.pemistahl.lingua.api.Language.AZERBAIJANI
import com.github.pemistahl.lingua.api.Language.BASQUE
import com.github.pemistahl.lingua.api.Language.BELARUSIAN
import com.github.pemistahl.lingua.api.Language.BOKMAL
import com.github.pemistahl.lingua.api.Language.BOSNIAN
import com.github.pemistahl.lingua.api.Language.BULGARIAN
import com.github.pemistahl.lingua.api.Language.CATALAN
import com.github.pemistahl.lingua.api.Language.CROATIAN
import com.github.pemistahl.lingua.api.Language.CZECH
import com.github.pemistahl.lingua.api.Language.DANISH
import com.github.pemistahl.lingua.api.Language.DUTCH
import com.github.pemistahl.lingua.api.Language.ENGLISH
import com.github.pemistahl.lingua.api.Language.ESPERANTO
import com.github.pemistahl.lingua.api.Language.ESTONIAN
import com.github.pemistahl.lingua.api.Language.FINNISH
import com.github.pemistahl.lingua.api.Language.FRENCH
import com.github.pemistahl.lingua.api.Language.GANDA
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.HUNGARIAN
import com.github.pemistahl.lingua.api.Language.ICELANDIC
import com.github.pemistahl.lingua.api.Language.INDONESIAN
import com.github.pemistahl.lingua.api.Language.IRISH
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.KAZAKH
import com.github.pemistahl.lingua.api.Language.LATIN
import com.github.pemistahl.lingua.api.Language.LATVIAN
import com.github.pemistahl.lingua.api.Language.LITHUANIAN
import com.github.pemistahl.lingua.api.Language.MACEDONIAN
import com.github.pemistahl.lingua.api.Language.MALAY
import com.github.pemistahl.lingua.api.Language.MAORI
import com.github.pemistahl.lingua.api.Language.MONGOLIAN
import com.github.pemistahl.lingua.api.Language.NYNORSK
import com.github.pemistahl.lingua.api.Language.PERSIAN
import com.github.pemistahl.lingua.api.Language.POLISH
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.ROMANIAN
import com.github.pemistahl.lingua.api.Language.RUSSIAN
import com.github.pemistahl.lingua.api.Language.SERBIAN
import com.github.pemistahl.lingua.api.Language.SHONA
import com.github.pemistahl.lingua.api.Language.SLOVAK
import com.github.pemistahl.lingua.api.Language.SLOVENE
import com.github.pemistahl.lingua.api.Language.SOMALI
import com.github.pemistahl.lingua.api.Language.SOTHO
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWAHILI
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.TAGALOG
import com.github.pemistahl.lingua.api.Language.TSONGA
import com.github.pemistahl.lingua.api.Language.TSWANA
import com.github.pemistahl.lingua.api.Language.TURKISH
import com.github.pemistahl.lingua.api.Language.UKRAINIAN
import com.github.pemistahl.lingua.api.Language.UNKNOWN
import com.github.pemistahl.lingua.api.Language.URDU
import com.github.pemistahl.lingua.api.Language.VIETNAMESE
import com.github.pemistahl.lingua.api.Language.WELSH
import com.github.pemistahl.lingua.api.Language.XHOSA
import com.github.pemistahl.lingua.api.Language.YORUBA
import com.github.pemistahl.lingua.api.Language.ZULU
import com.github.pemistahl.lingua.internal.Ngram
import com.github.pemistahl.lingua.internal.TestDataLanguageModel
import com.github.pemistahl.lingua.internal.TrainingDataLanguageModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.assertj.core.data.Offset
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.ln

@ExtendWith(MockKExtension::class)
class LanguageDetectorTest {

    // language model mocks for training data

    @MockK
    private lateinit var unigramLanguageModelForEnglish: TrainingDataLanguageModel
    @MockK
    private lateinit var bigramLanguageModelForEnglish: TrainingDataLanguageModel
    @MockK
    private lateinit var trigramLanguageModelForEnglish: TrainingDataLanguageModel
    @MockK
    private lateinit var quadrigramLanguageModelForEnglish: TrainingDataLanguageModel
    @MockK
    private lateinit var fivegramLanguageModelForEnglish: TrainingDataLanguageModel
    @MockK
    private lateinit var unigramLanguageModelForGerman: TrainingDataLanguageModel
    @MockK
    private lateinit var bigramLanguageModelForGerman: TrainingDataLanguageModel
    @MockK
    private lateinit var trigramLanguageModelForGerman: TrainingDataLanguageModel
    @MockK
    private lateinit var quadrigramLanguageModelForGerman: TrainingDataLanguageModel
    @MockK
    private lateinit var fivegramLanguageModelForGerman: TrainingDataLanguageModel

    // language model mocks for test data

    @MockK
    private lateinit var unigramTestDataLanguageModel: TestDataLanguageModel

    @MockK
    private lateinit var trigramTestDataLanguageModel: TestDataLanguageModel

    @MockK
    private lateinit var quadrigramTestDataLanguageModel: TestDataLanguageModel

    @SpyK
    private var detectorForEnglishAndGerman = LanguageDetector(
        languages = mutableSetOf(ENGLISH, GERMAN),
        minimumRelativeDistance = 0.0,
        isEveryLanguageModelPreloaded = false
    )

    private val detectorForAllLanguages = LanguageDetector(
        languages = Language.all().toMutableSet(),
        minimumRelativeDistance = 0.0,
        isEveryLanguageModelPreloaded = false
    )

    @BeforeAll
    fun beforeAll() {
        defineBehaviorOfUnigramLanguageModels()
        defineBehaviorOfBigramLanguageModels()
        defineBehaviorOfTrigramLanguageModels()
        defineBehaviorOfQuadrigramLanguageModels()
        defineBehaviorOfFivegramLanguageModels()
        defineBehaviorOfTestDataLanguageModels()
    }

    @BeforeEach
    fun beforeEach() {
        addLanguageModelsToDetector()
    }

    @AfterEach
    fun afterEach() {
        removeLanguageModelsFromDetector()
    }

    // text preprocessing

    @Test
    fun `assert that text is cleaned up properly`() {
        assertThat(
            detectorForAllLanguages.cleanUpInputText(
                """
                Weltweit    gibt es ungefähr 6.000 Sprachen,
                wobei laut Schätzungen zufolge ungefähr 90  Prozent davon
                am Ende dieses Jahrhunderts verdrängt sein werden.
                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                "weltweit gibt es ungefähr sprachen wobei laut schätzungen zufolge ungefähr",
                "prozent davon am ende dieses jahrhunderts verdrängt sein werden"
            ).joinToString(separator = " ")
        )
    }

    @Test
    fun `assert that text is split into words correctly`() {
        assertThat(
            detectorForAllLanguages.splitTextIntoWords("this is a sentence")
        ).isEqualTo(
            listOf("this", "is", "a", "sentence")
        )

        assertThat(
            detectorForAllLanguages.splitTextIntoWords("sentence")
        ).isEqualTo(
            listOf("sentence")
        )

        assertThat(
            detectorForAllLanguages.splitTextIntoWords("上海大学是一个好大学 this is a sentence")
        ).isEqualTo(
            listOf("上", "海", "大", "学", "是", "一", "个", "好", "大", "学", "this", "is", "a", "sentence")
        )
    }

    // ngram probability lookup

    private fun ngramProbabilityProvider() = listOf(
        arguments(ENGLISH, "a", 0.01),
        arguments(ENGLISH, "lt", 0.12),
        arguments(ENGLISH, "ter", 0.21),
        arguments(ENGLISH, "alte", 0.25),
        arguments(ENGLISH, "alter", 0.29),

        arguments(GERMAN, "t", 0.08),
        arguments(GERMAN, "er", 0.18),
        arguments(GERMAN, "alt", 0.22),
        arguments(GERMAN, "lter", 0.28),
        arguments(GERMAN, "alter", 0.30)
    )

    @ParameterizedTest
    @MethodSource("ngramProbabilityProvider")
    internal fun `assert that ngram probability lookup works correctly`(
        language: Language,
        ngram: Ngram,
        expectedProbability: Double
    ) {
        assertThat(
            detectorForEnglishAndGerman.lookUpNgramProbability(language, ngram)
        ).`as`(
            "language '$language', ngram '$ngram'"
        ).isEqualTo(
            expectedProbability.toFloat()
        )
    }

    @Test
    fun `assert that ngram probability lookup does not work for Zerogram`() {
        assertThatIllegalArgumentException().isThrownBy {
            detectorForEnglishAndGerman.lookUpNgramProbability(ENGLISH, Ngram(""))
        }.withMessage(
            "Zerogram detected"
        )
    }

    // ngram probability summation

    private fun ngramProbabilitySumProvider() = listOf(
        arguments(
            setOf(Ngram("a"), Ngram("l"), Ngram("t"), Ngram("e"), Ngram("r")),
            ln(0.01) + ln(0.02) + ln(0.03) + ln(0.04) + ln(0.05)
        ),
        arguments(
            // back off unknown Trigram("tez") to known Bigram("te")
            setOf(Ngram("alt"), Ngram("lte"), Ngram("tez")),
            ln(0.19) + ln(0.2) + ln(0.13)
        ),
        arguments(
            // back off unknown Fivegram("aquas") to known Unigram("a")
            setOf(Ngram("aquas")),
            ln(0.01)
        )
    )

    @ParameterizedTest
    @MethodSource("ngramProbabilitySumProvider")
    internal fun `assert that sum of ngram probabilities can be computed correctly`(
        ngrams: Set<Ngram>,
        expectedSumOfProbabilities: Double
    ) {
        assertThat(
            detectorForEnglishAndGerman.computeSumOfNgramProbabilities(ENGLISH, ngrams)
        ).`as`(
            "ngrams $ngrams"
        ).isCloseTo(
            expectedSumOfProbabilities, Offset.offset(0.00001)
        )
    }

    // language probability estimation

    private fun languageProbabilitiesProvider() = listOf(
        arguments(
            unigramTestDataLanguageModel,
            mapOf(
                ENGLISH to ln(0.01) + ln(0.02) + ln(0.03) + ln(0.04) + ln(0.05),
                GERMAN to ln(0.06) + ln(0.07) + ln(0.08) + ln(0.09) + ln(0.1)
            )
        ),
        arguments(
            trigramTestDataLanguageModel,
            mapOf(
                ENGLISH to ln(0.19) + ln(0.2) + ln(0.21),
                GERMAN to ln(0.22) + ln(0.23) + ln(0.24)
            )
        ),
        arguments(
            quadrigramTestDataLanguageModel,
            mapOf(
                ENGLISH to ln(0.25) + ln(0.26),
                GERMAN to ln(0.27) + ln(0.28)
            )
        )
    )

    @ParameterizedTest
    @MethodSource("languageProbabilitiesProvider")
    internal fun `assert that language probabilities can be computed correctly`(
        testDataModel: TestDataLanguageModel,
        expectedProbabilities: Map<Language, Double>
    ) {
        val probabilities =
            detectorForEnglishAndGerman.computeLanguageProbabilities(
                testDataModel,
                detectorForEnglishAndGerman.languages
            )
        assertThat(probabilities.keys).isEqualTo(expectedProbabilities.keys)
        for (language in expectedProbabilities.keys) {
            assertThat(
                probabilities[language]
            ).isCloseTo(
                expectedProbabilities[language], Offset.offset(0.00001)
            )
        }
    }

    // language detection with rules

    @ParameterizedTest
    @CsvSource(
        "məhərrəm, AZERBAIJANI",
        "substituïts, CATALAN",
        "rozdělit, CZECH",
        "tvořen, CZECH",
        "subjektů, CZECH",
        "nesufiĉecon, ESPERANTO",
        "intermiksiĝis, ESPERANTO",
        "monaĥinoj, ESPERANTO",
        "kreitaĵoj, ESPERANTO",
        "ŝpinante, ESPERANTO",
        "apenaŭ, ESPERANTO",
        "groß, GERMAN",
        "σχέδια, GREEK",
        "fekvő, HUNGARIAN",
        "meggyűrűzni, HUNGARIAN",
        "ヴェダイヤモンド, JAPANESE",
        "әлем, KAZAKH",
        "шаруашылығы, KAZAKH",
        "ақын, KAZAKH",
        "оның, KAZAKH",
        "шұрайлы, KAZAKH",
        "teoloģiska, LATVIAN",
        "blaķene, LATVIAN",
        "ceļojumiem, LATVIAN",
        "numuriņu, LATVIAN",
        "mergelės, LITHUANIAN",
        "įrengus, LITHUANIAN",
        "slegiamų, LITHUANIAN",
        "припаѓа, MACEDONIAN",
        "ѕидови, MACEDONIAN",
        "ќерка, MACEDONIAN",
        "џамиите, MACEDONIAN",
        "मिळते, MARATHI",
        "үндсэн, MONGOLIAN",
        "дөхөж, MONGOLIAN",
        "zmieniły, POLISH",
        "państwowych, POLISH",
        "mniejszości, POLISH",
        "groźne, POLISH",
        "ialomiţa, ROMANIAN",
        "наслеђивања, SERBIAN",
        "неисквареношћу, SERBIAN",
        "podĺa, SLOVAK",
        "pohľade, SLOVAK",
        "mŕtvych, SLOVAK",
        "ґрунтовому, UKRAINIAN",
        "пропонує, UKRAINIAN",
        "пристрої, UKRAINIAN",
        "cằm, VIETNAMESE",
        "thần, VIETNAMESE",
        "chẳng, VIETNAMESE",
        "quẩy, VIETNAMESE",
        "sẵn, VIETNAMESE",
        "nhẫn, VIETNAMESE",
        "dắt, VIETNAMESE",
        "chất, VIETNAMESE",
        "đạp, VIETNAMESE",
        "mặn, VIETNAMESE",
        "hậu, VIETNAMESE",
        "hiền, VIETNAMESE",
        "lẻn, VIETNAMESE",
        "biểu, VIETNAMESE",
        "kẽm, VIETNAMESE",
        "diễm, VIETNAMESE",
        "phế, VIETNAMESE",
        "việc, VIETNAMESE",
        "chỉnh, VIETNAMESE",
        "trĩ, VIETNAMESE",
        "ravị, VIETNAMESE",
        "thơ, VIETNAMESE",
        "nguồn, VIETNAMESE",
        "thờ, VIETNAMESE",
        "sỏi, VIETNAMESE",
        "tổng, VIETNAMESE",
        "nhở, VIETNAMESE",
        "mỗi, VIETNAMESE",
        "bỡi, VIETNAMESE",
        "tốt, VIETNAMESE",
        "giới, VIETNAMESE",
        "một, VIETNAMESE",
        "hợp, VIETNAMESE",
        "hưng, VIETNAMESE",
        "từng, VIETNAMESE",
        "của, VIETNAMESE",
        "sử, VIETNAMESE",
        "cũng, VIETNAMESE",
        "những, VIETNAMESE",
        "chức, VIETNAMESE",
        "dụng, VIETNAMESE",
        "thực, VIETNAMESE",
        "kỳ, VIETNAMESE",
        "kỷ, VIETNAMESE",
        "mỹ, VIETNAMESE",
        "mỵ, VIETNAMESE",
        "aṣiwèrè, YORUBA",
        "ṣaaju, YORUBA",
        "والموضوع, UNKNOWN",
        "сопротивление, UNKNOWN",
        "house, UNKNOWN"
    )
    fun `assert that language of single word with unique characters can be unambiguously identified with rules`(
        word: String,
        expectedLanguage: Language
    ) {
        assertThat(
            detectorForAllLanguages.detectLanguageWithRules(listOf(word))
        ).`as`(
            "word '$word'"
        ).isEqualTo(
            expectedLanguage
        )
    }

    @ParameterizedTest
    @CsvSource(
        "ունենա, ARMENIAN",
        "জানাতে, BENGALI",
        "გარეუბან, GEORGIAN",
        "σταμάτησε, GREEK",
        "ઉપકરણોની, GUJARATI",
        "בתחרויות, HEBREW",
        "びさ, JAPANESE",
        "대결구도가, KOREAN",
        "ਮੋਟਰਸਾਈਕਲਾਂ, PUNJABI",
        "துன்பங்களை, TAMIL",
        "కృష్ణదేవరాయలు, TELUGU",
        "ในทางหลวงหมายเลข, THAI"
    )
    fun `assert that language of single word with unique alphabet can be unambiguously identified with rules`(
        word: String,
        expectedLanguage: Language
    ) {
        assertThat(
            detectorForAllLanguages.detectLanguageWithRules(listOf(word))
        ).`as`(
            "word '$word'"
        ).isEqualTo(
            expectedLanguage
        )
    }

    // language filtering with rules

    private fun filteredLanguagesProvider() = listOf(
        arguments(
            "والموضوع",
            listOf(ARABIC, PERSIAN, URDU)
        ),
        arguments(
            "сопротивление",
            listOf(BELARUSIAN, BULGARIAN, KAZAKH, MACEDONIAN, MONGOLIAN, RUSSIAN, SERBIAN, UKRAINIAN)
        ),
        arguments(
            "раскрывае",
            listOf(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN)
        ),
        arguments(
            "этот",
            listOf(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN)
        ),
        arguments(
            "огнём",
            listOf(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN)
        ),
        arguments(
            "плаваща",
            listOf(BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN)
        ),
        arguments(
            "довършат",
            listOf(BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN)
        ),
        arguments(
            "павінен",
            listOf(BELARUSIAN, KAZAKH, UKRAINIAN)
        ),
        arguments(
            "затоплување",
            listOf(MACEDONIAN, SERBIAN)
        ),
        arguments(
            "ректасцензија",
            listOf(MACEDONIAN, SERBIAN)
        ),
        arguments(
            "набљудувач",
            listOf(MACEDONIAN, SERBIAN)
        ),
        arguments(
            "aizklātā",
            listOf(LATVIAN, MAORI, YORUBA)
        ),
        arguments(
            "sistēmas",
            listOf(LATVIAN, MAORI, YORUBA)
        ),
        arguments(
            "palīdzi",
            listOf(LATVIAN, MAORI, YORUBA)
        ),
        arguments(
            "nhẹn",
            listOf(VIETNAMESE, YORUBA)
        ),
        arguments(
            "chọn",
            listOf(VIETNAMESE, YORUBA)
        ),
        arguments(
            "prihvaćanju",
            listOf(BOSNIAN, CROATIAN, POLISH)
        ),
        arguments(
            "nađete",
            listOf(BOSNIAN, CROATIAN, VIETNAMESE)
        ),
        arguments(
            "visão",
            listOf(PORTUGUESE, VIETNAMESE)
        ),
        arguments(
            "wystąpią",
            listOf(LITHUANIAN, POLISH)
        ),
        arguments(
            "budowę",
            listOf(LITHUANIAN, POLISH)
        ),
        arguments(
            "nebūsime",
            listOf(LATVIAN, LITHUANIAN, MAORI, YORUBA)
        ),
        arguments(
            "afişate",
            listOf(AZERBAIJANI, ROMANIAN, TURKISH)
        ),
        arguments(
            "kradzieżami",
            listOf(POLISH, ROMANIAN)
        ),
        arguments(
            "înviat",
            listOf(FRENCH, ROMANIAN)
        ),
        arguments(
            "venerdì",
            listOf(ITALIAN, VIETNAMESE, YORUBA)
        ),
        arguments(
            "años",
            listOf(BASQUE, SPANISH)
        ),
        arguments(
            "rozohňuje",
            listOf(CZECH, SLOVAK)
        ),
        arguments(
            "rtuť",
            listOf(CZECH, SLOVAK)
        ),
        arguments(
            "pregătire",
            listOf(ROMANIAN, VIETNAMESE)
        ),
        arguments(
            "jeďte",
            listOf(CZECH, ROMANIAN, SLOVAK)
        ),
        arguments(
            "minjaverðir",
            listOf(ICELANDIC, TURKISH)
        ),
        arguments(
            "þagnarskyldu",
            listOf(ICELANDIC, TURKISH)
        ),
        arguments(
            "nebûtu",
            listOf(FRENCH, HUNGARIAN)
        ),
        arguments(
            "hashemidëve",
            listOf(AFRIKAANS, ALBANIAN, DUTCH, FRENCH)
        ),
        arguments(
            "forêt",
            listOf(AFRIKAANS, FRENCH, PORTUGUESE, VIETNAMESE)
        ),
        arguments(
            "succèdent",
            listOf(FRENCH, ITALIAN, VIETNAMESE, YORUBA)
        ),
        arguments(
            "où",
            listOf(FRENCH, ITALIAN, VIETNAMESE, YORUBA)
        ),
        arguments(
            "tõeliseks",
            listOf(ESTONIAN, HUNGARIAN, PORTUGUESE, VIETNAMESE)
        ),
        arguments(
            "viòiem",
            listOf(CATALAN, ITALIAN, VIETNAMESE, YORUBA)
        ),
        arguments(
            "contrôle",
            listOf(FRENCH, PORTUGUESE, SLOVAK, VIETNAMESE)
        ),
        arguments(
            "direktør",
            listOf(BOKMAL, DANISH, NYNORSK)
        ),
        arguments(
            "vývoj",
            listOf(CZECH, ICELANDIC, SLOVAK, TURKISH, VIETNAMESE)
        ),
        arguments(
            "päralt",
            listOf(ESTONIAN, FINNISH, GERMAN, SLOVAK, SWEDISH)
        ),
        arguments(
            "labâk",
            listOf(FRENCH, PORTUGUESE, ROMANIAN, TURKISH, VIETNAMESE)
        ),
        arguments(
            "pràctiques",
            listOf(CATALAN, FRENCH, ITALIAN, PORTUGUESE, VIETNAMESE)
        ),
        arguments(
            "überrascht",
            listOf(AZERBAIJANI, CATALAN, ESTONIAN, GERMAN, HUNGARIAN, SPANISH, TURKISH)
        ),
        arguments(
            "indebærer",
            listOf(BOKMAL, DANISH, ICELANDIC, NYNORSK)
        ),
        arguments(
            "måned",
            listOf(BOKMAL, DANISH, NYNORSK, SWEDISH)
        ),
        arguments(
            "zaručen",
            listOf(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE)
        ),
        arguments(
            "zkouškou",
            listOf(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE)
        ),
        arguments(
            "navržen",
            listOf(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE)
        ),
        arguments(
            "façonnage",
            listOf(ALBANIAN, AZERBAIJANI, BASQUE, CATALAN, FRENCH, PORTUGUESE, TURKISH)
        ),
        arguments(
            "höher",
            listOf(AZERBAIJANI, ESTONIAN, FINNISH, GERMAN, HUNGARIAN, ICELANDIC, SWEDISH, TURKISH)
        ),
        arguments(
            "catedráticos",
            listOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA)
        ),
        arguments(
            "política",
            listOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA)
        ),
        arguments(
            "música",
            listOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA)
        ),
        arguments(
            "contradicció",
            listOf(CATALAN, HUNGARIAN, ICELANDIC, IRISH, POLISH, PORTUGUESE, SLOVAK, SPANISH, VIETNAMESE, YORUBA)
        ),
        arguments(
            "només",
            listOf(
                CATALAN, CZECH, FRENCH, HUNGARIAN, ICELANDIC, IRISH, ITALIAN, PORTUGUESE, SLOVAK, SPANISH,
                VIETNAMESE, YORUBA
            )
        ),
        arguments(
            "house",
            listOf(
                AFRIKAANS, ALBANIAN, AZERBAIJANI, BASQUE, BOKMAL, BOSNIAN, CATALAN, CROATIAN, CZECH, DANISH,
                DUTCH, ENGLISH, ESPERANTO, ESTONIAN, FINNISH, FRENCH, GANDA, GERMAN, HUNGARIAN, ICELANDIC,
                INDONESIAN, IRISH, ITALIAN, LATIN, LATVIAN, LITHUANIAN, MALAY, MAORI, NYNORSK, POLISH,
                PORTUGUESE, ROMANIAN, SHONA, SLOVAK, SLOVENE, SOMALI, SOTHO, SPANISH, SWAHILI, SWEDISH,
                TAGALOG, TSONGA, TSWANA, TURKISH, VIETNAMESE, WELSH, XHOSA, YORUBA, ZULU
            )
        )
    )

    @ParameterizedTest
    @MethodSource("filteredLanguagesProvider")
    fun `assert that languages can be correctly filtered by rules`(
        word: String,
        expectedLanguages: List<Language>
    ) {
        assertThat(
            detectorForAllLanguages.filterLanguagesByRules(listOf(word)).toList()
        ).`as`(
            "word '$word'"
        ).containsExactlyInAnyOrderElementsOf(
            expectedLanguages
        )
    }

    // language detection

    @ParameterizedTest
    @ValueSource(strings = ["", " \n  \t;", "3<856%)§"])
    fun `assert that strings without letters return unknown language`(invalidString: String) {
        assertThat(
            detectorForAllLanguages.detectLanguageOf(invalidString)
        ).isEqualTo(
            UNKNOWN
        )
    }

    @Test
    fun `assert that language of German noun 'Alter' can be detected correctly`() {
        assertThat(
            detectorForEnglishAndGerman.detectLanguageOf("Alter")
        ).isEqualTo(
            GERMAN
        )
    }

    @Test
    fun `assert that language confidence values can be computed correctly`() {
        val unigramCountForBothLanguages = 5

        val totalProbabilityForGerman = (
            // Unigrams
            ln(0.06) + ln(0.07) + ln(0.08) + ln(0.09) + ln(0.1) +
                // Bigrams
                ln(0.15) + ln(0.16) + ln(0.17) + ln(0.18) +
                // Trigrams
                ln(0.22) + ln(0.23) + ln(0.24) +
                // Quadrigrams
                ln(0.27) + ln(0.28) +
                // Fivegrams
                ln(0.3)
            ) / unigramCountForBothLanguages

        val totalProbabilityForEnglish = (
            // Unigrams
            ln(0.01) + ln(0.02) + ln(0.03) + ln(0.04) + ln(0.05) +
                // Bigrams
                ln(0.11) + ln(0.12) + ln(0.13) + ln(0.14) +
                // Trigrams
                ln(0.19) + ln(0.2) + ln(0.21) +
                // Quadrigrams
                ln(0.25) + ln(0.26) +
                // Fivegrams
                ln(0.29)
            ) / unigramCountForBothLanguages

        val computed = detectorForEnglishAndGerman.computeLanguageConfidenceValues("Alter")

        assertThat(
            computed.keys
        ).containsExactly(
            GERMAN, ENGLISH
        )

        assertThat(
            computed[GERMAN]
        ).isCloseTo(
            1.0, Offset.offset(0.00001)
        )

        assertThat(
            computed[ENGLISH]
        ).isCloseTo(
            totalProbabilityForGerman / totalProbabilityForEnglish, Offset.offset(0.00001)
        )
    }

    @Test
    fun `assert that unknown language is returned when no ngram probabilities are available`() {
        assertThat(
            detectorForEnglishAndGerman.detectLanguageOf("проарплап")
        ).isEqualTo(
            UNKNOWN
        )
    }

    @Test
    fun `assert that no confidence values are returned when no ngram probabilities are available`() {
        assertThat(
            detectorForEnglishAndGerman.computeLanguageConfidenceValues("проарплап")
        ).isEmpty()
    }

    private fun ambiguousTextProvider() = listOf(
        arguments(
            "ام وی با نیکی میناج تیزر داشت؟؟؟؟؟؟ i vote for bts ( _ ) as the _ via ( _ )",
            arrayOf(ENGLISH, URDU)
        ),
        arguments(
            "Az elmúlt hétvégén 12-re emelkedett az elhunyt koronavírus-fertőzöttek száma Szlovákiában. " +
                "Mindegyik szociális otthon dolgozóját letesztelik, " +
                "Matovič szerint az ingázóknak még várniuk kellene a teszteléssel",
            arrayOf(HUNGARIAN, SLOVAK)
        )
    )

    @ParameterizedTest
    @MethodSource("ambiguousTextProvider")
    fun `assert that language detection is deterministic`(
        text: String,
        languages: Array<Language>
    ) {
        removeLanguageModelsFromDetector()

        val detector = LanguageDetectorBuilder
            .fromLanguages(*languages)
            .withPreloadedLanguageModels()
            .build()
        val detectedLanguages = mutableSetOf<Language>()
        for (i in 0..100) {
            val language = detector.detectLanguageOf(text)
            detectedLanguages.add(language)
        }
        assertThat(detectedLanguages.size).isEqualTo(1)
    }

    @Test
    fun `assert that language detector can be properly destroyed`() {
        removeLanguageModelsFromDetector()

        val detector = LanguageDetectorBuilder
            .fromLanguages(ENGLISH, GERMAN)
            .withPreloadedLanguageModels()
            .build()

        detector.destroy()

        assertThat(detector.threadPool.isShutdown).isTrue
        val exception = assertThrows<IllegalStateException> {
            detector.detectLanguageOf("languages are fascinating")
        }
        assertThat(exception.message).isEqualTo(
            "This LanguageDetector instance has been destroyed and cannot be reused"
        )

        assertThat(detector.unigramLanguageModels).isEmpty()
        assertThat(detector.bigramLanguageModels).isEmpty()
        assertThat(detector.trigramLanguageModels).isEmpty()
        assertThat(detector.quadrigramLanguageModels).isEmpty()
        assertThat(detector.fivegramLanguageModels).isEmpty()
    }

    private fun defineBehaviorOfUnigramLanguageModels() {
        with(unigramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("a")) } returns 0.01F
            every { getRelativeFrequency(Ngram("l")) } returns 0.02F
            every { getRelativeFrequency(Ngram("t")) } returns 0.03F
            every { getRelativeFrequency(Ngram("e")) } returns 0.04F
            every { getRelativeFrequency(Ngram("r")) } returns 0.05F

            // unknown unigrams in model
            every { getRelativeFrequency(Ngram("w")) } returns 0.0F
        }

        with(unigramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("a")) } returns 0.06F
            every { getRelativeFrequency(Ngram("l")) } returns 0.07F
            every { getRelativeFrequency(Ngram("t")) } returns 0.08F
            every { getRelativeFrequency(Ngram("e")) } returns 0.09F
            every { getRelativeFrequency(Ngram("r")) } returns 0.1F

            // unknown unigrams in model
            every { getRelativeFrequency(Ngram("w")) } returns 0.0F
        }
    }

    private fun defineBehaviorOfBigramLanguageModels() {
        with(bigramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("al")) } returns 0.11F
            every { getRelativeFrequency(Ngram("lt")) } returns 0.12F
            every { getRelativeFrequency(Ngram("te")) } returns 0.13F
            every { getRelativeFrequency(Ngram("er")) } returns 0.14F

            // unknown bigrams in model
            for (value in listOf("aq", "wx")) {
                every { getRelativeFrequency(Ngram(value)) } returns 0.0F
            }
        }

        with(bigramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("al")) } returns 0.15F
            every { getRelativeFrequency(Ngram("lt")) } returns 0.16F
            every { getRelativeFrequency(Ngram("te")) } returns 0.17F
            every { getRelativeFrequency(Ngram("er")) } returns 0.18F

            // unknown bigrams in model
            every { getRelativeFrequency(Ngram("wx")) } returns 0.0F
        }
    }

    private fun defineBehaviorOfTrigramLanguageModels() {
        with(trigramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("alt")) } returns 0.19F
            every { getRelativeFrequency(Ngram("lte")) } returns 0.2F
            every { getRelativeFrequency(Ngram("ter")) } returns 0.21F

            // unknown trigrams in model
            for (value in listOf("aqu", "tez", "wxy")) {
                every { getRelativeFrequency(Ngram(value)) } returns 0.0F
            }
        }

        with(trigramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("alt")) } returns 0.22F
            every { getRelativeFrequency(Ngram("lte")) } returns 0.23F
            every { getRelativeFrequency(Ngram("ter")) } returns 0.24F

            // unknown trigrams in model
            every { getRelativeFrequency(Ngram("wxy")) } returns 0.0F
        }
    }

    private fun defineBehaviorOfQuadrigramLanguageModels() {
        with(quadrigramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("alte")) } returns 0.25F
            every { getRelativeFrequency(Ngram("lter")) } returns 0.26F

            // unknown quadrigrams in model
            for (value in listOf("aqua", "wxyz")) {
                every { getRelativeFrequency(Ngram(value)) } returns 0.0F
            }
        }

        with(quadrigramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("alte")) } returns 0.27F
            every { getRelativeFrequency(Ngram("lter")) } returns 0.28F

            // unknown quadrigrams in model
            every { getRelativeFrequency(Ngram("wxyz")) } returns 0.0F
        }
    }

    private fun defineBehaviorOfFivegramLanguageModels() {
        with(fivegramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("alter")) } returns 0.29F

            // unknown fivegrams in model
            every { getRelativeFrequency(Ngram("aquas")) } returns 0.0F
        }

        with(fivegramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("alter")) } returns 0.30F
        }
    }

    private fun addLanguageModelsToDetector() {
        with(detectorForEnglishAndGerman) {
            unigramLanguageModels[ENGLISH] = unigramLanguageModelForEnglish
            unigramLanguageModels[GERMAN] = unigramLanguageModelForGerman

            bigramLanguageModels[ENGLISH] = bigramLanguageModelForEnglish
            bigramLanguageModels[GERMAN] = bigramLanguageModelForGerman

            trigramLanguageModels[ENGLISH] = trigramLanguageModelForEnglish
            trigramLanguageModels[GERMAN] = trigramLanguageModelForGerman

            quadrigramLanguageModels[ENGLISH] = quadrigramLanguageModelForEnglish
            quadrigramLanguageModels[GERMAN] = quadrigramLanguageModelForGerman

            fivegramLanguageModels[ENGLISH] = fivegramLanguageModelForEnglish
            fivegramLanguageModels[GERMAN] = fivegramLanguageModelForGerman
        }
    }

    private fun removeLanguageModelsFromDetector() {
        with(detectorForEnglishAndGerman) {
            unigramLanguageModels.clear()
            bigramLanguageModels.clear()
            trigramLanguageModels.clear()
            quadrigramLanguageModels.clear()
            fivegramLanguageModels.clear()
        }
    }

    private fun defineBehaviorOfTestDataLanguageModels() {
        with(unigramTestDataLanguageModel) {
            every { ngrams } returns setOf(
                Ngram("a"),
                Ngram("l"),
                Ngram("t"),
                Ngram("e"),
                Ngram("r")
            )
        }

        with(trigramTestDataLanguageModel) {
            every { ngrams } returns setOf(
                Ngram("alt"),
                Ngram("lte"),
                Ngram("ter"),
                Ngram("wxy")
            )
        }

        with(quadrigramTestDataLanguageModel) {
            every { ngrams } returns setOf(
                Ngram("alte"),
                Ngram("lter"),
                Ngram("wxyz")
            )
        }
    }
}

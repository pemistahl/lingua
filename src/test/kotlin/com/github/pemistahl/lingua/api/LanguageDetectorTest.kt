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
import com.github.pemistahl.lingua.api.Language.OROMO
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
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.ln

@ExtendWith(MockKExtension::class)
class LanguageDetectorTest {

    // language models for training data

    private val unigramLanguageModelForEnglish = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "a" to 0.01F,
                "l" to 0.02F,
                "t" to 0.03F,
                "e" to 0.04F,
                "r" to 0.05F,
                // unknown unigram in model
                "w" to 0F
            )
        )
    }

    private val bigramLanguageModelForEnglish = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "al" to 0.11F,
                "lt" to 0.12F,
                "te" to 0.13F,
                "er" to 0.14F,
                // unknown bigrams in model
                "aq" to 0F,
                "wx" to 0F
            )
        )
    }

    private val trigramLanguageModelForEnglish = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "alt" to 0.19F,
                "lte" to 0.2F,
                "ter" to 0.21F,
                // unknown trigrams in model
                "aqu" to 0F,
                "tez" to 0F,
                "wxy" to 0F
            )
        )
    }

    private val quadrigramLanguageModelForEnglish = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "alte" to 0.25F,
                "lter" to 0.26F,
                // unknown quadrigrams in model
                "aqua" to 0F,
                "wxyz" to 0F
            )
        )
    }

    private val fivegramLanguageModelForEnglish = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "alter" to 0.29F,
                // unknown fivegrams in model
                "aquas" to 0F
            )
        )
    }

    private val unigramLanguageModelForGerman = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "a" to 0.06F,
                "l" to 0.07F,
                "t" to 0.08F,
                "e" to 0.09F,
                "r" to 0.1F,
                // unknown unigrams in model
                "w" to 0F
            )
        )
    }

    private val bigramLanguageModelForGerman = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "al" to 0.15F,
                "lt" to 0.16F,
                "te" to 0.17F,
                "er" to 0.18F,
                // unknown bigrams in model
                "wx" to 0F
            )
        )
    }

    private val trigramLanguageModelForGerman = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "alt" to 0.22F,
                "lte" to 0.23F,
                "ter" to 0.24F,
                // unknown trigrams in model
                "wxy" to 0F
            )
        )
    }

    private val quadrigramLanguageModelForGerman = Object2FloatOpenHashMap<String>().apply {
        putAll(
            mapOf(
                "alte" to 0.27F,
                "lter" to 0.28F,
                // unknown quadrigrams in model
                "wxyz" to 0F
            )
        )
    }

    private val fivegramLanguageModelForGerman = Object2FloatOpenHashMap<String>().apply {
        putAll(mapOf("alter" to 0.3F))
    }

    // language model mocks for test data

    @MockK
    private lateinit var unigramTestDataLanguageModel: TestDataLanguageModel
    @MockK
    private lateinit var trigramTestDataLanguageModel: TestDataLanguageModel
    @MockK
    private lateinit var quadrigramTestDataLanguageModel: TestDataLanguageModel

    private var detectorForEnglishAndGerman = LanguageDetector(
        languages = mutableSetOf(ENGLISH, GERMAN),
        minimumRelativeDistance = 0.0,
        isEveryLanguageModelPreloaded = false,
        isLowAccuracyModeEnabled = false
    )

    private val detectorForAllLanguages = LanguageDetector(
        languages = Language.all().toMutableSet(),
        minimumRelativeDistance = 0.0,
        isEveryLanguageModelPreloaded = false,
        isLowAccuracyModeEnabled = false
    )

    @BeforeAll
    fun beforeAll() {
        addLanguageModelsToDetector()
        defineBehaviorOfTestDataLanguageModels()
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
        arguments(ENGLISH, "a", 0.01F),
        arguments(ENGLISH, "lt", 0.12F),
        arguments(ENGLISH, "ter", 0.21F),
        arguments(ENGLISH, "alte", 0.25F),
        arguments(ENGLISH, "alter", 0.29F),

        arguments(GERMAN, "t", 0.08F),
        arguments(GERMAN, "er", 0.18F),
        arguments(GERMAN, "alt", 0.22F),
        arguments(GERMAN, "lter", 0.28F),
        arguments(GERMAN, "alter", 0.30F)
    )

    @ParameterizedTest
    @MethodSource("ngramProbabilityProvider")
    internal fun `assert that ngram probability lookup works correctly`(
        language: Language,
        ngram: Ngram,
        expectedProbability: Float
    ) {
        assertThat(
            detectorForEnglishAndGerman.lookUpNgramProbability(language, ngram)
        ).`as`(
            "language '$language', ngram '$ngram'"
        ).isEqualTo(
            expectedProbability
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
            ln(0.01F) + ln(0.02F) + ln(0.03F) + ln(0.04F) + ln(0.05F)
        ),
        arguments(
            // back off unknown Trigram("tez") to known Bigram("te")
            setOf(Ngram("alt"), Ngram("lte"), Ngram("tez")),
            ln(0.19F) + ln(0.2F) + ln(0.13F)
        ),
        arguments(
            // back off unknown Fivegram("aquas") to known Unigram("a")
            setOf(Ngram("aquas")),
            ln(0.01F)
        )
    )

    @ParameterizedTest
    @MethodSource("ngramProbabilitySumProvider")
    internal fun `assert that sum of ngram probabilities can be computed correctly`(
        ngrams: Set<Ngram>,
        expectedSumOfProbabilities: Float
    ) {
        assertThat(
            detectorForEnglishAndGerman.computeSumOfNgramProbabilities(ENGLISH, ngrams)
        ).`as`(
            "ngrams $ngrams"
        ).isEqualTo(
            expectedSumOfProbabilities
        )
    }

    // language probability estimation

    private fun languageProbabilitiesProvider() = listOf(
        arguments(
            unigramTestDataLanguageModel,
            mapOf(
                ENGLISH to ln(0.01F) + ln(0.02F) + ln(0.03F) + ln(0.04F) + ln(0.05F),
                GERMAN to ln(0.06F) + ln(0.07F) + ln(0.08F) + ln(0.09F) + ln(0.1F)
            )
        ),
        arguments(
            trigramTestDataLanguageModel,
            mapOf(
                ENGLISH to ln(0.19F) + ln(0.2F) + ln(0.21F),
                GERMAN to ln(0.22F) + ln(0.23F) + ln(0.24F)
            )
        ),
        arguments(
            quadrigramTestDataLanguageModel,
            mapOf(
                ENGLISH to ln(0.25F) + ln(0.26F),
                GERMAN to ln(0.27F) + ln(0.28F)
            )
        )
    )

    @ParameterizedTest
    @MethodSource("languageProbabilitiesProvider")
    internal fun `assert that language probabilities can be computed correctly`(
        testDataModel: TestDataLanguageModel,
        expectedProbabilities: Map<Language, Float>
    ) {
        assertThat(
            detectorForEnglishAndGerman.computeLanguageProbabilities(
                testDataModel,
                detectorForEnglishAndGerman.languages
            )
        ).isEqualTo(
            expectedProbabilities
        )
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
                INDONESIAN, IRISH, ITALIAN, LATIN, LATVIAN, LITHUANIAN, MALAY, MAORI, NYNORSK, OROMO, POLISH,
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
            ln(0.06F) + ln(0.07F) + ln(0.08F) + ln(0.09F) + ln(0.1F) +
                // Bigrams
                ln(0.15F) + ln(0.16F) + ln(0.17F) + ln(0.18F) +
                // Trigrams
                ln(0.22F) + ln(0.23F) + ln(0.24F) +
                // Quadrigrams
                ln(0.27F) + ln(0.28F) +
                // Fivegrams
                ln(0.3F)
            ) / unigramCountForBothLanguages

        val totalProbabilityForEnglish = (
            // Unigrams
            ln(0.01F) + ln(0.02F) + ln(0.03F) + ln(0.04F) + ln(0.05F) +
                // Bigrams
                ln(0.11F) + ln(0.12F) + ln(0.13F) + ln(0.14F) +
                // Trigrams
                ln(0.19F) + ln(0.2F) + ln(0.21F) +
                // Quadrigrams
                ln(0.25F) + ln(0.26F) +
                // Fivegrams
                ln(0.29F)
            ) / unigramCountForBothLanguages

        val confidenceValues = detectorForEnglishAndGerman.computeLanguageConfidenceValues("Alter")

        assertThat(confidenceValues.firstKey()).isEqualTo(GERMAN)
        assertThat(confidenceValues.lastKey()).isEqualTo(ENGLISH)

        assertThat(confidenceValues[GERMAN]).isEqualTo(1.0)
        assertThat(confidenceValues[ENGLISH]).isCloseTo(
            (totalProbabilityForGerman / totalProbabilityForEnglish).toDouble(),
            within(0.000001)
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

        assertThatAllLanguageModelsAreUnloaded()

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

        removeLanguageModelsFromDetector()

        assertThatAllLanguageModelsAreUnloaded()

        addLanguageModelsToDetector()

        assertThatAllLanguageModelsAreLoaded()
    }

    @Test
    fun `assert that language models can be properly unloaded`() {
        assertThatAllLanguageModelsAreLoaded()

        detectorForEnglishAndGerman.unloadLanguageModels()

        assertThatAllLanguageModelsAreUnloaded()

        addLanguageModelsToDetector()

        assertThatAllLanguageModelsAreLoaded()
    }

    @Test
    fun `assert that high accuracy mode can be properly disabled`() {
        removeLanguageModelsFromDetector()

        assertThatAllLanguageModelsAreUnloaded()

        val detector = LanguageDetectorBuilder
            .fromLanguages(ENGLISH, GERMAN)
            .withPreloadedLanguageModels()
            .withLowAccuracyMode()
            .build()

        assertThatOnlyTrigramLanguageModelsAreLoaded()

        detector.detectLanguageOf("short text")

        assertThatOnlyTrigramLanguageModelsAreLoaded()

        addLanguageModelsToDetector()

        assertThatAllLanguageModelsAreLoaded()
    }

    @Test
    fun `assert that low accuracy mode reports unknown language for unigrams and bigrams`() {
        removeLanguageModelsFromDetector()

        val detector = LanguageDetectorBuilder
            .fromLanguages(ENGLISH, GERMAN)
            .withPreloadedLanguageModels()
            .withLowAccuracyMode()
            .build()

        assertThat(detector.detectLanguageOf("bed")).isNotEqualTo(UNKNOWN)
        assertThat(detector.detectLanguageOf("be")).isEqualTo(UNKNOWN)
        assertThat(detector.detectLanguageOf("b")).isEqualTo(UNKNOWN)
        assertThat(detector.detectLanguageOf("")).isEqualTo(UNKNOWN)

        addLanguageModelsToDetector()
    }

    private fun assertThatAllLanguageModelsAreUnloaded() {
        assertThat(LanguageDetector.unigramLanguageModels).isEmpty()
        assertThat(LanguageDetector.bigramLanguageModels).isEmpty()
        assertThat(LanguageDetector.trigramLanguageModels).isEmpty()
        assertThat(LanguageDetector.quadrigramLanguageModels).isEmpty()
        assertThat(LanguageDetector.fivegramLanguageModels).isEmpty()
    }

    private fun assertThatAllLanguageModelsAreLoaded() {
        assertThat(LanguageDetector.unigramLanguageModels).isNotEmpty
        assertThat(LanguageDetector.bigramLanguageModels).isNotEmpty
        assertThat(LanguageDetector.trigramLanguageModels).isNotEmpty
        assertThat(LanguageDetector.quadrigramLanguageModels).isNotEmpty
        assertThat(LanguageDetector.fivegramLanguageModels).isNotEmpty
    }

    private fun assertThatOnlyTrigramLanguageModelsAreLoaded() {
        assertThat(LanguageDetector.unigramLanguageModels).isEmpty()
        assertThat(LanguageDetector.bigramLanguageModels).isEmpty()
        assertThat(LanguageDetector.trigramLanguageModels).isNotEmpty
        assertThat(LanguageDetector.quadrigramLanguageModels).isEmpty()
        assertThat(LanguageDetector.fivegramLanguageModels).isEmpty()
    }

    private fun addLanguageModelsToDetector() {
        LanguageDetector.unigramLanguageModels[ENGLISH] = unigramLanguageModelForEnglish
        LanguageDetector.unigramLanguageModels[GERMAN] = unigramLanguageModelForGerman

        LanguageDetector.bigramLanguageModels[ENGLISH] = bigramLanguageModelForEnglish
        LanguageDetector.bigramLanguageModels[GERMAN] = bigramLanguageModelForGerman

        LanguageDetector.trigramLanguageModels[ENGLISH] = trigramLanguageModelForEnglish
        LanguageDetector.trigramLanguageModels[GERMAN] = trigramLanguageModelForGerman

        LanguageDetector.quadrigramLanguageModels[ENGLISH] = quadrigramLanguageModelForEnglish
        LanguageDetector.quadrigramLanguageModels[GERMAN] = quadrigramLanguageModelForGerman

        LanguageDetector.fivegramLanguageModels[ENGLISH] = fivegramLanguageModelForEnglish
        LanguageDetector.fivegramLanguageModels[GERMAN] = fivegramLanguageModelForGerman
    }

    private fun removeLanguageModelsFromDetector() {
        LanguageDetector.unigramLanguageModels.clear()
        LanguageDetector.bigramLanguageModels.clear()
        LanguageDetector.trigramLanguageModels.clear()
        LanguageDetector.quadrigramLanguageModels.clear()
        LanguageDetector.fivegramLanguageModels.clear()
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

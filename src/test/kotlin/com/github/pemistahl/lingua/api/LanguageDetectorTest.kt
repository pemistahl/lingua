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

import com.github.pemistahl.lingua.api.Language.AFRIKAANS
import com.github.pemistahl.lingua.api.Language.ALBANIAN
import com.github.pemistahl.lingua.api.Language.ARABIC
import com.github.pemistahl.lingua.api.Language.AZERBAIJANI
import com.github.pemistahl.lingua.api.Language.BASQUE
import com.github.pemistahl.lingua.api.Language.BELARUSIAN
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
import com.github.pemistahl.lingua.api.Language.GERMAN
import com.github.pemistahl.lingua.api.Language.GREEK
import com.github.pemistahl.lingua.api.Language.HUNGARIAN
import com.github.pemistahl.lingua.api.Language.ICELANDIC
import com.github.pemistahl.lingua.api.Language.INDONESIAN
import com.github.pemistahl.lingua.api.Language.IRISH
import com.github.pemistahl.lingua.api.Language.ITALIAN
import com.github.pemistahl.lingua.api.Language.JAPANESE
import com.github.pemistahl.lingua.api.Language.KAZAKH
import com.github.pemistahl.lingua.api.Language.LATIN
import com.github.pemistahl.lingua.api.Language.LATVIAN
import com.github.pemistahl.lingua.api.Language.LITHUANIAN
import com.github.pemistahl.lingua.api.Language.MACEDONIAN
import com.github.pemistahl.lingua.api.Language.MALAY
import com.github.pemistahl.lingua.api.Language.MARATHI
import com.github.pemistahl.lingua.api.Language.MONGOLIAN
import com.github.pemistahl.lingua.api.Language.NORWEGIAN
import com.github.pemistahl.lingua.api.Language.PERSIAN
import com.github.pemistahl.lingua.api.Language.POLISH
import com.github.pemistahl.lingua.api.Language.PORTUGUESE
import com.github.pemistahl.lingua.api.Language.ROMANIAN
import com.github.pemistahl.lingua.api.Language.RUSSIAN
import com.github.pemistahl.lingua.api.Language.SERBIAN
import com.github.pemistahl.lingua.api.Language.SLOVAK
import com.github.pemistahl.lingua.api.Language.SLOVENE
import com.github.pemistahl.lingua.api.Language.SOMALI
import com.github.pemistahl.lingua.api.Language.SPANISH
import com.github.pemistahl.lingua.api.Language.SWEDISH
import com.github.pemistahl.lingua.api.Language.TAGALOG
import com.github.pemistahl.lingua.api.Language.TURKISH
import com.github.pemistahl.lingua.api.Language.UKRAINIAN
import com.github.pemistahl.lingua.api.Language.UNKNOWN
import com.github.pemistahl.lingua.api.Language.URDU
import com.github.pemistahl.lingua.api.Language.VIETNAMESE
import com.github.pemistahl.lingua.api.Language.WELSH
import com.github.pemistahl.lingua.internal.Ngram
import com.github.pemistahl.lingua.internal.TestDataLanguageModel
import com.github.pemistahl.lingua.internal.TrainingDataLanguageModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
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
        minimumRelativeDistance = 0.0
    )

    private val detectorForAllLanguages = LanguageDetector(
        languages = Language.all().toMutableSet(),
        minimumRelativeDistance = 0.0
    )

    @BeforeAll
    fun beforeAll() {
        defineBehaviorOfUnigramLanguageModels()
        defineBehaviorOfBigramLanguageModels()
        defineBehaviorOfTrigramLanguageModels()
        defineBehaviorOfQuadrigramLanguageModels()
        defineBehaviorOfFivegramLanguageModels()

        addLanguageModelsToDetector()

        defineBehaviorOfTestDataLanguageModels()
    }

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

    @ParameterizedTest
    @MethodSource("identifiedLanguageProvider")
    fun `assert that language can be unambiguously identified with rules`(
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
        ).isEqualTo(
            expectedSumOfProbabilities
        )
    }

    @ParameterizedTest
    @MethodSource("languageProbabilitiesProvider")
    internal fun `assert that language probabilities can be computed correctly`(
        testDataModel: TestDataLanguageModel,
        expectedProbabilitiesMap: Map<Language, Double>
    ) {
        assertThat(
            detectorForEnglishAndGerman.computeLanguageProbabilities(
                testDataModel,
                detectorForEnglishAndGerman.languages.asSequence()
            )
        ).isEqualTo(
            expectedProbabilitiesMap
        )
    }

    @ParameterizedTest
    @MethodSource("probabilitiesListProvider")
    fun `assert that most likely language can be computed correctly`(
        probabilitiesList: List<Map<Language, Double>>,
        expectedLanguage: Language
    ) {
        assertThat(
            detectorForEnglishAndGerman.getMostLikelyLanguage(
                probabilitiesList,
                emptyMap(),
                detectorForEnglishAndGerman.languages.asSequence()
            )
        ).isEqualTo(
            expectedLanguage
        )
    }

    @Test
    fun `assert that ngram probabilities are correctly added to probabilities list`() {
        val probabilitiesList = mutableListOf(
            mapOf(ENGLISH to -0.1, GERMAN to -0.2),
            mapOf(ENGLISH to -0.3, GERMAN to -0.4)
        )

        val languagesSequence = detectorForEnglishAndGerman.languages.asSequence()

        every {
            detectorForEnglishAndGerman.computeLanguageProbabilities(trigramTestDataLanguageModel,
                languagesSequence)
        } returns
            mapOf(ENGLISH to -0.5, GERMAN to -0.6)

        detectorForEnglishAndGerman.addNgramProbabilities(
            probabilitiesList,
            languagesSequence,
            trigramTestDataLanguageModel
        )

        assertThat(
            probabilitiesList
        ).isEqualTo(
            mutableListOf(
                mapOf(ENGLISH to -0.1, GERMAN to -0.2),
                mapOf(ENGLISH to -0.3, GERMAN to -0.4),
                mapOf(ENGLISH to -0.5, GERMAN to -0.6)
            )
        )
    }

    private fun defineBehaviorOfUnigramLanguageModels() {
        with (unigramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("a")) } returns 0.01
            every { getRelativeFrequency(Ngram("l")) } returns 0.02
            every { getRelativeFrequency(Ngram("t")) } returns 0.03
            every { getRelativeFrequency(Ngram("e")) } returns 0.04
            every { getRelativeFrequency(Ngram("r")) } returns 0.05

            // unknown unigrams in model
            every { getRelativeFrequency(Ngram("w")) } returns 0.0
        }

        with (unigramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("a")) } returns 0.06
            every { getRelativeFrequency(Ngram("l")) } returns 0.07
            every { getRelativeFrequency(Ngram("t")) } returns 0.08
            every { getRelativeFrequency(Ngram("e")) } returns 0.09
            every { getRelativeFrequency(Ngram("r")) } returns 0.1

            // unknown unigrams in model
            every { getRelativeFrequency(Ngram("w")) } returns 0.0
        }
    }

    private fun defineBehaviorOfBigramLanguageModels() {
        with (bigramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("al")) } returns 0.11
            every { getRelativeFrequency(Ngram("lt")) } returns 0.12
            every { getRelativeFrequency(Ngram("te")) } returns 0.13
            every { getRelativeFrequency(Ngram("er")) } returns 0.14

            // unknown bigrams in model
            for (value in listOf("aq", "wx")) {
                every { getRelativeFrequency(Ngram(value)) } returns 0.0
            }
        }

        with (bigramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("al")) } returns 0.15
            every { getRelativeFrequency(Ngram("lt")) } returns 0.16
            every { getRelativeFrequency(Ngram("te")) } returns 0.17
            every { getRelativeFrequency(Ngram("er")) } returns 0.18

            // unknown bigrams in model
            every { getRelativeFrequency(Ngram("wx")) } returns 0.0
        }
    }

    private fun defineBehaviorOfTrigramLanguageModels() {
        with (trigramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("alt")) } returns 0.19
            every { getRelativeFrequency(Ngram("lte")) } returns 0.2
            every { getRelativeFrequency(Ngram("ter")) } returns 0.21

            // unknown trigrams in model
            for (value in listOf("aqu", "tez", "wxy")) {
                every { getRelativeFrequency(Ngram(value)) } returns 0.0
            }
        }

        with (trigramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("alt")) } returns 0.22
            every { getRelativeFrequency(Ngram("lte")) } returns 0.23
            every { getRelativeFrequency(Ngram("ter")) } returns 0.24

            // unknown trigrams in model
            every { getRelativeFrequency(Ngram("wxy")) } returns 0.0
        }
    }

    private fun defineBehaviorOfQuadrigramLanguageModels() {
        with (quadrigramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("alte")) } returns 0.25
            every { getRelativeFrequency(Ngram("lter")) } returns 0.26

            // unknown quadrigrams in model
            for (value in listOf("aqua", "wxyz")) {
                every { getRelativeFrequency(Ngram(value)) } returns 0.0
            }
        }

        with (quadrigramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("alte")) } returns 0.27
            every { getRelativeFrequency(Ngram("lter")) } returns 0.28

            // unknown quadrigrams in model
            every { getRelativeFrequency(Ngram("wxyz")) } returns 0.0
        }
    }

    private fun defineBehaviorOfFivegramLanguageModels() {
        with (fivegramLanguageModelForEnglish) {
            every { getRelativeFrequency(Ngram("alter")) } returns 0.29

            // unknown fivegrams in model
            every { getRelativeFrequency(Ngram("aquas")) } returns 0.0
        }

        with (fivegramLanguageModelForGerman) {
            every { getRelativeFrequency(Ngram("alter")) } returns 0.30
        }
    }

    private fun addLanguageModelsToDetector() {
        with (detectorForEnglishAndGerman) {
            unigramLanguageModels[ENGLISH] = lazy { unigramLanguageModelForEnglish }
            unigramLanguageModels[GERMAN] = lazy { unigramLanguageModelForGerman }

            bigramLanguageModels[ENGLISH] = lazy { bigramLanguageModelForEnglish }
            bigramLanguageModels[GERMAN] = lazy { bigramLanguageModelForGerman }

            trigramLanguageModels[ENGLISH] = lazy { trigramLanguageModelForEnglish }
            trigramLanguageModels[GERMAN] = lazy { trigramLanguageModelForGerman }

            quadrigramLanguageModels[ENGLISH] = lazy { quadrigramLanguageModelForEnglish }
            quadrigramLanguageModels[GERMAN] = lazy { quadrigramLanguageModelForGerman }

            fivegramLanguageModels[ENGLISH] = lazy { fivegramLanguageModelForEnglish }
            fivegramLanguageModels[GERMAN] = lazy { fivegramLanguageModelForGerman }
        }
    }

    private fun defineBehaviorOfTestDataLanguageModels() {
        with (unigramTestDataLanguageModel) {
            every { ngrams } returns setOf(
                Ngram("a"),
                Ngram("l"),
                Ngram("t"),
                Ngram("e"),
                Ngram("r")
            )
        }

        with (quadrigramTestDataLanguageModel) {
            every { ngrams } returns setOf(
                Ngram("alte"),
                Ngram("lter"),
                Ngram("wxyz")
            )
        }
    }

    private fun identifiedLanguageProvider() = listOf(
        arguments("والموضوع", UNKNOWN),
        arguments("сопротивление", UNKNOWN),
        arguments("house", UNKNOWN),
        arguments("hashemidëve", ALBANIAN),
        arguments("məhərrəm", AZERBAIJANI),
        arguments("substituïts", CATALAN),
        arguments("rozdělit", CZECH),
        arguments("tvořen", CZECH),
        arguments("subjektů", CZECH),
        arguments("nesufiĉecon", ESPERANTO),
        arguments("intermiksiĝis", ESPERANTO),
        arguments("monaĥinoj", ESPERANTO),
        arguments("kreitaĵoj", ESPERANTO),
        arguments("ŝpinante", ESPERANTO),
        arguments("apenaŭ", ESPERANTO),
        arguments("groß", GERMAN),
        arguments("σχέδια", GREEK),
        arguments("fekvő", HUNGARIAN),
        arguments("meggyűrűzni", HUNGARIAN),
        arguments("ヴェダイkqヤモンド", JAPANESE),
        arguments("әлем", KAZAKH),
        arguments("шаруашылығы", KAZAKH),
        arguments("ақын", KAZAKH),
        arguments("оның", KAZAKH),
        arguments("шұрайлы", KAZAKH),
        arguments("aizklātā", LATVIAN),
        arguments("sistēmas", LATVIAN),
        arguments("teoloģiska", LATVIAN),
        arguments("palīdzi", LATVIAN),
        arguments("blaķene", LATVIAN),
        arguments("ceļojumiem", LATVIAN),
        arguments("numuriņu", LATVIAN),
        arguments("mergelės", LITHUANIAN),
        arguments("įrengus", LITHUANIAN),
        arguments("slegiamų", LITHUANIAN),
        arguments("припаѓа", MACEDONIAN),
        arguments("ѕидови", MACEDONIAN),
        arguments("ќерка", MACEDONIAN),
        arguments("џамиите", MACEDONIAN),
        arguments("मिळते", MARATHI),
        arguments("үндсэн", MONGOLIAN),
        arguments("дөхөж", MONGOLIAN),
        arguments("zmieniły", POLISH),
        arguments("państwowych", POLISH),
        arguments("mniejszości", POLISH),
        arguments("groźne", POLISH),
        arguments("ialomiţa", ROMANIAN),
        arguments("наслеђивања", SERBIAN),
        arguments("неисквареношћу", SERBIAN),
        arguments("podĺa", SLOVAK),
        arguments("pohľade", SLOVAK),
        arguments("mŕtvych", SLOVAK),
        arguments("ґрунтовому", UKRAINIAN),
        arguments("пропонує", UKRAINIAN),
        arguments("пристрої", UKRAINIAN),
        arguments("cằm", VIETNAMESE),
        arguments("thần", VIETNAMESE),
        arguments("chẳng", VIETNAMESE),
        arguments("quẩy", VIETNAMESE),
        arguments("sẵn", VIETNAMESE),
        arguments("nhẫn", VIETNAMESE),
        arguments("dắt", VIETNAMESE),
        arguments("chất", VIETNAMESE),
        arguments("đạp", VIETNAMESE),
        arguments("mặn", VIETNAMESE),
        arguments("hậu", VIETNAMESE),
        arguments("hiền", VIETNAMESE),
        arguments("lẻn", VIETNAMESE),
        arguments("biểu", VIETNAMESE),
        arguments("kẽm", VIETNAMESE),
        arguments("diễm", VIETNAMESE),
        arguments("phế", VIETNAMESE),
        arguments("nhẹn", VIETNAMESE),
        arguments("việc", VIETNAMESE),
        arguments("chỉnh", VIETNAMESE),
        arguments("trĩ", VIETNAMESE),
        arguments("ravị", VIETNAMESE),
        arguments("thơ", VIETNAMESE),
        arguments("nguồn", VIETNAMESE),
        arguments("thờ", VIETNAMESE),
        arguments("sỏi", VIETNAMESE),
        arguments("tổng", VIETNAMESE),
        arguments("nhở", VIETNAMESE),
        arguments("mỗi", VIETNAMESE),
        arguments("bỡi", VIETNAMESE),
        arguments("tốt", VIETNAMESE),
        arguments("giới", VIETNAMESE),
        arguments("chọn", VIETNAMESE),
        arguments("một", VIETNAMESE),
        arguments("hợp", VIETNAMESE),
        arguments("hưng", VIETNAMESE),
        arguments("từng", VIETNAMESE),
        arguments("của", VIETNAMESE),
        arguments("sử", VIETNAMESE),
        arguments("cũng", VIETNAMESE),
        arguments("những", VIETNAMESE),
        arguments("chức", VIETNAMESE),
        arguments("dụng", VIETNAMESE),
        arguments("thực", VIETNAMESE),
        arguments("kỳ", VIETNAMESE),
        arguments("kỷ", VIETNAMESE),
        arguments("mỹ", VIETNAMESE),
        arguments("mỵ", VIETNAMESE)
    )

    private fun filteredLanguagesProvider() = listOf(
        arguments("والموضوع", listOf(ARABIC, PERSIAN, URDU)),
        arguments("сопротивление", listOf(BELARUSIAN, BULGARIAN, KAZAKH, MACEDONIAN, MONGOLIAN, RUSSIAN, SERBIAN, UKRAINIAN)),
        arguments("раскрывае", listOf(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN)),
        arguments("этот", listOf(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN)),
        arguments("огнём", listOf(BELARUSIAN, KAZAKH, MONGOLIAN, RUSSIAN)),
        arguments("плаваща", listOf(BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN)),
        arguments("довършат", listOf(BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN)),
        arguments("хвалить", listOf(BELARUSIAN, BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN, UKRAINIAN)),
        arguments("людях", listOf(BELARUSIAN, BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN, UKRAINIAN)),
        arguments("десятков", listOf(BELARUSIAN, BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN, UKRAINIAN)),
        arguments("толстой", listOf(BELARUSIAN, BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN, UKRAINIAN)),
        arguments("очень", listOf(BELARUSIAN, BULGARIAN, KAZAKH, MONGOLIAN, RUSSIAN, UKRAINIAN)),
        arguments("павінен", listOf(BELARUSIAN, KAZAKH, UKRAINIAN)),
        arguments("затоплување", listOf(MACEDONIAN, SERBIAN)),
        arguments("ректасцензија", listOf(MACEDONIAN, SERBIAN)),
        arguments("набљудувач", listOf(MACEDONIAN, SERBIAN)),
        arguments("prihvaćanju", listOf(BOSNIAN, CROATIAN, POLISH)),
        arguments("nađete", listOf(BOSNIAN, CROATIAN, VIETNAMESE)),
        arguments("visão", listOf(PORTUGUESE, VIETNAMESE)),
        arguments("wystąpią", listOf(LITHUANIAN, POLISH)),
        arguments("budowę", listOf(LITHUANIAN, POLISH)),
        arguments("nebūsime", listOf(LATVIAN, LITHUANIAN)),
        arguments("afişate", listOf(AZERBAIJANI, ROMANIAN, TURKISH)),
        arguments("kradzieżami", listOf(POLISH, ROMANIAN)),
        arguments("înviat", listOf(FRENCH, ROMANIAN)),
        arguments("venerdì", listOf(ITALIAN, VIETNAMESE)),
        arguments("años", listOf(BASQUE, SPANISH)),
        arguments("rozohňuje", listOf(CZECH, SLOVAK)),
        arguments("rtuť", listOf(CZECH, SLOVAK)),
        arguments("pregătire", listOf(ROMANIAN, VIETNAMESE)),
        arguments("jeďte", listOf(CZECH, ROMANIAN, SLOVAK)),
        arguments("minjaverðir", listOf(ICELANDIC, LATVIAN, TURKISH)),
        arguments("þagnarskyldu", listOf(ICELANDIC, LATVIAN, TURKISH)),
        arguments("nebûtu", listOf(FRENCH, HUNGARIAN, LATVIAN)),
        arguments("forêt", listOf(AFRIKAANS, FRENCH, PORTUGUESE, VIETNAMESE)),
        arguments("succèdent", listOf(FRENCH, ITALIAN, VIETNAMESE)),
        arguments("où", listOf(FRENCH, ITALIAN, VIETNAMESE)),
        arguments("tõeliseks", listOf(ESTONIAN, HUNGARIAN, PORTUGUESE, VIETNAMESE)),
        arguments("viòiem", listOf(CATALAN, ITALIAN, LATVIAN, VIETNAMESE)),
        arguments("contrôle", listOf(FRENCH, PORTUGUESE, SLOVAK, VIETNAMESE)),
        arguments("direktør", listOf(DANISH, NORWEGIAN)),
        arguments("vývoj", listOf(CZECH, ICELANDIC, SLOVAK, TURKISH, VIETNAMESE)),
        arguments("päralt", listOf(ESTONIAN, FINNISH, GERMAN, SLOVAK, SWEDISH)),
        arguments("labâk", listOf(LATVIAN, PORTUGUESE, ROMANIAN, TURKISH, VIETNAMESE)),
        arguments("pràctiques", listOf(CATALAN, FRENCH, ITALIAN, PORTUGUESE, VIETNAMESE)),
        arguments("überrascht", listOf(AZERBAIJANI, CATALAN, ESTONIAN, GERMAN, HUNGARIAN, TURKISH)),
        arguments("indebærer", listOf(DANISH, ICELANDIC, NORWEGIAN)),
        arguments("måned", listOf(DANISH, NORWEGIAN, SWEDISH)),
        arguments("zaručen", listOf(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE)),
        arguments("zkouškou", listOf(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE)),
        arguments("navržen", listOf(BOSNIAN, CZECH, CROATIAN, LATVIAN, LITHUANIAN, SLOVAK, SLOVENE)),
        arguments("façonnage", listOf(ALBANIAN, AZERBAIJANI, BASQUE, CATALAN, FRENCH, LATVIAN, PORTUGUESE, TURKISH)),
        arguments("höher", listOf(AZERBAIJANI, ESTONIAN, FINNISH, GERMAN, HUNGARIAN, ICELANDIC, SWEDISH, TURKISH)),
        arguments("catedráticos", listOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, VIETNAMESE)),
        arguments("política", listOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, VIETNAMESE)),
        arguments("música", listOf(CATALAN, CZECH, ICELANDIC, IRISH, HUNGARIAN, PORTUGUESE, SLOVAK, VIETNAMESE)),
        arguments("contradicció", listOf(CATALAN, HUNGARIAN, ICELANDIC, IRISH, POLISH, PORTUGUESE, SLOVAK, VIETNAMESE)),
        arguments("només", listOf(CATALAN, CZECH, FRENCH, HUNGARIAN, ICELANDIC, IRISH, ITALIAN, PORTUGUESE, SLOVAK, VIETNAMESE)),
        arguments("house", listOf(AFRIKAANS, ALBANIAN, AZERBAIJANI, BASQUE, BOSNIAN, CATALAN, CROATIAN, CZECH, DANISH,
            DUTCH, ENGLISH, ESPERANTO, ESTONIAN, FINNISH, FRENCH, GERMAN, HUNGARIAN, ICELANDIC, INDONESIAN, IRISH, ITALIAN, LATIN,
            LATVIAN, LITHUANIAN, MALAY, NORWEGIAN, POLISH, PORTUGUESE, ROMANIAN, SLOVAK, SLOVENE, SOMALI, SPANISH,
            SWEDISH, TAGALOG, TURKISH, VIETNAMESE, WELSH
        ))
    )

    private fun ngramProbabilityProvider() = listOf(
        arguments(ENGLISH, Ngram("a"), 0.01),
        arguments(ENGLISH, Ngram("lt"), 0.12),
        arguments(ENGLISH, Ngram("ter"), 0.21),
        arguments(ENGLISH, Ngram("alte"), 0.25),
        arguments(ENGLISH, Ngram("alter"), 0.29),

        arguments(GERMAN, Ngram("t"), 0.08),
        arguments(GERMAN, Ngram("er"), 0.18),
        arguments(GERMAN, Ngram("alt"), 0.22),
        arguments(GERMAN, Ngram("lter"), 0.28),
        arguments(GERMAN, Ngram("alter"), 0.30)
    )

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

    private fun languageProbabilitiesProvider() = listOf(
        arguments(
            unigramTestDataLanguageModel,
            mapOf(
                ENGLISH to ln(0.01) + ln(0.02) + ln(0.03) + ln(0.04) + ln(0.05),
                GERMAN to ln(0.06) + ln(0.07) + ln(0.08) + ln(0.09) + ln(0.1)
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

    private fun probabilitiesListProvider(): List<Arguments> {
        val unigramProbabilities = mapOf(
            ENGLISH to ln(0.01) + ln(0.02) + ln(0.03) + ln(0.04) + ln(0.05),
            GERMAN to ln(0.06) + ln(0.07) + ln(0.08) + ln(0.09) + ln(0.1)
        )
        val bigramProbabilities = mapOf(
            ENGLISH to ln(0.11) + ln(0.12) + ln(0.13) + ln(0.14),
            GERMAN to ln(0.15) + ln(0.16) + ln(0.17) + ln(0.18)
        )
        val trigramProbabilities = mapOf(
            ENGLISH to ln(0.19) + ln(0.2) + ln(0.21),
            GERMAN to ln(0.22) + ln(0.23) + ln(0.24)
        )
        val quadrigramProbabilities = mapOf(
            ENGLISH to ln(0.25) + ln(0.26),
            GERMAN to ln(0.27) + ln(0.28)
        )
        val fivegramProbabilities = mapOf(
            ENGLISH to ln(0.29),
            GERMAN to ln(0.3)
        )

        return listOf(
            arguments(
                listOf(
                    unigramProbabilities,
                    bigramProbabilities,
                    trigramProbabilities,
                    quadrigramProbabilities,
                    fivegramProbabilities
                ),
                GERMAN
            )
        )
    }
}

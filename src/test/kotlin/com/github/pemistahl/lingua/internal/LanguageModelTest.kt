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

package com.github.pemistahl.lingua.internal

import com.github.pemistahl.lingua.api.Language
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.jupiter.api.Test

class LanguageModelTest {

    private val text = """
        These sentences are intended for testing purposes.
        Do not use them in production!
        By the way, they consist of 23 words in total.
        """.toLowerCase().trimIndent()

    private val expectedUnigramAbsoluteFrequencies = mapOf(
        "a" to 3,  "b" to 1,  "c" to 3,  "d" to 5, "e" to 14,
        "f" to 2,  "g" to 1,  "h" to 4,  "i" to 6, "l" to 1,
        "m" to 1,  "n" to 10, "o" to 10, "p" to 3, "r" to 5,
        "s" to 10, "t" to 13, "u" to 3,  "w" to 2, "y" to 3
    ).mapKeys { Ngram(it.key) }

    private val expectedUnigramRelativeFrequencies = mapOf(
        "a" to "3/100",  "b" to "1/100", "c" to "3/100", "d" to "1/20",
        "e" to "7/50",   "f" to "1/50",  "g" to "1/100", "h" to "1/25",
        "i" to "3/50",   "l" to "1/100", "m" to "1/100", "n" to "1/10",
        "o" to "1/10",   "p" to "3/100", "r" to "1/20",  "s" to "1/10",
        "t" to "13/100", "u" to "3/100", "w" to "1/50",  "y" to "3/100"
    )

    private val expectedBigramAbsoluteFrequencies = mapOf(
        "de" to 1, "pr" to 1, "pu" to 1, "do" to 1, "uc" to 1, "ds" to 1,
        "du" to 1, "ur" to 1, "us" to 1, "ed" to 1, "in" to 4, "io" to 1,
        "em" to 1, "en" to 3, "is" to 1, "al" to 1, "es" to 4, "ar" to 1,
        "rd" to 1, "re" to 1, "ey" to 1, "nc" to 1, "nd" to 1, "ay" to 1,
        "ng" to 1, "ro" to 1, "rp" to 1, "no" to 1, "ns" to 1, "nt" to 2,
        "fo" to 1, "wa" to 1, "se" to 4, "od" to 1, "si" to 1, "of" to 1,
        "by" to 1, "wo" to 1, "on" to 2, "st" to 2, "ce" to 1, "or" to 2,
        "os" to 1, "ot" to 2, "co" to 1, "ta" to 1, "ct" to 1, "te" to 3,
        "th" to 4, "ti" to 2, "to" to 1, "he" to 4, "po" to 1
    ).mapKeys { Ngram(it.key) }

    private val expectedBigramRelativeFrequencies = mapOf(
        "de" to "1/5",  "pr" to "1/3",  "pu" to "1/3",  "do" to "1/5",
        "uc" to "1/3",  "ds" to "1/5",  "du" to "1/5",  "ur" to "1/3",
        "us" to "1/3",  "ed" to "1/14", "in" to "2/3",  "io" to "1/6",
        "em" to "1/14", "en" to "3/14", "is" to "1/6",  "al" to "1/3",
        "es" to "2/7",  "ar" to "1/3",  "rd" to "1/5",  "re" to "1/5",
        "ey" to "1/14", "nc" to "1/10", "nd" to "1/10", "ay" to "1/3",
        "ng" to "1/10", "ro" to "1/5",  "rp" to "1/5",  "no" to "1/10",
        "ns" to "1/10", "nt" to "1/5",  "fo" to "1/2",  "wa" to "1/2",
        "se" to "2/5",  "od" to "1/10", "si" to "1/10", "of" to "1/10",
        "by" to "1/1",  "wo" to "1/2",  "on" to "1/5",  "st" to "1/5",
        "ce" to "1/3",  "or" to "1/5",  "os" to "1/10", "ot" to "1/5",
        "co" to "1/3",  "ta" to "1/13", "ct" to "1/3",  "te" to "3/13",
        "th" to "4/13", "ti" to "2/13", "to" to "1/13", "he" to "1/1",
        "po" to "1/3"
    )

    private val expectedTrigramAbsoluteFrequencies = mapOf(
        "rds" to 1, "ose" to 1, "ded" to 1, "con" to 1, "use" to 1,
        "est" to 1, "ion" to 1, "ist" to 1, "pur" to 1, "hem" to 1,
        "hes" to 1, "tin" to 1, "cti" to 1, "wor" to 1, "tio" to 1,
        "ten" to 2, "ota" to 1, "hey" to 1, "tal" to 1, "tes" to 1,
        "uct" to 1, "sti" to 1, "pro" to 1, "odu" to 1, "nsi" to 1,
        "rod" to 1, "for" to 1, "ces" to 1, "nce" to 1, "not" to 1,
        "pos" to 1, "are" to 1, "tot" to 1, "end" to 1, "enc" to 1,
        "sis" to 1, "sen" to 1, "nte" to 2, "ord" to 1, "ses" to 1,
        "ing" to 1, "ent" to 1, "way" to 1, "nde" to 1, "int" to 1,
        "rpo" to 1, "the" to 4, "urp" to 1, "duc" to 1, "ons" to 1,
        "ese" to 1
    ).mapKeys { Ngram(it.key) }

    private val expectedTrigramRelativeFrequencies = mapOf(
        "rds" to "1/1", "ose" to "1/1", "ded" to "1/1", "con" to "1/1",
        "use" to "1/1", "est" to "1/4", "ion" to "1/1", "ist" to "1/1",
        "pur" to "1/1", "hem" to "1/4", "hes" to "1/4", "tin" to "1/2",
        "cti" to "1/1", "wor" to "1/1", "tio" to "1/2", "ten" to "2/3",
        "ota" to "1/2", "hey" to "1/4", "tal" to "1/1", "tes" to "1/3",
        "uct" to "1/1", "sti" to "1/2", "pro" to "1/1", "odu" to "1/1",
        "nsi" to "1/1", "rod" to "1/1", "for" to "1/1", "ces" to "1/1",
        "nce" to "1/1", "not" to "1/1", "pos" to "1/1", "are" to "1/1",
        "tot" to "1/1", "end" to "1/3", "enc" to "1/3", "sis" to "1/1",
        "sen" to "1/4", "nte" to "1/1", "ord" to "1/2", "ses" to "1/4",
        "ing" to "1/4", "ent" to "1/3", "way" to "1/1", "nde" to "1/1",
        "int" to "1/4", "rpo" to "1/1", "the" to "1/1", "urp" to "1/1",
        "duc" to "1/1", "ons" to "1/2", "ese" to "1/4"
    )

    private val expectedQuadrigramAbsoluteFrequencies = mapOf(
        "onsi" to 1, "sist" to 1, "ende" to 1, "ords" to 1, "esti" to 1,
        "oduc" to 1, "nces" to 1, "tenc" to 1, "tend" to 1, "thes" to 1,
        "rpos" to 1, "ting" to 1, "nsis" to 1, "nten" to 2, "tota" to 1,
        "they" to 1, "cons" to 1, "tion" to 1, "prod" to 1, "otal" to 1,
        "test" to 1, "ence" to 1, "pose" to 1, "oses" to 1, "nded" to 1,
        "inte" to 1, "them" to 1, "urpo" to 1, "duct" to 1, "sent" to 1,
        "stin" to 1, "ucti" to 1, "ente" to 1, "purp" to 1, "ctio" to 1,
        "rodu" to 1, "word" to 1, "hese" to 1
    ).mapKeys { Ngram(it.key) }

    private val expectedQuadrigramRelativeFrequencies = mapOf(
        "onsi" to "1/1", "sist" to "1/1", "ende" to "1/1", "ords" to "1/1",
        "esti" to "1/1", "oduc" to "1/1", "nces" to "1/1", "tenc" to "1/2",
        "tend" to "1/2", "thes" to "1/4", "rpos" to "1/1", "ting" to "1/1",
        "nsis" to "1/1", "nten" to "1/1", "tota" to "1/1", "they" to "1/4",
        "cons" to "1/1", "tion" to "1/1", "prod" to "1/1", "otal" to "1/1",
        "test" to "1/1", "ence" to "1/1", "pose" to "1/1", "oses" to "1/1",
        "nded" to "1/1", "inte" to "1/1", "them" to "1/4", "urpo" to "1/1",
        "duct" to "1/1", "sent" to "1/1", "stin" to "1/1", "ucti" to "1/1",
        "ente" to "1/1", "purp" to "1/1", "ctio" to "1/1", "rodu" to "1/1",
        "word" to "1/1", "hese" to "1/1"
    )

    private val expectedFivegramAbsoluteFrequencies = mapOf(
        "testi" to 1, "sente" to 1, "ences" to 1, "tende" to 1,
        "ducti" to 1, "ntenc" to 1, "these" to 1, "onsis" to 1,
        "ntend" to 1, "total" to 1, "uctio" to 1, "enten" to 1,
        "poses" to 1, "ction" to 1, "produ" to 1, "inten" to 1,
        "nsist" to 1, "words" to 1, "sting" to 1, "purpo" to 1,
        "tence" to 1, "estin" to 1, "roduc" to 1, "urpos" to 1,
        "rpose" to 1, "ended" to 1, "oduct" to 1, "consi" to 1
    ).mapKeys { Ngram(it.key) }

    private val expectedFivegramRelativeFrequencies = mapOf(
        "testi" to "1/1", "sente" to "1/1", "ences" to "1/1", "tende" to "1/1",
        "ducti" to "1/1", "ntenc" to "1/2", "these" to "1/1", "onsis" to "1/1",
        "ntend" to "1/2", "total" to "1/1", "uctio" to "1/1", "enten" to "1/1",
        "poses" to "1/1", "ction" to "1/1", "produ" to "1/1", "inten" to "1/1",
        "nsist" to "1/1", "words" to "1/1", "sting" to "1/1", "purpo" to "1/1",
        "tence" to "1/1", "estin" to "1/1", "roduc" to "1/1", "urpos" to "1/1",
        "rpose" to "1/1", "ended" to "1/1", "oduct" to "1/1", "consi" to "1/1"
    )

    @Test
    fun `assert that unigram language model can be created from training data`() {
        val model = TrainingDataLanguageModel.fromText(
            text = text.lineSequence(),
            language = Language.ENGLISH,
            ngramLength = 1,
            charClass = "IsLatin",
            lowerNgramAbsoluteFrequencies = emptyMap()
        )
        val expectedAbsoluteFrequencies = expectedUnigramAbsoluteFrequencies.entries.toTypedArray()
        val expectedRelativeFrequencies = expectedUnigramRelativeFrequencies.map {
            val (numerator, denominator) = it.value.split('/').map { it.toInt() }
            entry(Ngram(it.key), Fraction(numerator, denominator))
        }.toTypedArray()

        assertThat(model.language).isEqualTo(Language.ENGLISH)
        assertThat(model.absoluteFrequencies).containsOnly(*expectedAbsoluteFrequencies)
        assertThat(model.relativeFrequencies).containsOnly(*expectedRelativeFrequencies)
    }

    @Test
    fun `assert that bigram language model can be created from training data`() {
        val model = TrainingDataLanguageModel.fromText(
            text = text.lineSequence(),
            language = Language.ENGLISH,
            ngramLength = 2,
            charClass = "IsLatin",
            lowerNgramAbsoluteFrequencies = expectedUnigramAbsoluteFrequencies
        )
        val expectedAbsoluteFrequencies = expectedBigramAbsoluteFrequencies.entries.toTypedArray()
        val expectedRelativeFrequencies = expectedBigramRelativeFrequencies.map {
            val (numerator, denominator) = it.value.split('/').map { it.toInt() }
            entry(Ngram(it.key), Fraction(numerator, denominator))
        }.toTypedArray()

        assertThat(model.language).isEqualTo(Language.ENGLISH)
        assertThat(model.absoluteFrequencies).containsOnly(*expectedAbsoluteFrequencies)
        assertThat(model.relativeFrequencies).containsOnly(*expectedRelativeFrequencies)
    }

    @Test
    fun `assert that trigram language model can be created from training data`() {
        val model = TrainingDataLanguageModel.fromText(
            text = text.lineSequence(),
            language = Language.ENGLISH,
            ngramLength = 3,
            charClass = "IsLatin",
            lowerNgramAbsoluteFrequencies = expectedBigramAbsoluteFrequencies
        )
        val expectedAbsoluteFrequencies = expectedTrigramAbsoluteFrequencies.entries.toTypedArray()
        val expectedRelativeFrequencies = expectedTrigramRelativeFrequencies.map {
            val (numerator, denominator) = it.value.split('/').map { it.toInt() }
            entry(Ngram(it.key), Fraction(numerator, denominator))
        }.toTypedArray()

        assertThat(model.language).isEqualTo(Language.ENGLISH)
        assertThat(model.absoluteFrequencies).containsOnly(*expectedAbsoluteFrequencies)
        assertThat(model.relativeFrequencies).containsOnly(*expectedRelativeFrequencies)
    }

    @Test
    fun `assert that quadrigram language model can be created from training data`() {
        val model = TrainingDataLanguageModel.fromText(
            text = text.lineSequence(),
            language = Language.ENGLISH,
            ngramLength = 4,
            charClass = "IsLatin",
            lowerNgramAbsoluteFrequencies = expectedTrigramAbsoluteFrequencies
        )
        val expectedAbsoluteFrequencies = expectedQuadrigramAbsoluteFrequencies.entries.toTypedArray()
        val expectedRelativeFrequencies = expectedQuadrigramRelativeFrequencies.map {
            val (numerator, denominator) = it.value.split('/').map { it.toInt() }
            entry(Ngram(it.key), Fraction(numerator, denominator))
        }.toTypedArray()

        assertThat(model.language).isEqualTo(Language.ENGLISH)
        assertThat(model.absoluteFrequencies).containsOnly(*expectedAbsoluteFrequencies)
        assertThat(model.relativeFrequencies).containsOnly(*expectedRelativeFrequencies)
    }

    @Test
    fun `assert that fivegram language model can be created from training data`() {
        val model = TrainingDataLanguageModel.fromText(
            text = text.lineSequence(),
            language = Language.ENGLISH,
            ngramLength = 5,
            charClass = "IsLatin",
            lowerNgramAbsoluteFrequencies = expectedQuadrigramAbsoluteFrequencies
        )
        val expectedAbsoluteFrequencies = expectedFivegramAbsoluteFrequencies.entries.toTypedArray()
        val expectedRelativeFrequencies = expectedFivegramRelativeFrequencies.map {
            val (numerator, denominator) = it.value.split('/').map { it.toInt() }
            entry(Ngram(it.key), Fraction(numerator, denominator))
        }.toTypedArray()

        assertThat(model.language).isEqualTo(Language.ENGLISH)
        assertThat(model.absoluteFrequencies).containsOnly(*expectedAbsoluteFrequencies)
        assertThat(model.relativeFrequencies).containsOnly(*expectedRelativeFrequencies)
    }

    @Test
    fun `assert that unigram language model can be created from test data`() {
        val model = TestDataLanguageModel.fromText(text, ngramLength = 1)

        assertThat(model.ngrams).containsExactlyInAnyOrderElementsOf(
            setOf(
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "l",
                "m", "n", "o", "p", "r", "s", "t", "u", "w", "y"
            ).map { Ngram(it) }
        )
    }

    @Test
    fun `assert that bigram language model can be created from test data`() {
        val model = TestDataLanguageModel.fromText(text, ngramLength = 2)

        assertThat(model.ngrams).containsExactlyInAnyOrderElementsOf(
            setOf(
                "de", "pr", "pu", "do", "uc", "ds", "du", "ur", "us", "ed",
                "in", "io", "em", "en", "is", "al", "es", "ar", "rd", "re",
                "ey", "nc", "nd", "ay", "ng", "ro", "rp", "no", "ns", "nt",
                "fo", "wa", "se", "od", "si", "by", "of", "wo", "on", "st",
                "ce", "or", "os", "ot", "co", "ta", "te", "ct", "th", "ti",
                "to", "he", "po"
            ).map { Ngram(it) }
        )
    }

    @Test
    fun `assert that trigram language model can be created from test data`() {
        val model = TestDataLanguageModel.fromText(text, ngramLength = 3)

        assertThat(model.ngrams).containsExactlyInAnyOrderElementsOf(
            setOf(
                "rds", "ose", "ded", "con", "use", "est", "ion", "ist", "pur",
                "hem", "hes", "tin", "cti", "tio", "wor", "ten", "hey", "ota",
                "tal", "tes", "uct", "sti", "pro", "odu", "nsi", "rod", "for",
                "ces", "nce", "not", "are", "pos", "tot", "end", "enc", "sis",
                "sen", "nte", "ses", "ord", "ing", "ent", "int", "nde", "way",
                "the", "rpo", "urp", "duc", "ons", "ese"
            ).map { Ngram(it) }
        )
    }

    @Test
    fun `assert that quadrigram language model can be created from test data`() {
        val model = TestDataLanguageModel.fromText(text, ngramLength = 4)

        assertThat(model.ngrams).containsExactlyInAnyOrderElementsOf(
            setOf(
                "onsi", "sist", "ende", "ords", "esti", "tenc", "nces", "oduc",
                "tend", "thes", "rpos", "ting", "nten", "nsis", "they", "tota",
                "cons", "tion", "prod", "ence", "test", "otal", "pose", "nded",
                "oses", "inte", "urpo", "them", "sent", "duct", "stin", "ente",
                "ucti", "purp", "ctio", "rodu", "word", "hese"
            ).map { Ngram(it) }
        )
    }

    @Test
    fun `assert that fivegram language model can be created from test data`() {
        val model = TestDataLanguageModel.fromText(text, ngramLength = 5)

        assertThat(model.ngrams).containsExactlyInAnyOrderElementsOf(
            setOf(
                "testi", "sente", "ences", "tende", "these", "ntenc", "ducti",
                "ntend", "onsis", "total", "uctio", "enten", "poses", "ction",
                "produ", "inten", "nsist", "words", "sting", "tence", "purpo",
                "estin", "roduc", "urpos", "ended", "rpose", "oduct", "consi"
            ).map { Ngram(it) }
        )
    }

    @Test
    fun `assert that unigram language model is correctly written to json`() {
        val model = TrainingDataLanguageModel.fromText(
            text = text.lineSequence(),
            language = Language.ENGLISH,
            ngramLength = 1,
            charClass = "IsLatin",
            lowerNgramAbsoluteFrequencies = emptyMap()
        )
        assertThat(model.toJson()).isEqualTo("""
        {"language":"ENGLISH","ngrams":{"3/100":"a c p u y","1/100":"b g l m","1/20":"d r","7/50":"e","1/50":"f w","1/25":"h","3/50":"i","1/10":"n o s","13/100":"t"}}
        """.trimIndent())
    }
}

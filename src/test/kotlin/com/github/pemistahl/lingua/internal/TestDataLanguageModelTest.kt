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

package com.github.pemistahl.lingua.internal

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

class TestDataLanguageModelTest {

    private val text =
        """
        These sentences are intended for testing purposes.
        Do not use them in production!
        By the way, they consist of 23 words in total.
        """.lowercase().trimIndent()

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
}

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

package com.github.pemistahl.lingua.internal.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.assertj.core.api.Assertions.assertThatIllegalStateException
import org.junit.jupiter.api.Test

class NgramTest {

    private val zerogram = Ngram("")
    private val unigram = Ngram("q")
    private val bigram = Ngram("qw")
    private val trigram = Ngram("qwe")
    private val quadrigram = Ngram("qwer")
    private val fivegram = Ngram("qwert")
    private val ngrams = listOf(unigram, bigram, trigram, quadrigram, fivegram)

    @Test
    fun `assert that toString() implementation of Ngram is its value`() {
        assertThat(fivegram.toString()).isEqualTo("qwert")
        assertThat(quadrigram.toString()).isEqualTo("qwer")
        assertThat(trigram.toString()).isEqualTo("qwe")
        assertThat(bigram.toString()).isEqualTo("qw")
        assertThat(unigram.toString()).isEqualTo("q")
        assertThat(zerogram.toString()).isEqualTo("")
    }

    @Test
    fun `assert that Ngram comparisons work correctly`() {
        val comparisons = listOf(
            fivegram > quadrigram,
            fivegram > trigram,
            fivegram > bigram,
            fivegram > unigram,
            fivegram > zerogram,

            quadrigram > trigram,
            quadrigram > bigram,
            quadrigram > unigram,
            quadrigram > zerogram,

            trigram > bigram,
            trigram > unigram,
            trigram > zerogram,

            bigram > unigram,
            bigram > zerogram,

            unigram > zerogram,

            quadrigram < fivegram,
            trigram < fivegram,
            bigram < fivegram,
            unigram < fivegram,
            zerogram < fivegram,

            trigram < quadrigram,
            bigram < quadrigram,
            unigram < quadrigram,
            zerogram < trigram,

            bigram < trigram,
            unigram < trigram,
            zerogram < trigram,

            unigram < bigram,
            zerogram < bigram,

            zerogram < unigram
        )

        for (comparison in comparisons) {
            assertThat(comparison).isTrue()
        }
    }

    @Test
    fun `assert that Fivegrams can be decremented correctly`() {
        val quadrigram = fivegram.dec()
        assertThat(quadrigram).isEqualTo(this.quadrigram)

        val trigram = quadrigram.dec()
        assertThat(trigram).isEqualTo(this.trigram)

        val bigram = trigram.dec()
        assertThat(bigram).isEqualTo(this.bigram)

        val unigram = bigram.dec()
        assertThat(unigram).isEqualTo(this.unigram)

        val zerogram = unigram.dec()
        assertThat(zerogram).isEqualTo(zerogram)

        assertThatIllegalStateException().isThrownBy {
            zerogram.dec()
        }.withMessage(
            "Zerogram is ngram type of lowest order and can not be decremented"
        )
    }

    @Test
    fun `assert that NgramRange works correctly`() {
        val ngramRange = NgramRange(fivegram, bigram)

        with (ngramRange) {
            assertThat(contains(fivegram)).isTrue()
            assertThat(contains(quadrigram)).isTrue()
            assertThat(contains(trigram)).isTrue()
            assertThat(contains(bigram)).isTrue()

            assertThat(contains(unigram)).isFalse()
            assertThat(contains(zerogram)).isFalse()

            assertThat(iterator()).isEqualTo(NgramIterator(fivegram))
        }

        assertThatIllegalArgumentException().isThrownBy {
            NgramRange(bigram, fivegram)
        }.withMessage("'qw' must be of higher order than 'qwert'")
    }

    @Test
    fun `assert that range of lower order ngrams can be generated correctly`() {
        for (ngram in ngrams) {
            assertThat(
                ngram.rangeOfLowerOrderNgrams()
            ).isEqualTo(NgramRange(ngram, unigram))
        }
    }

    @Test
    fun `assert that NgramIterator works correctly`() {
        val iterator = NgramIterator(fivegram)

        with (iterator) {
            assertThat(next()).isEqualTo(fivegram)
            assertThat(next()).isEqualTo(quadrigram)
            assertThat(next()).isEqualTo(trigram)
            assertThat(next()).isEqualTo(bigram)
            assertThat(next()).isEqualTo(unigram)

            assertThatExceptionOfType(
                NoSuchElementException::class.java
            ).isThrownBy {
                next()
            }
        }
    }
}

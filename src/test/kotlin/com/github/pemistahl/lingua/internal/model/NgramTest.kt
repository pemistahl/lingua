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

    private val unigram = Unigram("q")
    private val bigram = Bigram("qw")
    private val trigram = Trigram("qwe")
    private val quadrigram = Quadrigram("qwer")
    private val fivegram = Fivegram("qwert")
    private val ngrams = listOf(unigram, bigram, trigram, quadrigram, fivegram)

    @Test
    fun `assert that Unigram is of correct length`() {
        assertThat(unigram.value).isEqualTo("q")
        assertThatIllegalArgumentException()
            .isThrownBy { Unigram("qw") }
            .withMessage("value 'qw' must be of length 1 for type Unigram but is 2")
    }

    @Test
    fun `assert that Bigram is of correct length`() {
        assertThat(bigram.value).isEqualTo("qw")
        assertThatIllegalArgumentException()
            .isThrownBy { Bigram("q") }
            .withMessage("value 'q' must be of length 2 for type Bigram but is 1")
    }

    @Test
    fun `assert that Trigram is of correct length`() {
        assertThat(trigram.value).isEqualTo("qwe")
        assertThatIllegalArgumentException()
            .isThrownBy { Trigram("qwer") }
            .withMessage("value 'qwer' must be of length 3 for type Trigram but is 4")
    }

    @Test
    fun `assert that Quadrigram is of correct length`() {
        assertThat(Quadrigram("qwer").value).isEqualTo("qwer")
        assertThatIllegalArgumentException()
            .isThrownBy { Quadrigram("qwe") }
            .withMessage("value 'qwe' must be of length 4 for type Quadrigram but is 3")
    }

    @Test
    fun `assert that Fivegram is of correct length`() {
        assertThat(Fivegram("qwert").value).isEqualTo("qwert")
        assertThatIllegalArgumentException()
            .isThrownBy { Fivegram("") }
            .withMessage("value '' must be of length 5 for type Fivegram but is 0")
    }

    @Test
    fun `assert that toString() implementation of Ngram is its value`() {
        assertThat(fivegram.toString()).isEqualTo("qwert")
        assertThat(quadrigram.toString()).isEqualTo("qwer")
        assertThat(trigram.toString()).isEqualTo("qwe")
        assertThat(bigram.toString()).isEqualTo("qw")
        assertThat(unigram.toString()).isEqualTo("q")
        assertThat(Zerogram.toString()).isEqualTo("")
    }

    @Test
    fun `assert that Ngram equality checks work correctly`() {
        assertThat(fivegram).isEqualTo(Fivegram("qwert"))
        assertThat(fivegram).isNotEqualTo(Fivegram("abcde"))
        assertThat(fivegram).isNotEqualTo(Zerogram)

        assertThat(quadrigram).isEqualTo(Quadrigram("qwer"))
        assertThat(quadrigram).isNotEqualTo(Quadrigram("abcd"))
        assertThat(quadrigram).isNotEqualTo(fivegram)
        assertThat(quadrigram).isNotEqualTo(Zerogram)

        assertThat(trigram).isEqualTo(Trigram("qwe"))
        assertThat(trigram).isNotEqualTo(Trigram("abc"))
        assertThat(trigram).isNotEqualTo(quadrigram)
        assertThat(trigram).isNotEqualTo(fivegram)
        assertThat(trigram).isNotEqualTo(Zerogram)

        assertThat(bigram).isEqualTo(Bigram("qw"))
        assertThat(bigram).isNotEqualTo(Bigram("ab"))
        assertThat(bigram).isNotEqualTo(trigram)
        assertThat(bigram).isNotEqualTo(quadrigram)
        assertThat(bigram).isNotEqualTo(fivegram)
        assertThat(bigram).isNotEqualTo(Zerogram)

        assertThat(unigram).isEqualTo(Unigram("q"))
        assertThat(unigram).isNotEqualTo(Unigram("a"))
        assertThat(unigram).isNotEqualTo(bigram)
        assertThat(unigram).isNotEqualTo(trigram)
        assertThat(unigram).isNotEqualTo(quadrigram)
        assertThat(unigram).isNotEqualTo(fivegram)
        assertThat(unigram).isNotEqualTo(Zerogram)
    }

    @Test
    fun `assert that Ngram comparisons work correctly`() {
        val comparisons = listOf(
            fivegram > quadrigram,
            fivegram > trigram,
            fivegram > bigram,
            fivegram > unigram,
            fivegram > Zerogram,

            quadrigram > trigram,
            quadrigram > bigram,
            quadrigram > unigram,
            quadrigram > Zerogram,

            trigram > bigram,
            trigram > unigram,
            trigram > Zerogram,

            bigram > unigram,
            bigram > Zerogram,

            unigram > Zerogram,

            quadrigram < fivegram,
            trigram < fivegram,
            bigram < fivegram,
            unigram < fivegram,
            Zerogram < fivegram,

            trigram < quadrigram,
            bigram < quadrigram,
            unigram < quadrigram,
            Zerogram < trigram,

            bigram < trigram,
            unigram < trigram,
            Zerogram < trigram,

            unigram < bigram,
            Zerogram < bigram,

            Zerogram < unigram
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
        assertThat(zerogram).isEqualTo(Zerogram)

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
            assertThat(contains(Zerogram)).isFalse()

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

    @Test
    fun `assert that Ngram length can be determined correctly`() {
        assertThat(Ngram.getLength(Zerogram::class)).isEqualTo(0)
        assertThat(Ngram.getLength(Unigram::class)).isEqualTo(1)
        assertThat(Ngram.getLength(Bigram::class)).isEqualTo(2)
        assertThat(Ngram.getLength(Trigram::class)).isEqualTo(3)
        assertThat(Ngram.getLength(Quadrigram::class)).isEqualTo(4)
        assertThat(Ngram.getLength(Fivegram::class)).isEqualTo(5)
    }

    @Test
    fun `assert that Ngram instance can be created correctly`() {
        assertThat(Ngram.getInstance("")).isEqualTo(Zerogram)
        assertThat(Ngram.getInstance("q")).isEqualTo(unigram)
        assertThat(Ngram.getInstance("qw")).isEqualTo(bigram)
        assertThat(Ngram.getInstance("qwe")).isEqualTo(trigram)
        assertThat(Ngram.getInstance("qwer")).isEqualTo(quadrigram)
        assertThat(Ngram.getInstance("qwert")).isEqualTo(fivegram)

        assertThatIllegalArgumentException().isThrownBy {
            Ngram.getInstance("qwertz")
        }.withMessage("unsupported ngram length: 6")
    }
}

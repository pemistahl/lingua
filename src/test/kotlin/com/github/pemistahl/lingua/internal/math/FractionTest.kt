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

package com.github.pemistahl.lingua.internal.math

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType

class FractionTest {

    private val fraction1 = Fraction(12, 144)
    private val fraction2 = Fraction(63, 27)
    private val fraction3 = Fraction(0, 1234)
    private val fraction4 = Fraction(-42, 210)
    private val fraction5 = Fraction(169, -65)

    @Test
    fun `assert that Fraction is correctly reduced to lowest terms`() {
        assertThat(fraction1).isEqualTo(Fraction(1, 12))
        assertThat(fraction2).isEqualTo(Fraction(7, 3))
        assertThat(fraction3).isEqualTo(Fraction(0, 1))
        assertThat(fraction4).isEqualTo(Fraction(-1, 5))
        assertThat(fraction5).isEqualTo(Fraction(-13, 5))
    }

    @Test
    fun `assert that Fraction with denominator zero can not be created`() {
        assertThatExceptionOfType(ArithmeticException::class.java).isThrownBy {
            Fraction(1234, 0)
        }.withMessage("zero denominator in fraction '1234/0'")
    }

    @Test
    fun `assert that toString() implementation of Fraction is correct`() {
        assertThat(fraction1.toString()).isEqualTo("1/12")
        assertThat(fraction2.toString()).isEqualTo("7/3")
        assertThat(fraction3.toString()).isEqualTo("0/1")
        assertThat(fraction4.toString()).isEqualTo("-1/5")
        assertThat(fraction5.toString()).isEqualTo("-13/5")
    }

    @Test
    fun `assert that toDouble() implementation of Fraction is correct`() {
        assertThat(fraction1.toDouble()).isEqualTo(1.0 / 12)
        assertThat(fraction2.toDouble()).isEqualTo(7.0 / 3)
        assertThat(fraction3.toDouble()).isEqualTo(0.0)
        assertThat(fraction4.toDouble()).isEqualTo(-0.2)
        assertThat(fraction5.toDouble()).isEqualTo(-2.6)
    }

    @Test
    fun `assert that Fraction comparisons work correctly`() {
        val comparisons = listOf(
            fraction1 > fraction3,
            fraction1 > fraction4,
            fraction1 > fraction5,
            fraction2 > fraction1,
            fraction2 > fraction3,
            fraction2 > fraction4,
            fraction2 > fraction5,
            fraction3 > fraction4,
            fraction3 > fraction5,
            fraction4 > fraction5,

            fraction1 < fraction2,
            fraction3 < fraction1,
            fraction3 < fraction2,
            fraction4 < fraction1,
            fraction4 < fraction2,
            fraction4 < fraction3,
            fraction5 < fraction1,
            fraction5 < fraction2,
            fraction5 < fraction3,
            fraction5 < fraction4
        )

        for (comparison in comparisons) {
            assertThat(comparison).isTrue()
        }
    }
}

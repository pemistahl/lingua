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

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
internal data class Fraction(
    var numerator: Int,
    var denominator: Int
) : Number(), Comparable<Fraction> {

    init {
        val (num, den) = reduceToLowestTerms(numerator, denominator)
        this.numerator = num
        this.denominator = den
    }

    override fun compareTo(other: Fraction): Int {
        val n0d = numerator.toLong() * other.denominator
        val d0n = denominator.toLong() * other.numerator
        return when {
            n0d < d0n -> -1
            n0d > d0n ->  1
            else      ->  0
        }
    }

    override fun toString() = "$numerator/$denominator"

    override fun toByte() = toDouble().toByte()

    override fun toChar() = toDouble().toChar()

    override fun toDouble() = numerator.toDouble() / denominator.toDouble()

    override fun toFloat() = toDouble().toFloat()

    override fun toInt() = toDouble().toInt()

    override fun toLong() = toDouble().toLong()

    override fun toShort() = toDouble().toShort()

    private fun reduceToLowestTerms(numerator: Int, denominator: Int): Pair<Int, Int> {
        var num = numerator
        var den = denominator

        if (den == 0) throw ArithmeticException("zero denominator in fraction '$num/$den'")
        if (den < 0) {
            if (num == Int.MIN_VALUE || den == Int.MIN_VALUE) {
                throw ArithmeticException("overflow in fraction $this, cannot negate")
            }
            num = -num
            den = -den
        }

        val gcd = greatestCommonDenominator(num, den)

        if (gcd > 1) {
            num /= gcd
            den /= gcd
        }
        if (den < 0) {
            num = -num
            den = -den
        }

        return Pair(num, den)
    }

    private fun greatestCommonDenominator(a: Int, b: Int): Int {
        if (a == 0 || b == 0) {
            if (a == Int.MIN_VALUE || b == Int.MIN_VALUE) {
                throw ArithmeticException("overflow: greatestCommonDenominator($a, $b) is 2^31")
            }
            return abs(a + b)
        }

        var x = a
        var y = b
        var xl = x.toLong()
        var yl = y.toLong()
        var useLong = false

        if (x < 0) {
            if (x == Int.MIN_VALUE) useLong = true
            else x = -x
            xl = -xl
        }
        if (y < 0) {
            if (y == Int.MIN_VALUE) useLong = true
            else y = -y
            yl = -yl
        }
        if (useLong) {
            if (xl == yl) throw ArithmeticException("overflow: greatestCommonDenominator($a, $b) is 2^31")
            var ylyu = yl
            yl = xl
            xl = ylyu % xl
            if (xl == 0L) {
                if (yl > Int.MAX_VALUE) throw ArithmeticException("overflow: greatestCommonDenominator($a, $b) is 2^31")
                return yl.toInt()
            }
            ylyu = yl

            y = xl.toInt()
            x = (ylyu % xl).toInt()
        }

        return greatestCommonDivisor(x, y)
    }

    private fun greatestCommonDivisor(a: Int, b: Int): Int {
        assert(a >= 0)
        assert(b >= 0)

        if (a == 0) return b
        if (b == 0) return a

        var x = a
        var y = b

        val xTwos = numberOfTrailingZeros(x)
        val yTwos = numberOfTrailingZeros(y)
        val shift = Math.min(xTwos, yTwos)

        x = x shr xTwos
        y = y shr yTwos

        while (x != y) {
            val delta = x - y
            y = Math.min(x, y)
            x = Math.abs(delta)
            x = x shr numberOfTrailingZeros(x)
        }

        return x shl shift
    }

    private fun numberOfTrailingZeros(i: Int): Int {
        if (i == 0) return 32

        var j = i
        var n = 31

        var y = j shl 16; if (y != 0) { n -= 16; j = y }
        y = j shl 8; if (y != 0) { n -= 8; j = y }
        y = j shl 4; if (y != 0) { n -= 4; j = y }
        y = j shl 2; if (y != 0) { n -= 2; j = y }

        return n - (j shl 1).ushr(31)
    }

    private fun abs(x: Int): Int {
        val i = x.ushr(31)
        return (x xor i.inv() + 1) + i
    }

    @Serializer(forClass = Fraction::class)
    companion object : KSerializer<Fraction> {
        override val descriptor = PrimitiveDescriptor("Fraction", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Fraction) {
            encoder.encodeString(value.toString())
        }

        override fun deserialize(decoder: Decoder): Fraction {
            val (numerator, denominator) = decoder.decodeString().split('/')
            return Fraction(numerator.toInt(), denominator.toInt())
        }
    }
}

/*
 * Copyright 2018 Peter M. Stahl
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

package com.github.pemistahl.lingua.math

import java.io.Serializable
import java.math.BigInteger

class Fraction : Number, Comparable<Fraction>, Serializable {

    val numerator: Int
    val denominator: Int

    constructor(numerator: Int, denominator: Int) {
        val (num, den) = reduceToLowestTerms(numerator, denominator)
        this.numerator = num
        this.denominator = den
    }

    constructor(numerator: Int) : this(numerator, 1)

    constructor(fraction: String) {
        val matchResult = FRACTION_REGEX.matchEntire(fraction)
            ?: throw IllegalArgumentException("'$fraction' does not represent a valid fraction")
        val (numerator, denominator) = matchResult.destructured
        val (num, den) = reduceToLowestTerms(numerator.toInt(), denominator.toInt())
        this.numerator = num
        this.denominator = den
    }

    fun abs() = if (numerator >= 0) this else negate()

    fun negate(): Fraction {
        if (numerator == Int.MIN_VALUE) {
            throw ArithmeticException(
                "overflow in fraction $this, cannot negate"
            )
        }
        return Fraction(-numerator, denominator)
    }

    fun reciprocal() = Fraction(denominator, numerator)

    fun percentage() = 100 * toDouble()

    operator fun plus(i: Int) = Fraction(numerator + i * denominator, denominator)

    operator fun plus(other: Fraction) = addOrSubtract(other, isAddition = true)

    operator fun minus(i: Int) = Fraction(numerator - i * denominator, denominator)

    operator fun minus(other: Fraction) = addOrSubtract(other, isAddition = false)

    operator fun times(other: Fraction): Fraction {
        if (numerator == 0 || other.numerator == 0) return ZERO
        val gcd1 = greatestCommonDenominator(numerator, other.denominator)
        val gcd2 = greatestCommonDenominator(other.numerator, denominator)
        return getReducedFrationAfterMultiplication(
            multiplyAndCheckForOverflow(numerator / gcd1, other.numerator / gcd2),
            multiplyAndCheckForOverflow(denominator / gcd2, other.denominator / gcd1)
        )
    }

    operator fun times(i: Int) = times(Fraction(i))

    operator fun div(other: Fraction): Fraction {
        if (other.numerator == 0) throw ArithmeticException("the fraction to divide by must not be zero: $other")
        return times(other.reciprocal())
    }

    operator fun div(i: Int) = div(Fraction(i))

    override fun compareTo(other: Fraction): Int {
        val n0d = numerator.toLong() * other.denominator
        val d0n = denominator.toLong() * other.numerator
        return when {
            n0d < d0n -> -1
            n0d > d0n -> 1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is Fraction) {
            return (numerator == other.numerator) &&
                (denominator == other.denominator)
        }
        return false
    }

    override fun hashCode() = 37 * (37 * 17 + numerator) + denominator

    override fun toString() = "$numerator/$denominator"

    override fun toByte() = toDouble().toByte()

    override fun toChar() = toDouble().toChar()

    override fun toDouble() = numerator.toDouble() / denominator.toDouble()

    override fun toFloat() = toDouble().toFloat()

    override fun toInt() = toDouble().toInt()

    override fun toLong() = toDouble().toLong()

    override fun toShort() = toDouble().toShort()

    private fun addOrSubtract(other: Fraction, isAddition: Boolean): Fraction {
        if (numerator == 0) {
            return if (isAddition) other else other.negate()
        }
        if (other.numerator == 0) return this

        val gcd = greatestCommonDenominator(denominator, other.denominator)
        if (gcd == 1) {
            val uvp = multiplyAndCheckForOverflow(numerator, other.denominator)
            val upv = multiplyAndCheckForOverflow(other.numerator, denominator)
            val num = if (isAddition) addAndCheckForOverflow(uvp, upv)
            else subtractAndCheckForOverflow(uvp, upv)
            val den = multiplyAndCheckForOverflow(denominator, other.denominator)
            return Fraction(num, den)
        }

        val uvp = BigInteger.valueOf(numerator.toLong())
            .multiply(BigInteger.valueOf(other.denominator.toLong() / gcd))
        val upv = BigInteger.valueOf(other.numerator.toLong())
            .multiply(BigInteger.valueOf(denominator.toLong() / gcd))
        val t = if (isAddition) uvp.add(upv) else uvp.subtract(upv)
        val tModGcd = t.mod(BigInteger.valueOf(gcd.toLong())).toInt()
        val gcd2 = if (tModGcd == 0) gcd else greatestCommonDenominator(tModGcd, gcd)

        val w = t.divide(BigInteger.valueOf(gcd2.toLong()))
        if (w.bitLength() > 31) throw ArithmeticException("overflow, numerator too large after multiplication: $w")
        return Fraction(w.toInt(), multiplyAndCheckForOverflow(denominator / gcd, other.denominator / gcd2))
    }

    private fun addAndCheckForOverflow(x: Int, y: Int): Int {
        val z = x.toLong() + y.toLong()
        if (z < Int.MIN_VALUE || z > Int.MAX_VALUE) {
            throw ArithmeticException("overflow in addition: $x + $y")
        }
        return z.toInt()
    }

    private fun subtractAndCheckForOverflow(x: Int, y: Int): Int {
        val z = x.toLong() - y.toLong()
        if (z < Int.MIN_VALUE || z > Int.MAX_VALUE) {
            throw ArithmeticException("overflow in subtraction: $x - $y")
        }
        return z.toInt()
    }

    private fun multiplyAndCheckForOverflow(x: Int, y: Int): Int {
        val z = x.toLong() * y.toLong()
        if (z < Int.MIN_VALUE || z > Int.MAX_VALUE) {
            throw ArithmeticException("overflow in multiplication: $x * $y")
        }
        return z.toInt()
    }

    private fun reduceToLowestTerms(numerator: Int, denominator: Int): Pair<Int, Int> {
        var num = numerator
        var den = denominator

        if (den == 0) throw ArithmeticException("zero denominator in fraction $this")
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

    private fun getReducedFrationAfterMultiplication(numerator: Int, denominator: Int): Fraction {
        var num = numerator
        var den = denominator

        if (den == 0) throw ArithmeticException("zero denominator in $this")
        if (num == 0) return ZERO
        if (den == Int.MIN_VALUE && (num and 1) == 0) {
            num /= 2
            den /= 2
        }
        if (den < 0) {
            if (num == Int.MIN_VALUE || den == Int.MIN_VALUE) {
                throw ArithmeticException("overflow in $this, cannot negate")
            }
            num = -num
            den = -den
        }
        val gcd = greatestCommonDenominator(num, den)
        num /= gcd
        den /= gcd
        return Fraction(num, den)
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

        var y = j shl 16; if (y != 0) {
            n -= 16; j = y
        }
        y = j shl 8; if (y != 0) {
            n -= 8; j = y
        }
        y = j shl 4; if (y != 0) {
            n -= 4; j = y
        }
        y = j shl 2; if (y != 0) {
            n -= 2; j = y
        }

        return n - (j shl 1).ushr(31)
    }

    private fun abs(x: Int): Int {
        val i = x.ushr(31)
        return (x xor i.inv() + 1) + i
    }

    companion object {
        private val FRACTION_REGEX = Regex("(\\d+)\\s*/\\s*(\\d+)")
        private val ZERO = Fraction(0, 1)
    }
}

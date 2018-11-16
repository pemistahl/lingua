package com.github.pemistahl.lingua.util.extension

import org.apache.commons.math3.fraction.Fraction

internal fun Fraction(value: String): Fraction {
    val regex = Regex("(\\d+)\\s*/\\s*(\\d+)")
    val matchResult =
        regex.matchEntire(value) ?: throw IllegalArgumentException(
            "value '$value' does not represent a valid fraction"
        )

    val (numerator, denominator) = matchResult.destructured
    return Fraction(numerator.toInt(), denominator.toInt())
}

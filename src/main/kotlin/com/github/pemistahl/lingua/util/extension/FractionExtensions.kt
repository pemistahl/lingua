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

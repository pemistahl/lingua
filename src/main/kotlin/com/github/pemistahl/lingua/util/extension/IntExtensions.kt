package com.github.pemistahl.lingua.util.extension

import com.github.pemistahl.lingua.math.Fraction

internal infix fun Int.over(other: Int) = Fraction(this, other)

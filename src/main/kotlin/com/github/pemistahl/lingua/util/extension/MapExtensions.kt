package com.github.pemistahl.lingua.util.extension

import com.github.pemistahl.lingua.model.Ngram
import org.apache.commons.math3.fraction.Fraction

internal fun <T : Ngram> Map<T, Fraction>.inverse(): Map<Fraction, Set<String>> {
    val result = mutableMapOf<Fraction, MutableSet<String>>()
    for ((key, value) in this) {
        result.computeIfAbsent(value) { mutableSetOf() }.add(key.toString())
    }
    return result
}

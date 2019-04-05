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

internal sealed class Ngram(val length: Int, value: String) : Comparable<Ngram> {
    val value: String = validate(value)

    override fun toString() = value

    override fun hashCode() = value.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ngram) return false
        if (value != other.value) return false
        return true
    }

    override fun compareTo(other: Ngram): Int = when {
        this.value.length > other.value.length -> 1
        this.value.length < other.value.length -> -1
        else -> 0
    }

    fun rangeOfLowerOrderNgrams() = NgramRange(this, Unigram(this.value[0].toString()))

    operator fun rangeTo(other: Ngram) = NgramRange(this, other)

    operator fun dec(): Ngram = when (this) {
        is Sixgram -> Fivegram(this.value.slice(0..4))
        is Fivegram -> Quadrigram(this.value.slice(0..3))
        is Quadrigram -> Trigram(this.value.slice(0..2))
        is Trigram -> Bigram(this.value.slice(0..1))
        is Bigram -> Unigram(this.value[0].toString())
        is Unigram -> Zerogram
        is Zerogram -> throw IllegalStateException(
            "$this is ngram type of lowest order and can not be decreased"
        )
    }

    private fun validate(value: String): String {
        require(value.length == this.length) {
            "value '$value' must be of length $length for type ${this::class.simpleName} but is ${value.length}"
        }
        return value
    }
}

internal class NgramRange(
    override val start: Ngram,
    override val endInclusive: Ngram
) : ClosedRange<Ngram>, Iterable<Ngram> {
    init {
        require(start >= endInclusive) { "$start must be of higher order than $endInclusive" }
    }

    override fun contains(value: Ngram): Boolean = value <= start && value >= endInclusive

    override fun iterator(): Iterator<Ngram> = NgramIterator(start)
}

internal class NgramIterator(start: Ngram) : Iterator<Ngram> {
    var current = start

    override fun hasNext(): Boolean = current !is Zerogram

    override fun next(): Ngram {
        if (!hasNext()) throw NoSuchElementException()
        return current--
    }
}

internal object Zerogram : Ngram(0, "")
internal class Unigram(value: String) : Ngram(1, value)
internal class Bigram(value: String) : Ngram(2, value)
internal class Trigram(value: String) : Ngram(3, value)
internal class Quadrigram(value: String) : Ngram(4, value)
internal class Fivegram(value: String) : Ngram(5, value)
internal class Sixgram(value: String) : Ngram(6, value)

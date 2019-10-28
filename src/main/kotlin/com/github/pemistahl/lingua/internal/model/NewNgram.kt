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

internal data class NewNgram(val value: String) : Comparable<NewNgram> {
    init {
        require(value.length in 1..5) {
            "length of ngram '$value' is not in range 1..5"
        }
    }

    override fun toString() = value

    override fun compareTo(other: NewNgram) = when {
        this.value.length > other.value.length -> 1
        this.value.length < other.value.length -> -1
        else -> 0
    }

    fun rangeOfLowerOrderNgrams() = NewNgramRange(this, NewNgram(this.value[0].toString()))

    operator fun dec(): NewNgram = when {
        this.value.length > 1 -> NewNgram(this.value.slice(0..this.value.length-2))
        this.value.length == 1 -> NewNgram("")
        else -> throw IllegalStateException(
            "Zerogram is ngram type of lowest order and can not be decremented"
        )
    }
}

internal class NewNgramRange(
    override val start: NewNgram,
    override val endInclusive: NewNgram
) : ClosedRange<NewNgram>, Iterable<NewNgram> {
    init {
        require(start >= endInclusive) {
            "'$start' must be of higher order than '$endInclusive'"
        }
    }

    override fun contains(value: NewNgram): Boolean = value <= start && value >= endInclusive

    override fun iterator(): Iterator<NewNgram> = NewNgramIterator(start)
}

internal class NewNgramIterator(start: NewNgram) : Iterator<NewNgram> {
    private var current = start

    override fun hasNext(): Boolean = current.value.isNotEmpty()

    override fun next(): NewNgram {
        if (!hasNext()) throw NoSuchElementException()
        return current--
    }
}

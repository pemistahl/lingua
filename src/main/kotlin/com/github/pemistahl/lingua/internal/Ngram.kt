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
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName

@Serializable
internal data class Ngram(val value: String) : Comparable<Ngram> {
    init {
        require(value.length in 0..5) {
            "length of ngram '$value' is not in range 0..5"
        }
    }

    override fun toString() = value

    override fun compareTo(other: Ngram) = when {
        this.value.length > other.value.length -> 1
        this.value.length < other.value.length -> -1
        else -> 0
    }

    fun rangeOfLowerOrderNgrams() = NgramRange(this, Ngram(this.value[0].toString()))

    operator fun dec(): Ngram = when {
        this.value.length > 1 -> Ngram(this.value.slice(0..this.value.length - 2))
        this.value.length == 1 -> Ngram("")
        else -> throw IllegalStateException(
            "Zerogram is ngram type of lowest order and can not be decremented"
        )
    }

    @Serializer(forClass = Ngram::class)
    companion object : KSerializer<Ngram> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("Ngram")

        override fun serialize(encoder: Encoder, obj: Ngram) {
            encoder.encodeString(obj.toString())
        }

        override fun deserialize(decoder: Decoder): Ngram {
            return Ngram(decoder.decodeString())
        }

        fun getNgramNameByLength(ngramLength: Int) = when (ngramLength) {
            1 -> "unigram"
            2 -> "bigram"
            3 -> "trigram"
            4 -> "quadrigram"
            5 -> "fivegram"
            else -> throw IllegalArgumentException("ngram length $ngramLength is not in range 1..5")
        }
    }
}

internal data class NgramRange(
    override val start: Ngram,
    override val endInclusive: Ngram
) : ClosedRange<Ngram>, Iterable<Ngram> {
    init {
        require(start >= endInclusive) {
            "'$start' must be of higher order than '$endInclusive'"
        }
    }

    override fun contains(value: Ngram): Boolean = value <= start && value >= endInclusive

    override fun iterator(): Iterator<Ngram> = NgramIterator(start)
}

internal data class NgramIterator(private val start: Ngram) : Iterator<Ngram> {
    private var current = start

    override fun hasNext(): Boolean = current.value.isNotEmpty()

    override fun next(): Ngram {
        if (!hasNext()) throw NoSuchElementException()
        return current--
    }
}

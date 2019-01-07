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

package com.github.pemistahl.lingua.model

internal sealed class Ngram(private val length: Int, value: String) {
    val value: String = validate(value)

    override fun toString() = value

    override fun hashCode() = value.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ngram) return false
        if (value != other.value) return false
        return true
    }

    private fun validate(value: String): String {
        if (value.length != this.length) {
            throw IllegalArgumentException(
                """
                value '$value' must be of length $length
                for type ${this::class.simpleName}
                """.trimIndent()
            )
        }
        return value
    }
}

internal class Unigram(value: String) : Ngram(1, value)
internal class Bigram(value: String) : Ngram(2, value)
internal class Trigram(value: String) : Ngram(3, value)
internal class Quadrigram(value: String) : Ngram(4, value)
internal class Fivegram(value: String) : Ngram(5, value)

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

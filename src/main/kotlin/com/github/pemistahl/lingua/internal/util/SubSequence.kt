package com.github.pemistahl.lingua.internal.util

internal class SubSequence private constructor(
    private val parent: CharSequence,
    private val start: Int,
    override val length: Int
) : CharSequence {

    init {
        require(start >= 0 && start < parent.length)
        require(length <= parent.length - start)
    }

    override fun get(index: Int): Char {
        if (index < 0 || index >= length) throw StringIndexOutOfBoundsException()
        return parent[start + index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        if (startIndex < 0 || startIndex >= length) throw StringIndexOutOfBoundsException()
        if (endIndex < startIndex || endIndex >= length) throw StringIndexOutOfBoundsException()
        return SubSequence(parent, start + startIndex, length - (endIndex - startIndex))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CharSequence) return false
        if (length != other.length) return false
        repeat(length) { i ->
            if (get(i) != other[i]) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var hash = 0
        repeat(length) { i ->
            hash = hash * 31 + get(i).code
        }
        return hash
    }

    companion object {
        operator fun invoke(parent: CharSequence, start: Int, length: Int) =
            if (parent is SubSequence) SubSequence(parent.parent, start + parent.start, length)
            else SubSequence(parent, start, length)
    }
}

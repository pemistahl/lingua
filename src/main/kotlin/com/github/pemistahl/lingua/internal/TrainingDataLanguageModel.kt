/*
 * Copyright © 2018-today Peter M. Stahl pemistahl@gmail.com
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

import com.github.pemistahl.lingua.api.Language
import com.github.pemistahl.lingua.internal.util.extension.incrementCounter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.TreeMap

@Serializable
internal data class JsonLanguageModel(val language: Language, val ngrams: Map<Fraction, String>)

internal data class TrainingDataLanguageModel(
    val language: Language,
    val absoluteFrequencies: Map<Ngram, Int>,
    val relativeFrequencies: Map<Ngram, Fraction>,
    val jsonRelativeFrequencies: RelativeFrequencies
) {
    fun getRelativeFrequency(ngram: Ngram): Float = jsonRelativeFrequencies[ngram.value]

    fun toJson(): String {
        val ngrams = mutableMapOf<Fraction, MutableList<Ngram>>()

        for ((ngram, fraction) in relativeFrequencies) {
            ngrams.computeIfAbsent(fraction) { mutableListOf() }.add(ngram)
        }

        val jsonLanguageModel = JsonLanguageModel(language, ngrams.mapValues { it.value.joinToString(separator = " ") })

        return Json.encodeToString(jsonLanguageModel)
    }

    companion object {
        fun fromText(
            text: Sequence<String>,
            language: Language,
            ngramLength: Int,
            charClass: String,
            lowerNgramAbsoluteFrequencies: Map<Ngram, Int>
        ): TrainingDataLanguageModel {

            require(ngramLength in 1..5) {
                "ngram length $ngramLength is not in range 1..5"
            }

            val absoluteFrequencies = computeAbsoluteFrequencies(
                text,
                ngramLength,
                charClass
            )

            val relativeFrequencies = computeRelativeFrequencies(
                ngramLength,
                absoluteFrequencies,
                lowerNgramAbsoluteFrequencies
            )

            return TrainingDataLanguageModel(
                language,
                absoluteFrequencies,
                relativeFrequencies,
                RelativeFrequencies.build(emptySequence(), BuilderCache())
            )
        }

        fun fromJson(
            language: Language, jsonLanguageModels: Sequence<JsonLanguageModel>,
        ): TrainingDataLanguageModel =
            fromJson(language, jsonLanguageModels, BuilderCache())

        internal fun fromJson(
            language: Language, jsonLanguageModels: Sequence<JsonLanguageModel>,
            builderCache: BuilderCache = BuilderCache()
        ): TrainingDataLanguageModel {
            val jsonDataSequence =
                sequence {
                    for (jsonLanguageModel in jsonLanguageModels) {
                        for ((fraction, ngrams) in jsonLanguageModel.ngrams) {
                            val fractionAsFloat = fraction.toFloat()
                            for (ngram in ngrams.split(' ')) {
                                yield(ngram to fractionAsFloat)
                            }
                        }
                    }
                }

            return TrainingDataLanguageModel(
                language = language,
                absoluteFrequencies = emptyMap(),
                relativeFrequencies = emptyMap(),
                jsonRelativeFrequencies = RelativeFrequencies.build(jsonDataSequence, builderCache)
            )
        }

        private fun computeAbsoluteFrequencies(
            text: Sequence<String>,
            ngramLength: Int,
            charClass: String
        ): Map<Ngram, Int> {

            val absoluteFrequencies = mutableMapOf<Ngram, Int>()
            val regex = Regex("[$charClass]+")

            for (line in text) {
                val lowerCasedLine = line.lowercase()
                for (i in 0..lowerCasedLine.length - ngramLength) {
                    val textSlice = lowerCasedLine.substring(i, i + ngramLength)
                    if (regex.matches(textSlice)) {
                        val ngram = Ngram(textSlice)
                        absoluteFrequencies.incrementCounter(ngram)
                    }
                }
            }

            return absoluteFrequencies
        }

        private fun computeRelativeFrequencies(
            ngramLength: Int,
            absoluteFrequencies: Map<Ngram, Int>,
            lowerNgramAbsoluteFrequencies: Map<Ngram, Int>
        ): Map<Ngram, Fraction> {

            val ngramProbabilities = mutableMapOf<Ngram, Fraction>()
            val totalNgramFrequency = absoluteFrequencies.values.sum()

            for ((ngram, frequency) in absoluteFrequencies) {
                val denominator = if (ngramLength == 1 || lowerNgramAbsoluteFrequencies.isEmpty()) {
                    totalNgramFrequency
                } else {
                    lowerNgramAbsoluteFrequencies.getValue(Ngram(ngram.value.substring(0, ngramLength - 1)))
                }
                ngramProbabilities[ngram] = Fraction(frequency, denominator)
            }

            return ngramProbabilities
        }
    }

    internal class BuilderCache(
        val keysCache: MutableMap<List<Char>, CharArray> = hashMapOf(),
        val floatsCache: MutableMap<List<Float>, FloatArray> = hashMapOf(),
        val nodeCache: MutableMap<RelativeFrequencies, RelativeFrequencies> = hashMapOf(RelativeFrequencies.Leaf(0F) to RelativeFrequencies.EmptyNode)
    )

    /**
     * N-ary search tree.
     */
    internal sealed class RelativeFrequencies {

        abstract val frequency: Float

        operator fun get(ngram: CharSequence): Float = getImpl(ngram, 0)

        protected abstract fun getImpl(ngram: CharSequence, depth: Int): Float

        internal class GenericNode(
            override val frequency: Float,
            private val keys: CharArray,
            private val values: Array<RelativeFrequencies>
        ) : RelativeFrequencies() {
            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                val key = ngram[depth]
                val i = if (keys.size <= 1024) {
                    keys.binarySearch(key)
                } else {
                    val first = ngram.first()
                    if (key < first) return 0F
                    if (key == first) return values.first().getImpl(ngram, depth + 1)
                    val last = ngram.last()
                    if (key > last) return 0F
                    if (key == last) return values.last().getImpl(ngram, depth + 1)
                    keys.binarySearch(key, 1, keys.size - 1)
                }
                return if (i >= 0) values[i].getImpl(ngram, depth + 1) else 0F
            }
        }

        /**
         * A node with one leaf child
         */
        internal data class PreLeaf1(
            override val frequency: Float,
            private val key: Char,
            private val value: Float
        ) : RelativeFrequencies() {
            override fun getImpl(ngram: CharSequence, depth: Int) =
                when {
                    depth == ngram.length -> frequency
                    depth + 1 == ngram.length && key == ngram[depth] -> value
                    else -> 0F
                }
        }

        /**
         * A node with only 2 leaf children
         */
        internal data class PreLeaf2(
            override val frequency: Float,
            private val key1: Char,
            private val key2: Char,
            private val value1: Float,
            private val value2: Float
        ) : RelativeFrequencies() {
            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                if (depth + 1 == ngram.length) {
                    val key = ngram[depth]
                    if (key1 == key) return value1
                    if (key2 == key) return value2
                }
                return 0F
            }
        }

        /**
         * A node with only 3 leaf children
         */
        internal data class PreLeaf3(
            override val frequency: Float,
            private val key1: Char,
            private val key2: Char,
            private val key3: Char,
            private val value1: Float,
            private val value2: Float,
            private val value3: Float
        ) : RelativeFrequencies() {

            constructor(frequency: Float, keys: CharArray, values: List<Float>) :
                this(
                    frequency = frequency,
                    key1 = keys[0],
                    key2 = keys[1],
                    key3 = keys[2],
                    value1 = values[0],
                    value2 = values[1],
                    value3 = values[2]
                )

            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                if (depth + 1 == ngram.length) {
                    val key = ngram[depth]
                    if (key1 == key) return value1
                    if (key2 == key) return value2
                    if (key3 == key) return value3
                }
                return 0F
            }
        }

        /**
         * A node with only 4 leaf children
         */
        internal data class PreLeaf4(
            override val frequency: Float,
            private val key1: Char,
            private val key2: Char,
            private val key3: Char,
            private val key4: Char,
            private val value1: Float,
            private val value2: Float,
            private val value3: Float,
            private val value4: Float
        ) : RelativeFrequencies() {

            constructor(frequency: Float, keys: CharArray, values: List<Float>) :
                this(
                    frequency = frequency,
                    key1 = keys[0],
                    key2 = keys[1],
                    key3 = keys[2],
                    key4 = keys[3],
                    value1 = values[0],
                    value2 = values[1],
                    value3 = values[2],
                    value4 = values[3]
                )

            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                if (depth + 1 == ngram.length) {
                    val key = ngram[depth]
                    if (key <= key1) {
                        if (key == key1) return value1
                    } else if (key >= key4) {
                        if (key == key4) return value4
                    } else {
                        if (key2 == key) return value2
                        if (key3 == key) return value3
                    }
                }
                return 0F
            }
        }

        /**
         * A node with only 5 leaf children
         */
        internal data class PreLeaf5(
            override val frequency: Float,
            private val key1: Char,
            private val key2: Char,
            private val key3: Char,
            private val key4: Char,
            private val key5: Char,
            private val value1: Float,
            private val value2: Float,
            private val value3: Float,
            private val value4: Float,
            private val value5: Float
        ) : RelativeFrequencies() {

            constructor(frequency: Float, keys: CharArray, values: List<Float>) :
                this(
                    frequency = frequency,
                    key1 = keys[0],
                    key2 = keys[1],
                    key3 = keys[2],
                    key4 = keys[3],
                    key5 = keys[4],
                    value1 = values[0],
                    value2 = values[1],
                    value3 = values[2],
                    value4 = values[3],
                    value5 = values[4]
                )

            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                if (depth + 1 == ngram.length) {
                    val key = ngram[depth]
                    if (key <= key1) {
                        if (key == key1) return value1
                    } else if (key >= key5) {
                        if (key == key5) return value5
                    } else {
                        if (key2 == key) return value2
                        if (key3 == key) return value3
                        if (key4 == key) return value4
                    }
                }
                return 0F
            }
        }

        /**
         * A node with only 5 leaf children
         */
        internal data class PreLeaf6(
            override val frequency: Float,
            private val key1: Char,
            private val key2: Char,
            private val key3: Char,
            private val key4: Char,
            private val key5: Char,
            private val key6: Char,
            private val value1: Float,
            private val value2: Float,
            private val value3: Float,
            private val value4: Float,
            private val value5: Float,
            private val value6: Float
        ) : RelativeFrequencies() {

            constructor(frequency: Float, keys: CharArray, values: List<Float>) :
                this(
                    frequency = frequency,
                    key1 = keys[0],
                    key2 = keys[1],
                    key3 = keys[2],
                    key4 = keys[3],
                    key5 = keys[4],
                    key6 = keys[5],
                    value1 = values[0],
                    value2 = values[1],
                    value3 = values[2],
                    value4 = values[3],
                    value5 = values[4],
                    value6 = values[5]
                )

            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                if (depth + 1 == ngram.length) {
                    val key = ngram[depth]
                    if (key <= key1) {
                        if (key == key1) return value1
                    } else if (key >= key6) {
                        if (key == key6) return value6
                    } else {
                        if (key2 == key) return value2
                        if (key3 == key) return value3
                        if (key4 == key) return value4
                        if (key5 == key) return value5
                    }
                }
                return 0F
            }
        }

        /**
         * A node with only leaf children
         */
        internal class PreLeafN(
            override val frequency: Float,
            private val keys: CharArray,
            private val values: FloatArray
        ) : RelativeFrequencies() {
            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                if (depth + 1 == ngram.length) {
                    val key = ngram[depth]
                    val i = keys.binarySearch(key)
                    if (i >= 0) return values[i]
                }
                return 0F
            }
        }

        /**
         * A leaf node
         */
        internal data class Leaf(override val frequency: Float) : RelativeFrequencies() {
            override fun getImpl(ngram: CharSequence, depth: Int): Float =
                if (depth == ngram.length) frequency else 0F
        }

        internal object EmptyNode : RelativeFrequencies() {
            override val frequency: Float get() = 0F
            override fun getImpl(ngram: CharSequence, depth: Int) = 0F
        }

        private class MutableNode(
            var frequency: Float = 0F,
            val children: TreeMap<Char, MutableNode> = TreeMap()
        ) {
            operator fun set(ngram: String, frequency: Float) {
                var node = this
                repeat(ngram.length) { i ->
                    node = node.children.computeIfAbsent(ngram[i]) { MutableNode() }
                }
                node.frequency = frequency
            }

            fun toRelativeFrequencies(builderCache: BuilderCache): RelativeFrequencies {
                if (children.size == 0) {
                    // leaf node
                    val node = Leaf(frequency)
                    return builderCache.nodeCache.computeIfAbsent(node) { node }
                }

                var keysArray = children.keys.toCharArray()
                if (keysArray.size < 256) {
                    keysArray = builderCache.keysCache.computeIfAbsent(keysArray.asList()) { keysArray }
                }

                if (children.values.all { it.children.isEmpty() }) {
                    // pre-leaf node
                    if (children.size == 1) {
                        val node = PreLeaf1(frequency, keysArray.single(), children.values.single().frequency)
                        return builderCache.nodeCache.computeIfAbsent(node) { node }
                    }
                    if (children.size == 2) {
                        val node = PreLeaf2(
                            frequency = frequency,
                            key1 = keysArray.first(),
                            key2 = keysArray.last(),
                            value1 = children.values.first().frequency,
                            value2 = children.values.last().frequency
                        )
                        return builderCache.nodeCache.computeIfAbsent(node) { node }
                    }
                    if (children.size == 3) {
                        val node = PreLeaf3(frequency, keysArray, children.values.map { it.frequency })
                        return builderCache.nodeCache.computeIfAbsent(node) { node }
                    }
                    if (children.size == 4) {
                        val node = PreLeaf4(frequency, keysArray, children.values.map { it.frequency })
                        return builderCache.nodeCache.computeIfAbsent(node) { node }
                    }
                    if (children.size == 5) {
                        val node = PreLeaf5(frequency, keysArray, children.values.map { it.frequency })
                        return builderCache.nodeCache.computeIfAbsent(node) { node }
                    }
                    if (children.size == 6) {
                        val node = PreLeaf6(frequency, keysArray, children.values.map { it.frequency })
                        return builderCache.nodeCache.computeIfAbsent(node) { node }
                    }

                    var valuesArray = FloatArray(children.size)
                    children.values.forEachIndexed { index, mutableNode ->
                        valuesArray[index] = mutableNode.frequency
                    }
                    if (valuesArray.size < 256) {
                        valuesArray = builderCache.floatsCache.computeIfAbsent(valuesArray.asList()) { valuesArray }
                    }
                    return PreLeafN(frequency = frequency, keys = keysArray, values = valuesArray)
                }

                // intermediate node
                val valuesArray = arrayOfNulls<RelativeFrequencies>(children.size)
                children.values.forEachIndexed { index, mutableNode ->
                    valuesArray[index] = mutableNode.toRelativeFrequencies(builderCache)
                }
                @Suppress("UNCHECKED_CAST")
                return GenericNode(
                    frequency = frequency,
                    keys = keysArray,
                    values = valuesArray as Array<RelativeFrequencies>
                )
            }
        }

        companion object {
            internal fun build(
                relativeFrequencies: Sequence<Pair<String, Float>>,
                builderCache: BuilderCache
            ): RelativeFrequencies {
                val mutableRoot = MutableNode()
                for ((ngram, frequency) in relativeFrequencies) {
                    mutableRoot[ngram] = frequency
                }
                return mutableRoot.toRelativeFrequencies(builderCache)
            }
        }
    }
}

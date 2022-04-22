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
                RelativeFrequencies.build(emptySequence())
            )
        }

        fun fromJson(language: Language, jsonLanguageModels: Sequence<JsonLanguageModel>): TrainingDataLanguageModel {
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
                jsonRelativeFrequencies = RelativeFrequencies.build(jsonDataSequence)
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

    /**
     * N-ary search tree.
     */
    internal sealed class RelativeFrequencies {

        abstract val frequency: Float

        operator fun get(ngram: CharSequence): Float = getImpl(ngram, 0)

        protected abstract fun getImpl(ngram: CharSequence, depth: Int): Float

        class GenericNode(
            override val frequency: Float,
            private val keys: CharArray,
            private val values: Array<RelativeFrequencies>
        ) : RelativeFrequencies() {
            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                val i = keys.binarySearch(ngram[depth])
                return if (i >= 0) values[i].getImpl(ngram, depth + 1) else 0F
            }
        }

        /**
         * A node with one leaf child
         */
        data class PreLeaf1(
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
        data class PreLeaf2(
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
        data class PreLeaf3(
            override val frequency: Float,
            private val key1: Char,
            private val key2: Char,
            private val key3: Char,
            private val value1: Float,
            private val value2: Float,
            private val value3: Float
        ) : RelativeFrequencies() {

            constructor(frequency: Float, keys: Collection<Char>, values: Collection<Float>) :
                this(
                    frequency = frequency,
                    key1 = keys.first(),
                    key2 = keys.drop(1).first(),
                    key3 = keys.last(),
                    value1 = values.first(),
                    value2 = values.drop(1).first(),
                    value3 = values.last()
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
         * A node with only leaf children
         */
        class PreLeafN(
            override val frequency: Float,
            private val keys: CharArray,
            private val values: FloatArray
        ) : RelativeFrequencies() {
            override fun getImpl(ngram: CharSequence, depth: Int): Float {
                if (depth == ngram.length) return frequency
                if (depth + 1 == ngram.length) {
                    val i = keys.binarySearch(ngram[depth])
                    if (i >= 0) return values[i]
                }
                return 0F
            }
        }

        /**
         * A leaf node
         */
        data class Leaf(override val frequency: Float) : RelativeFrequencies() {
            override fun getImpl(ngram: CharSequence, depth: Int): Float =
                if (depth == ngram.length) frequency else 0F
        }

        object EmptyNode : RelativeFrequencies() {
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

            fun toRelativeFrequencies() =
                toRelativeFrequenciesImpl(
                    keysCache = mutableMapOf(),
                    floatsCache = mutableMapOf(),
                    nodeCache = mutableMapOf(Leaf(0F) to EmptyNode)
                )

            private fun toRelativeFrequenciesImpl(
                keysCache: MutableMap<List<Char>, CharArray>,
                floatsCache: MutableMap<List<Float>, FloatArray>,
                nodeCache: MutableMap<RelativeFrequencies, RelativeFrequencies>
            ): RelativeFrequencies {
                if (children.size == 0) {
                    val node = Leaf(frequency)
                    return nodeCache.computeIfAbsent(node) { node }
                }
                if (children.size == 1) {
                    val node = PreLeaf1(frequency, children.keys.single(), children.values.single().frequency)
                    return nodeCache.computeIfAbsent(node) { node }
                }
                if (children.size == 2) {
                    val node = PreLeaf2(
                        frequency = frequency,
                        key1 = children.keys.first(),
                        key2 = children.keys.last(),
                        value1 = children.values.first().frequency,
                        value2 = children.values.last().frequency
                    )
                    return nodeCache.computeIfAbsent(node) { node }
                }
                if (children.size == 3) {
                    val node = PreLeaf3(frequency, children.keys.toList(), children.values.map { it.frequency })
                    return nodeCache.computeIfAbsent(node) { node }
                }

                var keysArray = children.keys.toCharArray()
                if (keysArray.size < 128) {
                    keysArray = keysCache.computeIfAbsent(keysArray.asList()) { keysArray }
                }

                if (children.values.all { it.children.isEmpty() }) {
                    var valuesArray = children.values.map { it.frequency }.toFloatArray()
                    if (valuesArray.size < 128) {
                        valuesArray = floatsCache.computeIfAbsent(valuesArray.asList()) { valuesArray }
                    }
                    return PreLeafN(
                        frequency = frequency,
                        keys = keysArray,
                        values = valuesArray
                    )
                }

                val values =
                    children.values
                        .map { it.toRelativeFrequenciesImpl(keysCache, floatsCache, nodeCache) }
                        .toTypedArray()
                return GenericNode(
                    frequency = frequency,
                    keys = keysArray,
                    values = values
                )
            }
        }

        companion object {
            internal fun build(relativeFrequencies: Sequence<Pair<String, Float>>): RelativeFrequencies {
                val mutableRoot = MutableNode()
                for ((ngram, frequency) in relativeFrequencies) {
                    mutableRoot[ngram] = frequency
                }
                return mutableRoot.toRelativeFrequencies()
            }
        }
    }
}

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

package com.github.pemistahl.lingua.internal.util.extension

import java.util.EnumMap
import java.util.EnumSet

internal inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): EnumMap<K, V> = when (pairs.size) {
    0 -> EnumMap(K::class.java)
    else -> EnumMap(pairs.toMap())
}

internal inline fun <reified E : Enum<E>> enumSetOf(vararg elements: E): EnumSet<E> = when (elements.size) {
    0 -> EnumSet.noneOf(E::class.java)
    1 -> EnumSet.of(elements[0])
    2 -> EnumSet.of(elements[0], elements[1])
    3 -> EnumSet.of(elements[0], elements[1], elements[2])
    4 -> EnumSet.of(elements[0], elements[1], elements[2], elements[3])
    5 -> EnumSet.of(elements[0], elements[1], elements[2], elements[3], elements[4])
    else -> EnumSet.of(elements[0], *elements.drop(1).toTypedArray())
}

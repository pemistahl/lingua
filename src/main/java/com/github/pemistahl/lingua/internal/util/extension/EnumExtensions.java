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

package com.github.pemistahl.lingua.internal.util.extension;

import java.util.EnumMap;
import java.util.EnumSet;

/**
 * Utility methods for creating {@link EnumMap} and {@link EnumSet} instances.
 *
 * <p>This class contains extension methods that help to create {@link EnumMap} and {@link EnumSet}
 * more concisely, based on the size of the input pairs or elements.
 *
 * @author Peter M. Stahl pemistahl@gmail.com
 * @author Migration to Java from Kotlin by Alexander Zagniotov azagniotov@gmail.com
 */
public class EnumExtensions {

  /**
   * Creates an {@link EnumMap} with the provided pairs of enum keys and corresponding values.
   *
   * <p>If the input is empty, an empty {@link EnumMap} is created. Otherwise, the map is created
   * from the given pairs.
   *
   * @param <K> the type of the enum key
   * @param <V> the type of the value
   * @param pairs a variable number of key-value pairs
   * @return a new {@link EnumMap} containing the provided pairs
   */
  @SafeVarargs
  public static <K extends Enum<K>, V> EnumMap<K, V> enumMapOf(Pair<K, V>... pairs) {
    if (pairs.length == 0) {
      return new EnumMap<>(pairs[0].getKey().getDeclaringClass());
    } else {
      EnumMap<K, V> map = new EnumMap<>(pairs[0].getKey().getDeclaringClass());
      for (Pair<K, V> pair : pairs) {
        map.put(pair.getKey(), pair.getValue());
      }
      return map;
    }
  }

  /**
   * Creates an {@link EnumSet} with the provided elements.
   *
   * <p>If no elements are provided, an empty {@link EnumSet} is created. If one or more elements
   * are provided, the corresponding {@link EnumSet} is created.
   *
   * @param <E> the type of the enum element
   * @param elements a variable number of enum elements
   * @return a new {@link EnumSet} containing the provided elements
   */
  @SafeVarargs
  public static <E extends Enum<E>> EnumSet<E> enumSetOf(E... elements) {
    switch (elements.length) {
      case 0:
        return EnumSet.noneOf(elements[0].getDeclaringClass());
      case 1:
        return EnumSet.of(elements[0]);
      case 2:
        return EnumSet.of(elements[0], elements[1]);
      case 3:
        return EnumSet.of(elements[0], elements[1], elements[2]);
      case 4:
        return EnumSet.of(elements[0], elements[1], elements[2], elements[3]);
      case 5:
        return EnumSet.of(elements[0], elements[1], elements[2], elements[3], elements[4]);
      default:
        return EnumSet.of(elements[0], elements);
    }
  }

  /**
   * A simple container for holding a pair of values (key and value). This is a utility class used
   * for passing key-value pairs to methods like {@link #enumMapOf}.
   *
   * @param <K> the type of the key
   * @param <V> the type of the value
   */
  public static class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public K getKey() {
      return key;
    }

    public V getValue() {
      return value;
    }
  }
}

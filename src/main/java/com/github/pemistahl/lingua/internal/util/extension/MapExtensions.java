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

import java.util.Map;

/**
 * Utility methods for working with collections and maps.
 *
 * <p>This class contains extension-like methods for commonly used map operations.
 *
 * @author Peter M. Stahl pemistahl@gmail.com
 * @author Migration by Alexander Zagniotov azagniotov@gmail.com
 */
public class MapExtensions {

  /**
   * Increments the counter for the given key in the map.
   *
   * <p>If the key is already present in the map, its value is incremented by 1. If the key is not
   * present, it is added to the map with a value of 1.
   *
   * @param <T> the type of the key
   * @param map the mutable map to update
   * @param key the key whose counter is to be incremented
   */
  public static <T> void incrementCounter(Map<T, Integer> map, T key) {
    map.put(key, map.getOrDefault(key, 0) + 1);
  }
}

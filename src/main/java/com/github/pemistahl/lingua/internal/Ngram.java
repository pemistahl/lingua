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

package com.github.pemistahl.lingua.internal;

import java.util.Objects;

/**
 * This class represents an Ngram value, a string-based object with specific constraints on its
 * length. The Ngram is comparable to other Ngrams based on its length.
 *
 * @author Peter M. Stahl <pemistahl@gmail.com>
 * @author Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
public final class Ngram implements Comparable<Ngram> {

  private final String value;

  /**
   * Constructs an Ngram with a given string value.
   *
   * @param value the string value of the Ngram
   * @throws IllegalArgumentException if the length of the value is not in the range 0..5
   */
  public Ngram(final String value) {
    if (value == null || value.trim().length() > 5) {
      throw new IllegalArgumentException("Length of ngram '" + value + "' is not in range 0..5");
    }
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  /**
   * Returns the string representation of the Ngram.
   *
   * @return the string value of the Ngram
   */
  @Override
  public String toString() {
    return value;
  }

  /**
   * Compares the Ngram to another Ngram based on the length of their values.
   *
   * @param other the other Ngram to compare to
   * @return a negative integer, zero, or a positive integer as this Ngram is less than, equal to,
   *     or greater than the specified Ngram
   */
  @Override
  public int compareTo(final Ngram other) {
    return Integer.compare(this.value.length(), other.value.length());
  }

  /**
   * Returns the range of lower order Ngrams that this Ngram can generate.
   *
   * @return the range of lower order Ngrams
   */
  public NgramRange rangeOfLowerOrderNgrams() {
    return new NgramRange(this, new Ngram(String.valueOf(this.value.charAt(0))));
  }

  /**
   * Decrements the Ngram by removing the last character, unless it is a zerogram.
   *
   * @return the decremented Ngram
   * @throws IllegalArgumentException if the Ngram is a zerogram and cannot be decremented
   */
  public Ngram dec() {
    if (value.isEmpty()) {
      throw new IllegalStateException(
          "Zerogram is ngram type of lowest order and can not be decremented");
    } else if (value.length() == 1) {
      return new Ngram("");
    } else {
      return new Ngram(value.substring(0, value.length() - 1));
    }
  }

  /**
   * Returns the name of the Ngram type based on the given length.
   *
   * @param ngramLength the length of the Ngram
   * @return the name of the Ngram type (unigram, bigram, trigram, quadrigram, or fivegram)
   * @throws IllegalArgumentException if the length is not between 1 and 5
   */
  public static String getNgramNameByLength(int ngramLength) {
    switch (ngramLength) {
      case 1:
        return "unigram";
      case 2:
        return "bigram";
      case 3:
        return "trigram";
      case 4:
        return "quadrigram";
      case 5:
        return "fivegram";
      default:
        throw new IllegalArgumentException("Ngram length " + ngramLength + " is not in range 1..5");
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ngram ngram = (Ngram) o;
    return Objects.equals(value, ngram.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}

/**
 * A simple closed range interface representing a range of values.
 *
 * @param <T> the type of the range elements
 */
interface ClosedRange<T> {

  boolean contains(final Ngram value);

  T getStart();

  T getEndInclusive();
}

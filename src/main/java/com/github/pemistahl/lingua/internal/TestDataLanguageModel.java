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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * A data class representing a language model built from n-grams.
 *
 * <p>This class contains a set of n-grams (sequences of characters) and provides functionality for
 * creating a language model from a given text.
 *
 * @author Peter M. Stahl pemistahl@gmail.com
 * @author Migration by Alexander Zagniotov azagniotov@gmail.com
 */
public class TestDataLanguageModel {

  private static final Pattern LETTER_PATTERN = Pattern.compile("\\p{L}+");

  private final Set<Ngram> ngrams;

  public TestDataLanguageModel(final Set<Ngram> ngrams) {
    this.ngrams = ngrams;
  }

  public Set<Ngram> getNgrams() {
    return ngrams;
  }

  /**
   * Creates a TestDataLanguageModel from the provided text and ngram length.
   *
   * <p>The ngram length must be between 1 and 5 inclusive. The method extracts n-grams of the
   * specified length from the input text, ensuring that the extracted n-grams only contain
   * alphabetic characters.
   *
   * @param text the input text from which to generate n-grams
   * @param ngramLength the length of each n-gram
   * @return a TestDataLanguageModel object containing the extracted n-grams
   * @throws IllegalArgumentException if the ngramLength is not between 1 and 5 inclusive
   */
  public static TestDataLanguageModel fromText(final String text, final int ngramLength) {
    if (ngramLength < 1 || ngramLength > 5) {
      throw new IllegalArgumentException("ngram length " + ngramLength + " is not in range 1..5");
    }

    Set<Ngram> ngrams = new HashSet<>();
    for (int idx = 0; idx <= text.length() - ngramLength; idx++) {
      String textSlice = text.substring(idx, idx + ngramLength);
      if (LETTER_PATTERN.matcher(textSlice).matches()) {
        ngrams.add(new Ngram(textSlice));
      }
    }

    return new TestDataLanguageModel(ngrams);
  }
}

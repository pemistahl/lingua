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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Ngram class and related components like NgramRange and NgramIterator. These tests
 * verify the expected behavior and correctness of the Ngram functionality.
 *
 * @author Peter M. Stahl pemistahl@gmail.com
 * @author Alexander Zagniotov azagniotov@gmail.com
 */
public class NgramTest {

  private final Ngram zerogram = new Ngram("");
  private final Ngram unigram = new Ngram("q");
  private final Ngram bigram = new Ngram("qw");
  private final Ngram trigram = new Ngram("qwe");
  private final Ngram quadrigram = new Ngram("qwer");
  private final Ngram fivegram = new Ngram("qwert");
  private final List<Ngram> ngrams = List.of(unigram, bigram, trigram, quadrigram, fivegram);

  @Test
  public void testToString() {
    // Assert that the toString() implementation of Ngram is its value
    assertThat(fivegram.toString()).isEqualTo("qwert");
    assertThat(quadrigram.toString()).isEqualTo("qwer");
    assertThat(trigram.toString()).isEqualTo("qwe");
    assertThat(bigram.toString()).isEqualTo("qw");
    assertThat(unigram.toString()).isEqualTo("q");
    assertThat(zerogram.toString()).isEqualTo("");
  }

  @Test
  public void testNgramComparisons() {
    // Assert that Ngram comparisons work correctly
    List<Boolean> comparisons =
        List.of(
            fivegram.compareTo(quadrigram) > 0,
            fivegram.compareTo(trigram) > 0,
            fivegram.compareTo(bigram) > 0,
            fivegram.compareTo(unigram) > 0,
            fivegram.compareTo(zerogram) > 0,
            quadrigram.compareTo(trigram) > 0,
            quadrigram.compareTo(bigram) > 0,
            quadrigram.compareTo(unigram) > 0,
            quadrigram.compareTo(zerogram) > 0,
            trigram.compareTo(bigram) > 0,
            trigram.compareTo(unigram) > 0,
            trigram.compareTo(zerogram) > 0,
            bigram.compareTo(unigram) > 0,
            bigram.compareTo(zerogram) > 0,
            unigram.compareTo(zerogram) > 0,
            quadrigram.compareTo(fivegram) < 0,
            trigram.compareTo(fivegram) < 0,
            bigram.compareTo(fivegram) < 0,
            unigram.compareTo(fivegram) < 0,
            zerogram.compareTo(fivegram) < 0,
            trigram.compareTo(quadrigram) < 0,
            bigram.compareTo(quadrigram) < 0,
            unigram.compareTo(quadrigram) < 0,
            zerogram.compareTo(trigram) < 0,
            bigram.compareTo(trigram) < 0,
            unigram.compareTo(trigram) < 0,
            zerogram.compareTo(trigram) < 0,
            unigram.compareTo(bigram) < 0,
            zerogram.compareTo(bigram) < 0,
            zerogram.compareTo(unigram) < 0);

    comparisons.forEach(Assertions::assertThat);
  }

  @Test
  public void testDecrement() {
    // Assert that Fivegrams can be decremented correctly
    Ngram quadrigram = fivegram.dec();
    assertThat(quadrigram).isEqualTo(this.quadrigram);

    Ngram trigram = quadrigram.dec();
    assertThat(trigram).isEqualTo(this.trigram);

    Ngram bigram = trigram.dec();
    assertThat(bigram).isEqualTo(this.bigram);

    Ngram unigram = bigram.dec();
    assertThat(unigram).isEqualTo(this.unigram);

    Ngram zerogram = unigram.dec();
    assertThat(zerogram).isEqualTo(zerogram);

    Assertions.assertThatIllegalStateException()
        .isThrownBy(zerogram::dec)
        .withMessage("Zerogram is ngram type of lowest order and can not be decremented");
  }

  @Test
  public void testNgramRange() {
    // Assert that NgramRange works correctly
    NgramRange ngramRange = new NgramRange(fivegram, bigram);

    assertThat(ngramRange.contains(fivegram)).isTrue();
    assertThat(ngramRange.contains(quadrigram)).isTrue();
    assertThat(ngramRange.contains(trigram)).isTrue();
    assertThat(ngramRange.contains(bigram)).isTrue();

    assertThat(ngramRange.contains(unigram)).isFalse();
    assertThat(ngramRange.contains(zerogram)).isFalse();

    assertThat(ngramRange.iterator()).isEqualTo(new NgramIterator(fivegram));

    Assertions.assertThatThrownBy(() -> new NgramRange(bigram, fivegram))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("'qw' must be of higher order than 'qwert'");
  }

  @Test
  public void testLowerOrderNgrams() {
    // Assert that range of lower order ngrams can be generated correctly
    for (Ngram ngram : ngrams) {
      assertThat(ngram.rangeOfLowerOrderNgrams()).isEqualTo(new NgramRange(ngram, unigram));
    }
  }

  @Test
  public void testNgramIterator() {
    // Assert that NgramIterator works correctly
    NgramIterator iterator = new NgramIterator(fivegram);

    assertThat(iterator.next()).isEqualTo(fivegram);
    assertThat(iterator.next()).isEqualTo(quadrigram);
    assertThat(iterator.next()).isEqualTo(trigram);
    assertThat(iterator.next()).isEqualTo(bigram);
    assertThat(iterator.next()).isEqualTo(unigram);

    Assertions.assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
  }
}

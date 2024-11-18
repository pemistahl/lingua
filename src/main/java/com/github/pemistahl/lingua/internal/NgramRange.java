package com.github.pemistahl.lingua.internal;

import java.util.Iterator;
import java.util.Objects;

/**
 * Represents a closed range of Ngrams from a start Ngram to an end Ngram. The range includes all
 * Ngrams from the start Ngram to the end Ngram.
 *
 * @author Peter M. Stahl <pemistahl@gmail.com>
 * @author Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
public final class NgramRange implements Iterable<Ngram>, ClosedRange<Ngram> {

  private final Ngram start;
  private final Ngram endInclusive;

  /**
   * Constructs an NgramRange with a start and an end Ngram.
   *
   * @param start the start Ngram
   * @param endInclusive the end Ngram (inclusive)
   * @throws IllegalArgumentException if the start Ngram is not of a higher order than the end Ngram
   */
  public NgramRange(final Ngram start, final Ngram endInclusive) {
    if (start.compareTo(endInclusive) < 0) {
      throw new IllegalArgumentException(
          "'" + start + "' must be of higher order than '" + endInclusive + "'");
    }
    this.start = start;
    this.endInclusive = endInclusive;
  }

  /**
   * Checks if a given Ngram is within this range.
   *
   * @param value the Ngram to check
   * @return true if the Ngram is within the range, false otherwise
   */
  @Override
  public boolean contains(final Ngram value) {
    return value.compareTo(endInclusive) >= 0 && value.compareTo(start) <= 0;
  }

  /**
   * Returns an iterator over the Ngrams in the range.
   *
   * @return an iterator over the Ngrams
   */
  @Override
  public Iterator<Ngram> iterator() {
    return new NgramIterator(start);
  }

  @Override
  public Ngram getStart() {
    return start;
  }

  @Override
  public Ngram getEndInclusive() {
    return endInclusive;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NgramRange ngrams = (NgramRange) o;
    return Objects.equals(getStart(), ngrams.getStart())
        && Objects.equals(getEndInclusive(), ngrams.getEndInclusive());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getStart(), getEndInclusive());
  }
}

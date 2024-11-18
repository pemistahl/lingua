package com.github.pemistahl.lingua.internal;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Iterator for iterating over Ngrams starting from a specific Ngram.
 *
 * @author Peter M. Stahl <pemistahl@gmail.com>
 * @author Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
public final class NgramIterator implements Iterator<Ngram> {

  private Ngram current;

  /**
   * Constructs an NgramIterator starting at a specific Ngram.
   *
   * @param start the starting Ngram
   */
  public NgramIterator(final Ngram start) {
    this.current = start;
  }

  /**
   * Checks if there are more Ngrams to iterate over.
   *
   * @return true if there are more Ngrams, false otherwise
   */
  @Override
  public boolean hasNext() {
    return !current.toString().isEmpty();
  }

  /**
   * Returns the next Ngram in the iteration.
   *
   * @return the next Ngram
   * @throws NoSuchElementException if there are no more Ngrams to iterate over
   */
  @Override
  public Ngram next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    Ngram result = current;
    current = current.dec();
    return result;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NgramIterator that = (NgramIterator) o;
    return Objects.equals(current, that.current);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(current);
  }
}

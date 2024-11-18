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

import com.squareup.moshi.ToJson;
import java.util.Objects;

/**
 * A class representing a fraction with a numerator and denominator. Provides methods for reducing
 * fractions to their lowest terms, comparing fractions, and converting them to different numeric
 * types.
 *
 * @author Peter M. Stahl <pemistahl@gmail.com>
 * @author Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
public class Fraction extends Number implements Comparable<Fraction> {

  private int numerator;
  private int denominator;

  public Fraction(final int numerator, final int denominator) {
    final int[] data = reduceToLowestTerms(numerator, denominator);
    this.numerator = data[0];
    this.denominator = data[1];
  }

  @Override
  public int compareTo(final Fraction other) {
    long n0d = (long) numerator * other.denominator;
    long d0n = (long) denominator * other.numerator;
    if (n0d < d0n) {
      return -1;
    } else if (n0d > d0n) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return numerator + "/" + denominator;
  }

  @Override
  public byte byteValue() {
    return (byte) (int) doubleValue();
  }

  @Override
  public double doubleValue() {
    return (double) numerator / denominator;
  }

  @Override
  public float floatValue() {
    return (float) doubleValue();
  }

  @Override
  public int intValue() {
    return (int) doubleValue();
  }

  @Override
  public long longValue() {
    return (long) doubleValue();
  }

  @Override
  public short shortValue() {
    return (short) (int) doubleValue();
  }

  /**
   * Reduces the fraction to its lowest terms.
   *
   * @param numerator The numerator of the fraction.
   * @param denominator The denominator of the fraction.
   * @return A Pair containing the reduced numerator and denominator.
   * @throws ArithmeticException if the denominator is zero.
   */
  private int[] reduceToLowestTerms(final int numerator, final int denominator) {
    int num = numerator;
    int den = denominator;

    if (den == 0) {
      throw new ArithmeticException("zero denominator in fraction '" + num + "/" + den + "'");
    }

    if (den < 0) {
      if (num == Integer.MIN_VALUE || den == Integer.MIN_VALUE) {
        throw new ArithmeticException("overflow in fraction " + this + ", cannot negate");
      }
      num = -num;
      den = -den;
    }

    int gcd = greatestCommonDenominator(num, den);

    if (gcd > 1) {
      num /= gcd;
      den /= gcd;
    }

    if (den < 0) {
      num = -num;
      den = -den;
    }

    return new int[] {num, den};
  }

  /**
   * Calculates the greatest common denominator (GCD) of two integers.
   *
   * @param a The first integer.
   * @param b The second integer.
   * @return The GCD of the two integers.
   * @throws ArithmeticException if an overflow occurs.
   */
  private int greatestCommonDenominator(final int a, final int b) {
    if (a == 0 || b == 0) {
      if (a == Integer.MIN_VALUE || b == Integer.MIN_VALUE) {
        throw new ArithmeticException(
            "overflow: greatestCommonDenominator(" + a + ", " + b + ") is 2^31");
      }
      return Math.abs(a + b);
    }

    int x = a;
    int y = b;
    long xl = x;
    long yl = y;
    boolean useLong = false;

    if (x < 0) {
      if (x == Integer.MIN_VALUE) {
        useLong = true;
      } else {
        x = -x;
      }
      xl = -xl;
    }

    if (y < 0) {
      if (y == Integer.MIN_VALUE) {
        useLong = true;
      } else {
        y = -y;
      }
      yl = -yl;
    }

    if (useLong) {
      if (xl == yl) {
        throw new ArithmeticException(
            "overflow: greatestCommonDenominator(" + a + ", " + b + ") is 2^31");
      }
      long ylyu = yl;
      yl = xl;
      xl = ylyu % xl;
      if (xl == 0L) {
        if (yl > Integer.MAX_VALUE) {
          throw new ArithmeticException(
              "overflow: greatestCommonDenominator(" + a + ", " + b + ") is 2^31");
        }
        return (int) yl;
      }
      ylyu = yl;

      y = (int) xl;
      x = (int) (ylyu % xl);
    }

    return greatestCommonDivisor(x, y);
  }

  /**
   * Computes the greatest common divisor (GCD) of two non-negative integers using binary GCD
   * algorithm.
   *
   * @param a The first integer.
   * @param b The second integer.
   * @return The GCD of the two integers.
   */
  private int greatestCommonDivisor(final int a, final int b) {
    assert a >= 0;
    assert b >= 0;

    if (a == 0) return b;
    if (b == 0) return a;

    int x = a;
    int y = b;

    int xTwos = numberOfTrailingZeros(x);
    int yTwos = numberOfTrailingZeros(y);
    int shift = Math.min(xTwos, yTwos);

    x = x >> xTwos;
    y = y >> yTwos;

    while (x != y) {
      int delta = x - y;
      y = Math.min(x, y);
      x = Math.abs(delta);
      x = x >> numberOfTrailingZeros(x);
    }

    return x << shift;
  }

  /**
   * Counts the number of trailing zeros in the binary representation of the given integer.
   *
   * @param i The integer whose trailing zeros are to be counted.
   * @return The number of trailing zeros in the binary representation of i.
   */
  private int numberOfTrailingZeros(final int i) {
    if (i == 0) return 32;

    int j = i;
    int n = 31;

    int y = j << 16;
    if (y != 0) {
      n -= 16;
      j = y;
    }

    y = j << 8;
    if (y != 0) {
      n -= 8;
      j = y;
    }

    y = j << 4;
    if (y != 0) {
      n -= 4;
      j = y;
    }

    y = j << 2;
    if (y != 0) {
      n -= 2;
      j = y;
    }

    return n - ((j << 1) >>> 31);
  }

  /**
   * Computes the absolute value of an integer.
   *
   * @param x The integer whose absolute value is to be computed.
   * @return The absolute value of x.
   */
  private int abs(final int x) {
    int i = x >>> 31;
    return (x ^ i) + i;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Fraction fraction = (Fraction) o;
    return numerator == fraction.numerator && denominator == fraction.denominator;
  }

  @Override
  public int hashCode() {
    return Objects.hash(numerator, denominator);
  }
}

/** A class to handle conversion of Fraction objects to JSON using Moshi. */
class FractionAdapter {

  /**
   * Converts a Fraction object to its string representation.
   *
   * @param fraction The Fraction object to be converted.
   * @return The string representation of the fraction.
   */
  @ToJson
  public String toJson(final Fraction fraction) {
    return fraction.toString();
  }
}

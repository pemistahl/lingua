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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Fraction class.
 *
 * @author Peter M. Stahl <pemistahl@gmail.com>
 * @author Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
class FractionTest {

  private final Fraction fraction1 = new Fraction(12, 144);
  private final Fraction fraction2 = new Fraction(63, 27);
  private final Fraction fraction3 = new Fraction(0, 1234);
  private final Fraction fraction4 = new Fraction(-42, 210);
  private final Fraction fraction5 = new Fraction(169, -65);

  /** Test that Fraction is correctly reduced to lowest terms. */
  @Test
  void assertThatFractionIsCorrectlyReducedToLowestTerms() {
    assertThat(fraction1).isEqualTo(new Fraction(1, 12));
    assertThat(fraction2).isEqualTo(new Fraction(7, 3));
    assertThat(fraction3).isEqualTo(new Fraction(0, 1));
    assertThat(fraction4).isEqualTo(new Fraction(-1, 5));
    assertThat(fraction5).isEqualTo(new Fraction(-13, 5));
  }

  /** Test that a Fraction with a zero denominator cannot be created. */
  @Test
  void assertThatFractionWithDenominatorZeroCannotBeCreated() {
    assertThatExceptionOfType(ArithmeticException.class)
        .isThrownBy(() -> new Fraction(1234, 0))
        .withMessage("zero denominator in fraction '1234/0'");
  }

  /** Test the toString() implementation of Fraction. */
  @Test
  void assertThatToStringImplementationOfFractionIsCorrect() {
    assertThat(fraction1.toString()).isEqualTo("1/12");
    assertThat(fraction2.toString()).isEqualTo("7/3");
    assertThat(fraction3.toString()).isEqualTo("0/1");
    assertThat(fraction4.toString()).isEqualTo("-1/5");
    assertThat(fraction5.toString()).isEqualTo("-13/5");
  }

  /** Test the doubleValue() implementation of Fraction. */
  @Test
  void assertThatToDoubleImplementationOfFractionIsCorrect() {
    assertThat(fraction1.doubleValue()).isEqualTo(1.0 / 12);
    assertThat(fraction2.doubleValue()).isEqualTo(7.0 / 3);
    assertThat(fraction3.doubleValue()).isEqualTo(0.0);
    assertThat(fraction4.doubleValue()).isEqualTo(-0.2);
    assertThat(fraction5.doubleValue()).isEqualTo(-2.6);
  }

  /** Test the floatValue() implementation of Fraction. */
  @Test
  void assertThatToFloatImplementationOfFractionIsCorrect() {
    assertThat(fraction1.floatValue()).isEqualTo(1.0f / 12);
    assertThat(fraction2.floatValue()).isEqualTo(7.0f / 3);
    assertThat(fraction3.floatValue()).isEqualTo(0.0f);
    assertThat(fraction4.floatValue()).isEqualTo(-0.2f);
    assertThat(fraction5.floatValue()).isEqualTo(-2.6f);
  }

  /** Test the intValue() implementation of Fraction. */
  @Test
  void assertThatToIntImplementationOfFractionIsCorrect() {
    assertThat(fraction1.intValue()).isEqualTo(0);
    assertThat(fraction2.intValue()).isEqualTo(2);
    assertThat(fraction3.intValue()).isEqualTo(0);
    assertThat(fraction4.intValue()).isEqualTo(0);
    assertThat(fraction5.intValue()).isEqualTo(-2);
  }

  /** Test the longValue() implementation of Fraction. */
  @Test
  void assertThatToLongImplementationOfFractionIsCorrect() {
    assertThat(fraction1.longValue()).isEqualTo(0);
    assertThat(fraction2.longValue()).isEqualTo(2);
    assertThat(fraction3.longValue()).isEqualTo(0);
    assertThat(fraction4.longValue()).isEqualTo(0);
    assertThat(fraction5.longValue()).isEqualTo(-2);
  }

  /** Test the shortValue() implementation of Fraction. */
  @Test
  void assertThatToShortImplementationOfFractionIsCorrect() {
    assertThat(fraction1.shortValue()).isEqualTo((short) 0);
    assertThat(fraction2.shortValue()).isEqualTo((short) 2);
    assertThat(fraction3.shortValue()).isEqualTo((short) 0);
    assertThat(fraction4.shortValue()).isEqualTo((short) 0);
    assertThat(fraction5.shortValue()).isEqualTo((short) -2);
  }

  /** Test the byteValue() implementation of Fraction. */
  @Test
  void assertThatToByteImplementationOfFractionIsCorrect() {
    assertThat(fraction1.byteValue()).isEqualTo((byte) 0);
    assertThat(fraction2.byteValue()).isEqualTo((byte) 2);
    assertThat(fraction3.byteValue()).isEqualTo((byte) 0);
    assertThat(fraction4.byteValue()).isEqualTo((byte) 0);
    assertThat(fraction5.byteValue()).isEqualTo((byte) -2);
  }

  /** Test that Fraction comparisons work correctly. */
  @Test
  void assertThatFractionComparisonsWorkCorrectly() {
    boolean[] comparisons = {
      fraction1.compareTo(fraction3) > 0,
      fraction1.compareTo(fraction4) > 0,
      fraction1.compareTo(fraction5) > 0,
      fraction2.compareTo(fraction1) > 0,
      fraction2.compareTo(fraction3) > 0,
      fraction2.compareTo(fraction4) > 0,
      fraction2.compareTo(fraction5) > 0,
      fraction3.compareTo(fraction4) > 0,
      fraction3.compareTo(fraction5) > 0,
      fraction4.compareTo(fraction5) > 0,
      fraction1.compareTo(fraction2) < 0,
      fraction3.compareTo(fraction1) < 0,
      fraction3.compareTo(fraction2) < 0,
      fraction4.compareTo(fraction1) < 0,
      fraction4.compareTo(fraction2) < 0,
      fraction4.compareTo(fraction3) < 0,
      fraction5.compareTo(fraction1) < 0,
      fraction5.compareTo(fraction2) < 0,
      fraction5.compareTo(fraction3) < 0,
      fraction5.compareTo(fraction4) < 0
    };

    for (boolean comparison : comparisons) {
      assertThat(comparison).isTrue();
    }
  }
}

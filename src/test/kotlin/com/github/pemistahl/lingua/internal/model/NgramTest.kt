/*
 * Copyright 2018-2019 Peter M. Stahl pemistahl@googlemail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pemistahl.lingua.internal.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class NgramTest {

    @Test
    fun `assert that Unigram is of correct length`() {
        assertThat(Unigram("a").value).isEqualTo("a")
        assertThatIllegalArgumentException()
            .isThrownBy { Unigram("ab") }
            .withMessage("value 'ab' must be of length 1 for type Unigram but is 2")
    }

    @Test
    fun `assert that Bigram is of correct length`() {
        assertThat(Bigram("ab").value).isEqualTo("ab")
        assertThatIllegalArgumentException()
            .isThrownBy { Bigram("a") }
            .withMessage("value 'a' must be of length 2 for type Bigram but is 1")
    }

    @Test
    fun `assert that Trigram is of correct length`() {
        assertThat(Trigram("abc").value).isEqualTo("abc")
        assertThatIllegalArgumentException()
            .isThrownBy { Trigram("abcd") }
            .withMessage("value 'abcd' must be of length 3 for type Trigram but is 4")
    }

    @Test
    fun `assert that Quadrigram is of correct length`() {
        assertThat(Quadrigram("abcd").value).isEqualTo("abcd")
        assertThatIllegalArgumentException()
            .isThrownBy { Quadrigram("abc") }
            .withMessage("value 'abc' must be of length 4 for type Quadrigram but is 3")
    }

    @Test
    fun `assert that Fivegram is of correct length`() {
        assertThat(Fivegram("abcde").value).isEqualTo("abcde")
        assertThatIllegalArgumentException()
            .isThrownBy { Fivegram("") }
            .withMessage("value '' must be of length 5 for type Fivegram but is 0")
    }

    @Test
    fun `assert that Sixgram is of correct length`() {
        assertThat(Sixgram("abcdef").value).isEqualTo("abcdef")
        assertThatIllegalArgumentException()
            .isThrownBy { Sixgram("abcd") }
            .withMessage("value 'abcd' must be of length 6 for type Sixgram but is 4")
    }
}

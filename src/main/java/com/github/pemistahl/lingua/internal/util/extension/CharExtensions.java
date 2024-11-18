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

import com.github.pemistahl.lingua.internal.Alphabet;
import com.github.pemistahl.lingua.internal.Constant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility functions related to characters and logograms.
 *
 * <p>This class provides utility methods for checking whether a character is a logogram by
 * verifying whether it belongs to specific scripts. The logograms are cached for performance,
 * preventing repeated evaluations of the same information.
 *
 * @author Peter M. Stahl <pemistahl@gmail.com>
 * @author Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
public class CharExtensions {

  private static final Set<Alphabet> scriptsWithLogograms =
      Constant.LANGUAGES_SUPPORTING_LOGOGRAMS.stream()
          .flatMap(language -> language.getAlphabets().stream())
          .collect(Collectors.toSet());

  public static boolean isLogogram(final char ch) {
    // Return false if the character is a whitespace
    if (Character.isWhitespace(ch)) {
      return false;
    }

    return scriptsWithLogograms.stream().anyMatch(alphabet -> alphabet.matches(ch));
  }
}

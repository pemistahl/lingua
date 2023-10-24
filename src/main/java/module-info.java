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

module com.github.pemistahl.lingua {
    exports com.github.pemistahl.lingua.api;
    exports com.github.pemistahl.lingua.api.io;

    requires kotlin.stdlib;

    requires it.unimi.dsi.fastutil;
    requires okio;

    // Moshi accesses JSON serializer using reflection; must open the package
    // TODO: Once new Moshi version has module names, change it to `opens ... to com.squareup.moshi`
    //       and comment in the `requires` declarations below
    opens com.github.pemistahl.lingua.internal;
    // requires com.squareup.moshi;
    // requires com.squareup.moshi.kotlin;
}

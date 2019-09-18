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

package com.github.pemistahl.lingua.api

enum class IsoCode639_1 {
    AF,
    AR,
    BE,
    BG,
    BN,
    CA,
    CS,
    CY,
    DA,
    DE,
    EL,
    EN,
    ES,
    ET,
    EU,
    FA,
    FI,
    FR,
    GA,
    GU,
    HE,
    HI,
    HR,
    HU,
    ID,
    IS,
    IT,
    JA,
    KO,
    LA,
    LT,
    LV,
    MS,
    NB,
    NL,
    NN,
    NO,
    PA,
    PL,
    PT,
    RO,
    RU,
    SK,
    SL,
    SO,
    SQ,
    SV,
    TA,
    TE,
    TH,
    TL,
    TR,
    UR,
    VI,
    ZH,

    UNKNOWN;

    override fun toString() = this.name.toLowerCase()
}

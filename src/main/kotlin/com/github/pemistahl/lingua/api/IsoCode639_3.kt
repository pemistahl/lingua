/*
 * Copyright © 2018-2019 Peter M. Stahl pemistahl@gmail.com
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

package com.github.pemistahl.lingua.api

enum class IsoCode639_3 {
    AFR,
    ARA,
    AZE,
    BEL,
    BEN,
    BOS,
    BUL,
    CAT,
    CES,
    CYM,
    DAN,
    DEU,
    ELL,
    ENG,
    EPO,
    EST,
    EUS,
    FAS,
    FIN,
    FRA,
    GLE,
    GUJ,
    HEB,
    HIN,
    HRV,
    HUN,
    HYE,
    IND,
    ISL,
    ITA,
    JPN,
    KAT,
    KOR,
    LAT,
    LAV,
    LIT,
    MKD,
    MON,
    MSA,
    NLD,
    NNO,
    NOB,
    NOR,
    PAN,
    POL,
    POR,
    RON,
    RUS,
    SLK,
    SLV,
    SOM,
    SPA,
    SQI,
    SRP,
    SWE,
    TAM,
    TEL,
    TGL,
    THA,
    TUR,
    UKR,
    URD,
    VIE,
    ZHO,

    UNKNOWN;

    override fun toString() = this.name.toLowerCase()
}

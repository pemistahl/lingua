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

import com.github.pemistahl.lingua.api.Language
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap
import org.mapdb.DBException
import org.mapdb.Serializer
import org.mapdb.SortedTableMap
import org.mapdb.volume.MappedFileVol
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Paths

internal class LanguageModelDeserializer<T : Ngram>(
    val useMapDBCache: Boolean
) : JsonDeserializer<LanguageModel<T, T>> {

    override fun deserialize(
        json: JsonElement,
        type: Type,
        context: JsonDeserializationContext
    ): LanguageModel<T, T> {

        val jsonObj = json.asJsonObject
        val language = Language.valueOf(jsonObj["language"].asString)
        val ngramRelativeFrequencies = if (useMapDBCache)
            readNgramsFromMapDB<T>(jsonObj, language, type)
        else
            loadNgramsIntoMemory(jsonObj)

        return LanguageModel(language, ngramRelativeFrequencies)
    }

    internal fun <T : Ngram> readNgramsFromMapDB(
        jsonObj: JsonObject,
        language: Language,
        type: Type
    ): Map<String, Double> {

        @Suppress("UNCHECKED_CAST")
        val ngramClass = (type as ParameterizedType).actualTypeArguments[0] as Class<T>

        val userHomeDirectory = System.getProperty("user.home")
        val mapdbDirectoryPath = Paths.get(userHomeDirectory, "lingua-mapdb-files", language.isoCode.toString())
        val mapdbFileName = "${ngramClass.simpleName}s_$language.mapdb"
        val mapdbFilePath = Paths.get(mapdbDirectoryPath.toString(), mapdbFileName)

        if (!Files.isDirectory(mapdbDirectoryPath)) {
            Files.createDirectories(mapdbDirectoryPath)
        }

        if (Files.exists(mapdbFilePath)) {
            try {
                val volume = MappedFileVol.FACTORY.makeVolume(mapdbFilePath.toString(), true)
                return SortedTableMap.open(volume, Serializer.STRING, Serializer.DOUBLE)
            } catch (e: DBException.DataCorruption) {
                // try to recreate the database
                Files.delete(mapdbFilePath)
            }
        }
        val volume = MappedFileVol.FACTORY.makeVolume(mapdbFilePath.toString(), false)
        val sink: SortedTableMap.Sink<String, Double> = SortedTableMap
            .create(volume, Serializer.STRING, Serializer.DOUBLE)
            .pageSize(1 * 1024) // page size of 1KB
            .nodeSize(1)
            .createFromSink()

        val ngramsJsonObj = jsonObj["ngrams"].asJsonObject
        val tempMap = hashMapOf<String, Double>()

        for ((fractionLiteral, ngramsJsonElem) in ngramsJsonObj.entrySet()) {
            val fractionParts = fractionLiteral.split('/').map { it.toInt() }
            val probability = fractionParts[0].toDouble() / fractionParts[1]

            for (ngramJsonElem in ngramsJsonElem.asString.split(' ')) {
                tempMap[ngramJsonElem] = probability
            }
        }

        for ((key, value) in tempMap.toList().sortedBy { it.first }) {
            sink.put(key, value)
        }

        return sink.create()
    }

    internal fun loadNgramsIntoMemory(jsonObj: JsonObject): Map<String, Double> {
        val ngramRelativeFrequencies = Object2DoubleOpenHashMap<String>()
        val ngramsJsonObj = jsonObj["ngrams"].asJsonObject

        for ((fractionLiteral, ngramsJsonElem) in ngramsJsonObj.entrySet()) {
            val fractionParts = fractionLiteral.split('/').map { it.toInt() }
            val probability = fractionParts[0].toDouble() / fractionParts[1]

            for (ngramJsonElem in ngramsJsonElem.asString.split(' ')) {
                ngramRelativeFrequencies[ngramJsonElem] = probability
            }
        }
        return ngramRelativeFrequencies
    }
}

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

import static com.github.pemistahl.lingua.internal.util.extension.MapExtensions.incrementCounter;

import com.github.pemistahl.lingua.api.Language;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okio.Okio;

/**
 * This class represents a training data language model. It contains methods to generate a language
 * model from training data, convert it to JSON format, and read it from JSON.
 *
 * @author Peter M. Stahl <pemistahl@gmail.com>
 * @author Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
public class TrainingDataLanguageModel {

  private static final String LANGUAGE_NAME = "language";
  private static final String NGRAMS_NAME = "ngrams";

  private static final JsonAdapter<JsonLanguageModel> JSON_ADAPTER =
      new Moshi.Builder()
          .add(new FractionAdapter())
          .addLast(new KotlinJsonAdapterFactory())
          .build()
          .adapter(JsonLanguageModel.class);

  private final Language language;
  private final Map<Ngram, Integer> absoluteFrequencies;
  private final Map<Ngram, Fraction> relativeFrequencies;

  public TrainingDataLanguageModel(
      final Language language,
      final Map<Ngram, Integer> absoluteFrequencies,
      final Map<Ngram, Fraction> relativeFrequencies) {
    this.language = language;
    this.absoluteFrequencies = absoluteFrequencies;
    this.relativeFrequencies = relativeFrequencies;
  }

  public Language getLanguage() {
    return language;
  }

  public Map<Ngram, Integer> getAbsoluteFrequencies() {
    return absoluteFrequencies;
  }

  public Map<Ngram, Fraction> getRelativeFrequencies() {
    return relativeFrequencies;
  }

  /**
   * Converts this language model to a JSON string.
   *
   * @return The JSON representation of the language model.
   */
  public String toJson() {

    Map<Fraction, List<Ngram>> allNgrams = new HashMap<>();
    for (Map.Entry<Ngram, Fraction> entry : relativeFrequencies.entrySet()) {
      allNgrams.computeIfAbsent(entry.getValue(), k -> new ArrayList<>()).add(entry.getKey());
    }

    Map<Fraction, String> ngrams =
        allNgrams.entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry ->
                        entry.getValue().stream()
                            .map(Ngram::getValue)
                            .collect(Collectors.joining(" "))));

    return JSON_ADAPTER.toJson(new JsonLanguageModel(language, ngrams));
  }

  /**
   * Creates a training data language model from a sequence of text.
   *
   * @param text The sequence of text to analyze.
   * @param language The language of the model.
   * @param ngramLength The length of the n-grams.
   * @param charClass A string representing the set of valid characters for n-grams.
   * @param lowerNgramAbsoluteFrequencies Frequencies of lower n-grams.
   * @return A TrainingDataLanguageModel object.
   */
  public static TrainingDataLanguageModel fromText(
      final Iterable<String> text,
      final Language language,
      final int ngramLength,
      final String charClass,
      final Map<Ngram, Integer> lowerNgramAbsoluteFrequencies) {

    if (ngramLength < 1 || ngramLength > 5) {
      throw new IllegalArgumentException("ngram length " + ngramLength + " is not in range 1..5");
    }

    Map<Ngram, Integer> absoluteFrequencies =
        computeAbsoluteFrequencies(text, ngramLength, charClass);
    Map<Ngram, Fraction> relativeFrequencies =
        computeRelativeFrequencies(ngramLength, absoluteFrequencies, lowerNgramAbsoluteFrequencies);

    return new TrainingDataLanguageModel(language, absoluteFrequencies, relativeFrequencies);
  }

  /**
   * Reads a JSON representation of a language model from an InputStream.
   *
   * @param json The InputStream containing the JSON data.
   * @return A map of n-gram frequencies.
   * @throws java.io.IOException If there is an error reading the InputStream.
   */
  public static Object2FloatMap<String> fromJson(final InputStream json)
      throws java.io.IOException {
    try (final JsonReader reader = JsonReader.of(Okio.buffer(Okio.source(json)))) {
      Object2FloatOpenHashMap<String> frequencies = new Object2FloatOpenHashMap<>();
      reader.beginObject();

      while (reader.hasNext()) {
        String name = reader.nextName();
        if (name.equals(LANGUAGE_NAME)) {
          reader.skipValue();
        } else if (name.equals(NGRAMS_NAME)) {
          reader.beginObject();
          while (reader.hasNext()) {
            String[] parts = reader.nextName().split("/");
            float numerator = Float.parseFloat(parts[0]);
            int denominator = Integer.parseInt(parts[1]);
            float frequency = numerator / denominator;

            String ngrams = reader.nextString();
            for (String ngram : ngrams.split(" ")) {
              frequencies.put(ngram, frequency);
            }
          }
          reader.endObject();
        } else {
          throw new AssertionError("Unexpected name in language model JSON");
        }
      }

      reader.endObject();

      // Rehashes the map, making the table as small as possible.
      // Trim to reduce in-memory model size
      frequencies.trim();
      return frequencies;
    }
  }

  private static Map<Ngram, Integer> computeAbsoluteFrequencies(
      final Iterable<String> text, final int ngramLength, final String charClass) {

    Map<Ngram, Integer> absoluteFrequencies = new HashMap<>();
    String regex = "[" + charClass + "]+";

    for (String line : text) {
      String lowerCasedLine = line.toLowerCase();
      for (int idx = 0; idx <= lowerCasedLine.length() - ngramLength; idx++) {
        String textSlice = lowerCasedLine.substring(idx, idx + ngramLength);
        if (textSlice.matches(regex)) {
          Ngram ngram = new Ngram(textSlice);
          incrementCounter(absoluteFrequencies, ngram);
        }
      }
    }

    return absoluteFrequencies;
  }

  private static Map<Ngram, Fraction> computeRelativeFrequencies(
      int ngramLength,
      Map<Ngram, Integer> absoluteFrequencies,
      Map<Ngram, Integer> lowerNgramAbsoluteFrequencies) {

    Map<Ngram, Fraction> ngramProbabilities = new HashMap<>();
    int totalNgramFrequency =
        absoluteFrequencies.values().stream().mapToInt(Integer::intValue).sum();

    for (Map.Entry<Ngram, Integer> entry : absoluteFrequencies.entrySet()) {
      Ngram ngram = entry.getKey();
      int frequency = entry.getValue();
      int denominator =
          (ngramLength == 1 || lowerNgramAbsoluteFrequencies.isEmpty())
              ? totalNgramFrequency
              : lowerNgramAbsoluteFrequencies.getOrDefault(
                  new Ngram(ngram.getValue().substring(0, ngramLength - 1)), 0);

      ngramProbabilities.put(ngram, new Fraction(frequency, denominator));
    }

    return ngramProbabilities;
  }

  /**
   * A class that represents a language model in JSON format. It holds the language and the n-grams
   * as fractions.
   */
  private static class JsonLanguageModel {

    private final Language language;
    private final Map<Fraction, String> ngrams;

    public JsonLanguageModel(final Language language, final Map<Fraction, String> ngrams) {
      this.language = language;
      this.ngrams = ngrams;
    }

    public Language getLanguage() {
      return language;
    }

    public Map<Fraction, String> getNgrams() {
      return ngrams;
    }
  }
}

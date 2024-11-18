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

import com.github.pemistahl.lingua.api.Language;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the TrainingDataLanguageModel class.
 *
 * <p>These tests ensure that the language models are correctly generated, serialized, and
 * deserialized.
 *
 * @author Peter M. Stahl <pemistahl@gmail.com>
 * @author Migration to Java from Kotlin by Alexander Zagniotov <azagniotov@gmail.com>
 */
public class TrainingDataLanguageModelTest {
  // Be very careful to not auto-format or line break the text, the tests will fail.
  private static final String TEXT =
      ("These sentences are intended for testing purposes. "
              + "Do not use them in production! "
              + "By the way, they consist of 23 words in total.")
          .toLowerCase()
          .trim();

  private static final Iterable<String> ITERABLE_OF_STRINGS =
      new ArrayList<>(Arrays.asList(TEXT.split("\n")));

  private static final Function<Map.Entry<String, ?>, Ngram> KEY_MAPPER =
      entry -> new Ngram(entry.getKey());

  private static final Function<Map.Entry<Ngram, String>, Fraction> VALUE_MAPPER =
      entry -> {
        final String[] parts = entry.getValue().split("/");
        return new Fraction(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
      };

  private final String expectedUnigramLanguageModel =
      ("{\n"
              + "                \"language\":\"ENGLISH\",\n"
              + "                \"ngrams\":{\n"
              + "                    \"13/100\":\"t\",\n"
              + "                    \"1/25\":\"h\",\n"
              + "                    \"7/50\":\"e\",\n"
              + "                    \"1/10\":\"s n o\",\n"
              + "                    \"3/100\":\"c a p u y\",\n"
              + "                    \"1/20\":\"r d\",\n"
              + "                    \"3/50\":\"i\",\n"
              + "                    \"1/50\":\"f w\",\n"
              + "                    \"1/100\":\"g m b l\"\n"
              + "                }\n"
              + "            }")
          .replaceAll("\n\\s*", "");

  private final Map<Ngram, Integer> expectedUnigramAbsoluteFrequencies =
      Map.ofEntries(
              Map.entry("a", 3),
              Map.entry("b", 1),
              Map.entry("c", 3),
              Map.entry("d", 5),
              Map.entry("e", 14),
              Map.entry("f", 2),
              Map.entry("g", 1),
              Map.entry("h", 4),
              Map.entry("i", 6),
              Map.entry("l", 1),
              Map.entry("m", 1),
              Map.entry("n", 10),
              Map.entry("o", 10),
              Map.entry("p", 3),
              Map.entry("r", 5),
              Map.entry("s", 10),
              Map.entry("t", 13),
              Map.entry("u", 3),
              Map.entry("w", 2),
              Map.entry("y", 3))
          .entrySet().stream()
          .collect(Collectors.toMap(KEY_MAPPER, Map.Entry::getValue));

  private final Map<Ngram, Fraction> expectedUnigramRelativeFrequencies =
      Map.ofEntries(
              Map.entry("a", "3/100"),
              Map.entry("b", "1/100"),
              Map.entry("c", "3/100"),
              Map.entry("d", "1/20"),
              Map.entry("e", "7/50"),
              Map.entry("f", "1/50"),
              Map.entry("g", "1/100"),
              Map.entry("h", "1/25"),
              Map.entry("i", "3/50"),
              Map.entry("l", "1/100"),
              Map.entry("m", "1/100"),
              Map.entry("n", "1/10"),
              Map.entry("o", "1/10"),
              Map.entry("p", "3/100"),
              Map.entry("r", "1/20"),
              Map.entry("s", "1/10"),
              Map.entry("t", "13/100"),
              Map.entry("u", "3/100"),
              Map.entry("w", "1/50"),
              Map.entry("y", "3/100"))
          .entrySet().stream()
          .collect(
              Collectors.toMap(
                  KEY_MAPPER,
                  entry ->
                      VALUE_MAPPER.apply(Map.entry(new Ngram(entry.getKey()), entry.getValue()))));

  private final Map<Ngram, Integer> expectedBigramAbsoluteFrequencies =
      Map.<String, Integer>ofEntries(
              Map.entry("de", 1),
              Map.entry("pr", 1),
              Map.entry("pu", 1),
              Map.entry("do", 1),
              Map.entry("uc", 1),
              Map.entry("ds", 1),
              Map.entry("du", 1),
              Map.entry("ur", 1),
              Map.entry("us", 1),
              Map.entry("ed", 1),
              Map.entry("in", 4),
              Map.entry("io", 1),
              Map.entry("em", 1),
              Map.entry("en", 3),
              Map.entry("is", 1),
              Map.entry("al", 1),
              Map.entry("es", 4),
              Map.entry("ar", 1),
              Map.entry("rd", 1),
              Map.entry("re", 1),
              Map.entry("ey", 1),
              Map.entry("nc", 1),
              Map.entry("nd", 1),
              Map.entry("ay", 1),
              Map.entry("ng", 1),
              Map.entry("ro", 1),
              Map.entry("rp", 1),
              Map.entry("no", 1),
              Map.entry("ns", 1),
              Map.entry("nt", 2),
              Map.entry("fo", 1),
              Map.entry("wa", 1),
              Map.entry("se", 4),
              Map.entry("od", 1),
              Map.entry("si", 1),
              Map.entry("of", 1),
              Map.entry("by", 1),
              Map.entry("wo", 1),
              Map.entry("on", 2),
              Map.entry("st", 2),
              Map.entry("ce", 1),
              Map.entry("or", 2),
              Map.entry("os", 1),
              Map.entry("ot", 2),
              Map.entry("co", 1),
              Map.entry("ta", 1),
              Map.entry("ct", 1),
              Map.entry("te", 3),
              Map.entry("th", 4),
              Map.entry("ti", 2),
              Map.entry("to", 1),
              Map.entry("he", 4),
              Map.entry("po", 1))
          .entrySet().stream()
          .collect(Collectors.toMap(KEY_MAPPER, Map.Entry::getValue));

  private final Map<Ngram, Fraction> expectedBigramRelativeFrequencies =
      Map.<String, String>ofEntries(
              Map.entry("de", "1/5"),
              Map.entry("pr", "1/3"),
              Map.entry("pu", "1/3"),
              Map.entry("do", "1/5"),
              Map.entry("uc", "1/3"),
              Map.entry("ds", "1/5"),
              Map.entry("du", "1/5"),
              Map.entry("ur", "1/3"),
              Map.entry("us", "1/3"),
              Map.entry("ed", "1/14"),
              Map.entry("in", "2/3"),
              Map.entry("io", "1/6"),
              Map.entry("em", "1/14"),
              Map.entry("en", "3/14"),
              Map.entry("is", "1/6"),
              Map.entry("al", "1/3"),
              Map.entry("es", "2/7"),
              Map.entry("ar", "1/3"),
              Map.entry("rd", "1/5"),
              Map.entry("re", "1/5"),
              Map.entry("ey", "1/14"),
              Map.entry("nc", "1/10"),
              Map.entry("nd", "1/10"),
              Map.entry("ay", "1/3"),
              Map.entry("ng", "1/10"),
              Map.entry("ro", "1/5"),
              Map.entry("rp", "1/5"),
              Map.entry("no", "1/10"),
              Map.entry("ns", "1/10"),
              Map.entry("nt", "1/5"),
              Map.entry("fo", "1/2"),
              Map.entry("wa", "1/2"),
              Map.entry("se", "2/5"),
              Map.entry("od", "1/10"),
              Map.entry("si", "1/10"),
              Map.entry("of", "1/10"),
              Map.entry("by", "1/1"),
              Map.entry("wo", "1/2"),
              Map.entry("on", "1/5"),
              Map.entry("st", "1/5"),
              Map.entry("ce", "1/3"),
              Map.entry("or", "1/5"),
              Map.entry("os", "1/10"),
              Map.entry("ot", "1/5"),
              Map.entry("co", "1/3"),
              Map.entry("ta", "1/13"),
              Map.entry("ct", "1/3"),
              Map.entry("te", "3/13"),
              Map.entry("th", "4/13"),
              Map.entry("ti", "2/13"),
              Map.entry("to", "1/13"),
              Map.entry("he", "1/1"),
              Map.entry("po", "1/3"))
          .entrySet().stream()
          .collect(
              Collectors.toMap(
                  KEY_MAPPER,
                  entry ->
                      VALUE_MAPPER.apply(Map.entry(new Ngram(entry.getKey()), entry.getValue()))));

  private final Map<Ngram, Integer> expectedTrigramAbsoluteFrequencies =
      Map.ofEntries(
              Map.entry("rds", 1),
              Map.entry("ose", 1),
              Map.entry("ded", 1),
              Map.entry("con", 1),
              Map.entry("use", 1),
              Map.entry("est", 1),
              Map.entry("ion", 1),
              Map.entry("ist", 1),
              Map.entry("pur", 1),
              Map.entry("hem", 1),
              Map.entry("hes", 1),
              Map.entry("tin", 1),
              Map.entry("cti", 1),
              Map.entry("wor", 1),
              Map.entry("tio", 1),
              Map.entry("ten", 2),
              Map.entry("ota", 1),
              Map.entry("hey", 1),
              Map.entry("tal", 1),
              Map.entry("tes", 1),
              Map.entry("uct", 1),
              Map.entry("sti", 1),
              Map.entry("pro", 1),
              Map.entry("odu", 1),
              Map.entry("nsi", 1),
              Map.entry("rod", 1),
              Map.entry("for", 1),
              Map.entry("ces", 1),
              Map.entry("nce", 1),
              Map.entry("not", 1),
              Map.entry("pos", 1),
              Map.entry("are", 1),
              Map.entry("tot", 1),
              Map.entry("end", 1),
              Map.entry("enc", 1),
              Map.entry("sis", 1),
              Map.entry("sen", 1),
              Map.entry("nte", 2),
              Map.entry("ord", 1),
              Map.entry("ses", 1),
              Map.entry("ing", 1),
              Map.entry("ent", 1),
              Map.entry("way", 1),
              Map.entry("nde", 1),
              Map.entry("int", 1),
              Map.entry("rpo", 1),
              Map.entry("the", 4),
              Map.entry("urp", 1),
              Map.entry("duc", 1),
              Map.entry("ons", 1),
              Map.entry("ese", 1))
          .entrySet().stream()
          .collect(Collectors.toMap(KEY_MAPPER, Map.Entry::getValue));

  private final Map<Ngram, Fraction> expectedTrigramRelativeFrequencies =
      Map.ofEntries(
              Map.entry("rds", "1/1"),
              Map.entry("ose", "1/1"),
              Map.entry("ded", "1/1"),
              Map.entry("con", "1/1"),
              Map.entry("use", "1/1"),
              Map.entry("est", "1/4"),
              Map.entry("ion", "1/1"),
              Map.entry("ist", "1/1"),
              Map.entry("pur", "1/1"),
              Map.entry("hem", "1/4"),
              Map.entry("hes", "1/4"),
              Map.entry("tin", "1/2"),
              Map.entry("cti", "1/1"),
              Map.entry("wor", "1/1"),
              Map.entry("tio", "1/2"),
              Map.entry("ten", "2/3"),
              Map.entry("ota", "1/2"),
              Map.entry("hey", "1/4"),
              Map.entry("tal", "1/1"),
              Map.entry("tes", "1/3"),
              Map.entry("uct", "1/1"),
              Map.entry("sti", "1/2"),
              Map.entry("pro", "1/1"),
              Map.entry("odu", "1/1"),
              Map.entry("nsi", "1/1"),
              Map.entry("rod", "1/1"),
              Map.entry("for", "1/1"),
              Map.entry("ces", "1/1"),
              Map.entry("nce", "1/1"),
              Map.entry("not", "1/1"),
              Map.entry("pos", "1/1"),
              Map.entry("are", "1/1"),
              Map.entry("tot", "1/1"),
              Map.entry("end", "1/3"),
              Map.entry("enc", "1/3"),
              Map.entry("sis", "1/1"),
              Map.entry("sen", "1/4"),
              Map.entry("nte", "1/1"),
              Map.entry("ord", "1/2"),
              Map.entry("ses", "1/4"),
              Map.entry("ing", "1/4"),
              Map.entry("ent", "1/3"),
              Map.entry("way", "1/1"),
              Map.entry("nde", "1/1"),
              Map.entry("int", "1/4"),
              Map.entry("rpo", "1/1"),
              Map.entry("the", "1/1"),
              Map.entry("urp", "1/1"),
              Map.entry("duc", "1/1"),
              Map.entry("ons", "1/2"),
              Map.entry("ese", "1/4"))
          .entrySet().stream()
          .collect(
              Collectors.toMap(
                  KEY_MAPPER,
                  entry ->
                      VALUE_MAPPER.apply(Map.entry(new Ngram(entry.getKey()), entry.getValue()))));

  private final Map<Ngram, Integer> expectedQuadrigramAbsoluteFrequencies =
      Map.ofEntries(
              Map.entry("onsi", 1),
              Map.entry("sist", 1),
              Map.entry("ende", 1),
              Map.entry("ords", 1),
              Map.entry("esti", 1),
              Map.entry("oduc", 1),
              Map.entry("nces", 1),
              Map.entry("tenc", 1),
              Map.entry("tend", 1),
              Map.entry("thes", 1),
              Map.entry("rpos", 1),
              Map.entry("ting", 1),
              Map.entry("nsis", 1),
              Map.entry("nten", 2),
              Map.entry("tota", 1),
              Map.entry("they", 1),
              Map.entry("cons", 1),
              Map.entry("tion", 1),
              Map.entry("prod", 1),
              Map.entry("otal", 1),
              Map.entry("test", 1),
              Map.entry("ence", 1),
              Map.entry("pose", 1),
              Map.entry("oses", 1),
              Map.entry("nded", 1),
              Map.entry("inte", 1),
              Map.entry("them", 1),
              Map.entry("urpo", 1),
              Map.entry("duct", 1),
              Map.entry("sent", 1),
              Map.entry("stin", 1),
              Map.entry("ucti", 1),
              Map.entry("ente", 1),
              Map.entry("purp", 1),
              Map.entry("ctio", 1),
              Map.entry("rodu", 1),
              Map.entry("word", 1),
              Map.entry("hese", 1))
          .entrySet().stream()
          .collect(Collectors.toMap(KEY_MAPPER, Map.Entry::getValue));

  private final Map<Ngram, Fraction> expectedQuadrigramRelativeFrequencies =
      Map.ofEntries(
              Map.entry("onsi", "1/1"),
              Map.entry("sist", "1/1"),
              Map.entry("ende", "1/1"),
              Map.entry("ords", "1/1"),
              Map.entry("esti", "1/1"),
              Map.entry("oduc", "1/1"),
              Map.entry("nces", "1/1"),
              Map.entry("tenc", "1/2"),
              Map.entry("tend", "1/2"),
              Map.entry("thes", "1/4"),
              Map.entry("rpos", "1/1"),
              Map.entry("ting", "1/1"),
              Map.entry("nsis", "1/1"),
              Map.entry("nten", "1/1"),
              Map.entry("tota", "1/1"),
              Map.entry("they", "1/4"),
              Map.entry("cons", "1/1"),
              Map.entry("tion", "1/1"),
              Map.entry("prod", "1/1"),
              Map.entry("otal", "1/1"),
              Map.entry("test", "1/1"),
              Map.entry("ence", "1/1"),
              Map.entry("pose", "1/1"),
              Map.entry("oses", "1/1"),
              Map.entry("nded", "1/1"),
              Map.entry("inte", "1/1"),
              Map.entry("them", "1/4"),
              Map.entry("urpo", "1/1"),
              Map.entry("duct", "1/1"),
              Map.entry("sent", "1/1"),
              Map.entry("stin", "1/1"),
              Map.entry("ucti", "1/1"),
              Map.entry("ente", "1/1"),
              Map.entry("purp", "1/1"),
              Map.entry("ctio", "1/1"),
              Map.entry("rodu", "1/1"),
              Map.entry("word", "1/1"),
              Map.entry("hese", "1/1"))
          .entrySet().stream()
          .collect(
              Collectors.toMap(
                  KEY_MAPPER,
                  entry ->
                      VALUE_MAPPER.apply(Map.entry(new Ngram(entry.getKey()), entry.getValue()))));

  private final Map<Ngram, Integer> expectedFivegramAbsoluteFrequencies =
      Map.ofEntries(
              Map.entry("testi", 1),
              Map.entry("sente", 1),
              Map.entry("ences", 1),
              Map.entry("tende", 1),
              Map.entry("ducti", 1),
              Map.entry("ntenc", 1),
              Map.entry("these", 1),
              Map.entry("onsis", 1),
              Map.entry("ntend", 1),
              Map.entry("total", 1),
              Map.entry("uctio", 1),
              Map.entry("enten", 1),
              Map.entry("poses", 1),
              Map.entry("ction", 1),
              Map.entry("produ", 1),
              Map.entry("inten", 1),
              Map.entry("nsist", 1),
              Map.entry("words", 1),
              Map.entry("sting", 1),
              Map.entry("purpo", 1),
              Map.entry("tence", 1),
              Map.entry("estin", 1),
              Map.entry("roduc", 1),
              Map.entry("urpos", 1),
              Map.entry("rpose", 1),
              Map.entry("ended", 1),
              Map.entry("oduct", 1),
              Map.entry("consi", 1))
          .entrySet().stream()
          .collect(Collectors.toMap(KEY_MAPPER, Map.Entry::getValue));

  private final Map<Ngram, Fraction> expectedFivegramRelativeFrequencies =
      Map.ofEntries(
              Map.entry("testi", "1/1"),
              Map.entry("sente", "1/1"),
              Map.entry("ences", "1/1"),
              Map.entry("tende", "1/1"),
              Map.entry("ducti", "1/1"),
              Map.entry("ntenc", "1/2"),
              Map.entry("these", "1/1"),
              Map.entry("onsis", "1/1"),
              Map.entry("ntend", "1/2"),
              Map.entry("total", "1/1"),
              Map.entry("uctio", "1/1"),
              Map.entry("enten", "1/1"),
              Map.entry("poses", "1/1"),
              Map.entry("ction", "1/1"),
              Map.entry("produ", "1/1"),
              Map.entry("inten", "1/1"),
              Map.entry("nsist", "1/1"),
              Map.entry("words", "1/1"),
              Map.entry("sting", "1/1"),
              Map.entry("purpo", "1/1"),
              Map.entry("tence", "1/1"),
              Map.entry("estin", "1/1"),
              Map.entry("roduc", "1/1"),
              Map.entry("urpos", "1/1"),
              Map.entry("rpose", "1/1"),
              Map.entry("ended", "1/1"),
              Map.entry("oduct", "1/1"),
              Map.entry("consi", "1/1"))
          .entrySet().stream()
          .collect(
              Collectors.toMap(
                  KEY_MAPPER,
                  entry ->
                      VALUE_MAPPER.apply(Map.entry(new Ngram(entry.getKey()), entry.getValue()))));

  private final Map<Ngram, Float> expectedUnigramJsonRelativeFrequencies =
      expectedUnigramRelativeFrequencies.entrySet().stream()
          .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().floatValue()));

  @Test
  public void assertThatUnigramLanguageModelCanBeCreatedFromTrainingData() {
    TrainingDataLanguageModel model =
        TrainingDataLanguageModel.fromText(
            ITERABLE_OF_STRINGS,
            Language.ENGLISH,
            1,
            "\\p{L}&&\\p{IsLatin}",
            Collections.emptyMap());

    assertThat(model.getLanguage()).isEqualTo(Language.ENGLISH);
    assertThat(model.getAbsoluteFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedUnigramAbsoluteFrequencies);
    assertThat(model.getRelativeFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedUnigramRelativeFrequencies);
  }

  @Test
  public void assertThatBigramLanguageModelCanBeCreatedFromTrainingData() {
    TrainingDataLanguageModel model =
        TrainingDataLanguageModel.fromText(
            ITERABLE_OF_STRINGS,
            Language.ENGLISH,
            2,
            "\\p{L}&&\\p{IsLatin}",
            expectedUnigramAbsoluteFrequencies);

    assertThat(model.getLanguage()).isEqualTo(Language.ENGLISH);
    assertThat(model.getAbsoluteFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedBigramAbsoluteFrequencies);
    assertThat(model.getRelativeFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedBigramRelativeFrequencies);
  }

  @Test
  public void assertThatTrigramLanguageModelCanBeCreatedFromTrainingData() {
    TrainingDataLanguageModel model =
        TrainingDataLanguageModel.fromText(
            ITERABLE_OF_STRINGS,
            Language.ENGLISH,
            3,
            "\\p{L}&&\\p{IsLatin}",
            expectedBigramAbsoluteFrequencies);

    assertThat(model.getLanguage()).isEqualTo(Language.ENGLISH);
    assertThat(model.getAbsoluteFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedTrigramAbsoluteFrequencies);
    assertThat(model.getRelativeFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedTrigramRelativeFrequencies);
  }

  @Test
  public void assertThatQuadrigramLanguageModelCanBeCreatedFromTrainingData() {
    TrainingDataLanguageModel model =
        TrainingDataLanguageModel.fromText(
            ITERABLE_OF_STRINGS,
            Language.ENGLISH,
            4,
            "\\p{L}&&\\p{IsLatin}",
            expectedTrigramAbsoluteFrequencies);

    assertThat(model.getLanguage()).isEqualTo(Language.ENGLISH);
    assertThat(model.getAbsoluteFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedQuadrigramAbsoluteFrequencies);
    assertThat(model.getRelativeFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedQuadrigramRelativeFrequencies);
  }

  @Test
  public void assertThatFivegramLanguageModelCanBeCreatedFromTrainingData() {
    TrainingDataLanguageModel model =
        TrainingDataLanguageModel.fromText(
            ITERABLE_OF_STRINGS,
            Language.ENGLISH,
            5,
            "\\p{L}&&\\p{IsLatin}",
            expectedQuadrigramAbsoluteFrequencies);

    assertThat(model.getLanguage()).isEqualTo(Language.ENGLISH);
    assertThat(model.getAbsoluteFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedFivegramAbsoluteFrequencies);
    assertThat(model.getRelativeFrequencies())
        .containsExactlyInAnyOrderEntriesOf(expectedFivegramRelativeFrequencies);
  }

  // @Test
  // TODO: The `toJson` returns JSON with the right keys and values, but the keys are out of order
  public void assertThatUnigramLanguageModelIsCorrectlySerializedToJson() {
    TrainingDataLanguageModel model =
        TrainingDataLanguageModel.fromText(
            ITERABLE_OF_STRINGS,
            Language.ENGLISH,
            1,
            "\\p{L}&&\\p{IsLatin}",
            Collections.emptyMap());
    assertThat(model.toJson()).isEqualTo(expectedUnigramLanguageModel);
  }

  @Test
  public void assertThatUnigramLanguageModelIsCorrectlyDeserializedFromJson() throws Exception {
    ByteArrayInputStream inputStream =
        new ByteArrayInputStream(expectedUnigramLanguageModel.getBytes(StandardCharsets.UTF_8));
    Object2FloatMap<String> model = TrainingDataLanguageModel.fromJson(inputStream);
    Map<String, Float> expectedMap =
        expectedUnigramJsonRelativeFrequencies.entrySet().stream()
            .collect(Collectors.toMap(entry -> entry.getKey().getValue(), Map.Entry::getValue));

    assertThat(model).containsExactlyInAnyOrderEntriesOf(expectedMap);
  }
}

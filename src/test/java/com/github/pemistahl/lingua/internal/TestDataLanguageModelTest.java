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

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link TestDataLanguageModel} class.
 *
 * <p>This test class contains tests that verify the correctness of the {@link
 * TestDataLanguageModel} when created with different ngram lengths.
 *
 * @author Peter M. Stahl pemistahl@gmail.com
 * @author Migration by Alexander Zagniotov azagniotov@gmail.com
 */
public class TestDataLanguageModelTest {

  // Be very careful to not auto-format or line break the text, the tests will fail.
  private final String text =
      ("These sentences are intended for testing purposes. "
              + "Do not use them in production! "
              + "By the way, they consist of 23 words in total.")
          .toLowerCase()
          .trim();

  @Test
  void assertThatUnigramLanguageModelCanBeCreatedFromTestData() {
    TestDataLanguageModel model = TestDataLanguageModel.fromText(text, 1);

    Set<Ngram> expected = new HashSet<>();
    expected.add(new Ngram("a"));
    expected.add(new Ngram("b"));
    expected.add(new Ngram("c"));
    expected.add(new Ngram("d"));
    expected.add(new Ngram("e"));
    expected.add(new Ngram("f"));
    expected.add(new Ngram("g"));
    expected.add(new Ngram("h"));
    expected.add(new Ngram("i"));
    expected.add(new Ngram("l"));
    expected.add(new Ngram("m"));
    expected.add(new Ngram("n"));
    expected.add(new Ngram("o"));
    expected.add(new Ngram("p"));
    expected.add(new Ngram("r"));
    expected.add(new Ngram("s"));
    expected.add(new Ngram("t"));
    expected.add(new Ngram("u"));
    expected.add(new Ngram("w"));
    expected.add(new Ngram("y"));

    assertThat(model.getNgrams()).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void assertThatBigramLanguageModelCanBeCreatedFromTestData() {
    TestDataLanguageModel model = TestDataLanguageModel.fromText(text, 2);

    Set<Ngram> expected = new HashSet<>();
    expected.add(new Ngram("de"));
    expected.add(new Ngram("pr"));
    expected.add(new Ngram("pu"));
    expected.add(new Ngram("do"));
    expected.add(new Ngram("uc"));
    expected.add(new Ngram("ds"));
    expected.add(new Ngram("du"));
    expected.add(new Ngram("ur"));
    expected.add(new Ngram("us"));
    expected.add(new Ngram("ed"));
    expected.add(new Ngram("in"));
    expected.add(new Ngram("io"));
    expected.add(new Ngram("em"));
    expected.add(new Ngram("en"));
    expected.add(new Ngram("is"));
    expected.add(new Ngram("al"));
    expected.add(new Ngram("es"));
    expected.add(new Ngram("ar"));
    expected.add(new Ngram("rd"));
    expected.add(new Ngram("re"));
    expected.add(new Ngram("ey"));
    expected.add(new Ngram("nc"));
    expected.add(new Ngram("nd"));
    expected.add(new Ngram("ay"));
    expected.add(new Ngram("ng"));
    expected.add(new Ngram("ro"));
    expected.add(new Ngram("rp"));
    expected.add(new Ngram("no"));
    expected.add(new Ngram("ns"));
    expected.add(new Ngram("nt"));
    expected.add(new Ngram("fo"));
    expected.add(new Ngram("wa"));
    expected.add(new Ngram("se"));
    expected.add(new Ngram("od"));
    expected.add(new Ngram("si"));
    expected.add(new Ngram("by"));
    expected.add(new Ngram("of"));
    expected.add(new Ngram("wo"));
    expected.add(new Ngram("on"));
    expected.add(new Ngram("st"));
    expected.add(new Ngram("ce"));
    expected.add(new Ngram("or"));
    expected.add(new Ngram("os"));
    expected.add(new Ngram("ot"));
    expected.add(new Ngram("co"));
    expected.add(new Ngram("ta"));
    expected.add(new Ngram("te"));
    expected.add(new Ngram("ct"));
    expected.add(new Ngram("th"));
    expected.add(new Ngram("ti"));
    expected.add(new Ngram("to"));
    expected.add(new Ngram("he"));
    expected.add(new Ngram("po"));

    assertThat(model.getNgrams()).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void assertThatTrigramLanguageModelCanBeCreatedFromTestData() {
    TestDataLanguageModel model = TestDataLanguageModel.fromText(text, 3);

    Set<Ngram> expected = new HashSet<>();
    expected.add(new Ngram("rds"));
    expected.add(new Ngram("ose"));
    expected.add(new Ngram("ded"));
    expected.add(new Ngram("con"));
    expected.add(new Ngram("use"));
    expected.add(new Ngram("est"));
    expected.add(new Ngram("ion"));
    expected.add(new Ngram("ist"));
    expected.add(new Ngram("pur"));
    expected.add(new Ngram("hem"));
    expected.add(new Ngram("hes"));
    expected.add(new Ngram("tin"));
    expected.add(new Ngram("cti"));
    expected.add(new Ngram("tio"));
    expected.add(new Ngram("wor"));
    expected.add(new Ngram("ten"));
    expected.add(new Ngram("hey"));
    expected.add(new Ngram("ota"));
    expected.add(new Ngram("tal"));
    expected.add(new Ngram("tes"));
    expected.add(new Ngram("uct"));
    expected.add(new Ngram("sti"));
    expected.add(new Ngram("pro"));
    expected.add(new Ngram("odu"));
    expected.add(new Ngram("nsi"));
    expected.add(new Ngram("rod"));
    expected.add(new Ngram("for"));
    expected.add(new Ngram("ces"));
    expected.add(new Ngram("nce"));
    expected.add(new Ngram("not"));
    expected.add(new Ngram("are"));
    expected.add(new Ngram("pos"));
    expected.add(new Ngram("tot"));
    expected.add(new Ngram("end"));
    expected.add(new Ngram("enc"));
    expected.add(new Ngram("sis"));
    expected.add(new Ngram("sen"));
    expected.add(new Ngram("nte"));
    expected.add(new Ngram("ses"));
    expected.add(new Ngram("ord"));
    expected.add(new Ngram("ing"));
    expected.add(new Ngram("ent"));
    expected.add(new Ngram("int"));
    expected.add(new Ngram("nde"));
    expected.add(new Ngram("way"));
    expected.add(new Ngram("the"));
    expected.add(new Ngram("rpo"));
    expected.add(new Ngram("urp"));
    expected.add(new Ngram("duc"));
    expected.add(new Ngram("ons"));
    expected.add(new Ngram("ese"));

    assertThat(model.getNgrams()).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void assertThatQuadrigramLanguageModelCanBeCreatedFromTestData() {
    TestDataLanguageModel model = TestDataLanguageModel.fromText(text, 4);

    Set<Ngram> expected = new HashSet<>();
    expected.add(new Ngram("onsi"));
    expected.add(new Ngram("sist"));
    expected.add(new Ngram("ende"));
    expected.add(new Ngram("ords"));
    expected.add(new Ngram("esti"));
    expected.add(new Ngram("tenc"));
    expected.add(new Ngram("nces"));
    expected.add(new Ngram("oduc"));
    expected.add(new Ngram("tend"));
    expected.add(new Ngram("thes"));
    expected.add(new Ngram("rpos"));
    expected.add(new Ngram("ting"));
    expected.add(new Ngram("nten"));
    expected.add(new Ngram("nsis"));
    expected.add(new Ngram("they"));
    expected.add(new Ngram("tota"));
    expected.add(new Ngram("cons"));
    expected.add(new Ngram("tion"));
    expected.add(new Ngram("prod"));
    expected.add(new Ngram("ence"));
    expected.add(new Ngram("test"));
    expected.add(new Ngram("otal"));
    expected.add(new Ngram("pose"));
    expected.add(new Ngram("nded"));
    expected.add(new Ngram("oses"));
    expected.add(new Ngram("inte"));
    expected.add(new Ngram("urpo"));
    expected.add(new Ngram("them"));
    expected.add(new Ngram("sent"));
    expected.add(new Ngram("duct"));
    expected.add(new Ngram("stin"));
    expected.add(new Ngram("ente"));
    expected.add(new Ngram("ucti"));
    expected.add(new Ngram("purp"));
    expected.add(new Ngram("ctio"));
    expected.add(new Ngram("rodu"));
    expected.add(new Ngram("word"));
    expected.add(new Ngram("hese"));

    assertThat(model.getNgrams()).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void assertThatFivegramLanguageModelCanBeCreatedFromTestData() {
    TestDataLanguageModel model = TestDataLanguageModel.fromText(text, 5);

    Set<Ngram> expected = new HashSet<>();
    expected.add(new Ngram("testi"));
    expected.add(new Ngram("sente"));
    expected.add(new Ngram("ences"));
    expected.add(new Ngram("tende"));
    expected.add(new Ngram("these"));
    expected.add(new Ngram("ntenc"));
    expected.add(new Ngram("ducti"));
    expected.add(new Ngram("ntend"));
    expected.add(new Ngram("onsis"));
    expected.add(new Ngram("total"));
    expected.add(new Ngram("uctio"));
    expected.add(new Ngram("enten"));
    expected.add(new Ngram("poses"));
    expected.add(new Ngram("ction"));
    expected.add(new Ngram("produ"));
    expected.add(new Ngram("inten"));
    expected.add(new Ngram("nsist"));
    expected.add(new Ngram("words"));
    expected.add(new Ngram("sting"));
    expected.add(new Ngram("tence"));
    expected.add(new Ngram("purpo"));
    expected.add(new Ngram("estin"));
    expected.add(new Ngram("roduc"));
    expected.add(new Ngram("urpos"));
    expected.add(new Ngram("ended"));
    expected.add(new Ngram("rpose"));
    expected.add(new Ngram("oduct"));
    expected.add(new Ngram("consi"));

    assertThat(model.getNgrams()).containsExactlyInAnyOrderElementsOf(expected);
  }
}

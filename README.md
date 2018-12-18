![lingua](logo.png)

# language detection done right
*lingua* is a language detection library for Kotlin and Java, suitable for long and short text alike.
___
[![ci build status][travis ci badge]][travis ci url]
[![Maintainability][maintainability badge]][maintainability url]
[![codecov][codecov badge]][codecov url]
[![Download][lingua version badge]][lingua download url]
---
[![Kotlin version][Kotlin version badge]][Kotlin url]
[![Kotlin platforms badge][Kotlin platforms badge]][Kotlin platforms url]
[![JVM target][JVM target badge]][JVM target url]
[![license badge][license badge]][license url]
___

## Table of Contents

* [What does this library do?](#library-purpose)
* [Why does this library exist?](#library-reason)
* [Which languages are supported?](#supported-languages)
* [How good is it?](#library-accuracy)
* [How to add it to your project?](#library-dependency)
  * [Using Gradle](#library-dependency-gradle)
  * [Using Maven](#library-dependency-maven)
* [How to build?](#library-build)
* [How to use?](#library-use)
  * [Programmatic use](#library-use-programmatic)
  * [Standalone mode](#library-use-standalone)
* [Do you want to contribute?](#library-contribution)
* [What's next for upcoming versions?](#whats-next)

### <a name="library-purpose"></a> What does this library do?
Its task is simple: It tells you which language some provided textual data is written in. This is very useful as a preprocessing step for linguistic data in natural language processing applications such as text classification and spell checking. Other use cases, for instance, might include routing e-mails to the right geographically located customer service department, based on the e-mails' languages.

### <a name="library-reason"></a> Why does this library exist?
Language detection is often done as part of large machine learning frameworks or natural language processing applications. In cases where you don't need the full-fledged functionality of those systems or don't want to learn the ropes of those, a small flexible library comes in handy. 

So far, one of the few other comprehensive open source libraries working on the JVM for this task is [language-detector]. Unfortunately, it has two major drawbacks:
 
1. Detection only works with quite lengthy text fragments. For very short text snippets such as Twitter messages, it doesn't provide adequate results.
2. Configuration of the library is quite cumbersome and requires some knowledge about the statistical methods that are used internally.

*lingua* aims at eliminating these problems. It nearly doesn't need any configuration and yields pretty accurate results on both long and short text, even on single words and phrases. It draws on both rule-based and statistical methods but does not use any dictionaries. Compared to other language detection libraries, *lingua's* focus is on *quality over quantity*, that is, getting detection right for a small set of languages before adding new ones.

### <a name="supported-languages"></a> Which languages are supported?

Currently, the following seven languages are supported:

| Language | ISO 639-1 code |
| -------- | -------------- |
| English  |   *en*         |
| French   |   *fr*         |
| German   |   *de*         |
| Italian  |   *it*         |
| Latin    |   *la*         |
| Portuguese | *pt*         |
| Spanish  |   *es*         |

### <a name="library-accuracy"></a> How good is it?

*lingua* is able to report accuracy statistics for some bundled test data available for each supported language. The test data for each language is split into three parts:
1. a list of single words with a minimum length of 5 characters
2. a list of word pairs with a minimum length of 10 characters
3. a list of complete grammatical sentences of various lengths

Both the language models and the test data have been created from the [Wortschatz corpora] offered by Leipzig University, Germany.

When running `mvn test -P accuracy-reports`, a report file for each language is created under `target/surefire-reports`.
As an example, here is the current output of the German report:

```
com.github.pemistahl.lingua.detector.report.GermanDetectionAccuracyReport afterAll 

##### GERMAN #####

>>> Accuracy on average: 95,28%

>> Detection of 11748 single words (average length: 10 chars)
Accuracy: 89,31%
Erroneously classified as LATIN: 3,10%, ENGLISH: 2,47%, FRENCH: 2,00%, ITALIAN: 1,54%, SPANISH: 0,87%, PORTUGUESE: 0,72%

>> Detection of 9347 word pairs (average length: 17 chars)
Accuracy: 97,86%
Erroneously classified as ENGLISH: 0,76%, LATIN: 0,70%, FRENCH: 0,27%, ITALIAN: 0,22%, SPANISH: 0,13%, PORTUGUESE: 0,06%

>> Detection of 10000 sentences (average length: 47 chars)
Accuracy: 98,67%
Erroneously classified as ENGLISH: 0,96%, LATIN: 0,12%, PORTUGUESE: 0,11%, FRENCH: 0,05%, ITALIAN: 0,05%, SPANISH: 0,04%
```

Here is a summary of all accuracy reports of the current *lingua* version `0.2.0`. All supported languages have been taken into account during the classification process. Accuracy values are stated as rounded percentages.

| Language | Average | Single Words | Word Pairs | Sentences |
| -------- | ------- | ------------ | ---------- | --------- |
| English  | 87      | 72           | 89         | 98        |
| French   | 90      | 78           | 93         | 97        |
| German   | 95      | 89           | 98         | 99        |
| Italian  | 87      | 73           | 92         | 96        |
| Latin    | 88      | 80           | 94         | 89        |
| Portuguese | 86    | 69           | 90         | 99        | 
| Spanish  | 83      | 64           | 87         | 98        |
| **overall** | **88** | **75**     | **92**     | **97**    |

### <a name="library-dependency"></a> How to add it to your project?

*lingua* is currently hosted on [Bintray] only, but it will soon be available on JCenter and Maven Central as well.

#### <a name="library-dependency-gradle"></a> Using Gradle

```
repositories {
    maven {
        url 'https://dl.bintray.com/pemistahl/nlp-libraries'
    }
}

dependencies {
    implementation 'com.github.pemistahl:lingua:0.2.0'
}
```

#### <a name="library-dependency-maven"></a> Using Maven

```
<project>
    <dependencies>
        <dependency>
            <groupId>com.github.pemistahl</groupId>
            <artifactId>lingua</artifactId>
            <version>0.2.0</version>
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>bintray-pemistahl-nlp-libraries</id>
            <url>https://dl.bintray.com/pemistahl/nlp-libraries</url>
        </repository>
    </repositories>
</project>
```

### <a name="library-build"></a> How to build?

*lingua* uses Maven to build. A switch to Gradle is planned for the future.

```
git clone https://github.com/pemistahl/lingua.git
cd lingua
mvn install
```
Maven's `package` phase is able to generate two jar files in the `target` directory:
1. `mvn package` creates `lingua-0.2.0.jar` that contains the compiled sources only.
2. `mvn package -P with-dependencies` creates `lingua-0.2.0-with-dependencies.jar` that additionally contains all dependencies needed to use the library. This jar file can be included in projects without dependency management systems. It can also be used to run *lingua* in standalone mode (see below).

### <a name="library-use"></a> How to use?
*lingua* can be used programmatically in your own code or in standalone mode.

#### <a name="library-use-programmatic"></a> Programmatic use
The API is pretty straightforward and can be used in both Kotlin and Java code.

```kotlin
/* Kotlin */

import com.github.pemistahl.lingua.detector.LanguageDetector
import com.github.pemistahl.lingua.model.Language

println(LanguageDetector.supportedLanguages())
// [ENGLISH, FRENCH, GERMAN, ITALIAN, LATIN, PORTUGUESE, SPANISH]

val detector = LanguageDetector.fromAllBuiltInLanguages()
val detectedLanguage: Language = detector.detectLanguageOf(text = "languages are awesome")

// returns Language.ENGLISH
```

If a string's language cannot be detected reliably because of missing linguistic information, `Language.UNKNOWN` is returned. The public API of *lingua* never returns null somewhere, so it is safe to be used from within Java code as well.

```java
/* Java */

import com.github.pemistahl.lingua.detector.LanguageDetector;
import com.github.pemistahl.lingua.model.Language;

final LanguageDetector detector = LanguageDetector.fromAllBuiltInLanguages();
final Language detectedLanguage = detector.detectLanguageOf("languages are awesome");

// returns Language.ENGLISH
```

There might be classification tasks where you know beforehand that your language data is definitely not written in Latin, for instance (what a surprise :-). The detection accuracy can become better in such cases if you exclude certain languages from the decision process or just explicitly include relevant languages:
```kotlin

// include only languages that are not yet extinct (= currently excludes Latin)
LanguageDetector.fromAllBuiltInSpokenLanguages()

// exclude the Spanish language from the decision algorithm
LanguageDetector.fromAllBuiltInLanguagesWithout(Language.SPANISH)

// only decide between English and German
LanguageDetector.fromLanguages(Language.ENGLISH, Language.GERMAN)
```

#### <a name="library-use-standalone"></a> Standalone mode
If you want to try out *lingua* before you decide whether to use it or not, you can run it in a REPL and immediately see its detection results.
1. With Maven: `mvn exec:java`
2. Without Maven: `java -jar lingua-0.2.0-with-dependencies.jar`

Then just play around:

```
This is Lingua.
Loading language models...
Done. 7 language models loaded.

Type some text and press <Enter> to detect its language.
Type :quit to exit.

> Good day
ENGLISH
> Guten Tag
GERMAN
> Bonjour
FRENCH
> Buon giorno
ITALIAN
> Buenos dias
SPANISH
> Bom dia
PORTUGUESE
> :quit
Bye! Ciao! Tschüss! Salut!
```

### <a name="library-contribution"></a> Do you want to contribute?

In case you want to contribute something to *lingua* even though it's in a very early stage of development, then I encourage you to do so nevertheless. Do you have ideas for improving the API? Are there some specific languages that you want to have supported early? Or have you found any bugs so far? Feel free to open an issue or send a pull request. It's very much appreciated. :-)

### <a name="whats-next"></a> What's next for upcoming versions?
- languages, languages, even more languages :-)
- accuracy improvements
- more unit tests
- API documentation
- public API stability until version 1.0.0

[travis ci badge]: https://travis-ci.org/pemistahl/lingua.svg?branch=master
[travis ci url]: https://travis-ci.org/pemistahl/lingua
[maintainability badge]: https://api.codeclimate.com/v1/badges/15822c202592b047f793/maintainability
[maintainability url]: https://codeclimate.com/github/pemistahl/lingua/maintainability
[codecov badge]: https://codecov.io/gh/pemistahl/lingua/branch/master/graph/badge.svg
[codecov url]: https://codecov.io/gh/pemistahl/lingua
[lingua version badge]: https://api.bintray.com/packages/pemistahl/nlp-libraries/lingua/images/download.svg
[lingua download url]: https://bintray.com/pemistahl/nlp-libraries/download_file?file_path=com%2Fgithub%2Fpemistahl%2Flingua%2F0.2.0%2Flingua-0.2.0-with-dependencies.jar
[Kotlin version badge]: https://img.shields.io/badge/Kotlin-1.3-blue.svg?logo=kotlin
[Kotlin url]: https://kotlinlang.org/docs/reference/whatsnew13.html
[Kotlin platforms badge]: https://img.shields.io/badge/platforms-Kotlin%2FJVM-blue.svg
[Kotlin platforms url]: https://kotlinlang.org/docs/reference/server-overview.html
[JVM target badge]: https://img.shields.io/badge/JVM%20target-1.6+-blue.svg?logo=java
[JVM target url]: https://www.oracle.com/technetwork/java/javase/java-archive-downloads-javase6-419409.html
[license badge]: https://img.shields.io/badge/license-Apache%202.0-blue.svg
[license url]: https://www.apache.org/licenses/LICENSE-2.0
[Wortschatz corpora]: http://wortschatz.uni-leipzig.de
[language-detector]: https://github.com/optimaize/language-detector
[Bintray]: https://bintray.com/pemistahl/nlp-libraries/lingua

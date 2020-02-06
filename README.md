![lingua](images/logo.png) 

<br>

[![ci build status][travis ci badge]][travis ci url]
[![codecov][codecov badge]][codecov url]
[![supported languages][supported languages badge]](#supported-languages)
[![Kotlin platforms badge][Kotlin platforms badge]][Kotlin platforms url]
[![license badge][license badge]][license url]

[![javadoc](https://javadoc.io/badge2/com.github.pemistahl/lingua/javadoc.svg)](https://javadoc.io/doc/com.github.pemistahl/lingua)
[![Maven Central][Maven Central badge]][Maven Central]
[![Jcenter][Jcenter badge]][Jcenter]
[![Download][lingua version badge]][lingua download url]

---
### Quick Info
* this library tries to solve language detection of very short words and phrases, even shorter than tweets
* makes use of both statistical and rule-based approaches
* outperforms *Apache Tika*, *Apache OpenNLP* and *Optimaize Language Detector* for more than 60 languages
* works within every Java 6+ application and on Android
* no additional training of language models necessary
* offline usage without having to connect to an external service or API
* can be used in a REPL for a quick try-out
---

## <a name="table-of-contents"></a> Table of Contents

1. [What does this library do?](#library-purpose)
2. [Why does this library exist?](#library-reason)
3. [Which languages are supported?](#supported-languages)
4. [How good is it?](#library-accuracy)
5. [Why is it better than other libraries?](#why-is-it-better)   
6. [Test report generation](#report-generation)
7. [How to add it to your project?](#library-dependency)  
  7.1 [Using Gradle](#library-dependency-gradle)  
  7.2 [Using Maven](#library-dependency-maven)
8. [How to build?](#library-build)
9. [How to use?](#library-use)  
  9.1 [Programmatic use](#library-use-programmatic)  
  9.2 [Standalone mode](#library-use-standalone)
10. [Do you want to contribute?](#library-contribution)
11. [What's next for upcoming versions?](#whats-next)

## 1. <a name="library-purpose"></a> What does this library do? <sup>[Top ▲](#table-of-contents)</sup>
Its task is simple: It tells you which language some provided textual data is written in. This is very useful as a preprocessing step for linguistic data in natural language processing applications such as text classification and spell checking. Other use cases, for instance, might include routing e-mails to the right geographically located customer service department, based on the e-mails' languages.

## 2. <a name="library-reason"></a> Why does this library exist? <sup>[Top ▲](#table-of-contents)</sup>
Language detection is often done as part of large machine learning frameworks or natural language processing applications. In cases where you don't need the full-fledged functionality of those systems or don't want to learn the ropes of those, a small flexible library comes in handy. 

So far, three other comprehensive open source libraries working on the JVM for this task are [Apache Tika], [Apache OpenNLP] and [Optimaize Language Detector]. Unfortunately, especially the latter has three major drawbacks:
 
1. Detection only works with quite lengthy text fragments. For very short text snippets such as Twitter messages, it doesn't provide adequate results.
2. The more languages take part in the decision process, the less accurate are the detection results.
3. Configuration of the library is quite cumbersome and requires some knowledge about the statistical methods that are used internally.

*Lingua* aims at eliminating these problems. It nearly doesn't need any configuration and yields pretty accurate results on both long and short text, even on single words and phrases. It draws on both rule-based and statistical methods but does not use any dictionaries of words. It does not need a connection to any external API or service either. Once the library has been downloaded, it can be used completely offline. 

## 3. <a name="supported-languages"></a> Which languages are supported? <sup>[Top ▲](#table-of-contents)</sup>

Compared to other language detection libraries, *Lingua's* focus is on *quality over quantity*, that is, getting detection right for a small set of languages first before adding new ones. Currently, the following 66 languages are supported:

- A
  - Afrikaans
  - Albanian
  - Arabic
  - Armenian
  - Azerbaijani
- B
  - Basque
  - Belarusian
  - Bengali
  - Bokmal
  - Bosnian
  - Bulgarian
- C
  - Catalan
  - Chinese
  - Croatian
  - Czech
- D
  - Danish
  - Dutch
- E
  - English
  - Esperanto
  - Estonian
- F
  - Finnish
  - French
- G
  - Georgian
  - German
  - Greek
  - Gujarati
- H
  - Hebrew
  - Hindi
  - Hungarian
- I
  - Icelandic
  - Indonesian
  - Irish
  - Italian
- J
  - Japanese
- K
  - Kazakh
  - Korean
- L
  - Latin
  - Latvian
  - Lithuanian
- M
  - Macedonian
  - Malay
  - Marathi
  - Mongolian
- N
  - Norwegian
  - Nynorsk
- P
  - Persian
  - Polish
  - Portuguese
  - Punjabi
- R
  - Romanian
  - Russian
- S
  - Serbian
  - Slovak
  - Slovene
  - Somali
  - Spanish
  - Swedish
- T
  - Tagalog
  - Tamil
  - Telugu
  - Thai
  - Turkish
- U
  - Ukrainian
  - Urdu
- V
  - Vietnamese
- W
  - Welsh   

## 4. <a name="library-accuracy"></a> How good is it? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* is able to report accuracy statistics for some bundled test data available for each supported language. The test data for each language is split into three parts:
1. a list of single words with a minimum length of 5 characters
2. a list of word pairs with a minimum length of 10 characters
3. a list of complete grammatical sentences of various lengths

Both the language models and the test data have been created from separate documents of the [Wortschatz corpora] offered by Leipzig University, Germany. Data crawled from various news websites have been used for training, each corpus comprising one million sentences. For testing, corpora made of arbitrarily chosen websites have been used, each comprising ten thousand sentences. From each test corpus, a random unsorted subset of 1000 single words, 1000 word pairs and 1000 sentences has been extracted, respectively.

Given the generated test data, I have compared the detection results of *Lingua*, *Apache Tika*, *Apache OpenNLP* and *Optimaize Language Detector* using parameterized JUnit tests running over the data of 66 languages. Bokmal, Latin and Nynorsk are currently only supported by *Apache OpenNLP* and *Lingua*. For the other two classifiers, Bokmal and Nynorsk are both mapped to Norwegian. *Apache OpenNLP* does not allow to restrict the set of languages that take part in the decision process. So this classifier might theoretically return one of its 103 supported languages as its result. *Apache Tika* and *Optimaize Language Detector* have been configured so that the set of examinable languages is equal to the set supported by *Lingua*. This restriction makes the detection results more comparable to each other.

The box plot below shows the distribution of the averaged accuracy values for all three performed tasks: Single word detection, word pair detection and sentence detection. *Lingua* clearly outperforms its contenders. Bar plots for each language and further box plots for the separate detection tasks can be found in the file [ACCURACY_PLOTS.md]. Detailed statistics including mean, median and standard deviation values for each language and classifier are available in the file [ACCURACY_TABLE.md]. 

![boxplot-average](/images/plots/boxplot-average.png)

## 5. <a name="why-is-it-better"></a> Why is it better than other libraries? <sup>[Top ▲](#table-of-contents)</sup>

Every language detector uses a probabilistic [n-gram](https://en.wikipedia.org/wiki/N-gram) model trained on the character distribution in some training corpus. Most libraries only use n-grams of size 3 (trigrams) which is satisfactory for detecting the language of longer text fragments consisting of multiple sentences. For short phrases or single words, however, trigrams are not enough. The shorter the input text is, the less n-grams are available. The probabilities estimated from such few n-grams are not reliable. This is why *Lingua* makes use of n-grams of sizes 1 up to 5 which results in much more accurate prediction of the correct language.  

A second important difference is that *Lingua* does not only use such a statistical model, but also a rule-based engine. This engine first determines the alphabet of the input text and searches for characters which are unique in one or more languages. If exactly one language can be reliably chosen this way, the statistical model is not necessary anymore. In any case, the rule-based engine filters out languages that do not satisfy the conditions of the input text. Only then, in a second step, the probabilistic n-gram model is taken into consideration. This makes sense because loading less language models means less memory consumption and better runtime performance.

In general, it is always a good idea to restrict the set of languages to be considered in the classification process using the respective [api methods](#library-use-programmatic). If you know beforehand that certain languages are never to occur in an input text, do not let those take part in the classifcation process. The filtering mechanism of the rule-based engine is quite good, however, filtering based on your own knowledge of the input text is always preferable.

## 6. <a name="report-generation"></a> Test report and plot generation <sup>[Top ▲](#table-of-contents)</sup>

If you want to reproduce the accuracy results above, you can generate the test reports yourself for all four classifiers and all languages by doing:

    ./gradlew writeAccuracyReports
    
You can also restrict the classifiers and languages to generate reports for by passing arguments to the Gradle task. The following task generates reports for *Lingua* and the languages English and German only:

    ./gradlew writeAccuracyReports -Pdetectors=Lingua -Planguages=English,German

By default, only a single CPU core is used for report generation. If you have a multi-core CPU in your machine, you can fork as many processes as you have CPU cores. This speeds up report generation significantly. However, be aware that forking more than one process can consume a lot of RAM. You do it like this:

    ./gradlew writeAccuracyReports -PcpuCores=2

For each detector and language, a test report file is then written into [`/accuracy-reports`](https://github.com/pemistahl/lingua/tree/master/accuracy-reports), to be found next to the `src` directory. 
As an example, here is the current output of the *Lingua* German report:

```
com.github.pemistahl.lingua.report.lingua.GermanDetectionAccuracyReport

##### GERMAN #####

>>> Accuracy on average: 89,43%

>> Detection of 1000 single words (average length: 9 chars)
Accuracy: 74,30%
Erroneously classified as LATIN: 2,40%, DUTCH: 2,40%, ENGLISH: 2,20%, DANISH: 2,20%, NORWEGIAN: 1,90%, ITALIAN: 1,40%, ESPERANTO: 1,30%, SWEDISH: 1,20%, BASQUE: 1,20%, FRENCH: 1,20%, AFRIKAANS: 0,90%, WELSH: 0,80%, PORTUGUESE: 0,60%, ESTONIAN: 0,60%, FINNISH: 0,50%, SPANISH: 0,40%, TAGALOG: 0,40%, IRISH: 0,40%, ICELANDIC: 0,40%, POLISH: 0,40%, SOMALI: 0,30%, TURKISH: 0,30%, ALBANIAN: 0,30%, CATALAN: 0,30%, BOSNIAN: 0,30%, LITHUANIAN: 0,20%, INDONESIAN: 0,20%, LATVIAN: 0,20%, SLOVAK: 0,20%, CROATIAN: 0,20%, ROMANIAN: 0,20%, MALAY: 0,10%, SLOVENE: 0,10%

>> Detection of 1000 word pairs (average length: 18 chars)
Accuracy: 94,40%
Erroneously classified as DUTCH: 0,90%, LATIN: 0,80%, ENGLISH: 0,70%, SWEDISH: 0,60%, DANISH: 0,60%, FRENCH: 0,40%, NORWEGIAN: 0,30%, TURKISH: 0,20%, TAGALOG: 0,20%, WELSH: 0,20%, IRISH: 0,20%, ESTONIAN: 0,10%, FINNISH: 0,10%, ITALIAN: 0,10%, ICELANDIC: 0,10%, AFRIKAANS: 0,10%

>> Detection of 1000 sentences (average length: 111 chars)
Accuracy: 99,60%
Erroneously classified as DUTCH: 0,20%, LATIN: 0,10%, ALBANIAN: 0,10%
```

The plots have been created with Python and the libraries Pandas, Matplotlib and Seaborn. If you have a global Python 3 installation and the `python3` command available on your command line, you can redraw the plots after modifying the test reports by executing the following Gradle task:

    ./gradlew drawAccuracyPlots
    
The detailed table in the file [ACCURACY_TABLE.md] containing all accuracy values can be written with:

    ./gradlew writeAccuracyTable

## 7. <a name="library-dependency"></a> How to add it to your project? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* is hosted on [Jcenter] and [Maven Central].

### 7.1 <a name="library-dependency-gradle"></a> Using Gradle

```
// Groovy syntax
implementation 'com.github.pemistahl:lingua:0.6.1'

// Kotlin syntax
implementation("com.github.pemistahl:lingua:0.6.1")
```

### 7.2 <a name="library-dependency-maven"></a> Using Maven

```
<dependency>
    <groupId>com.github.pemistahl</groupId>
    <artifactId>lingua</artifactId>
    <version>0.6.1</version>
</dependency>
```

## 8. <a name="library-build"></a> How to build? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* uses Gradle to build.

```
git clone https://github.com/pemistahl/lingua.git
cd lingua
./gradlew build
```
Several jar archives can be created from the project.
1. `./gradlew jar` assembles `lingua-0.6.1.jar` containing the compiled sources only.
2. `./gradlew sourcesJar` assembles `lingua-0.6.1-sources.jar` containing the plain source code.
3. `./gradlew jarWithDependencies` assembles `lingua-0.6.1-with-dependencies.jar` containing the compiled sources and all external dependencies needed at runtime. This jar file can be included in projects without dependency management systems. You should be able to use it in your Android project as well by putting it in your project's `lib` folder. This jar file can also be used to run *Lingua* in standalone mode (see below).

## 9. <a name="library-use"></a> How to use? <sup>[Top ▲](#table-of-contents)</sup>
*Lingua* can be used programmatically in your own code or in standalone mode.

### 9.1 <a name="library-use-programmatic"></a> Programmatic use <sup>[Top ▲](#table-of-contents)</sup>
The API is pretty straightforward and can be used in both Kotlin and Java code.

```kotlin
/* Kotlin */

import com.github.pemistahl.lingua.api.*
import com.github.pemistahl.lingua.api.Language.*

val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(ENGLISH, FRENCH, GERMAN, SPANISH).build()
val detectedLanguage: Language = detector.detectLanguageOf(text = "languages are awesome")
// ENGLISH
```

By default, *Lingua* returns the most likely language for a given input text. However, there are certain words that are spelled the same in more than one language. The word *prologue*, for instance, is both a valid English and French word. *Lingua* would output either English or French which might be wrong in the given context. For cases like that, it is possible to specify a minimum relative distance that the logarithmized and summed up probabilities for each possible language have to satisfy. It can be stated in the following way:

```kotlin
val detector = LanguageDetectorBuilder
    .fromAllBuiltInLanguages()
    .withMinimumRelativeDistance(0.25) // minimum: 0.00 maximum: 0.99 default: 0.00
    .build()
```

Be aware that the distance between the language probabilities is dependent on the length of the input text. The longer the input text, the larger the distance between the languages. So if you want to classify very short text phrases, do not set the minimum relative distance too high. Otherwise you will get most results returned as `Language.UNKNOWN` which is the return value for cases where language detection is not reliably possible. 

The public API of *Lingua* never returns `null` somewhere, so it is safe to be used from within Java code as well.

```java
/* Java */

import com.github.pemistahl.lingua.api.*;
import java.util.List;

import static com.github.pemistahl.lingua.api.Language.*;

final LanguageDetector detector = LanguageDetectorBuilder.fromLanguages(ENGLISH, FRENCH, GERMAN, SPANISH).build();
final Language detectedLanguage = detector.detectLanguageOf("languages are awesome");
```

There might be classification tasks where you know beforehand that your language data is definitely not written in Latin, for instance (what a surprise :-). The detection accuracy can become better in such cases if you exclude certain languages from the decision process or just explicitly include relevant languages:

```kotlin
// include all languages available in the library
// WARNING: in the worst case this produces high memory 
//          consumption and slow runtime performance
LanguageDetectorBuilder.fromAllBuiltInLanguages()

// include only languages that are not yet extinct (= currently excludes Latin)
LanguageDetectorBuilder.fromAllBuiltInSpokenLanguages()

// exclude the Spanish language from the decision algorithm
LanguageDetectorBuilder.fromAllBuiltInLanguagesWithout(Language.SPANISH)

// only decide between English and German
LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.GERMAN)

// select languages by ISO 639-1 code
LanguageDetectorBuilder.fromIsoCodes639_1(IsoCode639_1.EN, IsoCode639_3.DE)

// select languages by ISO 639-3 code
LanguageDetectorBuilder.fromIsoCodes639_3(IsoCode639_3.ENG, IsoCode639_3.DEU)
```

### 9.2 <a name="library-use-standalone"></a> Standalone mode <sup>[Top ▲](#table-of-contents)</sup>
If you want to try out *Lingua* before you decide whether to use it or not, you can run it in a REPL and immediately see its detection results.
1. With Gradle: `./gradlew runLinguaOnConsole --console=plain`
2. Without Gradle: `java -jar lingua-0.6.1-with-dependencies.jar`

Then just play around:

```
This is Lingua.
Select the language models to load.

1: enter language iso codes manually
2: all supported languages

Type a number and press <Enter>.
Type :quit to exit.

> 1
List some language iso 639-1 codes separated by spaces and press <Enter>.
Type :quit to exit.

> en fr de es
Loading language models...
Done. 4 language models loaded lazily.

Type some text and press <Enter> to detect its language.
Type :quit to exit.

> languages
ENGLISH
> Sprachen
GERMAN
> langues
FRENCH
> :quit
Bye! Ciao! Tschüss! Salut!
```

## 10. <a name="library-contribution"></a> Do you want to contribute? <sup>[Top ▲](#table-of-contents)</sup>

In case you want to contribute something to *Lingua*, then I encourage you to do so. Do you have ideas for improving the API? Are there some specific languages that you want to have supported early? Or have you found any bugs so far? Feel free to open an issue or send a pull request. It's very much appreciated. :-)

## 11. <a name="whats-next"></a> What's next for upcoming versions? <sup>[Top ▲](#table-of-contents)</sup>
- languages, languages, even more languages :-)
- accuracy improvements
- more unit tests
- API documentation
- for version 1.0.0: public API stability, multiplatform support

[travis ci badge]: https://travis-ci.org/pemistahl/lingua.svg?branch=master
[travis ci url]: https://travis-ci.org/pemistahl/lingua
[codecov badge]: https://codecov.io/gh/pemistahl/lingua/branch/master/graph/badge.svg
[codecov url]: https://codecov.io/gh/pemistahl/lingua
[supported languages badge]: https://img.shields.io/badge/supported%20languages-66-yellow.svg
[awesome nlp badge]: https://raw.githubusercontent.com/sindresorhus/awesome/master/media/mentioned-badge-flat.svg?sanitize=true
[lingua version badge]: https://img.shields.io/badge/Download%20Jar-0.6.1-blue.svg
[lingua download url]: https://bintray.com/pemistahl/nlp-libraries/download_file?file_path=com%2Fgithub%2Fpemistahl%2Flingua%2F0.6.1%2Flingua-0.6.1-with-dependencies.jar
[Kotlin platforms badge]: https://img.shields.io/badge/platforms-JDK%206%2B%20%7C%20Android-blue.svg
[Kotlin platforms url]: https://kotlinlang.org/docs/reference/server-overview.html
[license badge]: https://img.shields.io/badge/license-Apache%202.0-blue.svg
[license url]: https://www.apache.org/licenses/LICENSE-2.0
[Wortschatz corpora]: http://wortschatz.uni-leipzig.de
[Apache Tika]: https://tika.apache.org/1.23/detection.html#Language_Detection
[Apache OpenNLP]: https://opennlp.apache.org/docs/1.9.1/manual/opennlp.html#tools.langdetect
[Optimaize Language Detector]: https://github.com/optimaize/language-detector
[Jcenter]: https://bintray.com/pemistahl/nlp-libraries/lingua/0.6.1
[Jcenter badge]: https://img.shields.io/badge/JCenter-0.6.1-green.svg
[Maven Central]: https://search.maven.org/artifact/com.github.pemistahl/lingua/0.6.1/jar
[Maven Central badge]: https://img.shields.io/badge/Maven%20Central-0.6.1-green.svg
[ACCURACY_PLOTS.md]: https://github.com/pemistahl/lingua/blob/master/ACCURACY_PLOTS.md
[ACCURACY_TABLE.md]: https://github.com/pemistahl/lingua/blob/master/ACCURACY_TABLE.md

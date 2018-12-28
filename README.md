[![lingua](logo.png)][translate-lingua] 

# language detection done right [![awesome nlp badge][awesome nlp badge]][awesome nlp url]
*Lingua* is a language detection library for Kotlin and Java, suitable for long and short text alike.

---
[![Download][lingua version badge]][lingua download url]
[![supported languages][supported languages badge]](#supported-languages)
---
[![ci build status][travis ci badge]][travis ci url]
[![codebeat badge][codebeat badge]][codebeat url]
[![Codacy Badge][Codacy Badge]][codacy url]
[![Maintainability][maintainability badge]][maintainability url]
[![codecov][codecov badge]][codecov url]
---
[![Kotlin version][Kotlin version badge]][Kotlin url]
[![Kotlin platforms badge][Kotlin platforms badge]][Kotlin platforms url]
[![license badge][license badge]][license url]
---

## <a name="table-of-contents"></a> Table of Contents

* [What does this library do?](#library-purpose)
* [Why does this library exist?](#library-reason)
* [Which languages are supported?](#supported-languages)
* [How good is it?](#library-accuracy)
  * [Comparison of Libraries](#library-comparison)
  * [Test Report Generation](#report-generation)
* [How to add it to your project?](#library-dependency)
  * [Using Gradle](#library-dependency-gradle)
  * [Using Maven](#library-dependency-maven)
* [How to build?](#library-build)
* [How to use?](#library-use)
  * [Programmatic use](#library-use-programmatic)
  * [Standalone mode](#library-use-standalone)
* [Do you want to contribute?](#library-contribution)
* [What's next for upcoming versions?](#whats-next)

### <a name="library-purpose"></a> What does this library do? <sup>[Top ▲](#table-of-contents)</sup>
Its task is simple: It tells you which language some provided textual data is written in. This is very useful as a preprocessing step for linguistic data in natural language processing applications such as text classification and spell checking. Other use cases, for instance, might include routing e-mails to the right geographically located customer service department, based on the e-mails' languages.

### <a name="library-reason"></a> Why does this library exist? <sup>[Top ▲](#table-of-contents)</sup>
Language detection is often done as part of large machine learning frameworks or natural language processing applications. In cases where you don't need the full-fledged functionality of those systems or don't want to learn the ropes of those, a small flexible library comes in handy. 

So far, two other comprehensive open source libraries working on the JVM for this task are [Apache Tika] and [Optimaize Language Detector]. Unfortunately, especially the latter has two major drawbacks:
 
1. Detection only works with quite lengthy text fragments. For very short text snippets such as Twitter messages, it doesn't provide adequate results.
2. Configuration of the library is quite cumbersome and requires some knowledge about the statistical methods that are used internally.

*Lingua* aims at eliminating these problems. It nearly doesn't need any configuration and yields pretty accurate results on both long and short text, even on single words and phrases. It draws on both rule-based and statistical methods but does not use any dictionaries of words. It does not need a connection to any external API or service either. Once the library has been downloaded, it can be used completely offline. 

### <a name="supported-languages"></a> Which languages are supported? <sup>[Top ▲](#table-of-contents)</sup>

Compared to other language detection libraries, *Lingua's* focus is on *quality over quantity*, that is, getting detection right for a small set of languages first before adding new ones. Currently, the following seven languages are supported:

| Language | ISO 639-1 code |
| -------- | -------------- |
| English  |   *en*         |
| French   |   *fr*         |
| German   |   *de*         |
| Italian  |   *it*         |
| Latin    |   *la*         |
| Portuguese | *pt*         |
| Spanish  |   *es*         |

### <a name="library-accuracy"></a> How good is it? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* is able to report accuracy statistics for some bundled test data available for each supported language. The test data for each language is split into three parts:
1. a list of single words with a minimum length of 5 characters
2. a list of word pairs with a minimum length of 10 characters
3. a list of complete grammatical sentences of various lengths

Both the language models and the test data have been created from separate documents of the [Wortschatz corpora] offered by Leipzig University, Germany. 

#### <a name="library-comparison"></a> Comparison of Libraries <sup>[Top ▲](#table-of-contents)</sup>

Given the generated test data, I have compared the detection results of *Lingua*, *Apache Tika* and *Optimaize Language Detector* using parameterized JUnit tests running over the data of six languages. *Tika* actually uses a heavily optimized version of *Optimaize* internally. Latin is currently only supported by *Lingua*, so it's left out both in the decision process and in the comparison. 

As the table below shows, *Lingua* outperforms the other two libraries significantly. All values are rounded percentages. When it comes to detecting the language of entire sentences, all three libraries are nearly equally accurate. It is actually short paragraphs of text where *Lingua* plays to its strengths. Even though *Lingua* is in an early stage of development, detection accuracy for word pairs is already 12% higher on average than with *Tika*, for single words it is even 15% higher. 

<table>
    <tr>
        <th>Language</th>
        <th colspan="3">Average</th>
        <th colspan="3">Single Words</th>
        <th colspan="3">Word Pairs</th>
        <th colspan="3">Sentences</th>
    </tr>
    <tr>
        <th></th>
        <th>Lingua</th>
        <th>&nbsp;Tika&nbsp;</th>
        <th>Optimaize</th>
        <th>Lingua</th>
        <th>&nbsp;Tika&nbsp;</th>
        <th>Optimaize</th>
        <th>Lingua</th>
        <th>&nbsp;Tika&nbsp;</th>
        <th>Optimaize</th>
        <th>Lingua</th>
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>Optimaize</th>
    </tr>
    <tr>
        <td>English</td>
        <td>88 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>77 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>49 <img src="https://placehold.it/12/ff8c00/000000?text=+"></td>
        <td>76 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>57 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>12 <img src="https://placehold.it/12/ff0000/000000?text=+"></td>
        <td>91 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>76 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>43 <img src="https://placehold.it/12/ff8c00/000000?text=+"></td>
        <td>98 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>&nbsp;&nbsp;97 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>90 <img src="https://placehold.it/12/008000/000000?text=+"></td>
    </tr>
    <tr>
        <td>French</td>
        <td>91 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>84 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>62 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>81 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>67 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>27 <img src="https://placehold.it/12/ff0000/000000?text=+"></td>
        <td>94 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>85 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>59 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>97 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>100 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>99 <img src="https://placehold.it/12/008000/000000?text=+"></td>
    </tr>
    <tr>
        <td>German</td>
        <td>96 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>90 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>78 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>91 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>79 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>54 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>98 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>93 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>81 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>99 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>100 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>98 <img src="https://placehold.it/12/008000/000000?text=+"></td>
    </tr>
    <tr>
        <td>Italian</td>
        <td>90 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>87 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>59 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>80 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>74 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>23 <img src="https://placehold.it/12/ff0000/000000?text=+"></td>
        <td>94 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>88 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>56 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>96 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>&nbsp;&nbsp;99 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>98 <img src="https://placehold.it/12/008000/000000?text=+"></td>
    </tr>
    <tr>
        <td>Portuguese</td>
        <td>87 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>72 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>43 <img src="https://placehold.it/12/ff8c00/000000?text=+"></td>
        <td>72 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>50 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>&nbsp;&nbsp;9 <img src="https://placehold.it/12/ff0000/000000?text=+"></td>
        <td>91 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>68 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>25 <img src="https://placehold.it/12/ff8c00/000000?text=+"></td>
        <td>99 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>&nbsp;&nbsp;98 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>94 <img src="https://placehold.it/12/008000/000000?text=+"></td>
    </tr>
    <tr>
        <td>Spanish</td>
        <td>84 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>75 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>43 <img src="https://placehold.it/12/ff8c00/000000?text=+"></td>
        <td>67 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>52 <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td>&nbsp;&nbsp;8 <img src="https://placehold.it/12/ff0000/000000?text=+"></td>
        <td>88 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>75 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>24 <img src="https://placehold.it/12/ff0000/000000?text=+"></td>
        <td>98 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>&nbsp;&nbsp;99 <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>97 <img src="https://placehold.it/12/008000/000000?text=+"></td>
    </tr>
    <tr>
        <td colspan="9"></td>
    </tr>
    <tr>
        <td><strong>overall</strong></td>
        <td><strong>89</strong> <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td><strong>81</strong> <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td><strong>56</strong> <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td><strong>78</strong> <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td><strong>63</strong> <img src="https://placehold.it/12/ffff00/000000?text=+"></td>
        <td><strong>22</strong> <img src="https://placehold.it/12/ff0000/000000?text=+"></td>
        <td><strong>93</strong> <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td><strong>81</strong> <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td><strong>48</strong> <img src="https://placehold.it/12/ff8c00/000000?text=+"></td>
        <td><strong>98</strong> <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td>&nbsp;&nbsp;<strong>99</strong> <img src="https://placehold.it/12/008000/000000?text=+"></td>
        <td><strong>96</strong> <img src="https://placehold.it/12/008000/000000?text=+"></td>
    </tr>
</table>

#### <a name="report-generation"></a> Test Report Generation <sup>[Top ▲](#table-of-contents)</sup>

If you want to reproduce the accuracy results above, you can generate the test reports yourself:

- `mvn test -P accuracy-reports -D detector=lingua` generates reports for *Lingua*
- `mvn test -P accuracy-reports -D detector=tika` generates reports for *Apache Tika*
- `mvn test -P accuracy-reports -D detector=optimaize` generates reports for *Optimaize*

For each detector and language, a test report file is then written into `/accuracy-reports`, to be found next to the `src` directory. All accuracy reports of the current release are committed to this repository and can be found [here](https://github.com/pemistahl/lingua/tree/master/accuracy-reports).
As an example, here is the current output of the *Lingua* German report:

```
com.github.pemistahl.lingua.report.lingua.GermanDetectionAccuracyReport

##### GERMAN #####

>>> Accuracy on average: 95,90%

>> Detection of 11748 single words (average length: 10 chars)
Accuracy: 90,64%
Erroneously classified as ENGLISH: 2,95%, FRENCH: 2,38%, ITALIAN: 2,05%, SPANISH: 1,05%, PORTUGUESE: 0,94%

>> Detection of 9347 word pairs (average length: 17 chars)
Accuracy: 98,31%
Erroneously classified as ENGLISH: 0,78%, FRENCH: 0,35%, ITALIAN: 0,29%, SPANISH: 0,16%, PORTUGUESE: 0,11%

>> Detection of 10000 sentences (average length: 47 chars)
Accuracy: 98,75%
Erroneously classified as ENGLISH: 0,97%, PORTUGUESE: 0,12%, FRENCH: 0,06%, ITALIAN: 0,06%, SPANISH: 0,04%
```

### <a name="library-dependency"></a> How to add it to your project? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* is currently hosted on [Jcenter] and will soon be available on Maven Central as well.

#### <a name="library-dependency-gradle"></a> Using Gradle

```
repositories {
    jcenter()
}

dependencies {
    implementation 'com.github.pemistahl:lingua:0.2.2'
}
```

#### <a name="library-dependency-maven"></a> Using Maven

```
<dependency>
    <groupId>com.github.pemistahl</groupId>
    <artifactId>lingua</artifactId>
    <version>0.2.2</version>
</dependency>

<repository>
    <id>jcenter</id>
    <url>https://jcenter.bintray.com/</url>
</repository>
```

### <a name="library-build"></a> How to build? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* uses Maven to build. A switch to Gradle is planned for the future.

```
git clone https://github.com/pemistahl/lingua.git
cd lingua
mvn install
```
Maven's `package` phase is able to generate two jar files in the `target` directory:
1. `mvn package` creates `lingua-0.2.2.jar` that contains the compiled sources only.
2. `mvn package -P with-dependencies` creates `lingua-0.2.2-with-dependencies.jar` that additionally contains all dependencies needed to use the library. This jar file can be included in projects without dependency management systems. You should be able to use it in your Android project as well by putting it in your project's `lib` folder. This jar file can also be used to run *Lingua* in standalone mode (see below).

### <a name="library-use"></a> How to use? <sup>[Top ▲](#table-of-contents)</sup>
*Lingua* can be used programmatically in your own code or in standalone mode.

#### <a name="library-use-programmatic"></a> Programmatic use <sup>[Top ▲](#table-of-contents)</sup>
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

If a string's language cannot be detected reliably because of missing linguistic information, `Language.UNKNOWN` is returned. The public API of *Lingua* never returns `null` somewhere, so it is safe to be used from within Java code as well.

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

#### <a name="library-use-standalone"></a> Standalone mode <sup>[Top ▲](#table-of-contents)</sup>
If you want to try out *Lingua* before you decide whether to use it or not, you can run it in a REPL and immediately see its detection results.
1. With Maven: `mvn exec:java`
2. Without Maven: `java -jar lingua-0.2.2-with-dependencies.jar`

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

### <a name="library-contribution"></a> Do you want to contribute? <sup>[Top ▲](#table-of-contents)</sup>

In case you want to contribute something to *Lingua* even though it's in a very early stage of development, then I encourage you to do so nevertheless. Do you have ideas for improving the API? Are there some specific languages that you want to have supported early? Or have you found any bugs so far? Feel free to open an issue or send a pull request. It's very much appreciated. :-)

### <a name="whats-next"></a> What's next for upcoming versions? <sup>[Top ▲](#table-of-contents)</sup>
- languages, languages, even more languages :-)
- accuracy improvements
- more unit tests
- API documentation
- for version 1.0.0: public API stability, Gradle build, multiplatform support

[travis ci badge]: https://travis-ci.org/pemistahl/lingua.svg?branch=master
[travis ci url]: https://travis-ci.org/pemistahl/lingua
[codebeat badge]: https://codebeat.co/badges/92a1d221-35b8-4e96-8d40-ed101b6f128e
[codebeat url]: https://codebeat.co/projects/github-com-pemistahl-lingua-master
[Codacy Badge]: https://api.codacy.com/project/badge/Grade/1d09ea5efbe348058ac163962c5c5264
[codacy url]: https://www.codacy.com/app/pemistahl/lingua?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pemistahl/lingua&amp;utm_campaign=Badge_Grade
[maintainability badge]: https://api.codeclimate.com/v1/badges/15822c202592b047f793/maintainability
[maintainability url]: https://codeclimate.com/github/pemistahl/lingua/maintainability
[codecov badge]: https://codecov.io/gh/pemistahl/lingua/branch/master/graph/badge.svg
[codecov url]: https://codecov.io/gh/pemistahl/lingua
[supported languages badge]: https://img.shields.io/badge/supported%20languages-7-red.svg
[awesome nlp badge]: https://raw.githubusercontent.com/sindresorhus/awesome/master/media/mentioned-badge-flat.svg?sanitize=true
[awesome nlp url]: https://github.com/keon/awesome-nlp#user-content-kotlin
[lingua version badge]: https://api.bintray.com/packages/pemistahl/nlp-libraries/lingua/images/download.svg
[lingua download url]: https://bintray.com/pemistahl/nlp-libraries/download_file?file_path=com%2Fgithub%2Fpemistahl%2Flingua%2F0.2.2%2Flingua-0.2.2-with-dependencies.jar
[Kotlin version badge]: https://img.shields.io/badge/Kotlin-1.3-blue.svg?logo=kotlin
[Kotlin url]: https://kotlinlang.org/docs/reference/whatsnew13.html
[Kotlin platforms badge]: https://img.shields.io/badge/platforms-JDK%206%2B%20%7C%20Android-blue.svg
[Kotlin platforms url]: https://kotlinlang.org/docs/reference/server-overview.html
[license badge]: https://img.shields.io/badge/license-Apache%202.0-blue.svg
[license url]: https://www.apache.org/licenses/LICENSE-2.0
[Wortschatz corpora]: http://wortschatz.uni-leipzig.de
[Apache Tika]: https://tika.apache.org/1.19.1/detection.html#Language_Detection
[Optimaize Language Detector]: https://github.com/optimaize/language-detector
[Jcenter]: https://bintray.com/pemistahl/nlp-libraries/lingua
[green-marker]: https://placehold.it/10/008000/000000?text=+
[yellow-marker]: https://placehold.it/10/ffff00/000000?text=+
[orange-marker]: https://placehold.it/10/ff8c00/000000?text=+
[red-marker]: https://placehold.it/10/ff0000/000000?text=+
[translate-lingua]: https://translate.google.com/?hl=en#view=home&op=translate&sl=en&tl=la&text=tongue

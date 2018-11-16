# lingua - language detection done easily
*lingua* is a language detection library for Kotlin and Java, suitable for long and short text alike.
___
[![ci build status][travis ci badge]][travis ci url]
[![Kotlin version][Kotlin version badge]][Kotlin url]
[![JVM target][JVM target badge]][JVM target url]
___

### What does this library do?
Its task is simple: It tells you which language some provided textual data is written in. This is very useful as a preprocessing step for linguistic data in natural language processing applications such as text classification and spell checking. Other use cases, for instance, might include routing e-mails to the right geographically located customer service department, based on the e-mails' languages.

### Why does this library exist?
Language detection is often done as part of large machine learning frameworks or natural language processing applications. In cases where you don't need the full-fledged functionality of those systems or don't want to learn the ropes of those, a small flexible library comes in handy. 

So far, on the JVM the only other comprehensive open source library for this task is [language-detector](https://github.com/optimaize/language-detector). Unfortunately, it has two major drawbacks:
 
1. Detection only works with quite lengthy text fragments. For very short text snippets such as Twitter messages, it doesn't provide adequate results.
2. Configuration of the library is quite cumbersome and requires some knowledge about the statistical methods that are used internally.

*lingua* aims at eliminating these problems. It nearly doesn't need any configuration and yields pretty accurate results on both long and short text, even on single words and phrases. At the same time, it draws on statistical methods only and does not use any dictionaries or other linguistic knowledge.

### Which languages are supported?

Currently, the following six languages are supported:

| Language | ISO 639-1 code |
| -------- | -------------- |
| English  | *en*           |
| French   | *fr*           |
| German   | *de*           |
| Italian  | *it*           |
| Latin    | *la*           |
| Spanish  | *es*           |

### How to build?
```
git clone https://github.com/pemistahl/lingua.git
cd lingua
mvn install
```
Maven's `package` phase generates two jar files in the `target` directory:
1. `lingua-0.1.0.jar` contains the compiled sources only.
2. `lingua-0.1.0-with-dependencies.jar` additionally contains all dependencies needed to use the library. This jar file can be included in projects without dependency management systems. It can also be used to run *lingua* in standalone mode (see below).

### How to use?
*lingua* can be used programmatically in your own code or in standalone mode.

#### Programmatic use
The API is pretty straightforward and can be used in both Kotlin and Java code.

```kotlin
import com.github.pemistahl.lingua.detector.LanguageDetector
import com.github.pemistahl.lingua.model.Language

val detector = LanguageDetector.fromAllBuiltInLanguages()
val detectedLanguage: Language? = detector.detectLanguageFrom(text = "languages are awesome")
detectedLanguage?.let { println(it) } // prints ENGLISH
```

In rare cases where a specific language cannot be detected reliably, `detectedLanguage` is `null`. Therefore, if *lingua* is used within Java code, it is necessary to not forget to check for `null`. Kotlin warns you about this thanks to its type system.

```java
LanguageDetector detector = LanguageDetector.fromAllBuiltInLanguages();
Language detectedLanguage = detector.detectLanguageFrom("languages are awesome");
if (detectedLanguage != null) System.out.println(detectedLanguage);
```

There might be classification tasks where you know beforehand that your language data is definitely not written in Latin, for instance (what a surprise :-). The detection accuracy can become better in such cases if you exclude certain languages from the decision process or just explicitly include relevant languages:
```kotlin
// exclude the Latin language from the decision algorithm
val detector = LanguageDetector.fromAllBuiltInLanguagesWithout(Language.LATIN)

// only decide between English and German
val detector = LanguageDetector.fromLanguages(Language.ENGLISH, Language.GERMAN)
```

#### Standalone mode
If you want to try out *lingua* before you decide whether to use it or not, you can run it in a REPL and immediately see its detection results.
1. With Maven: `mvn exec:java`
2. Without Maven: `java -jar lingua-0.1.0-with-dependencies.jar`

Then just play around:

```
This is Lingua.
Loading language models...
Done. 6 language models loaded.

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
> :quit
Bye! Ciao! Tschüss! Salut!
```

### What's next for upcoming versions?
- languages, languages, even more languages :-)
- unit tests
- detection accuracy measurements (with comparison to [language-detector](https://github.com/optimaize/language-detector))

[travis ci badge]: https://travis-ci.org/pemistahl/lingua.svg?branch=master
[travis ci url]: https://travis-ci.org/pemistahl/lingua
[Kotlin version badge]: https://img.shields.io/badge/Kotlin-1.3-blue.svg
[Kotlin url]: https://kotlinlang.org/docs/reference/whatsnew13.html
[JVM target badge]: https://img.shields.io/badge/JVM%20target-1.6+-yellowgreen.svg
[JVM target url]: https://www.oracle.com/technetwork/java/javase/java-archive-downloads-javase6-419409.html

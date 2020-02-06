## Lingua 0.6.1 (released on 06 Feb 2020)

### Bug Fixes

- The rule-based engine did not take language subset filtering from public api into account (#23).
- It was possible to pass through `Language.UNKNOWN` within the public api (#24).
- Fixed a bug in the rule-based engine's alphabet detection algorithm which could be misled by single characters (#25). 

## Lingua 0.6.0 (released on 05 Jan 2020)

### Languages

- added 11 new languages: Armenian, Bosnian, Azerbaijani, Esperanto, Georgian, Kazakh, Macedonian, Marathi, Mongolian, Serbian, Ukrainian 

### Features

There are some breaking changes in this release:

- The support for MapDB has been removed. It did not provide enough advantages over Kotlin's lazy loading of language models. It used a lot of disc space and language detection became slow. With the long-term goal of creating a multiplatform library, only those features will be implemented in the future that support JavaScript as well.
- The dependency on the fastutil library has been removed. It did not provide enough advantages over Kotlin's lazy loading of language models. 
- The method `LanguageDetector.detectLanguagesOf(text: Iterable<String>)` has been removed because the sorting order of the returned languages was undefined for input collections such as a HashSet. From now on, the method `LanguageDetector.detectLanguageOf(text: String)` will be the only one to be used.
- The `LanguageDetector` can now be built with the following additional methods:
  - `LanguageDetectorBuilder.fromIsoCodes639_1(vararg isoCodes: IsoCode639_1)`
  - `LanguageDetectorBuilder.fromIsoCodes639_3(vararg isoCodes: IsoCode639_3)`
  - the following method has been removed: `LanguageDetectorBuilder.fromIsoCodes(isoCode: String, vararg isoCodes: String)`  
- The Gson library has been replaced with kotlinx-serialization for the loading of the json language models. This results in a significant reduction of code and makes reflection obsolete, so the dependency on kotlin-reflect could be removed.

### Improvements

- The overall detection algorithm has been improved again several times to fix several detection bugs.

## Lingua 0.5.0 (released on 12 Aug 2019)

### Languages

- added 12 new languages: Bengali, Chinese (not differentiated between traditional and simplified, as of now), Gujarati, Hebrew, Hindi, Japanese, Korean, Punjabi, Tamil, Telugu, Thai, Urdu

### Features

- The `LanguageDetectorBuilder` now supports the additional method `withMinimumRelativeDistance()` that allows to specify the minimum distance between the logarithmized and summed up probabilities for each possible language. If two or more languages yield nearly the same probability for a given input text, it is likely that the wrong language may be returned. By specifying a higher value for the minimum relative distance, `Language.UNKNOWN` is returned instead of risking false positives.

- Test report generation can now use multiple CPU cores, allowing to run as many reports as CPU cores are available. This has been implemented as an additional attribute for the respective Gradle task: `./gradlew writeAccuracyReports -PcpuCores=...`

- The REPL now allows to freely specify the languages you want to try out by entering the desired ISO 639-1 codes. Before, it has only been possible to choose between certain language combinations.

### Improvements

- The overall detection algorithm has been improved, yielding slightly more accurate results for those languages that are based on the Latin alphabet.

### Bug Fixes

Thanks to the great work of contributor [Bernhard Geisberger](https://github.com/bgeisberger/), two bugs could be fixed.

1. The fix in pull request [#8](https://github.com/pemistahl/lingua/pull/8) solves the problem of not being able to recreate the MapDB cache files automatically in case the data has been corrupted.

2. The fix in pull request [#9](https://github.com/pemistahl/lingua/pull/9) makes the class `LanguageDetector` completely thread-safe. Previously, in some rare cases it was possible that two threads mutated on of the internal variables at the same time, yielding inaccurate language detection results.

Thank you, Bernhard.

## Lingua 0.4.0 (released on 7 May 2019)

This release took some time, but here it is.

### Languages

- added 18 new languages: Afrikaans, Albanian, Basque, Bokmal, Catalan, Greek, Icelandic, Indonesian, Irish, Malay, Norwegian, Nynorsk, Slovak, Slovene, Somali, Tagalog, Vietnamese, Welsh

### Features

- Language models are now lazy-loaded into memory upon first access and not already when an instance of `LanguageDetector` is created. This way, if the rule-based engine can filter out some unlikely languages, their language models are not loaded into memory as they are not necessary at that point. So the overall memory consumption is further reduced.

- The [fastutil](http://fastutil.di.unimi.it) library is used to compress the probability values of the language models in memory. They are now stored as primitive data types (`double`) instead of objects (`Double`) which reduces memory consumption by approximately 500 MB if all language models are selected.

### Improvements

- The overall code quality has been improved significantly. This allows for easier unit testing, configuration and extensibility.

### Bug Fixes

- Reported bug [#3](https://github.com/pemistahl/lingua/issues/3) has been fixed which prevented certain character classes to be used on Android.

### Build system

- Starting from this version, Gradle is used as this library's build system instead of Maven. This allows for more customizations, such as in test report generation, and is a first step towards multiplatform support. Please take a look at this project's [README](https://github.com/pemistahl/lingua/blob/v0.4.0/README.md) to read about the available Gradle tasks.

### Test Coverage

- Test coverage has been extended from 24% to 55%. 

## Lingua 0.3.2 (released on 8 Feb 2019)

This minor update fixes a critical bug reported in issue [#1](https://github.com/pemistahl/lingua/issues/1).

### Bug Fixes

- The attempt to detect the language of a string solely containing characters that do not occur in any of the supported languages returned `kotlin.KotlinNullPointerException`. This has been fixed in this release. Instead, `Language.UNKNOWN` is now returned as expected.

### Dependency Updates

- The Kotlin compiler, standard library and runtime have been updated from version 1.3.20 to 1.3.21

## Lingua 0.3.1 (released on 24 Jan 2019)

This minor update contains some significant detection accuracy improvements.

### Accuracy Improvements

- added new detection rules to improve accuracy especially for single words and word pairs
- accuracy for single words has been increased from 78% to 82% on average
- accuracy for word pairs has been increased from 92% to 94% on average
- accuracy for sentences has been increased from 98% to 99% on average
- overall accuracy has been increased from 90% to 91% on average
- overall standard deviation has been reduced from 6.01 to 5.35

### API changes

- `LanguageDetectorBuilder.fromIsoCodes()` now accepts `vararg` arguments instead of a `List` in order to have a consistent API with the other methods of `LanguageDetectorBuilder`
- If a language iso 639-1 code is passed to `LanguageDetectorBuilder.fromIsoCodes()` which does not exist, then an `IllegalArgumentException` is thrown. Previously, `Language.UNKNOWN` was returned. However, this could lead to bugs as a `LanguageDetector` with `Language.UNKNOWN` was built. This is now prevented.

### Dependency Updates

- The Kotlin compiler, standard library and runtime have been updated from version 1.3.11 to 1.3.20

## Lingua 0.3.0 (released on 16 Jan 2019)

This major release offers a lot of new features, including new languages. Finally! :-)

### Languages

- added 18 languages: Arabic, Belarusian, Bulgarian, Croatian, Czech, Danish, Dutch, Estonian, Finnish, Hungarian, Latvian, Lithuanian, Polish, Persian, Romanian, Russian, Swedish, Turkish

### Features

- Language models can now be cached by [MapDB](https://github.com/jankotek/mapdb) to reduce memory usage and speed up loading times.

### Improvements

- In the standalone app, you can now choose which language models to load in order to compare detection accuracy between strongly related languages.
- For test report generation using Maven, you can now select a specific language using the attribute `language` and do not need to run the reports for all languages anymore: `mvn test -P accuracy-reports -D detector=lingua -D language=German`.

### API changes

- *Lingua's* package structure has been simplified. The public API intended for end users now lives in `com.github.pemistahl.lingua.api`. Breaking changes herein are tried to keep to a minimum in `0.*.*` versions and will not be performed anymore starting from version `1.0.0`. All other code is stored in `com.github.pemistahl.lingua.internal` and is subject to change without any further notice.
- added new class `com.github.pemistahl.lingua.api.LanguageDetectorBuilder` which is now responsible for building and configuring instances of `com.github.pemistahl.lingua.api.LanguageDetector`

### Test Coverage

- Test coverage of the public API has been extended from 6% to 23%.

### Documentation

- In addition to the test reports, graphical plots have been created in order to compare the detection results between the different classifiers even more easily. The code for the plots has been written in Python and is stored in an IPython notebook under [`/accuracy-reports/accuracy-reports-analysis-notebook.ipynb`](https://github.com/pemistahl/lingua/blob/master/accuracy-reports/accuracy-reports-analysis-notebook.ipynb).


## Lingua 0.2.1 (released on 28 Dec 2018)

This minor version update provides the following:

### Improvements

- The included language model JSON files now use a more efficient formatting, saving approximately 25% disk space in uncompressed format compared to version 0.2.1.

### Bug Fixes

- The version of the Jacoco test coverage Maven plugin was incorrectly specified, leading to download errors. Now the most current snapshot version of Jacoco is used which provides enhancements for Kotlin test coverage measurement.

## Lingua 0.2.0 (released on 17 Dec 2018)

This release provides both new features and bug fixes. It is the first release that has been published to JCenter. Publication on Maven Central will follow soon.

### Languages
- added detection support for Portuguese

### Features
- extended language models for already existing languages to provide for more accurate detection results
- the larger language models are now lazy-loaded to reduce waiting times during start-up, especially when starting the *lingua* REPL
- added some unit tests for the [LanguageDetector] class that cover the most basic functionality (will be extended in upcoming versions)
- added accuracy reports and test data for each supported language, in order to measure language detection accuracy (can be generated with `mvn test -P accuracy-reports`)
- added accuracy statistics summary of the current implementation to [README]

### API changes
- renamed method `LanguageDetector.detectLanguageFrom()` to `LanguageDetector.detectLanguageOf()` to use the grammatically correct English preposition
- in version `0.1.0`, the now called method `LanguageDetector.detectLanguageOf()` returned `null` for strings whose language could not be detected reliably. Now, `Language.UNKNOWN` is returned instead in those cases to prevent `NullPointerException`s especially in Java code.

### Bug Fixes
- fixed a bug in *lingua's* REPL that caused non-ASCII characters to get broken in consoles which do not use UTF-8 encoding by default, especially on Windows systems

[LanguageDetector]: https://github.com/pemistahl/lingua/blob/v0.2.0/src/main/kotlin/com/github/pemistahl/lingua/detector/LanguageDetector.kt
[README]: https://github.com/pemistahl/lingua/blob/v0.2.0/README.md

## Lingua 0.1.0 (released on 16 Nov 2018)

This is the very first release of *Lingua*. It aims at accurate language detection results for both long and especially short text. Detection on short text fragments such as Twitter messages is a weak spot of many similar libraries.

Supported languages so far:
- English
- French
- German
- Italian
- Latin
- Spanish

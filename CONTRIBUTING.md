## You want to contribute to Lingua? That's great!

In case you want to contribute something to *Lingua*, then I encourage you to do so. Do you have ideas for 
improving the API? Are there some specific languages that you want to have supported early? Or have you 
found any bugs so far? Feel free to open an issue or send a pull request. It's very much appreciated.

For pull requests, please make sure that all unit tests pass and that the code is formatted according to
the official Kotlin style guide. You can check this by running the Kotlin linter [ktlint](https://ktlint.github.io/) 
using `./gradlew ktlintCheck`. Most issues which the linter identifies can be fixed by running `./gradlew ktlintFormat`.
All other issues, especially lines which are longer than 120 characters, cannot be fixed automatically. In this case,
please format the respective lines by hand. You will notice that the build will fail if the formatting is not correct.

All kinds of pull requests are welcome. The pull requests I favor the most are new language additions. If you want
to contribute new languages to *Lingua*, here comes a detailed manual explaining how to accomplish that.

Thank you very much in advance for all contributions, however small they may be.

### How to add new languages?

**An important notice beforehand:** 
In order to execute the steps below, you will need Java 8 or greater. Even though the library itself
runs on Java >= 6, the `FilesWriter` classes make use of the [java.nio][java nio url] api which was
introduced with Java 8.

1. Clone *Lingua's* repository to your own computer as described in README's [section 8][library build url].
2. Open enums [`IsoCode639_1`][isocode639_1 url] and [`IsoCode639_3`][isocode639_3 url] and add the 
language's iso codes. Among other sites, Wikipedia provides a [comprehensive list][wikipedia isocodes list].
3. Open enum [`Language`][language url] and add a new entry for your language. If the language is written
with a script that is not yet supported by *Lingua's* [`Alphabet`][alphabet url] enum, then add a new entry
for it there as well.
4. If your language's script contains characters that are completely unique to it, then add them to the
respective entry in the [`Language`][language url] enum. However, if the characters occur in more than one
language **but** not in all languages, then add them to the 
[`CHARS_TO_LANGUAGES_MAPPING`][chars to languages mapping url] constant in class `Constant` instead.
5. Use [`LanguageModelFilesWriter`][language model files writer url] to create the language model files.
The training data file used for ngram probability estimation is not required to have a specific format
other than to be a valid txt file.
6. Create a new subdirectory in [`/src/main/resources/language-models`][language models directory url]
and put the generated language model files in there. Do **not** rename the language model files. 
The name of the subdirectory **must** be the language's ISO 639-1 code, completely lowercased.
7. Use [`TestDataFilesWriter`][test data files writer url] to create the test data files used for
accuracy report generation. The input file from which to create the test data should have each
sentence on a separate line.
8. Put the generated test data files in [`/src/test/resources/language-testdata`][test data directory url].
Do **not** rename the test data files.
9. For accuracy report generation, create an abstract base class for the main logic in
[`/src/test/kotlin/com/github/pemistahl/lingua/report/config`][accuracy report config url].
Look at the other languages' files in this directory to see how the class must look like.
It should be pretty self-explanatory.
10. Create a concrete test class in 
[`/src/test/kotlin/com/github/pemistahl/lingua/report/lingua`][accuracy report lingua url].
Look at the other languages' files in this directory to see how the class must look like.
It should be pretty self-explanatory. If one of the other language detector libraries 
supports your language already, you can add test classes for those as well. Each library 
has its own directory for this purpose.
11. Fix the existing unit tests by adding your new language.
12. Add your new language to property [`linguaSupportedLanguages`][gradle properties url] 
in `/gradle.properties`.
13. Run `./gradlew writeAccuracyReports` and add the updated accuracy reports to your pull request.
14. Run `./gradlew drawAccuracyPlots` and add the updated plots to your pull request.
15. Run `./gradlew writeAccuracyTable` and add the updated accuracy table to your pull request.
16. Be happy! :-) You have successfully contributed a new language and have thereby significantly widened
this library's fields of application. 

[java nio url]: https://docs.oracle.com/javase/8/docs/api/java/nio/package-summary.html
[library build url]: https://github.com/pemistahl/lingua#library-build
[isocode639_1 url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/IsoCode639_1.kt
[isocode639_3 url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/IsoCode639_3.kt
[wikipedia isocodes list]: https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
[language url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/Language.kt
[alphabet url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/internal/Alphabet.kt
[chars to languages mapping url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/internal/Constant.kt#L67
[language model files writer url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/io/LanguageModelFilesWriter.kt#L27
[language models directory url]: https://github.com/pemistahl/lingua/tree/main/src/main/resources/language-models
[test data files writer url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/io/TestDataFilesWriter.kt#L28
[test data directory url]: https://github.com/pemistahl/lingua/tree/main/src/test/resources/language-testdata
[accuracy report config url]: https://github.com/pemistahl/lingua/tree/main/src/test/kotlin/com/github/pemistahl/lingua/report/config
[accuracy report lingua url]: https://github.com/pemistahl/lingua/tree/main/src/test/kotlin/com/github/pemistahl/lingua/report/lingua
[gradle properties url]: https://github.com/pemistahl/lingua/blob/main/gradle.properties#L62

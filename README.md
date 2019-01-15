[![lingua](images/logo.png)][translate-lingua] 

# language detection done right [![awesome nlp badge][awesome nlp badge]][awesome nlp url]
*Lingua* is a language detection library for Kotlin and Java, suitable for long and short text alike.

---
[![Download][lingua version badge]][lingua download url]
[![supported languages][supported languages badge]](#supported-languages)
---
[![ci build status][travis ci badge]][travis ci url]
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
    * [Tabular Comparison](#library-comparison-tabular)
    * [Graphical Comparison](#library-comparison-graphical)
      * [Single Words](#library-comparison-graphical-singlewords)
      * [Word Pairs](#library-comparison-graphical-wordpairs)
      * [Sentences](#library-comparison-graphical-sentences)
      * [Average](#library-comparison-graphical-average)
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

## <a name="library-purpose"></a> What does this library do? <sup>[Top ▲](#table-of-contents)</sup>
Its task is simple: It tells you which language some provided textual data is written in. This is very useful as a preprocessing step for linguistic data in natural language processing applications such as text classification and spell checking. Other use cases, for instance, might include routing e-mails to the right geographically located customer service department, based on the e-mails' languages.

## <a name="library-reason"></a> Why does this library exist? <sup>[Top ▲](#table-of-contents)</sup>
Language detection is often done as part of large machine learning frameworks or natural language processing applications. In cases where you don't need the full-fledged functionality of those systems or don't want to learn the ropes of those, a small flexible library comes in handy. 

So far, two other comprehensive open source libraries working on the JVM for this task are [Apache Tika] and [Optimaize Language Detector]. Unfortunately, especially the latter has two major drawbacks:
 
1. Detection only works with quite lengthy text fragments. For very short text snippets such as Twitter messages, it doesn't provide adequate results.
2. Configuration of the library is quite cumbersome and requires some knowledge about the statistical methods that are used internally.

*Lingua* aims at eliminating these problems. It nearly doesn't need any configuration and yields pretty accurate results on both long and short text, even on single words and phrases. It draws on both rule-based and statistical methods but does not use any dictionaries of words. It does not need a connection to any external API or service either. Once the library has been downloaded, it can be used completely offline. 

## <a name="supported-languages"></a> Which languages are supported? <sup>[Top ▲](#table-of-contents)</sup>

Compared to other language detection libraries, *Lingua's* focus is on *quality over quantity*, that is, getting detection right for a small set of languages first before adding new ones. Currently, the following 25 languages are supported:

| Language   | ISO 639-1 code | Language   | ISO 639-1 code |
| --------   | -------------- | --------   | -------------- |
| Arabic     | *ar*           | Italian    | *it*           |
| Belarusian | *be*           | Latin      | *la*           |
| Bulgarian  | *bg*           | Latvian    | *lv*           |
| Croatian   | *hr*           | Lithuanian | *lt*           |
| Czech      | *cs*           | Polish     | *pl*           |
| Danish     | *da*           | Persian    | *fa*           |
| Dutch      | *nl*           | Portuguese | *pt*           |
| English    | *en*           | Romanian   | *ro*           |
| Estonian   | *et*           | Russian    | *ru*           |
| Finnish    | *fi*           | Spanish    | *es*           |
| French     | *fr*           | Swedish    | *sv*           |
| German     | *de*           | Turkish    | *tr*           |
| Hungarian  | *hu*           |            |                |

## <a name="library-accuracy"></a> How good is it? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* is able to report accuracy statistics for some bundled test data available for each supported language. The test data for each language is split into three parts:
1. a list of single words with a minimum length of 5 characters
2. a list of word pairs with a minimum length of 10 characters
3. a list of complete grammatical sentences of various lengths

Both the language models and the test data have been created from separate documents of the [Wortschatz corpora] offered by Leipzig University, Germany. Data crawled from various news websites have been used for training, each corpus comprising one million sentences. For testing, corpora made of arbitrarily chosen websites have been used, each comprising ten thousand sentences. From each test corpus, a random unsorted subset of 1000 single words, 1000 word pairs and 1000 sentences has been extracted, respectively.

### <a name="library-comparison"></a> Comparison of Libraries <sup>[Top ▲](#table-of-contents)</sup>

Given the generated test data, I have compared the detection results of *Lingua*, *Apache Tika* and *Optimaize Language Detector* using parameterized JUnit tests running over the data of 24 languages. *Tika* actually uses a heavily optimized version of *Optimaize* internally. Latin is currently only supported by *Lingua*, so it's left out both in the decision process and in the comparison. All other 24 are indeed part of the decision process, that is, each classifier might theoretically return one of these 24 languages as the result.

#### <a name="library-comparison-tabular"></a> Tabular Comparison <sup>[Top ▲](#table-of-contents)</sup>

As the table below shows, *Lingua* outperforms the other two libraries significantly. All values are rounded percentages. When it comes to detecting the language of entire sentences, all three libraries are nearly equally accurate. It is actually short paragraphs of text where *Lingua* plays to its strengths. Even though *Lingua* is in an early stage of development, detection accuracy for word pairs is already 8% higher on average than with *Tika*, for single words it is even 12% higher. 

It seems that languages with less characteristic letters are harder to identify than those that have, such as English and Dutch. This is probably due to their lack of diacritics and other distinctive letters, basically using the letters A-Z only. Even though English and Dutch are harder to identify for *Lingua* as well, it performs much better than *Apache Tika* with 28% more English and 23% more Dutch words classified correctly. 

By looking at the standard deviations of the separate categories, it is remarkable that *Lingua's* detection quality is much more stable and consistent across the different languages. The accuracy values of *Tika* and *Optimaize* fluctuate in a broader scope than *Lingua's* results. Whereas *Tika's* values for single word detection range between 33% (Spanish) and 95% (Arabic and Belarusian), *Lingua* only ranges between 55% (Spanish) and 97% (Arabic).  

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
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>Optimaize</th>
        <th>Lingua</th>
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>Optimaize</th>
        <th>Lingua</th>
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>Optimaize</th>
        <th>Lingua</th>
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>Optimaize</th>
    </tr>
    <tr>
        <td>Arabic</td>
        <td>98 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>91 <img src="images/green.png"></td>
        <td>97 <img src="images/green.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>77 <img src="images/lightgreen.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>96 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Belarusian</td>
        <td>98 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>91 <img src="images/green.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>78 <img src="images/lightgreen.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>96 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Bulgarian</td>
        <td>94 <img src="images/green.png"></td>
        <td>89 <img src="images/green.png"></td>
        <td>58 <img src="images/yellow.png"></td>
        <td>87 <img src="images/green.png"></td>
        <td>79 <img src="images/lightgreen.png"></td>
        <td>25 <img src="images/orange.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>89 <img src="images/green.png"></td>
        <td>51 <img src="images/yellow.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>97 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Croatian</td>
        <td>91 <img src="images/green.png"></td>
        <td>89 <img src="images/green.png"></td>
        <td>70 <img src="images/lightgreen.png"></td>
        <td>79 <img src="images/lightgreen.png"></td>
        <td>75 <img src="images/lightgreen.png"></td>
        <td>35 <img src="images/orange.png"></td>
        <td>94 <img src="images/green.png"></td>
        <td>92 <img src="images/green.png"></td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Czech</td>
        <td>84 <img src="images/green.png"></td>
        <td>83 <img src="images/green.png"></td>
        <td>71 <img src="images/lightgreen.png"></td>
        <td>72 <img src="images/lightgreen.png"></td>
        <td>70 <img src="images/lightgreen.png"></td>
        <td>49 <img src="images/yellow.png"></td>
        <td>85 <img src="images/green.png"></td>
        <td>86 <img src="images/green.png"></td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>94 <img src="images/green.png"></td>
        <td>93 <img src="images/green.png"></td>
        <td>89 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Danish</td>
        <td>89 <img src="images/green.png"></td>
        <td>85 <img src="images/green.png"></td>
        <td>58 <img src="images/yellow.png"></td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>68 <img src="images/lightgreen.png"></td>
        <td>24 <img src="images/orange.png"></td>
        <td>92 <img src="images/green.png"></td>
        <td>88 <img src="images/green.png"></td>
        <td>55 <img src="images/yellow.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>97 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Dutch</td>
        <td>85 <img src="images/green.png"></td>
        <td>72 <img src="images/lightgreen.png"></td>
        <td>51 <img src="images/yellow.png"></td>
        <td>69 <img src="images/lightgreen.png"></td>
        <td>46 <img src="images/yellow.png"></td>
        <td>14 <img src="images/red.png"></td>
        <td>89 <img src="images/green.png"></td>
        <td>71 <img src="images/lightgreen.png"></td>
        <td>42 <img src="images/yellow.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>English</td>
        <td>85 <img src="images/green.png"></td>
        <td>68 <img src="images/lightgreen.png"></td>
        <td>43 <img src="images/yellow.png"></td>
        <td>65 <img src="images/lightgreen.png"></td>
        <td>37 <img src="images/orange.png"></td>
        <td>5 <img src="images/red.png"></td>
        <td>90 <img src="images/green.png"></td>
        <td>66 <img src="images/lightgreen.png"></td>
        <td>28 <img src="images/orange.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>97 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Estonian</td>
        <td>93 <img src="images/green.png"></td>
        <td>87 <img src="images/green.png"></td>
        <td>64 <img src="images/lightgreen.png"></td>
        <td>84 <img src="images/green.png"></td>
        <td>72 <img src="images/lightgreen.png"></td>
        <td>26 <img src="images/orange.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>90 <img src="images/green.png"></td>
        <td>66 <img src="images/lightgreen.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Finnish</td>
        <td>97 <img src="images/green.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>80 <img src="images/lightgreen.png"></td>
        <td>94 <img src="images/green.png"></td>
        <td>88 <img src="images/green.png"></td>
        <td>55 <img src="images/yellow.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>96 <img src="images/green.png"></td>
        <td>87 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>French</td>
        <td>91 <img src="images/green.png"></td>
        <td>80 <img src="images/lightgreen.png"></td>
        <td>58 <img src="images/yellow.png"></td>
        <td>79 <img src="images/lightgreen.png"></td>
        <td>59 <img src="images/yellow.png"></td>
        <td>23 <img src="images/orange.png"></td>
        <td>96 <img src="images/green.png"></td>
        <td>83 <img src="images/green.png"></td>
        <td>55 <img src="images/yellow.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>German</td>
        <td>91 <img src="images/green.png"></td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>57 <img src="images/yellow.png"></td>
        <td>79 <img src="images/lightgreen.png"></td>
        <td>54 <img src="images/yellow.png"></td>
        <td>25 <img src="images/orange.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>74 <img src="images/lightgreen.png"></td>
        <td>48 <img src="images/yellow.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Hungarian</td>
        <td>95 <img src="images/green.png"></td>
        <td>90 <img src="images/green.png"></td>
        <td>81 <img src="images/green.png"></td>
        <td>88 <img src="images/green.png"></td>
        <td>79 <img src="images/lightgreen.png"></td>
        <td>58 <img src="images/yellow.png"></td>
        <td>97 <img src="images/green.png"></td>
        <td>92 <img src="images/green.png"></td>
        <td>85 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Italian</td>
        <td>90 <img src="images/green.png"></td>
        <td>83 <img src="images/green.png"></td>
        <td>54 <img src="images/yellow.png"></td>
        <td>77 <img src="images/lightgreen.png"></td>
        <td>64 <img src="images/lightgreen.png"></td>
        <td>16 <img src="images/red.png"></td>
        <td>93 <img src="images/green.png"></td>
        <td>85 <img src="images/green.png"></td>
        <td>47 <img src="images/yellow.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Latvian</td>
        <td>78 <img src="images/lightgreen.png"></td>
        <td>88 <img src="images/green.png"></td>
        <td>75 <img src="images/lightgreen.png"></td>
        <td>62 <img src="images/lightgreen.png"></td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>52 <img src="images/yellow.png"></td>
        <td>85 <img src="images/green.png"></td>
        <td>91 <img src="images/green.png"></td>
        <td>78 <img src="images/lightgreen.png"></td>
        <td>89 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>96 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Lithuanian</td>
        <td>86 <img src="images/green.png"></td>
        <td>90 <img src="images/green.png"></td>
        <td>74 <img src="images/lightgreen.png"></td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>77 <img src="images/lightgreen.png"></td>
        <td>45 <img src="images/yellow.png"></td>
        <td>87 <img src="images/green.png"></td>
        <td>94 <img src="images/green.png"></td>
        <td>79 <img src="images/lightgreen.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Persian</td>
        <td>96 <img src="images/green.png"></td>
        <td>86 <img src="images/green.png"></td>
        <td>73 <img src="images/lightgreen.png"></td>
        <td>91 <img src="images/green.png"></td>
        <td>73 <img src="images/lightgreen.png"></td>
        <td>49 <img src="images/yellow.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>85 <img src="images/green.png"></td>
        <td>72 <img src="images/lightgreen.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Polish</td>
        <td>92 <img src="images/green.png"></td>
        <td>91 <img src="images/green.png"></td>
        <td>84 <img src="images/green.png"></td>
        <td>81 <img src="images/green.png"></td>
        <td>79 <img src="images/lightgreen.png"></td>
        <td>62 <img src="images/lightgreen.png"></td>
        <td>96 <img src="images/green.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>89 <img src="images/green.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Portuguese</td>
        <td>83 <img src="images/green.png"></td>
        <td>65 <img src="images/lightgreen.png"></td>
        <td>41 <img src="images/yellow.png"></td>
        <td>65 <img src="images/lightgreen.png"></td>
        <td>38 <img src="images/orange.png"></td>
        <td>8 <img src="images/red.png"></td>
        <td>87 <img src="images/green.png"></td>
        <td>60 <img src="images/lightgreen.png"></td>
        <td>22 <img src="images/orange.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>95 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Romanian</td>
        <td>83 <img src="images/green.png"></td>
        <td>81 <img src="images/green.png"></td>
        <td>57 <img src="images/yellow.png"></td>
        <td>67 <img src="images/lightgreen.png"></td>
        <td>62 <img src="images/lightgreen.png"></td>
        <td>26 <img src="images/orange.png"></td>
        <td>88 <img src="images/green.png"></td>
        <td>82 <img src="images/green.png"></td>
        <td>53 <img src="images/yellow.png"></td>
        <td>94 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>92 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Russian</td>
        <td>95 <img src="images/green.png"></td>
        <td>87 <img src="images/green.png"></td>
        <td>64 <img src="images/lightgreen.png"></td>
        <td>88 <img src="images/green.png"></td>
        <td>75 <img src="images/lightgreen.png"></td>
        <td>34 <img src="images/orange.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>92 <img src="images/green.png"></td>
        <td>67 <img src="images/lightgreen.png"></td>
        <td>99 <img src="images/green.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>90 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Spanish</td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>62 <img src="images/lightgreen.png"></td>
        <td>34 <img src="images/orange.png"></td>
        <td>55 <img src="images/yellow.png"></td>
        <td>33 <img src="images/orange.png"></td>
        <td>1 <img src="images/red.png"></td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>54 <img src="images/yellow.png"></td>
        <td>8 <img src="images/red.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>92 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Swedish</td>
        <td>89 <img src="images/green.png"></td>
        <td>74 <img src="images/lightgreen.png"></td>
        <td>52 <img src="images/yellow.png"></td>
        <td>75 <img src="images/lightgreen.png"></td>
        <td>49 <img src="images/yellow.png"></td>
        <td>18 <img src="images/red.png"></td>
        <td>91 <img src="images/green.png"></td>
        <td>74 <img src="images/lightgreen.png"></td>
        <td>44 <img src="images/yellow.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
        <td>94 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td>Turkish</td>
        <td>93 <img src="images/green.png"></td>
        <td>86 <img src="images/green.png"></td>
        <td>74 <img src="images/lightgreen.png"></td>
        <td>85 <img src="images/green.png"></td>
        <td>71 <img src="images/lightgreen.png"></td>
        <td>49 <img src="images/yellow.png"></td>
        <td>95 <img src="images/green.png"></td>
        <td>87 <img src="images/green.png"></td>
        <td>76 <img src="images/lightgreen.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>100 <img src="images/green.png"></td>
        <td>98 <img src="images/green.png"></td>
    </tr>
    <tr>
        <td colspan="9"></td>
    </tr>
    <tr>
        <td><strong>Mean</strong></td>
        <td><strong>90</strong> <img src="images/green.png"></td>
        <td><strong>83</strong> <img src="images/green.png"></td>
        <td><strong>65</strong> <img src="images/lightgreen.png"></td>
        <td><strong>78</strong> <img src="images/lightgreen.png"></td>
        <td><strong>67</strong> <img src="images/lightgreen.png"></td>
        <td><strong>35</strong> <img src="images/orange.png"></td>
        <td><strong>92</strong> <img src="images/green.png"></td>
        <td><strong>84</strong> <img src="images/green.png"></td>
        <td><strong>62</strong> <img src="images/lightgreen.png"></td>
        <td><strong>98</strong> <img src="images/green.png"></td>
        <td><strong>99</strong> <img src="images/green.png"></td>
        <td><strong>97</strong> <img src="images/green.png"></td>
    </tr>
    <tr>
        <td colspan="9"></td>
    </tr>
    <tr>
        <td>Median</td>
        <td>90.83</td>
        <td>85.85</td>
        <td>63.68</td>
        <td>78.50</td>
        <td>71.30</td>
        <td>30.35</td>
        <td>94.50</td>
        <td>87.40</td>
        <td>66.60</td>
        <td>99.20</td>
        <td>99.30</td>
        <td>97.70</td>
    </tr>
    <tr>
        <td>Standard Deviation</td>
        <td>6.01</td>
        <td>9.71</td>
        <td>15.25</td>
        <td>11.11</td>
        <td>17.01</td>
        <td>21.69</td>
        <td>5.66</td>
        <td>11.99</td>
        <td>23.29</td>
        <td>2.70</td>
        <td>1.62</td>
        <td>3.09</td>
    </tr>
</table>

### <a name="library-comparison-graphical"></a> Graphical Comparison <sup>[Top ▲](#table-of-contents)</sup>

The following plots even better reflect how *Lingua* performs next to its contenders.

#### <a name="library-comparison-graphical-singlewords"></a> Single Words <sup>[Top ▲](#table-of-contents)</sup>

The line plot shows that *Lingua's* detection accuracy for single words is superior except for Latvian. Looking at the report for this language, more than 12% of the words are erroneously classified as Swedish or Turkish. This is probably due to some very specific characters that occur much more often in the other two languages.

As already stated, some languages generally seem harder to identify than others, especially the Romance languages French, Italian, Portuguese and Spanish which are very similar to each other. Also, German and English are difficult to keep apart.

![lineplot-singlewords](/images/plots/lineplot-singlewords.png)

The box plot very nicely shows the differences in the deviation of the results, proving that *Lingua* is generally more reliable, independent of the language. 

![boxplot-singlewords](/images/plots/boxplot-singlewords.png)

#### <a name="library-comparison-graphical-wordpairs"></a> Word Pairs <sup>[Top ▲](#table-of-contents)</sup>

Comparing the plots for word pairs, they illustrate the same aspects as mentioned for single words above. The detection results for Lithuanian are worse than with *Apache Tika*, showing that Baltic languages obviously need some more care.

![lineplot-wordpairs](/images/plots/lineplot-wordpairs.png)

![boxplot-wordpairs](/images/plots/boxplot-wordpairs.png)

#### <a name="library-comparison-graphical-sentences"></a> Sentences <sup>[Top ▲](#table-of-contents)</sup>

As already said, Baltic languages seem to be a problem. This will be fixed in the next minor version update.

![lineplot-sentences](/images/plots/lineplot-sentences.png)

![boxplot-sentences](/images/plots/boxplot-sentences.png)

#### <a name="library-comparison-graphical-average"></a> Average <sup>[Top ▲](#table-of-contents)</sup>

![lineplot-average](/images/plots/lineplot-average.png)

![boxplot-average](/images/plots/boxplot-average.png)

### <a name="report-generation"></a> Test Report Generation <sup>[Top ▲](#table-of-contents)</sup>

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

## <a name="library-dependency"></a> How to add it to your project? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* is hosted on [Jcenter] and [Maven Central].

### <a name="library-dependency-gradle"></a> Using Gradle

```
implementation 'com.github.pemistahl:lingua:0.3.0'
```

### <a name="library-dependency-maven"></a> Using Maven

```
<dependency>
    <groupId>com.github.pemistahl</groupId>
    <artifactId>lingua</artifactId>
    <version>0.3.0</version>
</dependency>
```

## <a name="library-build"></a> How to build? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* uses Maven to build. A switch to Gradle is planned for the future.

```
git clone https://github.com/pemistahl/lingua.git
cd lingua
mvn install
```
Maven's `package` phase is able to generate two jar files in the `target` directory:
1. `mvn package` creates `lingua-0.3.0.jar` that contains the compiled sources only.
2. `mvn package -P with-dependencies` creates `lingua-0.3.0-with-dependencies.jar` that additionally contains all dependencies needed to use the library. This jar file can be included in projects without dependency management systems. You should be able to use it in your Android project as well by putting it in your project's `lib` folder. This jar file can also be used to run *Lingua* in standalone mode (see below).

## <a name="library-use"></a> How to use? <sup>[Top ▲](#table-of-contents)</sup>
*Lingua* can be used programmatically in your own code or in standalone mode.

### <a name="library-use-programmatic"></a> Programmatic use <sup>[Top ▲](#table-of-contents)</sup>
The API is pretty straightforward and can be used in both Kotlin and Java code.

```kotlin
/* Kotlin */

import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.Language

println(LanguageDetector.supportedLanguages())
// [ENGLISH, FRENCH, GERMAN, ITALIAN, LATIN, PORTUGUESE, SPANISH]

val detector = LanguageDetector.fromAllBuiltInLanguages()
val detectedLanguage: Language = detector.detectLanguageOf(text = "languages are awesome")

// returns Language.ENGLISH
```

If a string's language cannot be detected reliably because of missing linguistic information, `Language.UNKNOWN` is returned. The public API of *Lingua* never returns `null` somewhere, so it is safe to be used from within Java code as well.

```java
/* Java */

import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.Language;

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

### <a name="library-use-standalone"></a> Standalone mode <sup>[Top ▲](#table-of-contents)</sup>
If you want to try out *Lingua* before you decide whether to use it or not, you can run it in a REPL and immediately see its detection results.
1. With Maven: `mvn exec:java`
2. Without Maven: `java -jar lingua-0.3.0-with-dependencies.jar`

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

## <a name="library-contribution"></a> Do you want to contribute? <sup>[Top ▲](#table-of-contents)</sup>

In case you want to contribute something to *Lingua* even though it's in a very early stage of development, then I encourage you to do so nevertheless. Do you have ideas for improving the API? Are there some specific languages that you want to have supported early? Or have you found any bugs so far? Feel free to open an issue or send a pull request. It's very much appreciated. :-)

## <a name="whats-next"></a> What's next for upcoming versions? <sup>[Top ▲](#table-of-contents)</sup>
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
[Jcenter]: https://bintray.com/pemistahl/nlp-libraries/lingua/0.2.2
[Maven Central]: https://search.maven.org/artifact/com.github.pemistahl/lingua/0.2.2/jar
[green-marker]: https://placehold.it/10/008000/000000?text=+
[yellow-marker]: https://placehold.it/10/ffff00/000000?text=+
[orange-marker]: https://placehold.it/10/ff8c00/000000?text=+
[red-marker]: https://placehold.it/10/ff0000/000000?text=+
[translate-lingua]: https://translate.google.com/?hl=en#view=home&op=translate&sl=en&tl=la&text=tongue

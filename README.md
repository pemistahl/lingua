[![lingua](images/logo.png)][translate-lingua] 

# language detection done right [![awesome nlp badge][awesome nlp badge]][awesome nlp url]
*Lingua* is a language detection library for Kotlin and Java, suitable for long and short text alike.

---
[![supported languages][supported languages badge]](#supported-languages)
[![change log][change log badge]][change log]
[![Maven Central][Maven Central badge]][Maven Central]
[![Jcenter][Jcenter badge]][Jcenter]
[![Download][lingua version badge]][lingua download url]
---
[![ci build status][travis ci badge]][travis ci url]
[![codecov][codecov badge]][codecov url]
---
[![Kotlin version][Kotlin version badge]][Kotlin url]
[![Kotlin platforms badge][Kotlin platforms badge]][Kotlin platforms url]
[![license badge][license badge]][license url]
---

## <a name="table-of-contents"></a> Table of Contents

1. [What does this library do?](#library-purpose)
2. [Why does this library exist?](#library-reason)
3. [Which languages are supported?](#supported-languages)
4. [How good is it?](#library-accuracy)  
  4.1 [Comparison of Libraries](#library-comparison)  
    &nbsp;&nbsp;&nbsp;&nbsp;4.1.1 [Tabular Comparison](#library-comparison-tabular)  
    &nbsp;&nbsp;&nbsp;&nbsp;4.1.2 [Graphical Comparison](#library-comparison-graphical)  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.1.2.1 [Single Words](#library-comparison-graphical-singlewords)  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.1.2.2 [Word Pairs](#library-comparison-graphical-wordpairs)  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.1.2.3 [Sentences](#library-comparison-graphical-sentences)  
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4.1.2.4 [Average](#library-comparison-graphical-average)  
  4.2 [Test Report Generation](#report-generation)
5. [How to add it to your project?](#library-dependency)  
  5.1 [Using Gradle](#library-dependency-gradle)  
  5.2 [Using Maven](#library-dependency-maven)
6. [How to build?](#library-build)
7. [How to use?](#library-use)  
  7.1 [Programmatic use](#library-use-programmatic)  
  7.2 [Standalone mode](#library-use-standalone)
8. [Do you want to contribute?](#library-contribution)
9. [What's next for upcoming versions?](#whats-next)

## 1. <a name="library-purpose"></a> What does this library do? <sup>[Top ▲](#table-of-contents)</sup>
Its task is simple: It tells you which language some provided textual data is written in. This is very useful as a preprocessing step for linguistic data in natural language processing applications such as text classification and spell checking. Other use cases, for instance, might include routing e-mails to the right geographically located customer service department, based on the e-mails' languages.

## 2. <a name="library-reason"></a> Why does this library exist? <sup>[Top ▲](#table-of-contents)</sup>
Language detection is often done as part of large machine learning frameworks or natural language processing applications. In cases where you don't need the full-fledged functionality of those systems or don't want to learn the ropes of those, a small flexible library comes in handy. 

So far, two other comprehensive open source libraries working on the JVM for this task are [Apache Tika] and [Optimaize Language Detector]. Unfortunately, especially the latter has three major drawbacks:
 
1. Detection only works with quite lengthy text fragments. For very short text snippets such as Twitter messages, it doesn't provide adequate results.
2. The more languages take part in the decision process, the less accurate are the detection results.
3. Configuration of the library is quite cumbersome and requires some knowledge about the statistical methods that are used internally.

*Lingua* aims at eliminating these problems. It nearly doesn't need any configuration and yields pretty accurate results on both long and short text, even on single words and phrases. It draws on both rule-based and statistical methods but does not use any dictionaries of words. It does not need a connection to any external API or service either. Once the library has been downloaded, it can be used completely offline. 

## 3. <a name="supported-languages"></a> Which languages are supported? <sup>[Top ▲](#table-of-contents)</sup>

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

## 4. <a name="library-accuracy"></a> How good is it? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* is able to report accuracy statistics for some bundled test data available for each supported language. The test data for each language is split into three parts:
1. a list of single words with a minimum length of 5 characters
2. a list of word pairs with a minimum length of 10 characters
3. a list of complete grammatical sentences of various lengths

Both the language models and the test data have been created from separate documents of the [Wortschatz corpora] offered by Leipzig University, Germany. Data crawled from various news websites have been used for training, each corpus comprising one million sentences. For testing, corpora made of arbitrarily chosen websites have been used, each comprising ten thousand sentences. From each test corpus, a random unsorted subset of 1000 single words, 1000 word pairs and 1000 sentences has been extracted, respectively.

### 4.1 <a name="library-comparison"></a> Comparison of Libraries <sup>[Top ▲](#table-of-contents)</sup>

Given the generated test data, I have compared the detection results of *Lingua*, *Apache Tika* and *Optimaize Language Detector* using parameterized JUnit tests running over the data of 24 languages. *Tika* actually uses a heavily optimized version of *Optimaize* internally. Latin is currently only supported by *Lingua*, so it's left out both in the decision process and in the comparison. All other 24 are indeed part of the decision process, that is, each classifier might theoretically return one of these 24 languages as the result.

#### 4.1.1 <a name="library-comparison-tabular"></a> Tabular Comparison <sup>[Top ▲](#table-of-contents)</sup>

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

#### 4.1.2 <a name="library-comparison-graphical"></a> Graphical Comparison <sup>[Top ▲](#table-of-contents)</sup>

The following plots even better reflect how *Lingua* performs next to its contenders.

##### 4.1.2.1 <a name="library-comparison-graphical-singlewords"></a> Single Words <sup>[Top ▲](#table-of-contents)</sup>

The line plot shows that *Lingua's* detection accuracy for single words is superior except for Latvian. Looking at the report for this language, more than 12% of the words are erroneously classified as Swedish or Turkish. This is probably due to some very specific characters that occur much more often in the other two languages.

As already stated, some languages generally seem harder to identify than others, especially the Romance languages French, Italian, Portuguese and Spanish which are very similar to each other. Also, German and English are difficult to keep apart.

![lineplot-singlewords](/images/plots/lineplot-singlewords.png)

The box plot very nicely shows the differences in the deviation of the results, proving that *Lingua* is generally more reliable, independent of the language. 

![boxplot-singlewords](/images/plots/boxplot-singlewords.png)

##### 4.1.2.2 <a name="library-comparison-graphical-wordpairs"></a> Word Pairs <sup>[Top ▲](#table-of-contents)</sup>

Comparing the plots for word pairs, they illustrate the same aspects as mentioned for single words above. The detection results for Lithuanian are worse than with *Apache Tika*, showing that Baltic languages obviously need some more care.

![lineplot-wordpairs](/images/plots/lineplot-wordpairs.png)

![boxplot-wordpairs](/images/plots/boxplot-wordpairs.png)

##### 4.1.2.3 <a name="library-comparison-graphical-sentences"></a> Sentences <sup>[Top ▲](#table-of-contents)</sup>

As already said, Baltic languages seem to be a problem. This will be fixed in the next minor version update.

![lineplot-sentences](/images/plots/lineplot-sentences.png)

![boxplot-sentences](/images/plots/boxplot-sentences.png)

##### 4.1.2.4 <a name="library-comparison-graphical-average"></a> Average <sup>[Top ▲](#table-of-contents)</sup>

![lineplot-average](/images/plots/lineplot-average.png)

![boxplot-average](/images/plots/boxplot-average.png)

### 4.2 <a name="report-generation"></a> Test Report Generation <sup>[Top ▲](#table-of-contents)</sup>

If you want to reproduce the accuracy results above, you can generate the test reports yourself:

- `mvn test -P accuracy-reports -D detector=lingua` generates reports for *Lingua*
- `mvn test -P accuracy-reports -D detector=tika` generates reports for *Apache Tika*
- `mvn test -P accuracy-reports -D detector=optimaize` generates reports for *Optimaize*

If you just want to create a report for a specific language, you can do so as well:

- `mvn test -P accuracy-reports -D detector=lingua -D language=German`

For each detector and language, a test report file is then written into [`/accuracy-reports`](https://github.com/pemistahl/lingua/tree/master/accuracy-reports), to be found next to the `src` directory. 
As an example, here is the current output of the *Lingua* German report:

```
com.github.pemistahl.lingua.report.lingua.GermanDetectionAccuracyReport

##### GERMAN #####

>>> Accuracy on average: 91,10%

>> Detection of 1000 single words (average length: 9 chars)
Accuracy: 78,50%
Erroneously classified as DANISH: 3,50%, ENGLISH: 3,30%, DUTCH: 2,80%, SWEDISH: 2,10%, ITALIAN: 1,90%, FRENCH: 1,70%, ESTONIAN: 0,90%, LITHUANIAN: 0,80%, FINNISH: 0,80%, PORTUGUESE: 0,80%, CROATIAN: 0,80%, POLISH: 0,60%, SPANISH: 0,50%, ROMANIAN: 0,30%, TURKISH: 0,30%, LATVIAN: 0,20%, CZECH: 0,20%

>> Detection of 1000 word pairs (average length: 18 chars)
Accuracy: 95,30%
Erroneously classified as ENGLISH: 0,90%, DANISH: 0,80%, DUTCH: 0,80%, SWEDISH: 0,60%, FRENCH: 0,40%, ESTONIAN: 0,30%, CZECH: 0,20%, TURKISH: 0,10%, LATVIAN: 0,10%, PORTUGUESE: 0,10%, FINNISH: 0,10%, HUNGARIAN: 0,10%, ROMANIAN: 0,10%, SPANISH: 0,10%

>> Detection of 1000 sentences (average length: 111 chars)
Accuracy: 99,50%
Erroneously classified as SWEDISH: 0,10%, POLISH: 0,10%, FINNISH: 0,10%, DUTCH: 0,10%, ENGLISH: 0,10%
```

The plots have been created with Python and the libraries Pandas, Matplotlib and Seaborn. The code is contained in an IPython notebook and can be found under [`/accuracy-reports/accuracy-reports-analysis-notebook.ipynb`](https://github.com/pemistahl/lingua/blob/master/accuracy-reports/accuracy-reports-analysis-notebook.ipynb).

## 5. <a name="library-dependency"></a> How to add it to your project? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* is hosted on [Jcenter] and [Maven Central].

### 5.1 <a name="library-dependency-gradle"></a> Using Gradle

```
implementation 'com.github.pemistahl:lingua:0.3.0'
```

### 5.2 <a name="library-dependency-maven"></a> Using Maven

```
<dependency>
    <groupId>com.github.pemistahl</groupId>
    <artifactId>lingua</artifactId>
    <version>0.3.0</version>
</dependency>
```

## 6. <a name="library-build"></a> How to build? <sup>[Top ▲](#table-of-contents)</sup>

*Lingua* uses Maven to build. A switch to Gradle is planned for the future.

```
git clone https://github.com/pemistahl/lingua.git
cd lingua
mvn install
```
Maven's `package` phase is able to generate two jar files in the `target` directory:
1. `mvn package` creates `lingua-0.3.0.jar` that contains the compiled sources only.
2. `mvn package -P with-dependencies` creates `lingua-0.3.0-with-dependencies.jar` that additionally contains all dependencies needed to use the library. This jar file can be included in projects without dependency management systems. You should be able to use it in your Android project as well by putting it in your project's `lib` folder. This jar file can also be used to run *Lingua* in standalone mode (see below).

## 7. <a name="library-use"></a> How to use? <sup>[Top ▲](#table-of-contents)</sup>
*Lingua* can be used programmatically in your own code or in standalone mode.

### 7.1 <a name="library-use-programmatic"></a> Programmatic use <sup>[Top ▲](#table-of-contents)</sup>
The API is pretty straightforward and can be used in both Kotlin and Java code.

```kotlin
/* Kotlin */

import com.github.pemistahl.lingua.api.LanguageDetectorBuilder
import com.github.pemistahl.lingua.api.LanguageDetector
import com.github.pemistahl.lingua.api.Language

println(LanguageDetectorBuilder.supportedLanguages())
// [ARABIC, BELARUSIAN, BULGARIAN, ...]

val detector: LanguageDetector = LanguageDetectorBuilder.fromAllBuiltInLanguages().build()
val detectedLanguage: Language = detector.detectLanguageOf(text = "languages are awesome")
// ENGLISH

val languages: List<Language> = detector.detectLanguagesOf(texts = listOf("languages", "are", "awesome"))
// [ENGLISH, ENGLISH, ENGLISH]
```

If a string's language cannot be detected reliably because of missing linguistic information, `Language.UNKNOWN` is returned. The public API of *Lingua* never returns `null` somewhere, so it is safe to be used from within Java code as well.

```java
/* Java */

import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.Language;
import static java.util.Arrays.asList;

final LanguageDetector detector = LanguageDetectorBuilder.fromAllBuiltInLanguages().build();
final Language detectedLanguage = detector.detectLanguageOf("languages are awesome");
final List<Language> languages = detector.detectLanguagesOf(asList("languages", "are", "awesome"));
```

There might be classification tasks where you know beforehand that your language data is definitely not written in Latin, for instance (what a surprise :-). The detection accuracy can become better in such cases if you exclude certain languages from the decision process or just explicitly include relevant languages:

```kotlin
// include only languages that are not yet extinct (= currently excludes Latin)
LanguageDetectorBuilder.fromAllBuiltInSpokenLanguages()

// exclude the Spanish language from the decision algorithm
LanguageDetectorBuilder.fromAllBuiltInLanguagesWithout(Language.SPANISH)

// only decide between English and German
LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.GERMAN)

// select languages by ISO 639-1 code
LanguageDetectorBuilder.fromIsoCodes(listOf("en", "de"))
```

If you build your detector from all built-in language models, this can consume quite a bit of time and memory, depending on your machine. In order to speed up the loading process and save memory, *Lingua* offers an optional [MapDB](https://github.com/jankotek/mapdb) cache that converts the language models to highly efficient [`SortedTableMap`](https://jankotek.gitbooks.io/mapdb/content/sortedtablemap/) instances upon first access. These MapDB files are then stored in your operating system account's user home directory under `[your home directory]/lingua-mapdb-files`. Every subsequent access to the language models will then read them from MapDB. This saves 33% of memory and 66% of loading times, approximately. You can activate the MapDB cache like so:

```kotlin
val detector = LanguageDetectorBuilder
    .fromAllBuiltInLanguages()
    .withMapDBCache() // this builds the cache
    .build()
```

### 7.2 <a name="library-use-standalone"></a> Standalone mode <sup>[Top ▲](#table-of-contents)</sup>
If you want to try out *Lingua* before you decide whether to use it or not, you can run it in a REPL and immediately see its detection results.
1. With Maven: `mvn exec:java`
2. Without Maven: `java -jar lingua-0.3.0-with-dependencies.jar`

Then just play around:

```
This is Lingua.
Select the language models to load.

1: all 25 supported languages
2: French, Italian, Spanish, Portuguese
3: Dutch, English, German
4: Belarusian, Bulgarian, Russian
5: Danish, Finnish, Swedish
6: Estonian, Latvian, Lithuanian
7: Czech, Polish
8: Croatian, Hungarian, Romanian
9: Arabic, Persian

Type a number (default: 1) and press <Enter>.
Type :quit to exit.

> 3
Loading language models...
Done. 3 language models loaded in 2 seconds.

Type some text and press <Enter> to detect its language.
Type :quit to exit.

> Sprachen sind schon toll.
GERMAN
> languages are great
ENGLISH
> :quit
Bye! Ciao! Tschüss! Salut!
```

## 8. <a name="library-contribution"></a> Do you want to contribute? <sup>[Top ▲](#table-of-contents)</sup>

In case you want to contribute something to *Lingua* even though it's in a very early stage of development, then I encourage you to do so nevertheless. Do you have ideas for improving the API? Are there some specific languages that you want to have supported early? Or have you found any bugs so far? Feel free to open an issue or send a pull request. It's very much appreciated. :-)

## 9. <a name="whats-next"></a> What's next for upcoming versions? <sup>[Top ▲](#table-of-contents)</sup>
- languages, languages, even more languages :-)
- accuracy improvements
- more unit tests
- API documentation
- for version 1.0.0: public API stability, Gradle build, multiplatform support

[change log]: https://github.com/pemistahl/lingua/releases
[change log badge]: https://img.shields.io/badge/change%20log-what's%20new%3F-yellow.svg
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
[supported languages badge]: https://img.shields.io/badge/supported%20languages-25-orange.svg
[awesome nlp badge]: https://raw.githubusercontent.com/sindresorhus/awesome/master/media/mentioned-badge-flat.svg?sanitize=true
[awesome nlp url]: https://github.com/keon/awesome-nlp#user-content-kotlin
[lingua version badge]: https://api.bintray.com/packages/pemistahl/nlp-libraries/lingua/images/download.svg
[lingua download url]: https://bintray.com/pemistahl/nlp-libraries/download_file?file_path=com%2Fgithub%2Fpemistahl%2Flingua%2F0.3.0%2Flingua-0.3.0-with-dependencies.jar
[Kotlin version badge]: https://img.shields.io/badge/Kotlin-1.3-blue.svg?logo=kotlin
[Kotlin url]: https://kotlinlang.org/docs/reference/whatsnew13.html
[Kotlin platforms badge]: https://img.shields.io/badge/platforms-JDK%206%2B%20%7C%20Android-blue.svg
[Kotlin platforms url]: https://kotlinlang.org/docs/reference/server-overview.html
[license badge]: https://img.shields.io/badge/license-Apache%202.0-blue.svg
[license url]: https://www.apache.org/licenses/LICENSE-2.0
[Wortschatz corpora]: http://wortschatz.uni-leipzig.de
[Apache Tika]: https://tika.apache.org/1.20/detection.html#Language_Detection
[Optimaize Language Detector]: https://github.com/optimaize/language-detector
[Jcenter]: https://bintray.com/pemistahl/nlp-libraries/lingua/0.3.0
[Jcenter badge]: https://img.shields.io/badge/JCenter-0.3.0-green.svg
[Maven Central]: https://search.maven.org/artifact/com.github.pemistahl/lingua/0.3.0/jar
[Maven Central badge]: https://img.shields.io/badge/Maven%20Central-0.3.0-green.svg
[green-marker]: https://placehold.it/10/008000/000000?text=+
[yellow-marker]: https://placehold.it/10/ffff00/000000?text=+
[orange-marker]: https://placehold.it/10/ff8c00/000000?text=+
[red-marker]: https://placehold.it/10/ff0000/000000?text=+
[translate-lingua]: https://translate.google.com/?hl=en#view=home&op=translate&sl=en&tl=la&text=tongue

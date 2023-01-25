![lingua](images/logo.png) 

<br>

[![ci build status][ci badge]][ci url]
[![codecov][codecov badge]][codecov url]
[![supported languages][supported languages badge]](#supported-languages)
[![Kotlin platforms badge][Kotlin platforms badge]][Kotlin platforms url]
[![license badge][license badge]][license url]

[![javadoc][javadoc badge]][javadoc url]
[![Maven Central][Maven Central badge]][Maven Central]
[![Download][lingua version badge]][lingua download url]

---
### Quick Info
* this library tries to solve language detection of very short words and phrases, even shorter than tweets
* makes use of both statistical and rule-based approaches
* outperforms *Apache Tika*, *Apache OpenNLP* and *Optimaize Language Detector* for more than 70 languages
* works within every Java 8+ application
* no additional training of language models necessary
* api for adding your own language models
* offline usage without having to connect to an external service or API
* can be used in a REPL for a quick try-out
---

## 1. What does this library do?
Its task is simple: It tells you which language some provided textual data is written in. 
This is very useful as a preprocessing step for linguistic data in natural language 
processing applications such as text classification and spell checking. 
Other use cases, for instance, might include routing e-mails to the right geographically 
located customer service department, based on the e-mails' languages.

## 2. Why does this library exist?
Language detection is often done as part of large machine learning frameworks or natural 
language processing applications. In cases where you don't need the full-fledged 
functionality of those systems or don't want to learn the ropes of those, 
a small flexible library comes in handy. 

So far, three other comprehensive open source libraries working on the JVM for this task 
are [Apache Tika], [Apache OpenNLP] and [Optimaize Language Detector]. 
Unfortunately, especially the latter has three major drawbacks:
 
1. Detection only works with quite lengthy text fragments. 
For very short text snippets such as Twitter messages, it doesn't provide adequate results.
2. The more languages take part in the decision process, the less accurate are the detection results.
3. Configuration of the library is quite cumbersome and requires some knowledge about the statistical 
methods that are used internally.

*Lingua* aims at eliminating these problems. It nearly doesn't need any configuration and 
yields pretty accurate results on both long and short text, even on single words and phrases. 
It draws on both rule-based and statistical methods but does not use any dictionaries of words. 
It does not need a connection to any external API or service either. 
Once the library has been downloaded, it can be used completely offline. 

## 3. Which languages are supported?

Compared to other language detection libraries, *Lingua's* focus is on *quality over quantity*, that is, 
getting detection right for a small set of languages first before adding new ones. 
Currently, the following 75 languages are supported:

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
  - Norwegian Bokmal
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
  - Ganda
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
  - Maori
  - Marathi
  - Mongolian
- N
  - Norwegian Nynorsk
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
  - Shona
  - Slovak
  - Slovene
  - Somali
  - Sotho
  - Spanish
  - Swahili
  - Swedish
- T
  - Tagalog
  - Tamil
  - Telugu
  - Thai
  - Tsonga
  - Tswana
  - Turkish
- U
  - Ukrainian
  - Urdu
- V
  - Vietnamese
- W
  - Welsh
- X
  - Xhosa
- Y
  - Yoruba
- Z
  - Zulu   

## 4. How good is it?

*Lingua* is able to report accuracy statistics for some bundled test data available for each supported language. 
The test data for each language is split into three parts:
1. a list of single words with a minimum length of 5 characters
2. a list of word pairs with a minimum length of 10 characters
3. a list of complete grammatical sentences of various lengths

Both the language models and the test data have been created from separate documents of the [Wortschatz corpora] 
offered by Leipzig University, Germany. Data crawled from various news websites have been used for training, 
each corpus comprising one million sentences. For testing, corpora made of arbitrarily chosen websites have been used, 
each comprising ten thousand sentences. From each test corpus, a random unsorted subset of 1000 single words, 
1000 word pairs and 1000 sentences has been extracted, respectively.

Given the generated test data, I have compared the detection results of *Lingua*, *Apache Tika*, *Apache OpenNLP* and 
*Optimaize Language Detector* using parameterized JUnit tests running over the data of *Lingua's* supported 75 languages. 
Languages that are not supported by the other libraries are simply ignored for those during the detection process.

Each of the following sections contains two plots. The bar plot shows the detailed accuracy results for each supported 
language. The box plot illustrates the distributions of the accuracy values for each classifier. The boxes themselves 
represent the areas which the middle 50 % of data lie within. Within the colored boxes, the horizontal lines mark the 
median of the distributions.

### 4.1 Single word detection

<br/>

<img src="https://raw.githubusercontent.com/pemistahl/lingua/main/images/plots/boxplot-single-words.png" alt="Single Word Detection Performance" />

<br/>

<details>
    <summary>Bar plot</summary>
    <img src="https://raw.githubusercontent.com/pemistahl/lingua/main/images/plots/barplot-single-words.png" alt="Single Word Detection Performance" />
</details>

<br/><br/>

### 4.2 Word pair detection

<br/>

<img src="https://raw.githubusercontent.com/pemistahl/lingua/main/images/plots/boxplot-word-pairs.png" alt="Word Pair Detection Performance" />

<br/>

<details>
    <summary>Bar plot</summary>
    <img src="https://raw.githubusercontent.com/pemistahl/lingua/main/images/plots/barplot-word-pairs.png" alt="Word Pair Detection Performance" />
</details>

<br/><br/>

### 4.3 Sentence detection

<br/>

<img src="https://raw.githubusercontent.com/pemistahl/lingua/main/images/plots/boxplot-sentences.png" alt="Sentence Detection Performance" />

<br/>

<details>
    <summary>Bar plot</summary>
    <img src="https://raw.githubusercontent.com/pemistahl/lingua/main/images/plots/barplot-sentences.png" alt="Sentence Detection Performance" />
</details>

<br/><br/>

### 4.4 Average detection

<br/>

<img src="https://raw.githubusercontent.com/pemistahl/lingua/main/images/plots/boxplot-average.png" alt="Average Detection Performance" />

<br/>

<details>
    <summary>Bar plot</summary>
    <img src="https://raw.githubusercontent.com/pemistahl/lingua/main/images/plots/barplot-average.png" alt="Average Detection Performance" />
</details>

<br/><br/>

### 4.5 Mean, median and standard deviation

The table below shows detailed statistics for each language and classifier
including mean, median and standard deviation.

<details>
    <summary>Open table</summary>
    <table>
    <tr>
        <th>Language</th>
        <th colspan="5">Average</th>
        <th colspan="5">Single Words</th>
        <th colspan="5">Word Pairs</th>
        <th colspan="5">Sentences</th>
    </tr>
    <tr>
        <th></th>
        <th>Lingua<br>(high accuracy mode)</th>
        <th>Lingua<br>(low accuracy mode)</th>
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>OpenNLP</th>
        <th>Optimaize</th>
        <th>Lingua<br>(high accuracy mode)</th>
        <th>Lingua<br>(low accuracy mode)</th>
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>OpenNLP</th>
        <th>Optimaize</th>
        <th>Lingua<br>(high accuracy mode)</th>
        <th>Lingua<br>(low accuracy mode)</th>
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>OpenNLP</th>
        <th>Optimaize</th>
        <th>Lingua<br>(high accuracy mode)</th>
        <th>Lingua<br>(low accuracy mode)</th>
        <th>&nbsp;&nbsp;Tika&nbsp;&nbsp;</th>
        <th>OpenNLP</th>
        <th>Optimaize</th>
    </tr>
    	<tr>
		<td>Afrikaans</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/orange.png"> 39</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/orange.png"> 37</td>
		<td><img src="images/yellow.png"> 44</td>
		<td><img src="images/yellow.png"> 41</td>
		<td><img src="images/red.png"> 3</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/orange.png"> 22</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 93</td>
	</tr>
	<tr>
		<td>Albanian</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/orange.png"> 40</td>
		<td><img src="images/orange.png"> 38</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
	</tr>
	<tr>
		<td>Amharic</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Arabic</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Armenian</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Azerbaijani</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Basque</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/yellow.png"> 55</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/yellow.png"> 56</td>
		<td><img src="images/orange.png"> 33</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 95</td>
	</tr>
	<tr>
		<td>Belarusian</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
	</tr>
	<tr>
		<td>Bengali</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Bokmal</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/yellow.png"> 49</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 38</td>
		<td><img src="images/orange.png"> 27</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 42</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 59</td>
		<td><img src="images/yellow.png"> 47</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Bosnian</td>
		<td><img src="images/orange.png"> 35</td>
		<td><img src="images/orange.png"> 29</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 26</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 29</td>
		<td><img src="images/orange.png"> 23</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/red.png"> 12</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 35</td>
		<td><img src="images/orange.png"> 29</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 22</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 40</td>
		<td><img src="images/orange.png"> 36</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 44</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Bulgarian</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/yellow.png"> 48</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/yellow.png"> 56</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/red.png"> 18</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/orange.png"> 36</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 91</td>
	</tr>
	<tr>
		<td>Catalan</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/yellow.png"> 42</td>
		<td><img src="images/orange.png"> 31</td>
		<td><img src="images/yellow.png"> 51</td>
		<td><img src="images/orange.png"> 33</td>
		<td><img src="images/orange.png"> 32</td>
		<td><img src="images/red.png"> 11</td>
		<td><img src="images/red.png"> 2</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/yellow.png"> 57</td>
		<td><img src="images/orange.png"> 32</td>
		<td><img src="images/red.png"> 16</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 77</td>
	</tr>
	<tr>
		<td>Chinese</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/orange.png"> 31</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/red.png"> 20</td>
		<td><img src="images/orange.png"> 40</td>
		<td><img src="images/red.png"> 0</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/red.png"> 2</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 91</td>
	</tr>
	<tr>
		<td>Croatian</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/yellow.png"> 41</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/orange.png"> 36</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/orange.png"> 23</td>
		<td><img src="images/red.png"> 8</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/yellow.png"> 57</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/yellow.png"> 44</td>
		<td><img src="images/orange.png"> 24</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/green.png"> 91</td>
	</tr>
	<tr>
		<td>Czech</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/yellow.png"> 49</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/yellow.png"> 42</td>
		<td><img src="images/orange.png"> 21</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/yellow.png"> 46</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 81</td>
	</tr>
	<tr>
		<td>Danish</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/yellow.png"> 55</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/yellow.png"> 45</td>
		<td><img src="images/lightgreen.png"> 63</td>
		<td><img src="images/orange.png"> 34</td>
		<td><img src="images/red.png"> 19</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/yellow.png"> 51</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 96</td>
	</tr>
	<tr>
		<td>Dutch</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 63</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/orange.png"> 39</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/orange.png"> 34</td>
		<td><img src="images/orange.png"> 31</td>
		<td><img src="images/orange.png"> 31</td>
		<td><img src="images/red.png"> 6</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/yellow.png"> 57</td>
		<td><img src="images/red.png"> 19</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 91</td>
	</tr>
	<tr>
		<td>English</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/yellow.png"> 41</td>
		<td><img src="images/yellow.png"> 55</td>
		<td><img src="images/orange.png"> 29</td>
		<td><img src="images/orange.png"> 30</td>
		<td><img src="images/red.png"> 10</td>
		<td><img src="images/red.png"> 2</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/yellow.png"> 46</td>
		<td><img src="images/orange.png"> 23</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 97</td>
	</tr>
	<tr>
		<td>Esperanto</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Estonian</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/yellow.png"> 59</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/orange.png"> 29</td>
		<td><img src="images/orange.png"> 23</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/lightgreen.png"> 63</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 98</td>
	</tr>
	<tr>
		<td>Finnish</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/yellow.png"> 51</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>French</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/yellow.png"> 59</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/yellow.png"> 51</td>
		<td><img src="images/yellow.png"> 55</td>
		<td><img src="images/orange.png"> 25</td>
		<td><img src="images/red.png"> 18</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/yellow.png"> 55</td>
		<td><img src="images/yellow.png"> 48</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 97</td>
	</tr>
	<tr>
		<td>Ganda</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 63</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Georgian</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>German</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/yellow.png"> 55</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/orange.png"> 38</td>
		<td><img src="images/orange.png"> 21</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/yellow.png"> 46</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 99</td>
	</tr>
	<tr>
		<td>Greek</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Gujarati</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Hebrew</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Hindi</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/orange.png"> 33</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/yellow.png"> 51</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/red.png"> 11</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/orange.png"> 28</td>
		<td><img src="images/red.png"> 16</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/red.png"> 20</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/yellow.png"> 49</td>
		<td><img src="images/orange.png"> 38</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
	</tr>
	<tr>
		<td>Hungarian</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/yellow.png"> 51</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
	</tr>
	<tr>
		<td>Icelandic</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
	</tr>
	<tr>
		<td>Indonesian</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/yellow.png"> 47</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/orange.png"> 29</td>
		<td><img src="images/red.png"> 18</td>
		<td><img src="images/orange.png"> 39</td>
		<td><img src="images/orange.png"> 25</td>
		<td><img src="images/orange.png"> 37</td>
		<td><img src="images/red.png"> 10</td>
		<td><img src="images/red.png"> 0</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/yellow.png"> 45</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/orange.png"> 25</td>
		<td><img src="images/red.png"> 1</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/yellow.png"> 54</td>
	</tr>
	<tr>
		<td>Irish</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/yellow.png"> 56</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 98</td>
	</tr>
	<tr>
		<td>Italian</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/yellow.png"> 51</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/yellow.png"> 42</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/orange.png"> 31</td>
		<td><img src="images/red.png"> 12</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
	</tr>
	<tr>
		<td>Japanese</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/orange.png"> 25</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/red.png"> 1</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/red.png"> 5</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 96</td>
	</tr>
	<tr>
		<td>Kazakh</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Korean</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Latin</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/yellow.png"> 48</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Latvian</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/yellow.png"> 56</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 97</td>
	</tr>
	<tr>
		<td>Lithuanian</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/yellow.png"> 56</td>
		<td><img src="images/orange.png"> 40</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
	</tr>
	<tr>
		<td>Macedonian</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/yellow.png"> 46</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/orange.png"> 37</td>
		<td><img src="images/red.png"> 10</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/orange.png"> 32</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 97</td>
	</tr>
	<tr>
		<td>Malay</td>
		<td><img src="images/orange.png"> 31</td>
		<td><img src="images/orange.png"> 31</td>
		<td><img src="images/orange.png"> 23</td>
		<td><img src="images/red.png"> 19</td>
		<td><img src="images/red.png"> 4</td>
		<td><img src="images/orange.png"> 26</td>
		<td><img src="images/orange.png"> 22</td>
		<td><img src="images/red.png"> 19</td>
		<td><img src="images/red.png"> 10</td>
		<td><img src="images/red.png"> 0</td>
		<td><img src="images/orange.png"> 38</td>
		<td><img src="images/orange.png"> 36</td>
		<td><img src="images/orange.png"> 22</td>
		<td><img src="images/red.png"> 20</td>
		<td><img src="images/red.png"> 0</td>
		<td><img src="images/orange.png"> 30</td>
		<td><img src="images/orange.png"> 36</td>
		<td><img src="images/orange.png"> 28</td>
		<td><img src="images/orange.png"> 27</td>
		<td><img src="images/red.png"> 11</td>
	</tr>
	<tr>
		<td>Maori</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Marathi</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/yellow.png"> 41</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/red.png"> 20</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/orange.png"> 30</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 96</td>
	</tr>
	<tr>
		<td>Mongolian</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Nynorsk</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 55</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 41</td>
		<td><img src="images/orange.png"> 25</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 24</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/yellow.png"> 49</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 47</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Oromo</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Persian</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/orange.png"> 29</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
	</tr>
	<tr>
		<td>Polish</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/yellow.png"> 57</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Portuguese</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/lightgreen.png"> 63</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/orange.png"> 40</td>
		<td><img src="images/yellow.png"> 59</td>
		<td><img src="images/yellow.png"> 42</td>
		<td><img src="images/orange.png"> 34</td>
		<td><img src="images/orange.png"> 22</td>
		<td><img src="images/red.png"> 7</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/red.png"> 19</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 94</td>
	</tr>
	<tr>
		<td>Punjabi</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Romanian</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/yellow.png"> 55</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/yellow.png"> 49</td>
		<td><img src="images/yellow.png"> 57</td>
		<td><img src="images/orange.png"> 34</td>
		<td><img src="images/orange.png"> 24</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 91</td>
	</tr>
	<tr>
		<td>Russian</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/yellow.png"> 59</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/red.png"> 20</td>
		<td><img src="images/orange.png"> 22</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 87</td>
	</tr>
	<tr>
		<td>Serbian</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/yellow.png"> 46</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/yellow.png"> 57</td>
		<td><img src="images/yellow.png"> 46</td>
		<td><img src="images/red.png"> 18</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/orange.png"> 39</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/lightgreen.png"> 80</td>
	</tr>
	<tr>
		<td>Shona</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/yellow.png"> 56</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Sinhala</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Slovak</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/yellow.png"> 47</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/yellow.png"> 49</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/orange.png"> 39</td>
		<td><img src="images/red.png"> 12</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/orange.png"> 38</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 92</td>
	</tr>
	<tr>
		<td>Slovene</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/orange.png"> 37</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/orange.png"> 39</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/red.png"> 3</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/red.png"> 18</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 90</td>
	</tr>
	<tr>
		<td>Somali</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/yellow.png"> 59</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/orange.png"> 35</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Sotho</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 90</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Spanish</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/yellow.png"> 56</td>
		<td><img src="images/yellow.png"> 59</td>
		<td><img src="images/yellow.png"> 42</td>
		<td><img src="images/orange.png"> 32</td>
		<td><img src="images/yellow.png"> 44</td>
		<td><img src="images/orange.png"> 26</td>
		<td><img src="images/orange.png"> 29</td>
		<td><img src="images/red.png"> 8</td>
		<td><img src="images/red.png"> 0</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/yellow.png"> 49</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/orange.png"> 25</td>
		<td><img src="images/red.png"> 6</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 91</td>
	</tr>
	<tr>
		<td>Swahili</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/yellow.png"> 45</td>
		<td><img src="images/orange.png"> 26</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/yellow.png"> 58</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
	</tr>
	<tr>
		<td>Swedish</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/yellow.png"> 46</td>
		<td><img src="images/yellow.png"> 44</td>
		<td><img src="images/yellow.png"> 41</td>
		<td><img src="images/red.png"> 15</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/yellow.png"> 42</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 94</td>
	</tr>
	<tr>
		<td>Swiss_German</td>
		<td><img src="images/yellow.png"> 48</td>
		<td><img src="images/orange.png"> 39</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 28</td>
		<td><img src="images/red.png"> 18</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/orange.png"> 34</td>
		<td><img src="images/orange.png"> 25</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Tagalog</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/yellow.png"> 52</td>
		<td><img src="images/orange.png"> 36</td>
		<td><img src="images/yellow.png"> 53</td>
		<td><img src="images/orange.png"> 27</td>
		<td><img src="images/orange.png"> 23</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/yellow.png"> 57</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 97</td>
	</tr>
	<tr>
		<td>Tamil</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Telugu</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Thai</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Tigrinya</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Tsonga</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/yellow.png"> 46</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 89</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Tswana</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/yellow.png"> 44</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/lightgreen.png"> 73</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Turkish</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/yellow.png"> 48</td>
		<td><img src="images/yellow.png"> 43</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 96</td>
	</tr>
	<tr>
		<td>Ukrainian</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 75</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/yellow.png"> 54</td>
		<td><img src="images/orange.png"> 39</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 69</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 94</td>
	</tr>
	<tr>
		<td>Urdu</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/yellow.png"> 45</td>
		<td><img src="images/yellow.png"> 49</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/lightgreen.png"> 71</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 96</td>
	</tr>
	<tr>
		<td>Vietnamese</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/green.png"> 84</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/lightgreen.png"> 79</td>
		<td><img src="images/lightgreen.png"> 76</td>
		<td><img src="images/lightgreen.png"> 63</td>
		<td><img src="images/lightgreen.png"> 66</td>
		<td><img src="images/lightgreen.png"> 65</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 92</td>
		<td><img src="images/green.png"> 86</td>
		<td><img src="images/green.png"> 95</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 100</td>
	</tr>
	<tr>
		<td>Welsh</td>
		<td><img src="images/green.png"> 91</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/yellow.png"> 60</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 87</td>
		<td><img src="images/green.png"> 88</td>
		<td><img src="images/green.png"> 81</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/green.png"> 99</td>
		<td><img src="images/green.png"> 99</td>
	</tr>
	<tr>
		<td>Xhosa</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/lightgreen.png"> 68</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 64</td>
		<td><img src="images/yellow.png"> 44</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 85</td>
		<td><img src="images/lightgreen.png"> 67</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 98</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Yoruba</td>
		<td><img src="images/lightgreen.png"> 74</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 50</td>
		<td><img src="images/orange.png"> 33</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 77</td>
		<td><img src="images/lightgreen.png"> 61</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 96</td>
		<td><img src="images/green.png"> 93</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td>Zulu</td>
		<td><img src="images/lightgreen.png"> 80</td>
		<td><img src="images/lightgreen.png"> 70</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 78</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/lightgreen.png"> 62</td>
		<td><img src="images/yellow.png"> 44</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/yellow.png"> 51</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 83</td>
		<td><img src="images/lightgreen.png"> 72</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 82</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 97</td>
		<td><img src="images/green.png"> 94</td>
		<td><img src="images/grey.png"> -</td>
		<td><img src="images/green.png"> 100</td>
		<td><img src="images/grey.png"> -</td>
	</tr>
	<tr>
		<td colspan="16"></td>
	</tr>
	<tr>
		<td><strong>Mean</strong></td>
		<td><img src="images/green.png"> <strong>86</strong></td>
		<td><img src="images/lightgreen.png"> <strong>78</strong></td>
		<td><img src="images/lightgreen.png"> <strong>80</strong></td>
		<td><img src="images/lightgreen.png"> <strong>74</strong></td>
		<td><img src="images/lightgreen.png"> <strong>65</strong></td>
		<td><img src="images/lightgreen.png"> <strong>74</strong></td>
		<td><img src="images/lightgreen.png"> <strong>61</strong></td>
		<td><img src="images/lightgreen.png"> <strong>64</strong></td>
		<td><img src="images/yellow.png"> <strong>53</strong></td>
		<td><img src="images/yellow.png"> <strong>41</strong></td>
		<td><img src="images/green.png"> <strong>89</strong></td>
		<td><img src="images/lightgreen.png"> <strong>79</strong></td>
		<td><img src="images/green.png"> <strong>81</strong></td>
		<td><img src="images/lightgreen.png"> <strong>74</strong></td>
		<td><img src="images/lightgreen.png"> <strong>61</strong></td>
		<td><img src="images/green.png"> <strong>96</strong></td>
		<td><img src="images/green.png"> <strong>93</strong></td>
		<td><img src="images/green.png"> <strong>96</strong></td>
		<td><img src="images/green.png"> <strong>95</strong></td>
		<td><img src="images/green.png"> <strong>93</strong></td>
	</tr>
	<tr>
		<td colspan="16"></td>
	</tr>
	<tr>
		<td>Median</td>
		<td>89.6</td>
		<td>79.63</td>
		<td>81.3</td>
		<td>75.55</td>
		<td>63.85</td>
		<td>76.15</td>
		<td>59.25</td>
		<td>63.39</td>
		<td>48.7</td>
		<td>30.75</td>
		<td>93.85</td>
		<td>81.5</td>
		<td>84.25</td>
		<td>75.55</td>
		<td>65.95</td>
		<td>98.75</td>
		<td>96.65</td>
		<td>99.15</td>
		<td>99.0</td>
		<td>97.4</td>
	</tr>
	<tr>
		<td>Standard Deviation</td>
		<td>13.67</td>
		<td>17.73</td>
		<td>16.2</td>
		<td>18.56</td>
		<td>23.87</td>
		<td>19.33</td>
		<td>25.63</td>
		<td>23.9</td>
		<td>27.37</td>
		<td>33.87</td>
		<td>14.33</td>
		<td>19.84</td>
		<td>18.74</td>
		<td>21.32</td>
		<td>31.32</td>
		<td>10.8</td>
		<td>11.67</td>
		<td>10.77</td>
		<td>12.59</td>
		<td>13.54</td>
	</tr>
</table>
</details>

## 5. Why is it better than other libraries?

Every language detector uses a probabilistic [n-gram](https://en.wikipedia.org/wiki/N-gram) model trained on the 
character distribution in some training corpus. Most libraries only use n-grams of size 3 (trigrams) which is 
satisfactory for detecting the language of longer text fragments consisting of multiple sentences. For short 
phrases or single words, however, trigrams are not enough. The shorter the input text is, the less n-grams are 
available. The probabilities estimated from such few n-grams are not reliable. This is why *Lingua* makes use 
of n-grams of sizes 1 up to 5 which results in much more accurate prediction of the correct language.  

A second important difference is that *Lingua* does not only use such a statistical model, but also a rule-based 
engine. This engine first determines the alphabet of the input text and searches for characters which are unique 
in one or more languages. If exactly one language can be reliably chosen this way, the statistical model is not 
necessary anymore. In any case, the rule-based engine filters out languages that do not satisfy the conditions 
of the input text. Only then, in a second step, the probabilistic n-gram model is taken into consideration. 
This makes sense because loading less language models means less memory consumption and better runtime performance.

In general, it is always a good idea to restrict the set of languages to be considered in the classification process 
using the respective [api methods](#library-use-programmatic). If you know beforehand that certain languages are 
never to occur in an input text, do not let those take part in the classifcation process. The filtering mechanism 
of the rule-based engine is quite good, however, filtering based on your own knowledge of the input text is always preferable.

## 6. Test report and plot generation

If you want to reproduce the accuracy results above, you can generate the test reports yourself for all four classifiers 
and all languages by doing:

    ./gradlew accuracyReport
    
You can also restrict the classifiers and languages to generate reports for by passing arguments to the Gradle task. 
The following task generates reports for *Lingua* and the languages English and German only:

    ./gradlew accuracyReport -Pdetectors=Lingua -Planguages=English,German

By default, only a single CPU core is used for report generation. If you have a multi-core CPU in your machine, 
you can fork as many processes as you have CPU cores. This speeds up report generation significantly. 
However, be aware that forking more than one process can consume a lot of RAM. You do it like this:

    ./gradlew accuracyReport -PcpuCores=2

For each detector and language, a test report file is then written into [`/accuracy-reports`][accuracy reports url], 
to be found next to the `src` directory. As an example, here is the current output of the *Lingua* German report:

```
##### GERMAN #####

Legend: 'low accuracy mode | high accuracy mode'

>>> Accuracy on average: 79.80% | 89.23%

>> Detection of 1000 single words (average length: 9 chars)
Accuracy: 56.70% | 73.90%
Erroneously classified as DUTCH: 2.80% | 2.30%, DANISH: 2.20% | 2.10%, ENGLISH: 1.90% | 2.00%, LATIN: 1.90% | 1.90%, BOKMAL: 2.40% | 1.60%, BASQUE: 1.60% | 1.20%, ITALIAN: 1.00% | 1.20%, FRENCH: 1.60% | 1.20%, ESPERANTO: 1.10% | 1.10%, SWEDISH: 3.20% | 1.00%, AFRIKAANS: 1.30% | 0.80%, TSONGA: 1.50% | 0.70%, NYNORSK: 1.40% | 0.60%, PORTUGUESE: 0.50% | 0.60%, YORUBA: 0.40% | 0.60%, SOTHO: 0.70% | 0.50%, FINNISH: 0.80% | 0.50%, WELSH: 1.30% | 0.50%, SPANISH: 1.20% | 0.40%, SWAHILI: 0.60% | 0.40%, TSWANA: 2.20% | 0.40%, POLISH: 0.70% | 0.40%, ESTONIAN: 0.90% | 0.40%, IRISH: 0.50% | 0.40%, TAGALOG: 0.10% | 0.30%, ICELANDIC: 0.30% | 0.30%, BOSNIAN: 0.10% | 0.30%, LITHUANIAN: 0.80% | 0.20%, MAORI: 0.50% | 0.20%, INDONESIAN: 0.40% | 0.20%, ALBANIAN: 0.60% | 0.20%, CATALAN: 0.70% | 0.20%, ZULU: 0.30% | 0.20%, ROMANIAN: 1.20% | 0.20%, CROATIAN: 0.10% | 0.20%, XHOSA: 0.40% | 0.20%, TURKISH: 0.70% | 0.10%, MALAY: 0.50% | 0.10%, LATVIAN: 0.40% | 0.10%, SLOVENE: 0.00% | 0.10%, SLOVAK: 0.30% | 0.10%, SOMALI: 0.00% | 0.10%, HUNGARIAN: 0.40% | 0.00%, SHONA: 0.80% | 0.00%, VIETNAMESE: 0.40% | 0.00%, CZECH: 0.30% | 0.00%, GANDA: 0.20% | 0.00%, AZERBAIJANI: 0.10% | 0.00%

>> Detection of 1000 word pairs (average length: 18 chars)
Accuracy: 83.50% | 94.10%
Erroneously classified as DUTCH: 1.50% | 0.90%, LATIN: 1.00% | 0.80%, ENGLISH: 1.40% | 0.70%, SWEDISH: 1.40% | 0.60%, DANISH: 1.20% | 0.50%, FRENCH: 0.60% | 0.40%, BOKMAL: 1.40% | 0.30%, TAGALOG: 0.10% | 0.20%, IRISH: 0.20% | 0.20%, TURKISH: 0.10% | 0.10%, NYNORSK: 0.90% | 0.10%, TSONGA: 0.40% | 0.10%, ZULU: 0.10% | 0.10%, ESPERANTO: 0.30% | 0.10%, AFRIKAANS: 0.60% | 0.10%, ITALIAN: 0.10% | 0.10%, ESTONIAN: 0.30% | 0.10%, FINNISH: 0.40% | 0.10%, SOMALI: 0.00% | 0.10%, SWAHILI: 0.20% | 0.10%, MAORI: 0.00% | 0.10%, WELSH: 0.10% | 0.10%, LITHUANIAN: 0.40% | 0.00%, INDONESIAN: 0.10% | 0.00%, CATALAN: 0.30% | 0.00%, LATVIAN: 0.20% | 0.00%, XHOSA: 0.30% | 0.00%, SPANISH: 0.50% | 0.00%, MALAY: 0.10% | 0.00%, SLOVAK: 0.10% | 0.00%, BASQUE: 0.40% | 0.00%, YORUBA: 0.20% | 0.00%, TSWANA: 0.30% | 0.00%, SHONA: 0.10% | 0.00%, PORTUGUESE: 0.10% | 0.00%, SOTHO: 0.30% | 0.00%, CZECH: 0.10% | 0.00%, ALBANIAN: 0.40% | 0.00%, AZERBAIJANI: 0.10% | 0.00%, ICELANDIC: 0.10% | 0.00%, SLOVENE: 0.10% | 0.00%

>> Detection of 1000 sentences (average length: 111 chars)
Accuracy: 99.20% | 99.70%
Erroneously classified as DUTCH: 0.00% | 0.20%, LATIN: 0.20% | 0.10%, NYNORSK: 0.10% | 0.00%, SPANISH: 0.10% | 0.00%, DANISH: 0.10% | 0.00%, SOTHO: 0.20% | 0.00%, ZULU: 0.10% | 0.00%
```

The plots have been created with Python and the libraries Pandas, Matplotlib and Seaborn. 
If you have a global Python 3 installation and the `python3` command available on your command line, 
you can redraw the plots after modifying the test reports by executing the following Gradle task:

    ./gradlew drawAccuracyPlots
    
The detailed statistics table that contains all accuracy values can be written with:

    ./gradlew writeAccuracyTable

## 7. How to add it to your project?

*Lingua* is hosted on [GitHub Packages] and [Maven Central].

### 7.1 Using Gradle

```
// Groovy syntax
implementation 'com.github.pemistahl:lingua:1.2.2'

// Kotlin syntax
implementation("com.github.pemistahl:lingua:1.2.2")
```

### 7.2 Using Maven

```
<dependency>
    <groupId>com.github.pemistahl</groupId>
    <artifactId>lingua</artifactId>
    <version>1.2.2</version>
</dependency>
```

## 8. How to build?

*Lingua* uses Gradle to build and requires Java >= 1.8 for that.

```
git clone https://github.com/pemistahl/lingua.git
cd lingua
./gradlew build
```
Several jar archives can be created from the project.
1. `./gradlew jar` assembles `lingua-1.2.2.jar` containing the compiled sources only.
2. `./gradlew sourcesJar` assembles `lingua-1.2.2-sources.jar` containing the plain source code.
3. `./gradlew jarWithDependencies` assembles `lingua-1.2.2-with-dependencies.jar` containing the 
compiled sources and all external dependencies needed at runtime. This jar file can be included 
in projects without dependency management systems. It can also be used to 
run *Lingua* in standalone mode (see below).

## 9. How to use?
*Lingua* can be used programmatically in your own code or in standalone mode.

### 9.1 Programmatic use
The API is pretty straightforward and can be used in both Kotlin and Java code.

#### 9.1.1 Basic usage

```kotlin
/* Kotlin */

import com.github.pemistahl.lingua.api.*
import com.github.pemistahl.lingua.api.Language.*

val detector: LanguageDetector = LanguageDetectorBuilder.fromLanguages(ENGLISH, FRENCH, GERMAN, SPANISH).build()
val detectedLanguage: Language = detector.detectLanguageOf(text = "languages are awesome")
```

The public API of *Lingua* never returns `null` somewhere, so it is safe to be used from within Java code as well.

```java
/* Java */

import com.github.pemistahl.lingua.api.*;
import static com.github.pemistahl.lingua.api.Language.*;

final LanguageDetector detector = LanguageDetectorBuilder.fromLanguages(ENGLISH, FRENCH, GERMAN, SPANISH).build();
final Language detectedLanguage = detector.detectLanguageOf("languages are awesome");
```

#### 9.1.2 Minimum relative distance

By default, *Lingua* returns the most likely language for a given input text. However, there are 
certain words that are spelled the same in more than one language. The word *prologue*, for instance, 
is both a valid English and French word. *Lingua* would output either English or French which might 
be wrong in the given context. For cases like that, it is possible to specify a minimum relative 
distance that the logarithmized and summed up probabilities for each possible language have to satisfy. 
It can be stated in the following way:

```kotlin
val detector = LanguageDetectorBuilder
    .fromAllLanguages()
    .withMinimumRelativeDistance(0.25) // minimum: 0.00 maximum: 0.99 default: 0.00
    .build()
```

Be aware that the distance between the language probabilities is dependent on the length of the input text. 
The longer the input text, the larger the distance between the languages. So if you want to classify very 
short text phrases, do not set the minimum relative distance too high. Otherwise you will get most results 
returned as `Language.UNKNOWN` which is the return value for cases where language detection is not reliably 
possible. 

#### 9.1.3 Confidence values

Knowing about the most likely language is nice but how reliable is the computed likelihood?
And how less likely are the other examined languages in comparison to the most likely one?
These questions can be answered as well:

```kotlin

val detector = LanguageDetectorBuilder.fromLanguages(GERMAN, ENGLISH, FRENCH, SPANISH).build()
val confidenceValues = detector.computeLanguageConfidenceValues(text = "Coding is fun.")

// {
//   ENGLISH=1.0, 
//   GERMAN=0.8665738136456169, 
//   FRENCH=0.8249537317466078, 
//   SPANISH=0.7792362923625288
// }
```

In the example above, a map of all possible languages is returned, sorted by their confidence
value in descending order. The values that the detector computes are part of a **relative**
confidence metric, not of an absolute one. Each value is a number between 0.0 and 1.0.
The most likely language is always returned with value 1.0. All other languages get values
assigned which are lower than 1.0, denoting how less likely those languages are in comparison
to the most likely language.

The map returned by this method does not necessarily contain all languages which the calling
instance of `LanguageDetector` was built from. If the rule-based engine decides that a specific
language is truly impossible, then it will not be part of the returned map. Likewise, if no
ngram probabilities can be found within the detector's languages for the given input text, the
returned map will be empty. The confidence value for each language not being part of the
returned map is assumed to be 0.0.

#### 9.1.4 Eager loading versus lazy loading

By default, *Lingua* uses lazy-loading to load only those language models on demand which are 
considered relevant by the rule-based filter engine. For web services, for instance, it is 
rather beneficial to preload all language models into memory to avoid unexpected latency while 
waiting for the service response. If you want to enable the eager-loading mode, you can do it
like this:

```kotlin
LanguageDetectorBuilder.fromAllLanguages().withPreloadedLanguageModels().build()
```

Multiple instances of `LanguageDetector` share the same language models in memory which are
accessed asynchronously by the instances.

#### 9.1.5 Low accuracy mode versus high accuracy mode

*Lingua's* high detection accuracy comes at the cost of being noticeably slower than other language detectors.
The large language models also consume significant amounts of memory. These requirements
might not be feasible for systems running low on resources. If you want to classify mostly
long texts or need to save resources, you can enable a *low accuracy mode* that loads only
a small subset of the language models into memory:

```kotlin
LanguageDetectorBuilder.fromAllLanguages().withLowAccuracyMode().build()
```

The downside of this approach is that detection accuracy for short texts consisting of less
than 120 characters will drop significantly. However, detection accuracy for texts which are
longer than 120 characters will remain mostly unaffected. 

An alternative for a smaller memory footprint and faster performance is to reduce the set
of languages when building the language detector. In most cases, it is not advisable to
build the detector from all supported languages. When you have knowledge about
the texts you want to classify you can almost always rule out certain languages as impossible
or unlikely to occur.

#### 9.1.6 Methods to build the LanguageDetector

There might be classification tasks where you know beforehand that your language data is definitely not
written in Latin, for instance (what a surprise :-). The detection accuracy can become better in such
cases if you exclude certain languages from the decision process or just explicitly include relevant languages:

```kotlin
// include all languages available in the library
// WARNING: in the worst case this produces high memory 
//          consumption of approximately 3.5GB 
//          and slow runtime performance
//          (in high accuracy mode)
LanguageDetectorBuilder.fromAllLanguages()

// include only languages that are not yet extinct (= currently excludes Latin)
LanguageDetectorBuilder.fromAllSpokenLanguages()

// include only languages written with Cyrillic script
LanguageDetectorBuilder.fromAllLanguagesWithCyrillicScript()

// exclude only the Spanish language from the decision algorithm
LanguageDetectorBuilder.fromAllLanguagesWithout(Language.SPANISH)

// only decide between English and German
LanguageDetectorBuilder.fromLanguages(Language.ENGLISH, Language.GERMAN)

// select languages by ISO 639-1 code
LanguageDetectorBuilder.fromIsoCodes639_1(IsoCode639_1.EN, IsoCode639_3.DE)

// select languages by ISO 639-3 code
LanguageDetectorBuilder.fromIsoCodes639_3(IsoCode639_3.ENG, IsoCode639_3.DEU)
```

#### 9.1.7 How to manage memory consumption within application server deployments

Internally, *Lingua* efficiently uses all cores of your CPU in order to speed up loading the language
models and language detection itself. For this purpose, an internal 
[ForkJoinPool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html) 
is used. If the library is used within an application server, the consumed memory will not be freed 
automatically when the application is undeployed.

If you want to free all of *Lingua's* resources, you will have to do this manually by calling
`detector.unloadLanguageModels()` during the undeployment. This will clear all loaded language models 
from memory but the thread pool will keep running.

### 9.2 <a name="library-use-standalone"></a> Standalone mode <sup>[Top ▲](#table-of-contents)</sup>
If you want to try out *Lingua* before you decide whether to use it or not, you can run it in a REPL 
and immediately see its detection results.
1. With Gradle: `./gradlew runLinguaOnConsole --console=plain`
2. Without Gradle: `java -jar lingua-1.2.2-with-dependencies.jar`

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

## 10. You want to contribute? That's great!

In case you want to contribute something to *Lingua*, then you are encouraged to do so. Do you have ideas for
improving the API? Are there some specific languages that you want to have supported early? Or have you
found any bugs so far? Feel free to open an issue or send a pull request. It's very much appreciated.

For pull requests, please make sure that all unit tests pass and that the code is formatted according to
the official Kotlin style guide. You can check this by running the Kotlin linter [ktlint](https://ktlint.github.io/)
using `./gradlew ktlintCheck`. Most issues which the linter identifies can be fixed by running `./gradlew ktlintFormat`.
All other issues, especially lines which are longer than 120 characters, cannot be fixed automatically. In this case,
please format the respective lines by hand. You will notice that the build will fail if the formatting is not correct.

All kinds of pull requests are welcome. The most favorite ones are new language additions. If you want
to contribute new languages to *Lingua*, here comes a detailed manual explaining how to accomplish that.

Thank you very much in advance for all contributions, however small they may be.

### 10.1 How to add new languages?

In order to execute the steps below, you will need Java 8 or greater. Even though the library itself
runs on Java >= 6, the `FilesWriter` classes make use of the [java.nio][java nio url] api which was
introduced with Java 8.

1. Clone *Lingua's* repository to your own computer as described in [section 8][library build url].
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
   Inside this subdirectory you **must** put another with the name of the language's ISO 639-3 code.
   An example would be `/src/main/resources/language-models/de/deu/`.
7. Use [`TestDataFilesWriter`][test data files writer url] to create the test data files used for
   accuracy report generation. The input file from which to create the test data should have each
   sentence on a separate line.
8. Put the generated test data files in [`/src/accuracyReport/resources/language-testdata`][test data directory url].
   Do **not** rename the test data files.
9. For accuracy report generation, create an abstract base class for the main logic in
   [`/src/accuracyReport/kotlin/com/github/pemistahl/lingua/report/config`][accuracy report config url].
   Look at the other languages' files in this directory to see how the class must look like.
   It should be pretty self-explanatory.
10. Create a concrete test class in
    [`/src/accuracyReport/kotlin/com/github/pemistahl/lingua/report/lingua`][accuracy report lingua url].
    Look at the other languages' files in this directory to see how the class must look like.
    It should be pretty self-explanatory. If one of the other language detector libraries
    supports your language already, you can add test classes for those as well. Each library
    has its own directory for this purpose. If your language is not supported by the other
    language detector libraries, exclude it in [`AbstractLanguageDetectionAccuracyReport`][accuracy report nonlingua url].
11. Fix the existing unit tests by adding your new language.
12. Add your new language to property [`linguaSupportedLanguages`][gradle properties url]
    in `/gradle.properties`.
13. Run `./gradlew accuracyReport` and add the updated accuracy reports to your pull request.
14. Run `./gradlew drawAccuracyPlots` and add the updated plots to your pull request.
15. Run `./gradlew writeAccuracyTable` and add the updated accuracy table to your pull request.
16. Be happy! :-) You have successfully contributed a new language and have thereby significantly widened
    this library's fields of application.

## 11. What's next for version 1.3.0?

Take a look at the [planned issues](https://github.com/pemistahl/lingua/milestone/10).

[javadoc badge]: https://javadoc.io/badge2/com.github.pemistahl/lingua/javadoc.svg
[javadoc url]: https://javadoc.io/doc/com.github.pemistahl/lingua
[ci badge]: https://github.com/pemistahl/lingua/actions/workflows/build.yml/badge.svg
[ci url]: https://github.com/pemistahl/lingua/actions/workflows/build.yml
[codecov badge]: https://codecov.io/gh/pemistahl/lingua/branch/main/graph/badge.svg
[codecov url]: https://codecov.io/gh/pemistahl/lingua
[supported languages badge]: https://img.shields.io/badge/supported%20languages-75-green.svg
[lingua version badge]: https://img.shields.io/badge/Download%20Jar-1.2.2-blue.svg
[lingua download url]: https://github.com/pemistahl/lingua/releases/download/v1.2.2/lingua-1.2.2-with-dependencies.jar
[Kotlin platforms badge]: https://img.shields.io/badge/platforms-JDK%208%2B-blue.svg
[Kotlin platforms url]: https://kotlinlang.org/docs/reference/server-overview.html
[license badge]: https://img.shields.io/badge/license-Apache%202.0-blue.svg
[license url]: https://www.apache.org/licenses/LICENSE-2.0
[Wortschatz corpora]: http://wortschatz.uni-leipzig.de
[Apache Tika]: https://tika.apache.org/1.26/detection.html#Language_Detection
[Apache OpenNLP]: https://opennlp.apache.org/docs/1.9.3/manual/opennlp.html#tools.langdetect
[Optimaize Language Detector]: https://github.com/optimaize/language-detector
[GitHub Packages]: https://github.com/pemistahl/lingua/packages/766181
[Maven Central]: https://search.maven.org/artifact/com.github.pemistahl/lingua/1.2.2/jar
[Maven Central badge]: https://img.shields.io/badge/Maven%20Central-1.2.2-green.svg
[ACCURACY_PLOTS.md]: https://github.com/pemistahl/lingua/blob/main/ACCURACY_PLOTS.md
[ACCURACY_TABLE.md]: https://github.com/pemistahl/lingua/blob/main/ACCURACY_TABLE.md
[accuracy reports url]: https://github.com/pemistahl/lingua/tree/main/accuracy-reports
[java nio url]: https://docs.oracle.com/javase/8/docs/api/java/nio/package-summary.html
[library build url]: https://github.com/pemistahl/lingua#8-how-to-build
[isocode639_1 url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/IsoCode639_1.kt
[isocode639_3 url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/IsoCode639_3.kt
[wikipedia isocodes list]: https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes
[language url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/Language.kt
[alphabet url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/internal/Alphabet.kt
[chars to languages mapping url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/internal/Constant.kt#L67
[language model files writer url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/io/LanguageModelFilesWriter.kt#L27
[language models directory url]: https://github.com/pemistahl/lingua/tree/main/src/main/resources/language-models
[test data files writer url]: https://github.com/pemistahl/lingua/blob/main/src/main/kotlin/com/github/pemistahl/lingua/api/io/TestDataFilesWriter.kt#L28
[test data directory url]: https://github.com/pemistahl/lingua/tree/main/src/accuracyReport/resources/language-testdata
[accuracy report config url]: https://github.com/pemistahl/lingua/tree/main/src/accuracyReport/kotlin/com/github/pemistahl/lingua/report/config
[accuracy report lingua url]: https://github.com/pemistahl/lingua/tree/main/src/accuracyReport/kotlin/com/github/pemistahl/lingua/report/lingua
[accuracy report nonlingua url]: https://github.com/pemistahl/lingua/blob/main/src/accuracyReport/kotlin/com/github/pemistahl/lingua/report/AbstractLanguageDetectionAccuracyReport.kt#L324
[gradle properties url]: https://github.com/pemistahl/lingua/blob/main/gradle.properties#L60

# Copyright 2018-2019 Peter M. Stahl pemistahl@googlemail.com
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import matplotlib
matplotlib.use('TkAgg')

import matplotlib.pyplot as plt
import seaborn as sns
import pandas as pd


def create_lineplot(data, columns, title, ylim, filename):
    filtered_data = data.loc[:, columns]

    plt.figure(figsize=(32,12))
    plt.title(title, fontsize=45, fontweight='bold')
    plt.xticks(rotation=90, fontsize=35)
    plt.yticks(fontsize=35)
    plt.grid(color='#A6A6A6')

    ax = sns.lineplot(data=filtered_data, linewidth=5, palette=['green', 'orange', 'red'])
    ax.set_ylim(ylim)
    ax.legend(labels=['Lingua 0.4.0', 'Tika 1.20', 'Optimaize 0.6'], fontsize=28, loc='lower left')
    ax.set_xlabel('Language', fontsize=38, fontweight='bold')
    ax.set_ylabel('Accuracy (%)', fontsize=38, fontweight='bold')

    plt.tight_layout()
    plt.savefig('images/plots/' + filename, dpi=72)


def create_boxplot(data, columns, title, ylim, filename):
    filtered_data = data.loc[:, columns]

    plt.figure(figsize=(32,12))
    plt.title(title, fontsize=45, fontweight='bold')
    plt.xticks(fontsize=35)
    plt.yticks(fontsize=35)
    plt.grid(color='#A6A6A6')

    ax = sns.boxplot(data=filtered_data, linewidth=5, palette=['red', 'orange', 'green'])
    ax.set_ylim(ylim)
    ax.set_xlabel('Classifier', fontsize=38, fontweight='bold')
    ax.set_ylabel('Accuracy (%)', fontsize=38, fontweight='bold')
    ax.set_xticklabels(['Optimaize 0.6', 'Tika 1.20', 'Lingua 0.4.0'])

    plt.tight_layout()
    plt.savefig('images/plots/' + filename, dpi=72)


def create_barplot(data, columns, title, ylim, filename):
    filtered_data = data.loc[:, columns]

    plt.figure(figsize=(32,12))
    plt.title(title, fontsize=45, fontweight='bold')
    plt.xticks(fontsize=35)
    plt.yticks(fontsize=35)
    plt.grid(color='#A6A6A6')

    ax = sns.barplot(
        data=filtered_data,
        palette=['red', 'orange', 'green'],
        errwidth=7.0,
        ci='sd',
        capsize=.1
    )
    ax.set_ylim(ylim)
    ax.set_xlabel('Classifier', fontsize=38, fontweight='bold')
    ax.set_ylabel('Mean Accuracy (%)', fontsize=38, fontweight='bold')
    ax.set_xticklabels(['Optimaize 0.6', 'Tika 1.20', 'Lingua 0.4.0'])

    plt.tight_layout()
    plt.savefig('images/plots/' + filename, dpi=72)


sns.set()
sns.set_style('whitegrid')

accuracy_values_data_frame = pd.read_csv(
    filepath_or_buffer='accuracy-reports/aggregated-accuracy-values.csv',
    delim_whitespace=True
).set_index('language')

accuracy_values_data_frame = accuracy_values_data_frame.reindex(
    sorted(accuracy_values_data_frame.columns),
    axis=1
)

# SINGLE WORD DETECTION ACCURACY #
create_lineplot(
    data=accuracy_values_data_frame,
    columns=['single-words-lingua', 'single-words-tika', 'single-words-optimaize'],
    title='Single Word Detection',
    ylim=[0,100],
    filename='lineplot-singlewords.png'
)

create_boxplot(
    data=accuracy_values_data_frame,
    columns=['single-words-optimaize', 'single-words-tika', 'single-words-lingua'],
    title='Single Word Detection',
    ylim=[0,100],
    filename='boxplot-singlewords.png'
)

create_barplot(
    data=accuracy_values_data_frame,
    columns=['single-words-optimaize', 'single-words-tika', 'single-words-lingua'],
    title='Single Word Detection',
    ylim=[0,100],
    filename='barplot-singlewords.png'
)

# WORD PAIR DETECTION ACCURACY #
create_lineplot(
    data=accuracy_values_data_frame,
    columns=['word-pairs-lingua', 'word-pairs-tika', 'word-pairs-optimaize'],
    title='Word Pair Detection',
    ylim=[0,100],
    filename='lineplot-wordpairs.png'
)

create_boxplot(
    data=accuracy_values_data_frame,
    columns=['word-pairs-optimaize', 'word-pairs-tika', 'word-pairs-lingua'],
    title='Word Pair Detection',
    ylim=[0,100],
    filename='boxplot-wordpairs.png'
)

create_barplot(
    data=accuracy_values_data_frame,
    columns=['word-pairs-optimaize', 'word-pairs-tika', 'word-pairs-lingua'],
    title='Word Pair Detection',
    ylim=[0,120],
    filename='barplot-wordpairs.png'
)

# SENTENCE DETECTION ACCURACY #
create_lineplot(
    data=accuracy_values_data_frame,
    columns=['sentences-lingua', 'sentences-tika', 'sentences-optimaize'],
    title='Sentence Detection',
    ylim=[10,100],
    filename='lineplot-sentences.png'
)

create_boxplot(
    data=accuracy_values_data_frame,
    columns=['sentences-optimaize', 'sentences-tika', 'sentences-lingua'],
    title='Sentence Detection',
    ylim=[75,100],
    filename='boxplot-sentences.png'
)

create_barplot(
    data=accuracy_values_data_frame,
    columns=['sentences-optimaize', 'sentences-tika', 'sentences-lingua'],
    title='Sentence Detection',
    ylim=[0,120],
    filename='barplot-sentences.png'
)

# AVERAGE DETECTION ACCURACY #
create_lineplot(
    data=accuracy_values_data_frame,
    columns=['average-lingua', 'average-tika', 'average-optimaize'],
    title='Average Detection',
    ylim=[0,100],
    filename='lineplot-average.png'
)

create_boxplot(
    data=accuracy_values_data_frame,
    columns=['average-optimaize', 'average-tika', 'average-lingua'],
    title='Average Detection',
    ylim=[0,100],
    filename='boxplot-average.png'
)

create_barplot(
    data=accuracy_values_data_frame,
    columns=['average-optimaize', 'average-tika', 'average-lingua'],
    title='Average Detection',
    ylim=[0,100],
    filename='barplot-average.png'
)

print("All plots created successfully")

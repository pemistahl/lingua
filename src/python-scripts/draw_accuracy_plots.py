# Copyright © 2018-2020 Peter M. Stahl pemistahl@gmail.com
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

from math import floor

import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns
from matplotlib.patches import Patch

matplotlib.use('TkAgg')
sns.set()
sns.set_style('whitegrid')


class AccuracyPlotDrawer(object):
    __dpi = 40
    __ticks_fontsize = 35
    __label_fontsize = 38
    __title_fontsize = 45
    __fontweight = 'bold'
    __hue = 'classifier'
    __grid_color = '#474747'
    __plot_filepath = 'images/plots/'
    __plot_titles = ('Single Word Detection', 'Word Pair Detection', 'Sentence Detection', 'Average Detection')
    __column_prefixes = ('single-words', 'word-pairs', 'sentences', 'average')
    __column_suffixes = ('optimaize', 'opennlp', 'tika', 'lingua')
    __legend_labels = ('Optimaize 0.6', 'OpenNLP 1.9.1', 'Tika 1.23', 'Lingua 0.6.1')
    __hatches = ('/', '+', '.', 'O')
    __palette = ('#b259ff', '#ff6347', '#ffc400', '#41c46b')
    __ticks = np.arange(0, 101, 10)
    __legend_handles = [Patch(facecolor=color, edgecolor='black', label=label, hatch=hatch)
                        for color, label, hatch in zip(__palette, __legend_labels, __hatches)]

    def __init__(self, filepath):
        self.__dataframe = self.__read_into_dataframe(filepath)

    def __read_into_dataframe(self, filepath):
        frame = pd.read_csv(filepath_or_buffer=filepath)
        return pd.melt(frame=frame, id_vars='language', value_name='accuracy', var_name=self.__hue)

    def draw_all_barplots(self):
        for title, prefix in zip(self.__plot_titles, self.__column_prefixes):
            columns = [prefix + '-' + suffix for suffix in self.__column_suffixes]
            self.draw_barplot(columns, title, xlim=(0, 100), filename='barplot-' + prefix + '.png')

    def draw_all_boxplots(self):
        for title, prefix in zip(self.__plot_titles, self.__column_prefixes):
            columns = [prefix + '-' + suffix for suffix in self.__column_suffixes]
            self.draw_boxplot(columns, title, ylim=(0, 100), filename='boxplot-' + prefix + '.png')

    def draw_barplot(self, columns, title, xlim, filename):
        row_filter = self.__dataframe[self.__hue].isin(columns)
        data = self.__dataframe[row_filter]

        plt.figure(figsize=(16, 100))
        plt.title(title + '\n', fontsize=self.__title_fontsize, fontweight=self.__fontweight)
        plt.xticks(fontsize=self.__ticks_fontsize, ticks=self.__ticks)
        plt.yticks(fontsize=self.__ticks_fontsize)
        plt.grid(color=self.__grid_color)

        axes = sns.barplot(data=data, x='accuracy', y='language', hue=self.__hue, palette=self.__palette)

        axes.set_xlabel('Accuracy (%)\n', fontsize=self.__label_fontsize, fontweight=self.__fontweight)
        axes.set_ylabel('Language', fontsize=self.__label_fontsize, fontweight=self.__fontweight)
        axes.set_xlim(xlim)
        axes.xaxis.tick_top()
        axes.xaxis.set_label_position('top')
        axes.tick_params(axis='both', which='major', labelsize=self.__label_fontsize)
        axes.tick_params(axis='both', which='minor', labelsize=self.__label_fontsize)
        axes.legend(handles=self.__legend_handles, fontsize=28, loc='upper left')

        language_count = len(axes.patches) / len(self.__legend_labels)
        for i, current_bar in enumerate(axes.patches):
            current_bar.set_edgecolor(self.__grid_color)
            current_bar.set_hatch(self.__hatches[floor(i / language_count)])

        plt.tight_layout()
        plt.savefig(self.__plot_filepath + filename, dpi=self.__dpi)

    def draw_boxplot(self, columns, title, ylim, filename):
        row_filter = self.__dataframe[self.__hue].isin(columns)
        data = self.__dataframe[row_filter]

        plt.figure(figsize=(24, 12))
        plt.title(title, fontsize=self.__title_fontsize, fontweight=self.__fontweight)
        plt.xticks(fontsize=self.__ticks_fontsize)
        plt.yticks(fontsize=self.__ticks_fontsize, ticks=self.__ticks)
        plt.grid(self.__grid_color)

        axes = sns.boxplot(data=data, x='classifier', y='accuracy', linewidth=5, palette=self.__palette)

        axes.set_ylim(ylim)
        axes.set_xlabel('Classifier', fontsize=self.__label_fontsize, fontweight=self.__fontweight)
        axes.set_ylabel('Accuracy (%)', fontsize=self.__label_fontsize, fontweight=self.__fontweight)
        axes.set_xticklabels(self.__legend_labels)

        plt.tight_layout()
        plt.savefig(self.__plot_filepath + filename, dpi=self.__dpi)


drawer = AccuracyPlotDrawer(filepath='accuracy-reports/aggregated-accuracy-values.csv')
drawer.draw_all_barplots()
drawer.draw_all_boxplots()

print("All plots created successfully")

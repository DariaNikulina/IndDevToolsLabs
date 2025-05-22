package org.example.lab2;

import org.example.lab1.WordCountUtils;
import org.example.lab1.WordsInTextResults;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Необходимо передать слово для проверки и путь к директории с файлами");
            return;
        }

        String word = args[0].toLowerCase();
        String dirPath = args[1];

        WordsInTextResults result = WordCountUtils.parallelCountWordsInFile(dirPath);
        int totalWords = result.getWordCount();
        int wordCount = result.getWordFrequencies().getOrDefault(word, 0);
        double frequency = WordCountUtils.countFrequency(totalWords, wordCount);

        System.out.println("Всего слов: " + totalWords);
        System.out.println("Слово '" + word + "', всего встречается в текстах: " + wordCount);
        System.out.printf("Частота: %.2f%% \n", frequency);
    }
}

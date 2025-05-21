package org.example.lab1;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Необходимо передать слово для проверки и путь к файлу");
            return;
        }
        String wordToCount = args[0].toLowerCase();
        String filePath = args[1];
        WordsInTextResults result = WordCountUtils.countWordsInFile(Path.of(filePath));
        int totalWords = result.getWordCount();
        int wordCount = result.getWordFrequencies().getOrDefault(wordToCount, 0);
        double frequency = (totalWords == 0) ? 0.0 : (wordCount * 100.0 / totalWords);

        System.out.println("Всего слов: " + totalWords);
        System.out.println("Слово '" + wordToCount + "', всего встречается в тексте: " + wordCount);
        System.out.printf("Частота: %.2f%% \n", frequency);
    }
}


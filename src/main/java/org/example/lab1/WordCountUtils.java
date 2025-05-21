package org.example.lab1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCountUtils {
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * Для 1 лабораторной
     */
    public static WordsInTextResults countWordsInFile(Path filePath) {
        List<String> lines;
        try {
           lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            System.out.println("Ошибка загрузки файла: " + e.getMessage());
            return new WordsInTextResults(Collections.emptyMap(), 0);
        }
        Map<String, Integer> frequencyMap = lines.stream()
                .map(String::toLowerCase)
                .flatMap(line -> Arrays.stream(line.split("[^\\p{L}]+")))
                .filter(WordFilter::filterWord)
                .collect(Collectors.toMap(
                        word -> word,
                        word -> 1,
                        Integer::sum
                ));

        int totalWords = frequencyMap.values().stream().mapToInt(Integer::intValue).sum();

        return new WordsInTextResults(frequencyMap, totalWords);
    }

    /**
     * Для 2 лабораторной
     */
    public static WordsInTextResults parallelCountWordsInFile(String dirPath) {
        try(ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT)) {
            List<Future<WordsInTextResults>> futures = new ArrayList<>();

            try (Stream<Path> pathStream = Files.list(Path.of(dirPath))) {
                pathStream.forEach(
                        path -> futures.add(executor.submit(() -> countWordsInFile(path)))
                );
            }

            Map<String, Integer> totalWordMap = new HashMap<>();
            int totalCount = 0;

            for (Future<WordsInTextResults> future : futures) {
                try {
                    WordsInTextResults result = future.get();
                    result.getWordFrequencies().forEach(
                            (word, count) -> totalWordMap.merge(word, count, Integer::sum)
                    );
                    totalCount += result.getWordCount();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Ошибка обработки файла: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            return new WordsInTextResults(totalWordMap, totalCount);
        } catch (IOException e) {
            System.out.println(String.format("Ошибка обработки файлов в директории %s: ", dirPath) + e.getMessage());
        }

        return new WordsInTextResults(Collections.emptyMap(), 0);
    }
}

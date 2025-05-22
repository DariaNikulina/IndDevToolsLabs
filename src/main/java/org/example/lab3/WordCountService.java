package org.example.lab3;

import org.example.lab1.WordCountUtils;
import org.example.lab1.WordsInTextResults;
import org.example.lab3.database.FileEntity;
import org.example.lab3.database.WordResultsEntity;
import org.example.lab3.database.WordResultsInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class WordCountService {
    private final WordCountRepository wordCountRepository;
    private static WordCountService INSTANCE;
    private static final Integer RESULTS_LIMIT = 20;
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();


    private WordCountService() {
        this.wordCountRepository = new WordCountRepository();
    }

    public static synchronized WordCountService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WordCountService();
        }
        return INSTANCE;
    }

    public void processDirectory(String dirPath) {
        try (ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT)) {
            List<Future<Void>> futures = new ArrayList<>();
            try(Stream<Path> streamPath = Files.list(Path.of(dirPath))) {
                streamPath.forEach(path ->
                        futures.add(executor.submit(() -> {
                            WordsInTextResults result = WordCountUtils.countWordsInFile(path);
                            processFile(path, result.getWordFrequencies(), result.getWordCount());
                            return null;
                        }))
                );
            }
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Ошибка обработки файла: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            System.out.println(String.format("Ошибка обработки файлов в директории %s: ", dirPath) + e.getMessage());
        }
    }

    public List<WordResultsInfo> searchByWord(String word) {
        return wordCountRepository.findByWordOrderByCount(word, RESULTS_LIMIT);
    }

    public File downloadFile(String filename) {
        FileEntity file = wordCountRepository.findFileByName(filename);
        return new File(file.getPath());
    }

    private void processFile(Path file, Map<String, Integer> wordResults, int totalWords) {
        FileEntity existedFile = wordCountRepository.findFileByPath(file.toString());
        Long fileId;
        if (existedFile == null) {
            fileId = wordCountRepository.insertNewFile(file.toString(), file.getFileName().toString());
        } else {
            fileId = existedFile.getId();
        }

        wordResults.forEach((word, count) -> {
            double frequency = WordCountUtils.countFrequency(totalWords, count);
            wordCountRepository.insertNewWordResults(new WordResultsEntity(fileId, word, count, frequency));
        });
        System.out.printf("Загрузка слов из файла %s завешена%n", file.getFileName().toString());
    }
}

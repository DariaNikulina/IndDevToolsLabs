package org.example.lab1;

import java.util.Map;

public class WordsInTextResults {
    private final Map<String, Integer> wordFrequencies;
    private final int wordCount;

    public WordsInTextResults(Map<String, Integer> wordFrequencies, int wordCount) {
        this.wordFrequencies = Map.copyOf(wordFrequencies);
        this.wordCount = wordCount;
    }

    public Map<String, Integer> getWordFrequencies() {
        return wordFrequencies;
    }

    public int getWordCount() {
        return wordCount;
    }
}

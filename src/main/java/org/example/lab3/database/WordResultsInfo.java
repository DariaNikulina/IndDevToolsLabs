package org.example.lab3.database;

public class WordResultsInfo {
    private String filename;
    private String word;
    private Integer count;
    private Double percentage;

    public WordResultsInfo(String filename, String word, Integer count, Double percentage) {
        this.filename = filename;
        this.word = word;
        this.count = count;
        this.percentage = percentage;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}

package org.example.lab3.database;

public class WordResultsEntity {
    private Long id;
    private final Long fileId;
    private final String word;
    private final Integer count;
    private final Double percentage;

    public WordResultsEntity(Long fileId, String word, Integer count, Double percentage) {
        this.fileId = fileId;
        this.word = word;
        this.count = count;
        this.percentage = percentage;
    }

    public WordResultsEntity(Long id, Long fileId, String word, Integer count, Double percentage) {
        this.id = id;
        this.fileId = fileId;
        this.word = word;
        this.count = count;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public Long getFileId() {
        return fileId;
    }

    public String getWord() {
        return word;
    }

    public Integer getCount() {
        return count;
    }

    public Double getPercentage() {
        return percentage;
    }
}

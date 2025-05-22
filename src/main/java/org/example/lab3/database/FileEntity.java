package org.example.lab3.database;

public class FileEntity {
    private final Long id;
    private final String path;
    private final String filename;

    public FileEntity(Long id, String path, String filename) {
        this.id = id;
        this.path = path;
        this.filename = filename;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getFilename() {
        return filename;
    }
}

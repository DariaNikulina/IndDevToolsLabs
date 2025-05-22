CREATE DATABASE test;
CREATE SCHEMA IF NOT EXISTS test;

CREATE TABLE test.files (
    id SERIAL NOT NULL PRIMARY KEY,
    path TEXT NOT NULL,
    filename TEXT UNIQUE NOT NULL
);

CREATE TABLE test.word_results(
    id SERIAL NOT NULL PRIMARY KEY,
    fileId BIGINT NOT NULL,
    word TEXT NOT NULL,
    count INT NOT NULL,
    frequency FLOAT NOT NULL,
    FOREIGN KEY (fileId) REFERENCES test.files(id)
);

CREATE INDEX idx_word ON test.word_results(word);
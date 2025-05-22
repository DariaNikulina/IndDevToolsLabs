package org.example.lab3;

import org.example.lab3.database.FileEntity;
import org.example.lab3.database.PostgresConnection;
import org.example.lab3.database.WordResultsEntity;
import org.example.lab3.database.WordResultsInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordCountRepository {
    private final Connection connection;

    public WordCountRepository() {
        this.connection = PostgresConnection.getConnection();
    }

    public FileEntity findFileByPath(String filePath) {
        String sql = "SELECT * FROM files WHERE path = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(sql)){
            selectStmt.setString(1, filePath);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new FileEntity(rs.getLong(1), rs.getString(2), rs.getString(3));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске файла", e);
        }
        return null;
    }

    public FileEntity findFileByName(String fileName) {
        String sql = "SELECT * FROM files WHERE filename = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(sql)){
            selectStmt.setString(1, fileName);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    return new FileEntity(rs.getLong(1), rs.getString(2), rs.getString(3));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске файла", e);
        }
        return null;
    }

    public Long insertNewFile(String filePath, String filename)  {
        String insertSql = "INSERT INTO files (path, filename) VALUES (?, ?) RETURNING id";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            insertStmt.setString(1, filePath);
            insertStmt.setString(2, filename);
            try (ResultSet rs = insertStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                } else {
                    throw new RuntimeException("Ошибка при получении ID новой сущности");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка записи", e);
        }
    }

    public void insertNewWordResults(WordResultsEntity wordResultsEntity) {
        String sql = "INSERT INTO word_results (fileId, word, count, frequency) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, wordResultsEntity.getFileId());
            statement.setString(2, wordResultsEntity.getWord());
            statement.setInt(3, wordResultsEntity.getCount());
            statement.setDouble(4, wordResultsEntity.getPercentage());
            statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка записи", e);
        }
    }

    public List<WordResultsInfo> findByWordOrderByCount(String word, int limit) {
        String searchString = word + "%";
        String sql = """
            SELECT f.filename, w.word, w.count, w.frequency
              FROM word_results w
              JOIN files f ON f.id = w.fileId
             WHERE w.word ILIKE ?
             ORDER BY w.count DESC
             LIMIT ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, searchString);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<WordResultsInfo> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(new WordResultsInfo(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getDouble(4)
                    ));
                }
                return results;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска по слову", e);
        }
    }
}

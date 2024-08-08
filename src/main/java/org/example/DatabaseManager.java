package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager(String url, String username, String password) throws SQLException {
        try {
            // PostgreSQL 드라이버 로드
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found.", e);
        }

        connection = DriverManager.getConnection(url, username, password);
    }

    public void insertArticle(String title, String href, String press) throws SQLException {
        String query = "INSERT INTO articles (title, href, press) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, href);
            stmt.setString(3, press);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // 기사 내용, 기자명, 날짜를 업데이트하는 메서드
    public void updateArticleContentAndJournalist(int articleId, String content, String journalist, String datetime) throws SQLException {
        String query = "UPDATE articles SET content = ?, journalist = ?, datetime = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, content);
            stmt.setString(2, journalist);
            stmt.setString(3, datetime);
            stmt.setInt(4, articleId);
            stmt.executeUpdate();
        }
    }

    // 모든 기사 URL과 ID를 가져오는 메서드
    public List<Article> getAllArticleUrls() {
        List<Article> articles = new ArrayList<>();
        String query = "SELECT id, title, href, datetime, content, press, journalist FROM articles";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                articles.add(mapResultSetToArticle(rs));
            }
        } catch (SQLException e) {
            // 예외 상세 확인하고 싶을 시 주석 해제
            // e.printStackTrace();
        }
        return articles;
    }

    // ResultSet을 Article 객체로 매핑하는 헬퍼 메서드
    private Article mapResultSetToArticle(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String href = rs.getString("href");
        String datetime = rs.getString("datetime");
        String content = rs.getString("content");
        String press = rs.getString("press");
        String journalist = rs.getString("journalist");
        return new Article(id, title, href, datetime, content, press, journalist);
    }
}
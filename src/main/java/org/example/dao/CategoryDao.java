package org.example.dao;

import org.example.domain.Category;
import org.example.test.DbUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao implements AutoCloseable {
    private Connection con;

    public CategoryDao() throws Exception {
        con = DbUtil.getConnection();
    }

    public List<Category> getAllCategories() throws Exception {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("id"));
                    category.setTitle(rs.getString("title"));
                    category.setDescription(rs.getString("description"));
                    Timestamp timestamp = rs.getTimestamp("created_time");
                    if (timestamp != null) {
                        category.setCreatedTime(timestamp.toLocalDateTime());
                    }
                    categories.add(category);
                }
            }
        }
        return categories;
    }

    public Category getCategoryById(int id) throws Exception {
        Category category = null;
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    category = new Category();
                    category.setId(rs.getInt("id"));
                    category.setTitle(rs.getString("title"));
                    category.setDescription(rs.getString("description"));
                    Timestamp timestamp = rs.getTimestamp("created_time");
                    if (timestamp != null) {
                        category.setCreatedTime(timestamp.toLocalDateTime());
                    }
                }
            }
        }
        return category;
    }

    public int addCategory(Category category) throws Exception {
        int count = 0;
        String sql = "INSERT INTO categories(title, description) VALUES(?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getTitle());
            stmt.setString(2, category.getDescription());
            count = stmt.executeUpdate();
            
            if (count > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        category.setId(rs.getInt(1));
                    }
                }
            }
        }
        return count;
    }

    @Override
    public void close() throws Exception {
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 
package org.example.dao;
import org.example.domain.Blog;
import org.example.test.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class BlogDao implements AutoCloseable {
    private Connection con;
    public BlogDao() throws Exception {
        con = DbUtil.getConnection();
    }
    public int createBlog(Blog blog) throws Exception {
        int count = 0;
        String sql = "INSERT INTO blogs(title, category_id, content, user_id) VALUES(?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, blog.getTitle());
            stmt.setInt(2, blog.getCategoryId());
            stmt.setString(3, blog.getContent());
            stmt.setInt(4, blog.getUserId());
            count = stmt.executeUpdate();
            
            if (count > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        blog.setId(rs.getInt(1));
                    }
                }
            }
        }
        return count;
    }
    public boolean editBlog(Blog blog) throws Exception {
        boolean updated = false;
        String sql = "UPDATE blogs SET title = ?, category_id = ?, content = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, blog.getTitle());
            stmt.setInt(2, blog.getCategoryId());
            stmt.setString(3, blog.getContent());
            stmt.setInt(4, blog.getId());
            stmt.setInt(5, blog.getUserId());
            int count = stmt.executeUpdate();
            if (count > 0) {
                updated = true;
            }
        }
        return updated;
    }
    public boolean deleteBlog(int blogId, int userId) throws Exception {
        boolean deleted = false;
        String sql = "DELETE FROM blogs WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, blogId);
            stmt.setInt(2, userId);
            int count = stmt.executeUpdate();
            if (count > 0) {
                deleted = true;
            }
        }
        return deleted;
    }
    public List<Blog> getAllBlogs() throws Exception {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT b.*, u.name as author_name, c.title as category_title " +
                     "FROM blogs b " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "JOIN categories c ON b.category_id = c.id " +
                     "ORDER BY b.creation_time DESC";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Blog blog = extractBlogFromResultSet(rs);
                    blogs.add(blog);
                }
            }
        }
        return blogs;
    }
    public List<Blog> getBlogsByUser(int userId) throws Exception {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT b.*, u.name as author_name, c.title as category_title " +
                     "FROM blogs b " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "JOIN categories c ON b.category_id = c.id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.creation_time DESC";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Blog blog = extractBlogFromResultSet(rs);
                    blogs.add(blog);
                }
            }
        }
        return blogs;
    }
    public Blog getBlogById(int blogId) throws Exception {
        Blog blog = null;
        String sql = "SELECT b.*, u.name as author_name, c.title as category_title " +
                     "FROM blogs b " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "JOIN categories c ON b.category_id = c.id " +
                     "WHERE b.id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, blogId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    blog = extractBlogFromResultSet(rs);
                }
            }
        }
        return blog;
    }
    public List<Blog> searchBlogs(String searchTerm) throws Exception {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT b.*, u.name as author_name, c.title as category_title " +
                     "FROM blogs b " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "JOIN categories c ON b.category_id = c.id " +
                     "WHERE b.title LIKE ? OR c.title LIKE ? OR b.content LIKE ? " +
                     "ORDER BY b.creation_time DESC";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Blog blog = extractBlogFromResultSet(rs);
                    blogs.add(blog);
                }
            }
        }
        return blogs;
    }
    public boolean isAuthorizedUser(int blogId, int userId) throws Exception {
        boolean isAuthorized = false;
        String sql = "SELECT user_id FROM blogs WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, blogId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int blogUserId = rs.getInt("user_id");
                    if (blogUserId == userId) {
                        isAuthorized = true;
                    }
                }
            }
        }
        return isAuthorized;
    }
    public List<Blog> searchBlogsByWord(String searchWord) throws Exception {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT b.*, u.name as author_name, c.title as category_title " +
                     "FROM blogs b " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "JOIN categories c ON b.category_id = c.id " +
                     "WHERE c.title LIKE ? OR b.content LIKE ? " +
                     "ORDER BY b.creation_time DESC";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            String searchPattern = "%" + searchWord + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Blog blog = extractBlogFromResultSet(rs);
                    blogs.add(blog);
                }
            }
        }
        return blogs;
    }
    private Blog extractBlogFromResultSet(ResultSet rs) throws SQLException {
        Blog blog = new Blog();
        blog.setId(rs.getInt("id"));
        blog.setTitle(rs.getString("title"));
        blog.setCategoryId(rs.getInt("category_id"));
        blog.setCategoryTitle(rs.getString("category_title"));
        blog.setContent(rs.getString("content"));
        blog.setUserId(rs.getInt("user_id"));
        blog.setCreationTime(rs.getTimestamp("creation_time"));
        blog.setAuthorName(rs.getString("author_name"));
        return blog;
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
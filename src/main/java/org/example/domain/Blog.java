package org.example.domain;

import java.sql.Timestamp;

public class Blog {
    private int id;
    private String title;
    private int categoryId;
    private String content;
    private int userId;
    private Timestamp creationTime;
    private String authorName;
    private String categoryTitle;

    public Blog() {
    }

    public Blog(int id, String title, int categoryId, String content, int userId, Timestamp creationTime) {
        this.id = id;
        this.title = title;
        this.categoryId = categoryId;
        this.content = content;
        this.userId = userId;
        this.creationTime = creationTime;
    }

    public Blog(String title, int categoryId, String content, int userId) {
        this.title = title;
        this.categoryId = categoryId;
        this.content = content;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public int getCategoryId() {

        return categoryId;
    }

    public void setCategoryId(int categoryId) {

        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {

        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {

        this.categoryTitle = categoryTitle;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public int getUserId() {

        return userId;
    }

    public void setUserId(int userId) {

        this.userId = userId;
    }

    public Timestamp getCreationTime() {

        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {

        this.creationTime = creationTime;
    }

    public String getAuthorName() {

        return authorName;
    }

    public void setAuthorName(String authorName) {

        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categoryId=" + categoryId +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", creationTime=" + creationTime +
                ", authorName='" + authorName + '\'' +
                ", categoryTitle='" + categoryTitle + '\'' +
                '}';
    }
} 
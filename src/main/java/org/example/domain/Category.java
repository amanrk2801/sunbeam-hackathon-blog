package org.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Category {
    private int id;
    private String title;
    private String description;
    private LocalDateTime createdTime;
    public Category() {

        this.createdTime = LocalDateTime.now();
    }


    public Category(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdTime = LocalDateTime.now();
    }
    public Category(int id, String title, String description, LocalDateTime createdTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
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

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public LocalDateTime getCreatedTime() {

        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {

        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 
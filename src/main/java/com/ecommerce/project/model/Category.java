package com.ecommerce.project.model;

public class Category {
    private long categoryId;
    private String categoryname;

    // getters and setters


    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
    // counstructor


    public Category(long categoryId, String categoryname) {
        this.categoryId = categoryId;
        this.categoryname = categoryname;
    }
}

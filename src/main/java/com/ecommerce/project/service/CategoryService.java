package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;

import java.util.List;


public interface CategoryService {
    List<Category> getAllCategories();
    void setAllcategories(Category category);


    String deletecategory(long categoryId);

    String updateCategory(Category category, long categoryId);
}

package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service

public class CategoryServiceImpl implements CategoryService{


    private List<Category> categories = new ArrayList<>();
    private int nextid = 1;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void setAllcategories(Category category) {
        category.setCategoryId(nextid++);
        categories.add(category);


    }
    @Override
    public String deletecategory(long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId() == categoryId)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resource not found"));

        categories.remove(category);

        return "Category with categoryId " + categoryId + " deleted successfully";
    }

    @Override
    public String updateCategory(Category category, long categoryId) {
        Category existingCategory = categories.stream()
                .filter(c -> c.getCategoryId()==(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category not found with id " + categoryId));

        existingCategory.setCategoryname(category.getCategoryname());

        return "Category updated successfully";


    }
    // update category










}

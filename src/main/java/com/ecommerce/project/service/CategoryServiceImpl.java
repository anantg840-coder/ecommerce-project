package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.Repositries.CategoryRepositries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepositries categoryRepositries;

    // GET
    @Override
    public List<Category> getAllCategories() {
        return categoryRepositries.findAll();
    }

    // POST
    @Override
    public void setAllcategories(Category category) {
        categoryRepositries.save(category);
    }

    // DELETE
    @Override
    public String deleteCategory(long categoryId) {
        Category category = categoryRepositries.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resource not found"));

        categoryRepositries.delete(category);

        return "Category with CategoryId: " + categoryId + " deleted successfully";





    }

    // PUT
    @Override
    public Category updateCategory(Category category, long categoryId) {

        Category existingCategory = categoryRepositries.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resource not found"));

        category.setCategoryId(categoryId);

        return categoryRepositries.save(category);




    }
}
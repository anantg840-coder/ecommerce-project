package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
@RequestMapping("/api")
@RestController
public class CategoryContoller {

    public CategoryService categoryService;

    public CategoryContoller(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> setAllcategories(@RequestBody Category category){
        categoryService.setAllcategories(category);


        return new ResponseEntity<>("Category added sucessfully", HttpStatus.CREATED);

    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {

        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }


    }


    @PutMapping("/public/categories/{categoryId}")

    public ResponseEntity<Category> updateCategory(@RequestBody Category category,
                                                 @PathVariable long categoryId){

        try {

            Category updatedCategory =
                    categoryService.updateCategory(category, categoryId);

            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

        } catch (ResponseStatusException e) {

            return new ResponseEntity<>(e.getStatusCode());

        }





    }





}




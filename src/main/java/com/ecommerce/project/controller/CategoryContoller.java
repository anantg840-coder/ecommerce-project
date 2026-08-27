package com.ecommerce.project.controller;

import com.ecommerce.project.PayLoad.CategoryDto;
import com.ecommerce.project.PayLoad.CategoryResponse;
import com.ecommerce.project.PayLoad.ProductDTO;
import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class CategoryContoller {

    public CategoryService categoryService;

    public CategoryContoller(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
        @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(name = "sortby", defaultValue = AppConstant.SORT_CATEGORIES_BY,required = false)String sortby,
        @RequestParam(name = "sortOrder", defaultValue = AppConstant.SORT_DIRN,required = false)String sortOrder){
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortby,sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<CategoryDto> setAllcategories(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto savedCategory =  categoryService.setAllcategories(categoryDto);


        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);

    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> deleteCategory(@PathVariable Long categoryId) {

        CategoryDto deleteCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deleteCategory,HttpStatus.OK);




    }
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updatedCategory(@Valid @RequestBody CategoryDto categoryDto,
                                                       @PathVariable Long categoryId){
        CategoryDto savedCategoryDto = categoryService.updateCategory(categoryDto,categoryId);
        return new ResponseEntity<>(savedCategoryDto,HttpStatus.OK);

    }














}




package com.ecommerce.project.service;

import com.ecommerce.project.PayLoad.CategoryDto;
import com.ecommerce.project.PayLoad.CategoryResponse;
import com.ecommerce.project.model.Category;

import java.util.List;


public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize, String sortby, String sortOrder);
    CategoryDto setAllcategories(CategoryDto categoryDto);


    CategoryDto deleteCategory(long categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto, long categoryId);
}






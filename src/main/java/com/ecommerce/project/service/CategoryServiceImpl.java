package com.ecommerce.project.service;

import com.ecommerce.project.Exception.ApiException;
import com.ecommerce.project.Exception.ResourceNotFoundException;
import com.ecommerce.project.PayLoad.CategoryDto;
import com.ecommerce.project.PayLoad.CategoryResponse;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.Repositries.CategoryRepositries;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepositries categoryRepositries;

    @Autowired
    private ModelMapper modelMapper;

    // GET
    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortby, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortby).ascending()
                : Sort.by(sortby).descending();
       Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
       Page<Category> categoryPage = categoryRepositries.findAll(pageable);

        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty())
            throw new ApiException("No Category created till now");
        List<CategoryDto> categoryDtos = categories.stream()
                .map((category -> modelMapper.map(category, CategoryDto.class)))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDtos);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());


        return categoryResponse;
    }

    // POST
    @Override
    public CategoryDto setAllcategories(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);

        // FIX: Force ID to null so Hibernate knows this is a brand-new insert, not an update
        category.setCategoryId(0);

        Category savedCategorybydatabase = categoryRepositries.findByCategoryname(category.getCategoryname());
        if (savedCategorybydatabase != null)
            throw new ApiException("Category with this name " + category.getCategoryname() + " already exist !!!  ");

        Category savedCategory = categoryRepositries.save(category);
        CategoryDto categoryDto1 = modelMapper.map(savedCategory, CategoryDto.class);
        return categoryDto1;
    }

    // DELETE
    @Override
    public CategoryDto deleteCategory(long categoryId) {
        Category category = categoryRepositries.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryid", categoryId));

        categoryRepositries.delete(category);

        return modelMapper.map(category,CategoryDto.class);


    }

    // PUT
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, long categoryId) {


        Category existingCategory = categoryRepositries.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryid", categoryId));

        // update existing category
        existingCategory.setCategoryname(categoryDto.getCategoryname());
        // saving existing category
        Category updatedCategory1 = categoryRepositries.save(existingCategory);

        CategoryDto categoryDto1 = modelMapper.map(updatedCategory1, CategoryDto.class);
        return categoryDto1;




    }

}

// custom 

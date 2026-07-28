package com.ecommerce.project.Repositries;

import com.ecommerce.project.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepositries extends JpaRepository<Category,Long> {

    Category findByCategoryname(@NotBlank @Size(min = 5, message = "Category name must contain atleast 5 characters") String categoryname);
}

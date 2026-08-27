package com.ecommerce.project.Repositries;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositories extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Pageable pageDetails, Category category);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}

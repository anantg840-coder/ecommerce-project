package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product Name Must contain atleast 3 characters")
    private String productName;
    private Integer quantity;
    private String image;
    private double price;
    @Size(min = 6, message = "Product Description Must contain atleast 6 characters")
    private String description;
    private double specialPrice;
    private double discounts;

    @ManyToOne
    @JoinColumn(name = "category_id" )
    private Category category;


}

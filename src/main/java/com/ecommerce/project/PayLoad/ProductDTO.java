package com.ecommerce.project.PayLoad;

import com.ecommerce.project.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {


    private Long productId;
    private String productName;
    private Integer quantity;
    private String image;
    private double price;
    private String description;
    private double specialPrice;
    private double discounts;


}

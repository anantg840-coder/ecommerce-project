package com.ecommerce.project.PayLoad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @Schema(description = "Category Name you wish to create", example = "CatHouse")
    private String categoryname;
    @Schema(description = "Cartegory Id",example = "45")
    private Long categoryId;

}

package com.ecommerce.project.controller;

import com.ecommerce.project.PayLoad.ProductDTO;
import com.ecommerce.project.PayLoad.ProductResponse;
import com.ecommerce.project.config.AppConstant;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
    @PathVariable Long categoryId ){
        ProductDTO savedproductDTO = productService.addProduct(productDTO,categoryId);
        return new ResponseEntity<>(savedproductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllproducts(
            @RequestParam(name = "keyword",required = false) String keyword,
            @RequestParam(name = "category",required = false) String category,
            @RequestParam(name = "pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstant.SORT_PRODUCT_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstant.SORT_DIRN,required = false) String sortOrder
    ){
        ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder,keyword,category);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy",defaultValue = AppConstant.SORT_PRODUCT_BY,required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder",defaultValue = AppConstant.SORT_DIRN,required = false) String sortOrder){
        ProductResponse productResponse = productService.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeywords(@PathVariable String keyword,
                                                                 @RequestParam(name = "pageNumber",defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize",defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy",defaultValue = AppConstant.SORT_PRODUCT_BY,required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder",defaultValue = AppConstant.SORT_DIRN,required = false) String sortOrder){
        ProductResponse productResponse = productService.searchProductByKeywords(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);

    }

    @PutMapping("/admin/product/productId/{productId}")
    public ResponseEntity<ProductDTO> updateProduct( @Valid @RequestBody ProductDTO productDTO
    ,@PathVariable Long productId){
        ProductDTO updatedproductDTO = productService.updateProduct(productDTO,productId);
        return new ResponseEntity<>(updatedproductDTO,HttpStatus.OK);
    }

    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);


    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO updatedProductImage = productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedProductImage,HttpStatus.OK);


    }
}

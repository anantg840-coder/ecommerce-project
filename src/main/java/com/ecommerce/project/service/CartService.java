package com.ecommerce.project.service;

import com.ecommerce.project.PayLoad.CartDTO;
import com.ecommerce.project.PayLoad.ProductDTO;
import jakarta.transaction.Transactional;

import java.util.List;


public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCartById(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);


    void updateProductInCarts(Long cartId, Long productId);
}

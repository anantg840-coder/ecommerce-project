package com.ecommerce.project.service;

import com.ecommerce.project.Exception.ApiException;
import com.ecommerce.project.Exception.ResourceNotFoundException;
import com.ecommerce.project.PayLoad.CartDTO;
import com.ecommerce.project.PayLoad.ProductDTO;
import com.ecommerce.project.Repositries.CartIItemsRepository;
import com.ecommerce.project.Repositries.CartRepository;
import com.ecommerce.project.Repositries.ProductRepositories;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepositories productRepositories;

    @Autowired
    CartIItemsRepository cartIItemsRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepositories.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
        CartItem cartItem = cartIItemsRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
        if(cartItem != null){
            throw new ApiException("product " + product.getProductName() + " is already exists");
        }
        if(product.getQuantity() == 0){
            throw new ApiException(product.getProductName() + " is not available");
        }
        if(product.getQuantity() < quantity){
            throw new ApiException(product.getProductName() + "please make sure quatity is less than or equal to " +  product.getQuantity());
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscounts());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartIItemsRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + product.getSpecialPrice()*quantity);
        cartRepository.save(cart);
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item ->{
            ProductDTO map = modelMapper.map(item.getProduct(),ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        } );
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;

    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new ApiException("cart is empty");
        }
        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
                    List<ProductDTO> productDTOS = cart.getCartItems().stream()
                            .map(p -> modelMapper.map(p.getProduct(),ProductDTO.class))
                            .collect(Collectors.toList());
                    cartDTO.setProducts(productDTOS);
                    return cartDTO;

                }).collect(Collectors.toList());
        return cartDTOS;

    }

    @Override
    public CartDTO getCartById(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId,cartId);
        if(cart == null){
            throw new ResourceNotFoundException("Cart","email",emailId);
        }

        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream()
                .map(p -> modelMapper.map(
                        p.getProduct(),ProductDTO.class
                )).toList();
        cartDTO.setProducts(products);

        return cartDTO;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtil.loggedInEmail();
        Cart usercart = cartRepository.findCartByEmail(email);
        Long cartId = usercart.getCartId();
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->
                new ResourceNotFoundException("Cart", "cartId",cartId));
        Product product = productRepositories.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        if(product.getQuantity() == 0){
            throw new ApiException(product.getProductName() + " is not available");
        }
        if(product.getQuantity() < quantity){
            throw new ApiException(product.getProductName() + "please make sure quatity is less than or equal to " +  product.getQuantity());
        }
        CartItem cartItem = cartIItemsRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
        if(cartItem == null){
            throw new ApiException("Product " + product.getProductName() + " is not available in the cart");
        }
        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getDiscounts());
        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getProductPrice()*quantity);
        cartRepository.save(cart);
        CartItem updatedItem = cartIItemsRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartIItemsRepository.deleteById(updatedItem.getCartItemId());
        }
        CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream()
                .map(item -> {
                    ProductDTO prd = modelMapper.map(item.getProduct(),ProductDTO.class);
                    prd.setQuantity(item.getQuantity());
                    return prd;
                });
        cartDTO.setProducts(productDTOStream.toList());
        return cartDTO;

    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart","cartId",cartId)
        );
        CartItem cartItem = cartIItemsRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem == null){
            throw new ResourceNotFoundException("Product", "productId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getProductPrice()*cartItem.getQuantity());
        cartIItemsRepository.deleteCartItemByProductIdAndCartId(cartId,productId);
        return "product" + cartItem.getProduct().getProductName() + " remoed sucessfully !!!";


    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart","cartId",cartId)
        );
        Product product = productRepositories.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product","productId",productId)
        );
        CartItem cartItem = cartIItemsRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem == null){
            throw new ApiException("Product " + product.getProductName() + " is not available in the cart!!!");
        }
        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice()*cartItem.getQuantity());
        cartItem.setProductPrice(product.getSpecialPrice());
        cart.setTotalPrice(cart.getTotalPrice() +  (cartItem.getProductPrice()*cartItem.getQuantity()));
        cartItem = cartIItemsRepository.save(cartItem);

    }


    private Cart createCart(){
        Cart usercart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
            if(usercart != null){
                return usercart;
            }
            Cart cart = new Cart();
            cart.setTotalPrice(0.00);
            cart.setUser(authUtil.loggedInUser());
            return cartRepository.save(cart);
    }
}

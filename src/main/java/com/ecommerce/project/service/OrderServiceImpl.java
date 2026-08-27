package com.ecommerce.project.service;

import com.ecommerce.project.Exception.ApiException;
import com.ecommerce.project.Exception.ResourceNotFoundException;
import com.ecommerce.project.PayLoad.OrderDto;
import com.ecommerce.project.PayLoad.OrderItemDTO;
import com.ecommerce.project.Repositries.*;
import com.ecommerce.project.model.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepositories productRepositories;

    @Autowired
    CartService cartService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDto placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        Cart cart = cartRepository.findCartByEmail(emailId);
        if(cart == null){
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->new ResourceNotFoundException("Address", "addressId",addressId));

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalOrderAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setAddress(address);
        Payment payment = new Payment(pgName,pgResponseMessage,pgStatus,pgPaymentId,paymentMethod);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()){
            throw  new ApiException("Cart is empty");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setDiscount(item.getDiscount());
            orderItem.setOrderTotalPrice(item.getProductPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        orderItems = orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(cartItem -> {
            int quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - quantity);
            productRepositories.save(product);
            cartService.deleteProductFromCart(cart.getCartId(),cartItem.getProduct().getProductId());

        });
        OrderDto orderDto = modelMapper.map(savedOrder, OrderDto.class);
        orderItems.forEach(orderItem -> {
            orderDto.getOrderItems().add(
                    modelMapper.map(orderItem, OrderItemDTO.class));
        });
        orderDto.setAddressId(addressId);
        return orderDto;


        






    }
}

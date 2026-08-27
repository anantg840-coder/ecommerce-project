package com.ecommerce.project.controller;

import com.ecommerce.project.PayLoad.OrderDto;
import com.ecommerce.project.PayLoad.OrderRequestDTO;
import com.ecommerce.project.service.OrderService;
import com.ecommerce.project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDto> orderProduct(
            @PathVariable String paymentMethod,
            @RequestBody OrderRequestDTO orderRequestDTO
            ){
        String emailId = authUtil.loggedInEmail();
        OrderDto orderDto = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()

        );
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }
}

package com.ecommerce.project.Repositries;

import com.ecommerce.project.model.Order;
import com.ecommerce.project.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}

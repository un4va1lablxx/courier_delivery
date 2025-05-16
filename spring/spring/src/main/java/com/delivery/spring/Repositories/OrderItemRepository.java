package com.delivery.spring.Repositories;

import com.delivery.spring.Models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> { }
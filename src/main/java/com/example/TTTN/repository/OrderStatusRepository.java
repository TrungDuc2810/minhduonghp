package com.example.TTTN.repository;

import com.example.TTTN.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    OrderStatus findByName(String orderStatusName);
}

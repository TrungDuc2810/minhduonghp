package com.example.TTTN.repository;

import com.example.TTTN.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByPartnerId(long partnerId, Pageable pageable);
}

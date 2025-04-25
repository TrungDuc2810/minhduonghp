package com.example.TTTN.repository;

import com.example.TTTN.entity.InternalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternalOrderRepository extends JpaRepository<InternalOrder, Long> {
}

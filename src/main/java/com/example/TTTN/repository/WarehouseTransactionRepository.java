package com.example.TTTN.repository;

import com.example.TTTN.entity.WarehouseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseTransactionRepository extends JpaRepository<WarehouseTransaction, Long> {
}

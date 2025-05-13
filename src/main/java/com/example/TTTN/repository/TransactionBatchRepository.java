package com.example.TTTN.repository;

import com.example.TTTN.entity.TransactionBatch;
import com.example.TTTN.entity.WarehouseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionBatchRepository extends JpaRepository<TransactionBatch, Long> {
    List<TransactionBatch> findByWarehouseTransaction(WarehouseTransaction transaction);
}

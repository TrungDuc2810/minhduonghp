package com.example.TTTN.repository;

import com.example.TTTN.entity.ImportBatch;
import com.example.TTTN.entity.Product;
import com.example.TTTN.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportBatchRepository extends JpaRepository<ImportBatch, Long> {
    List<ImportBatch> findByProductAndWarehouseOrderByImportDateAsc(Product product, Warehouse warehouse);
    List<ImportBatch> findByProductAndWarehouseOrderByExpireDateAsc(Product product, Warehouse warehouse);
}

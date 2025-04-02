package com.example.TTTN.repository;

import com.example.TTTN.entity.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {
    WarehouseProduct findByWarehouseIdAndProductId(Long warehouseId, Long productId);
}

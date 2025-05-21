package com.example.TTTN.repository;

import com.example.TTTN.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findProductsByProductTypeId(long productTypeId, Pageable pageable);
    Boolean existsByName(String productName);
}

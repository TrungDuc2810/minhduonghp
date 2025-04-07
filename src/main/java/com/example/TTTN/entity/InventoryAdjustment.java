package com.example.TTTN.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory_adjustments")
public class InventoryAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int quantity;
    private String createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_type_id", nullable = false)
    private InventoryAdjustmentType inventoryAdjustmentType;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now().toString();
    }
}

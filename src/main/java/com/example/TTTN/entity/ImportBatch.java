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
@Table(name = "import_batches")
public class ImportBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private int importQuantity;
    @Column(nullable = false)
    private int remainQuantity;
    @Column(nullable = false)
    private double unitCost;
    @Column(nullable = false)
    private LocalDateTime importDate;
    private LocalDateTime expireDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_transaction_id")
    private WarehouseTransaction warehouseTransaction;
    @PrePersist
    protected void onCreate() {
        this.importDate = LocalDateTime.now();
    }
}

package com.example.TTTN.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction_batches")
public class TransactionBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "warehouse_transaction_id")
    private WarehouseTransaction warehouseTransaction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "import_batch_id")
    private ImportBatch importBatch;

    @Column(nullable = false)
    private int quantityDeducted;
}

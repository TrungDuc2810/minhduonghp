package com.example.TTTN.payload;

import com.example.TTTN.entity.InventoryAdjustmentType;
import com.example.TTTN.entity.Product;
import com.example.TTTN.entity.Warehouse;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAdjustmentDto {
    private long id;
    private int quantity;
    private String createdAt;
    private long productId;
    private long warehouseId;
    private long inventoryAdjustmentTypeId;
}

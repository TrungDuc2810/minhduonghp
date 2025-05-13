package com.example.TTTN.payload;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ImportBatchDto {
    private long id;
    private int quantityRemaining;
    private double unitCost;
    private LocalDateTime importDate;
    private long productId;
    private long warehouseId;
}

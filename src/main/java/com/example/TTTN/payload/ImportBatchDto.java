package com.example.TTTN.payload;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ImportBatchDto {
    private long id;
    private int remainQuantity;
    private int importQuantity;
    private double unitCost;
    private LocalDateTime importDate;
    private LocalDateTime expireDate;
    private long productId;
    private long warehouseId;
    private long warehouseTransactionId;
}

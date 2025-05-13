package com.example.TTTN.payload;

import lombok.Data;

@Data
public class TransactionBatchDto {
    private long id;
    private long warehouseTransactionId;
    private long importBatchId;
    private int quantityDeducted;
}

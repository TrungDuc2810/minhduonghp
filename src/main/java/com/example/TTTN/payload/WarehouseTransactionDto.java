package com.example.TTTN.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseTransactionDto {
    private long id;
    private String createdAt;
    private long orderId;
    private long warehouseId;
    private long statusId;
    private long transactionTypeId;
}

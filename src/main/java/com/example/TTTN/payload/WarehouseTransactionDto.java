package com.example.TTTN.payload;

import com.example.TTTN.entity.Order;
import com.example.TTTN.entity.Status;
import com.example.TTTN.entity.Warehouse;
import com.example.TTTN.entity.WarehouseTransactionType;
import jakarta.persistence.*;
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
    private String createdBy;
    private String participant;
    private String storekeeper;
    private String accountant;
}

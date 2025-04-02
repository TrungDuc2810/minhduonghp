package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransactionDto;

public interface WarehouseTransactionService {
    WarehouseTransactionDto createWarehouseTransaction(WarehouseTransactionDto warehouseTransactionDto);
    WarehouseTransactionDto updateWarehouseTransaction(long id, WarehouseTransactionDto warehouseTransactionDto);
    ListResponse<WarehouseTransactionDto> getAllWarehouseTransactions(int pageNo, int pageSize, String sortBy, String sortDir);
    WarehouseTransactionDto getWarehouseTransaction(long id);
    void deleteWarehouseTransaction(long id);
}

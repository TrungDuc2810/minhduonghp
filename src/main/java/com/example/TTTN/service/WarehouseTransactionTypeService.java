package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransactionTypeDto;

public interface WarehouseTransactionTypeService {
    WarehouseTransactionTypeDto getWarehouseTransactionTypeById(long id);
    ListResponse<WarehouseTransactionTypeDto> getAllWarehouseTransactionTypes(int pageNo, int pageSize, String sortBy, String sortDir);
}

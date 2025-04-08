package com.example.TTTN.service;

import com.example.TTTN.payload.InventoryAdjustmentTypeDto;
import com.example.TTTN.payload.ListResponse;

public interface InventoryAdjustmentTypeService {
    InventoryAdjustmentTypeDto getInventoryAdjustmentTypeById(long id);
    ListResponse<InventoryAdjustmentTypeDto> getAllInventoryAdjustmentTypes(int pageNo, int pageSize, String sortBy, String sortDir);
}

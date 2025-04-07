package com.example.TTTN.service;

import com.example.TTTN.payload.InventoryAdjustmentDto;
import com.example.TTTN.payload.ListResponse;

public interface InventoryAdjustmentService {
    InventoryAdjustmentDto createInventoryAdjustment(InventoryAdjustmentDto inventoryAdjustmentDto);
    InventoryAdjustmentDto updateInventoryAdjustment(long id, InventoryAdjustmentDto inventoryAdjustmentDto);
    InventoryAdjustmentDto getInventoryAdjustment(long id);
    ListResponse<InventoryAdjustmentDto> getAllInventoryAdjustments(int pageNo, int pageSize, String sortBy, String sortDir);
    void deleteInventoryAdjustment(long id);
}

package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransferDto;

public interface WarehouseTransferService {
    WarehouseTransferDto createWarehouseTransfer(WarehouseTransferDto warehouseTransferDto);
    WarehouseTransferDto updateWarehouseTransfer(long id, WarehouseTransferDto warehouseTransferDto);
    ListResponse<WarehouseTransferDto> getAllWarehouseTransfers(int pageNo, int pageSize, String sortBy, String sortDir);
    WarehouseTransferDto getWarehouseTransferById(long id);
    void deleteWarehouseTransferById(long id);
}

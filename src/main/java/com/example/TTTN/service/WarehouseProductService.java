package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseProductDto;

public interface WarehouseProductService {
    WarehouseProductDto createWarehouseProduct(WarehouseProductDto warehouseProductDto);
    WarehouseProductDto updateWarehouseProduct(WarehouseProductDto warehouseProductDto, long id);
    ListResponse<WarehouseProductDto> getAllWarehouseProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    WarehouseProductDto getWarehouseProductById(long id);
    void deleteWarehouseProductById(long id);
}

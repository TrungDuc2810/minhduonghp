package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductUnitDto;

public interface ProductUnitService {
    ProductUnitDto getProductUnit(long productUnitId);
    ListResponse<ProductUnitDto> getAllProductUnits(int pageNo, int pageSize, String sortBy, String sortDir);
}

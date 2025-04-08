package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductTypeDto;

public interface ProductTypeService {
    ProductTypeDto getProductTypeById(long id);
    ListResponse<ProductTypeDto> getAllProductTypes(int pageNo, int pageSize, String sortBy, String sortDir);
}

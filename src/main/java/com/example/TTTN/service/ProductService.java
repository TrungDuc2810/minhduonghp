package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductDto;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);
    ListResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductDto getProductById(long id);
    ProductDto updateProduct(ProductDto productDto, long id);
    void deleteProductById(long id);
}

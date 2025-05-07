package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductDto;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);
    ListResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ListResponse<ProductDto> getProductsByProductTypeId(int pageNo, int pageSize, String sortBy, String sortDir, long productTypeId);
    ProductDto getProductById(long id);
    ProductDto updateProduct(long id, ProductDto productDto);
    void deleteProductById(long id);
}

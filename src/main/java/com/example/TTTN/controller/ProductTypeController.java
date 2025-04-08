package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PartnerTypeDto;
import com.example.TTTN.payload.ProductTypeDto;
import com.example.TTTN.service.ProductTypeService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-types")
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeDto> getProductTypeById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(productTypeService.getProductTypeById(id));
    }

    @GetMapping
    public ListResponse<ProductTypeDto> getAllProductTypes(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return productTypeService.getAllProductTypes(pageNo, pageSize, sortBy, sortDir);
    }
}

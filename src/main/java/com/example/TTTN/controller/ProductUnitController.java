package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductTypeDto;
import com.example.TTTN.payload.ProductUnitDto;
import com.example.TTTN.service.ProductUnitService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-units")
public class ProductUnitController {
    private final ProductUnitService productUnitService;

    public ProductUnitController(ProductUnitService productUnitService) {
        this.productUnitService = productUnitService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductUnitDto> getProductUnit(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(productUnitService.getProductUnit(id));
    }

    @GetMapping
    public ListResponse<ProductUnitDto> getProductUnits(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return productUnitService.getAllProductUnits(pageNo, pageSize, sortBy, sortDir);
    }
}

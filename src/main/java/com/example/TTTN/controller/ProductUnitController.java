package com.example.TTTN.controller;

import com.example.TTTN.payload.ProductUnitDto;
import com.example.TTTN.service.ProductUnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

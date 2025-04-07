package com.example.TTTN.controller;

import com.example.TTTN.payload.WarehouseTransactionTypeDto;
import com.example.TTTN.service.WarehouseTransactionTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/warehouse-transaction-types")
public class WarehouseTransactionTypeController {
    private final WarehouseTransactionTypeService warehouseTransactionTypeService;

    public WarehouseTransactionTypeController(WarehouseTransactionTypeService warehouseTransactionTypeService) {
        this.warehouseTransactionTypeService = warehouseTransactionTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseTransactionTypeDto> getWarehouseTransactionTypeById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(warehouseTransactionTypeService.getWarehouseTransactionTypeById(id));
    }
}

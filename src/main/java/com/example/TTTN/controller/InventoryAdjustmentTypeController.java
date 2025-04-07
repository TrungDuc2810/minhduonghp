package com.example.TTTN.controller;

import com.example.TTTN.payload.InventoryAdjustmentTypeDto;
import com.example.TTTN.service.InventoryAdjustmentTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory-adjustment-types")
public class InventoryAdjustmentTypeController {
    private final InventoryAdjustmentTypeService inventoryAdjustmentTypeService;

    public InventoryAdjustmentTypeController(InventoryAdjustmentTypeService inventoryAdjustmentTypeService) {
        this.inventoryAdjustmentTypeService = inventoryAdjustmentTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryAdjustmentTypeDto> getInventoryAdjustmentType(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(inventoryAdjustmentTypeService.getInventoryAdjustmentTypeById(id));
    }
}

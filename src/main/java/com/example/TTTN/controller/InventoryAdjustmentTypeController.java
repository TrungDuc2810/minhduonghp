package com.example.TTTN.controller;

import com.example.TTTN.payload.InventoryAdjustmentTypeDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.service.InventoryAdjustmentTypeService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ListResponse<InventoryAdjustmentTypeDto> getAllInventoryAdjustmentTypes(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return inventoryAdjustmentTypeService.getAllInventoryAdjustmentTypes(pageNo, pageSize, sortBy, sortDir);
    }
}

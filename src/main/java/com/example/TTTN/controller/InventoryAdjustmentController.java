package com.example.TTTN.controller;

import com.example.TTTN.payload.InventoryAdjustmentDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.service.InventoryAdjustmentService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-adjustments")
public class InventoryAdjustmentController {
    private final InventoryAdjustmentService inventoryAdjustmentService;

    public InventoryAdjustmentController(InventoryAdjustmentService inventoryAdjustmentService) {
        this.inventoryAdjustmentService = inventoryAdjustmentService;
    }

    @PostMapping
    public ResponseEntity<InventoryAdjustmentDto> createInventoryAdjustment(@RequestBody InventoryAdjustmentDto inventoryAdjustmentDto) {
        return ResponseEntity.ok(inventoryAdjustmentService.createInventoryAdjustment(inventoryAdjustmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryAdjustmentDto> updateInventoryAdjustment(@PathVariable(name = "id") long id,
                                                                            @RequestBody InventoryAdjustmentDto inventoryAdjustmentDto) {
        return ResponseEntity.ok(inventoryAdjustmentService.updateInventoryAdjustment(id, inventoryAdjustmentDto));
    }

    @GetMapping
    public ListResponse<InventoryAdjustmentDto> getAllInventoryAdjustments(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return inventoryAdjustmentService.getAllInventoryAdjustments(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryAdjustmentDto> getInventoryAdjustment(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(inventoryAdjustmentService.getInventoryAdjustment(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventoryAdjustment(@PathVariable(name = "id") long id) {
        inventoryAdjustmentService.deleteInventoryAdjustment(id);
        return new ResponseEntity<>("Delete successfully!!!", HttpStatus.OK);
    }
}

package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.StatusDto;
import com.example.TTTN.payload.WarehouseTransactionTypeDto;
import com.example.TTTN.service.WarehouseTransactionTypeService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ListResponse<WarehouseTransactionTypeDto> getAllWarehouseTransactionTypes(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return warehouseTransactionTypeService.getAllWarehouseTransactionTypes(pageNo, pageSize, sortBy, sortDir);
    }
}

package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransactionDto;
import com.example.TTTN.service.WarehouseTransactionService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse-transactions")
public class WarehouseTransactionController {
    private final WarehouseTransactionService warehouseTransactionService;

    public WarehouseTransactionController(WarehouseTransactionService warehouseTransactionService) {
        this.warehouseTransactionService = warehouseTransactionService;
    }

    @PostMapping
    public ResponseEntity<WarehouseTransactionDto> createWarehouseTransaction(@RequestBody WarehouseTransactionDto warehouseTransactionDto) {
        return new ResponseEntity<>(warehouseTransactionService.createWarehouseTransaction(warehouseTransactionDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseTransactionDto> updateWarehouseTransaction(@PathVariable(name = "id") long id,
                                                                              @RequestBody WarehouseTransactionDto warehouseTransactionDto) {
        return new ResponseEntity<>(warehouseTransactionService.updateWarehouseTransaction(id, warehouseTransactionDto), HttpStatus.OK);
    }

    @GetMapping
    public ListResponse<WarehouseTransactionDto> getAllWarehouseTransactions(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return warehouseTransactionService.getAllWarehouseTransactions(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseTransactionDto> getWarehouseTransactionById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(warehouseTransactionService.getWarehouseTransaction(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouseTransaction(@PathVariable(name = "id") long id) {
        warehouseTransactionService.deleteWarehouseTransaction(id);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }
}

package com.example.TTTN.controller;

import com.example.TTTN.payload.WarehouseTransactionDto;
import com.example.TTTN.service.WarehouseTransactionService;
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
}

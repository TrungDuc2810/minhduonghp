package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransferDto;
import com.example.TTTN.service.WarehouseTransferService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse-transfers")
public class WarehouseTransferController {
    private final WarehouseTransferService warehouseTransferService;

    public WarehouseTransferController(WarehouseTransferService warehouseTransferService) {
        this.warehouseTransferService = warehouseTransferService;
    }

    @PostMapping
    public ResponseEntity<WarehouseTransferDto> createWarehouseTransfer(@RequestBody WarehouseTransferDto warehouseTransferDto) {
        return new ResponseEntity<>(warehouseTransferService.createWarehouseTransfer(warehouseTransferDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseTransferDto> updateWarehouseTransfer(@PathVariable(name = "id") long id,
                                                                        @RequestBody WarehouseTransferDto warehouseTransferDto) {
        return new ResponseEntity<>(warehouseTransferService.updateWarehouseTransfer(id, warehouseTransferDto), HttpStatus.OK);
    }

    @GetMapping
    public ListResponse<WarehouseTransferDto> getAllWarehouseTransfers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return warehouseTransferService.getAllWarehouseTransfers(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseTransferDto> getWarehouseTransfer(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(warehouseTransferService.getWarehouseTransferById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouseTransfer(@PathVariable(name = "id") long id) {
        warehouseTransferService.deleteWarehouseTransferById(id);
        return new ResponseEntity<>("Deleted successfully!!!", HttpStatus.OK);
    }
}

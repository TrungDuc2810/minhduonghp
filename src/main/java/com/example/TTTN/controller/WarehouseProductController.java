package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseProductDto;
import com.example.TTTN.service.WarehouseProductService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse-products")
public class WarehouseProductController {
    private final WarehouseProductService warehouseProductService;

    public WarehouseProductController(WarehouseProductService warehouseProductService) {
        this.warehouseProductService = warehouseProductService;
    }

    @PreAuthorize("hasRole('ADMIN_K')")
    @PostMapping
    public ResponseEntity<WarehouseProductDto> addProduct(@RequestBody WarehouseProductDto warehouseProductDto) {
        return new ResponseEntity<>(warehouseProductService.createWarehouseProduct(warehouseProductDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ListResponse<WarehouseProductDto> getAllWarehouseProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return warehouseProductService.getAllWarehouseProducts(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseProductDto> getWarehouseProductById(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(warehouseProductService.getWarehouseProductById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_K')")
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseProductDto> updateProduct(@PathVariable(name = "id") long id,
                                                    @RequestBody WarehouseProductDto warehouseProductDto) {
        return new ResponseEntity<>(warehouseProductService.updateWarehouseProduct(warehouseProductDto, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_K')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouseProductById(@PathVariable(name = "id") long id) {
        warehouseProductService.deleteWarehouseProductById(id);
        return new ResponseEntity<>("Warehouse product deleted successfully", HttpStatus.OK);
    }


}

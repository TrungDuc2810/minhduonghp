package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.InternalOrderDto;
import com.example.TTTN.service.InternalOrderService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse-transfers")
public class WarehouseTransferController {
    private final InternalOrderService internalOrderService;

    public WarehouseTransferController(InternalOrderService internalOrderService) {
        this.internalOrderService = internalOrderService;
    }

    @PostMapping
    public ResponseEntity<InternalOrderDto> createWarehouseTransfer(@RequestBody InternalOrderDto internalOrderDto) {
        return new ResponseEntity<>(internalOrderService.createInternalOrder(internalOrderDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternalOrderDto> updateWarehouseTransfer(@PathVariable(name = "id") long id,
                                                                    @RequestBody InternalOrderDto internalOrderDto) {
        return new ResponseEntity<>(internalOrderService.updateInternalOrder(id, internalOrderDto), HttpStatus.OK);
    }

    @GetMapping
    public ListResponse<InternalOrderDto> getAllWarehouseTransfers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return internalOrderService.getAllInternalOrders(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternalOrderDto> getWarehouseTransfer(@PathVariable(name = "id") long id) {
        return new ResponseEntity<>(internalOrderService.getInternalOrderById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWarehouseTransfer(@PathVariable(name = "id") long id) {
        internalOrderService.deleteInternalOrderById(id);
        return new ResponseEntity<>("Deleted successfully!!!", HttpStatus.OK);
    }
}

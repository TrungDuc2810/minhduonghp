package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderStatusDto;
import com.example.TTTN.payload.OrderTypeDto;
import com.example.TTTN.service.OrderTypeService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-types")
public class OrderTypeController {
    private final OrderTypeService orderTypeService;

    public OrderTypeController(OrderTypeService orderTypeService) {
        this.orderTypeService = orderTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderTypeDto> getOrderTypeById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(orderTypeService.getOrderTypeById(id));
    }

    @GetMapping
    public ListResponse<OrderTypeDto> getAllOrderTypes(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return orderTypeService.getAllOrderTypes(pageNo, pageSize, sortBy, sortDir);
    }
}

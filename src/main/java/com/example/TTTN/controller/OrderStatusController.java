package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderStatusDto;
import com.example.TTTN.service.OrderStatusService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-status")
public class OrderStatusController {
    private final OrderStatusService orderStatusService;

    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusDto> getOrderStatusById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(orderStatusService.getOrderStatusById(id));
    }

    @GetMapping
    public ListResponse<OrderStatusDto> getAllOrderStatus(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return orderStatusService.getAllOrderStatus(pageNo, pageSize, sortBy, sortDir);
    }
}

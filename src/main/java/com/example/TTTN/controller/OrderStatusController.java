package com.example.TTTN.controller;

import com.example.TTTN.payload.OrderStatusDto;
import com.example.TTTN.service.OrderStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

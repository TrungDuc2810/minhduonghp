package com.example.TTTN.controller;

import com.example.TTTN.payload.OrderTypeDto;
import com.example.TTTN.service.OrderTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

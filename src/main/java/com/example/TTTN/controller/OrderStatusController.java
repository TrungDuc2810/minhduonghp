package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.OrderStatusDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-status")
public class OrderStatusController extends GenericController<OrderStatusDto> {

    public OrderStatusController(GenericService<OrderStatusDto> service) {
        super(service);
    }
}

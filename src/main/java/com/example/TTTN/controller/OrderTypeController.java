package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.OrderTypeDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-types")
public class OrderTypeController extends GenericController<OrderTypeDto> {

    public OrderTypeController(GenericService<OrderTypeDto> service) {
        super(service);
    }
}

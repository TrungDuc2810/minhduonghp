package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.WarehouseDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController extends GenericController<WarehouseDto> {

    public WarehouseController(GenericService<WarehouseDto> service) {
        super(service);
    }
}

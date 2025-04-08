package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.InventoryAdjustmentTypeDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory-adjustment-types")
public class InventoryAdjustmentTypeController extends GenericController<InventoryAdjustmentTypeDto> {

    public InventoryAdjustmentTypeController(GenericService<InventoryAdjustmentTypeDto> service) {
        super(service);
    }
}

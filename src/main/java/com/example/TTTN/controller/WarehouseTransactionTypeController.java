package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransactionTypeDto;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse-transaction-types")
public class WarehouseTransactionTypeController extends GenericController<WarehouseTransactionTypeDto> {

    public WarehouseTransactionTypeController(GenericService<WarehouseTransactionTypeDto> service) {
        super(service);
    }
}

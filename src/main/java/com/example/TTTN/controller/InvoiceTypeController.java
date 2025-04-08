package com.example.TTTN.controller;

import com.example.TTTN.controller.common.GenericController;
import com.example.TTTN.payload.InvoiceTypeDto;
import com.example.TTTN.service.common.GenericService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice-types")
public class InvoiceTypeController extends GenericController<InvoiceTypeDto> {

    public InvoiceTypeController(GenericService<InvoiceTypeDto> service) {
        super(service);
    }
}

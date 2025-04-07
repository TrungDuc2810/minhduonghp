package com.example.TTTN.controller;

import com.example.TTTN.payload.InvoiceTypeDto;
import com.example.TTTN.service.InvoiceTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice-types")
public class InvoiceTypeController {
    private final InvoiceTypeService invoiceTypeService;

    public InvoiceTypeController(InvoiceTypeService invoiceTypeService) {
        this.invoiceTypeService = invoiceTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceTypeDto> getInvoiceTypeById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(invoiceTypeService.getInvoiceTypeById(id));
    }
}

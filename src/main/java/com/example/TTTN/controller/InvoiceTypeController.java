package com.example.TTTN.controller;

import com.example.TTTN.payload.InventoryAdjustmentTypeDto;
import com.example.TTTN.payload.InvoiceTypeDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.service.InvoiceTypeService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ListResponse<InvoiceTypeDto> getAllInvoiceTypes(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return invoiceTypeService.getAllInvoiceTypes(pageNo, pageSize, sortBy, sortDir);
    }
}

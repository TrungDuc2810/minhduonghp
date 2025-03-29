package com.example.TTTN.controller;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.InvoiceDto;
import com.example.TTTN.service.InvoiceService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @PostMapping
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return new ResponseEntity<>(invoiceService.createInvoice(invoiceDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ListResponse<InvoiceDto> getAllInvoices(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return invoiceService.getAllInvoices(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable(name = "id") long invoiceId) {
        return new ResponseEntity<>(invoiceService.getInvoiceById(invoiceId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<InvoiceDto> updateInvoice(@PathVariable(name = "id") long invoiceId,
                                                    @RequestBody InvoiceDto invoiceDto) {
        return new ResponseEntity<>(invoiceService.updateInvoice(invoiceId, invoiceDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_KD')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteInvoiceById(@PathVariable(name = "id") long invoiceId) {
        invoiceService.deleteInvoiceById(invoiceId);
        return new ResponseEntity<>("Delete successfully!!!", HttpStatus.OK);
    }
}

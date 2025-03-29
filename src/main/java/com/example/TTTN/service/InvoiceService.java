package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.InvoiceDto;

public interface InvoiceService {
    InvoiceDto createInvoice(InvoiceDto invoiceDto);
    InvoiceDto getInvoiceById(long invoiceId);
    ListResponse<InvoiceDto> getAllInvoices(int pageNo, int pageSize, String sortBy, String sortDir);
    ListResponse<InvoiceDto> getInvoicesByPartnerId(long partnerId, int pageNo, int pageSize, String sortBy, String sortDir);
    ListResponse<InvoiceDto> getInvoicesByInvoiceTypeId(long invoiceTypeId, int pageNo, int pageSize, String sortBy, String sortDir);
    InvoiceDto updateInvoice(long invoiceId, InvoiceDto invoiceDto);
    void deleteInvoiceById(long invoiceId);
}

package com.example.TTTN.service;

import com.example.TTTN.payload.InvoiceTypeDto;
import com.example.TTTN.payload.ListResponse;

public interface InvoiceTypeService {
    InvoiceTypeDto getInvoiceTypeById(long id);
    ListResponse<InvoiceTypeDto> getAllInvoiceTypes(int pageNo, int pageSize, String sortBy, String sortDir);
}

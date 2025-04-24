package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.InternalOrderDto;

public interface InternalOrderService {
    InternalOrderDto createInternalOrder(InternalOrderDto internalOrderDto);
    InternalOrderDto updateInternalOrder(long id, InternalOrderDto internalOrderDto);
    ListResponse<InternalOrderDto> getAllInternalOrders(int pageNo, int pageSize, String sortBy, String sortDir);
    InternalOrderDto getInternalOrderById(long id);
    void deleteInternalOrderById(long id);
}

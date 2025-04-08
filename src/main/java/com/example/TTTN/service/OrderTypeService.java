package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderTypeDto;

public interface OrderTypeService {
    OrderTypeDto getOrderTypeById(long id);
    ListResponse<OrderTypeDto> getAllOrderTypes(int pageNo, int pageSize, String sortBy, String sortDir);
}

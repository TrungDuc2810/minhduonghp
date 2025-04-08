package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderStatusDto;

public interface OrderStatusService {
    OrderStatusDto getOrderStatusById(long id);
    ListResponse<OrderStatusDto> getAllOrderStatus(int pageNo, int pageSize, String sortBy, String sortDir);
}

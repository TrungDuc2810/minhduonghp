package com.example.TTTN.service;

import com.example.TTTN.payload.OrderStatusDto;

public interface OrderStatusService {
    OrderStatusDto getOrderStatusById(long id);
}

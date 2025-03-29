package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderDto;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    ListResponse<OrderDto> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir);
    OrderDto getOrderById(long orderId);
    OrderDto updateOrder(long orderId, OrderDto orderDto);
    void deleteOrderById(long orderId);
}

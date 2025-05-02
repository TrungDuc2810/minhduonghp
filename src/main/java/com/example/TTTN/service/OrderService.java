package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderDto;
import com.example.TTTN.payload.RevenueDto;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);

    ListResponse<OrderDto> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    ListResponse<OrderDto> getOrdersByPartnerId(long id, int pageNo, int pageSize, String sortBy, String sortDir);

    OrderDto getOrderById(long orderId);

    OrderDto updateOrder(long orderId, OrderDto orderDto);

    void deleteOrderById(long orderId);

    ListResponse<RevenueDto> getMonthlyRevenue(int year);

    ListResponse<RevenueDto> getQuarterlyRevenue(int year);

    ListResponse<RevenueDto> getYearRevenue();
}

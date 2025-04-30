package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.MonthlyRevenueDto;
import com.example.TTTN.payload.OrderDto;
import com.example.TTTN.payload.YearlyRevenueDto;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);

    ListResponse<OrderDto> getAllOrders(int pageNo, int pageSize, String sortBy, String sortDir);

    ListResponse<OrderDto> getOrdersByPartnerId(long id, int pageNo, int pageSize, String sortBy, String sortDir);

    OrderDto getOrderById(long orderId);

    OrderDto updateOrder(long orderId, OrderDto orderDto);

    void deleteOrderById(long orderId);

    ListResponse<MonthlyRevenueDto> getMonthlyRevenue(int year);

    ListResponse<MonthlyRevenueDto> getQuarterlyRevenue(int year);

    ListResponse<YearlyRevenueDto> getYearRevenue();
}

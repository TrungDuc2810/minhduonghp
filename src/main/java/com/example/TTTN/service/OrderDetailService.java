package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderDetailDto;

public interface OrderDetailService {
    OrderDetailDto createOrderDetail(OrderDetailDto orderDetailDto);
    ListResponse<OrderDetailDto> getAllOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir);
    ListResponse<OrderDetailDto> getOrderDetailsByOrderId(long orderId, int pageNo, int pageSize, String sortBy, String sortDir);
    OrderDetailDto getOrderDetailById(long orderDetailId);
    OrderDetailDto updateOrderDetail(long orderDetailId, OrderDetailDto orderDetailDto);
    void deleteOrderDetailById(long orderDetailId);
}

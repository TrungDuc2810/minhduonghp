package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderDetailDto;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailDto> createOrderDetails(List<OrderDetailDto> orderDetailDtos);
    ListResponse<OrderDetailDto> getAllOrderDetails(int pageNo, int pageSize, String sortBy, String sortDir);
    ListResponse<OrderDetailDto> getOrderDetailsByOrderId(long orderId, int pageNo, int pageSize, String sortBy, String sortDir);
    OrderDetailDto getOrderDetailById(long orderDetailId);
    List<OrderDetailDto> updateOrderDetails(long orderId, List<OrderDetailDto> orderDetailDtos);
    void deleteOrderDetailById(long orderDetailId);
}

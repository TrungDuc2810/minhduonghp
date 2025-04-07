package com.example.TTTN.service.impl;

import com.example.TTTN.entity.OrderStatus;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.OrderStatusDto;
import com.example.TTTN.repository.OrderStatusRepository;
import com.example.TTTN.service.OrderStatusService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusServiceImpl implements OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;
    private final ModelMapper modelMapper;

    public OrderStatusServiceImpl(OrderStatusRepository orderStatusRepository, ModelMapper modelMapper) {
        this.orderStatusRepository = orderStatusRepository;
        this.modelMapper = modelMapper;
    }

    private OrderStatusDto mapToDto(OrderStatus entity) {
        return modelMapper.map(entity, OrderStatusDto.class);
    }

    private OrderStatus mapToEntity(OrderStatusDto dto) {
        return modelMapper.map(dto, OrderStatus.class);
    }

    @Override
    public OrderStatusDto getOrderStatusById(long id) {
        OrderStatus orderStatus = orderStatusRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Order status", "id", String.valueOf(id)));
        return mapToDto(orderStatus);
    }
}

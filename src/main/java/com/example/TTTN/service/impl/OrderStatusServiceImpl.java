package com.example.TTTN.service.impl;

import com.example.TTTN.entity.OrderStatus;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderStatusDto;
import com.example.TTTN.repository.OrderStatusRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusServiceImpl implements GenericService<OrderStatusDto> {
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
    public OrderStatusDto getById(long id) {
        OrderStatus orderStatus = orderStatusRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Order status", "id", String.valueOf(id)));
        return mapToDto(orderStatus);
    }

    @Override
    public ListResponse<OrderStatusDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<OrderStatus> orderStatusPage = orderStatusRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(orderStatusPage, this::mapToDto);
    }

    @Override
    @Transactional
    public OrderStatusDto create(OrderStatusDto orderStatusDto) {
        OrderStatus orderStatus = mapToEntity(orderStatusDto);
        return mapToDto(orderStatusRepository.save(orderStatus));
    }

    @Override
    @Transactional
    public OrderStatusDto update(long id, OrderStatusDto orderStatusDto) {
        OrderStatus orderStatus = orderStatusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order status", "id", String.valueOf(id)));

        orderStatus.setName(orderStatusDto.getName());

        return mapToDto(orderStatusRepository.save(orderStatus));
    }

    @Override
    public void delete(long id) {
        OrderStatus orderStatus = orderStatusRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order status", "id", String.valueOf(id)));
        orderStatusRepository.delete(orderStatus);
    }
}

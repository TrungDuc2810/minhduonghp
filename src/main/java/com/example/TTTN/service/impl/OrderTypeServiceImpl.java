package com.example.TTTN.service.impl;

import com.example.TTTN.entity.OrderType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.OrderTypeDto;
import com.example.TTTN.repository.OrderTypeRepository;
import com.example.TTTN.service.OrderTypeService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class OrderTypeServiceImpl implements OrderTypeService {
    private final OrderTypeRepository orderTypeRepository;
    private final ModelMapper modelMapper;

    public OrderTypeServiceImpl(OrderTypeRepository orderTypeRepository, ModelMapper modelMapper) {
        this.orderTypeRepository = orderTypeRepository;
        this.modelMapper = modelMapper;
    }

    private OrderTypeDto mapToDto(OrderType orderType) {
        return modelMapper.map(orderType, OrderTypeDto.class);
    }

    private OrderType mapToEntity(OrderTypeDto orderTypeDto) {
        return modelMapper.map(orderTypeDto, OrderType.class);
    }

    @Override
    public OrderTypeDto getOrderTypeById(long id) {
        OrderType orderType = orderTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Order type", "id", String.valueOf(id)));
        return mapToDto(orderType);
    }

    @Override
    public ListResponse<OrderTypeDto> getAllOrderTypes(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<OrderType> orderTypes = orderTypeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(orderTypes, this::mapToDto);
    }
}

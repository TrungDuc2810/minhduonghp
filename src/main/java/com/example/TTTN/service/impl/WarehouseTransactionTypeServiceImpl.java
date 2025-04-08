package com.example.TTTN.service.impl;

import com.example.TTTN.entity.WarehouseTransactionType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransactionTypeDto;
import com.example.TTTN.repository.WarehouseTransactionTypeRepository;
import com.example.TTTN.service.WarehouseTransactionTypeService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WarehouseTransactionTypeServiceImpl implements WarehouseTransactionTypeService {
    private final WarehouseTransactionTypeRepository warehouseTransactionTypeRepository;
    private final ModelMapper modelMapper;

    public WarehouseTransactionTypeServiceImpl(WarehouseTransactionTypeRepository warehouseTransactionTypeRepository, ModelMapper modelMapper) {
        this.warehouseTransactionTypeRepository = warehouseTransactionTypeRepository;
        this.modelMapper = modelMapper;
    }

    private WarehouseTransactionTypeDto mapToDto(WarehouseTransactionType warehouseTransactionType) {
        return modelMapper.map(warehouseTransactionType, WarehouseTransactionTypeDto.class);
    }

    private WarehouseTransactionType mapToEntity(WarehouseTransactionTypeDto warehouseTransactionTypeDto) {
        return modelMapper.map(warehouseTransactionTypeDto, WarehouseTransactionType.class);
    }

    @Override
    public WarehouseTransactionTypeDto getWarehouseTransactionTypeById(long id) {
        WarehouseTransactionType warehouseTransactionType = warehouseTransactionTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transaction type", "id", String.valueOf(id)));
        return mapToDto(warehouseTransactionType);
    }

    @Override
    public ListResponse<WarehouseTransactionTypeDto> getAllWarehouseTransactionTypes(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<WarehouseTransactionType> warehouseTransactionTypes = warehouseTransactionTypeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(warehouseTransactionTypes, this::mapToDto);
    }
}

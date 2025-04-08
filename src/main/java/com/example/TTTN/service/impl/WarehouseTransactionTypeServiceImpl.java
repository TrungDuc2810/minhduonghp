package com.example.TTTN.service.impl;

import com.example.TTTN.entity.WarehouseTransactionType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseTransactionTypeDto;
import com.example.TTTN.repository.WarehouseTransactionTypeRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WarehouseTransactionTypeServiceImpl implements GenericService<WarehouseTransactionTypeDto> {
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
    public WarehouseTransactionTypeDto getById(long id) {
        WarehouseTransactionType warehouseTransactionType = warehouseTransactionTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transaction type", "id", String.valueOf(id)));
        return mapToDto(warehouseTransactionType);
    }

    @Override
    public ListResponse<WarehouseTransactionTypeDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<WarehouseTransactionType> warehouseTransactionTypes = warehouseTransactionTypeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(warehouseTransactionTypes, this::mapToDto);
    }

    @Override
    public WarehouseTransactionTypeDto create(WarehouseTransactionTypeDto warehouseTransactionTypeDto) {
        WarehouseTransactionType warehouseTransactionType = mapToEntity(warehouseTransactionTypeDto);
        return mapToDto(warehouseTransactionTypeRepository.save(warehouseTransactionType));
    }

    @Override
    public WarehouseTransactionTypeDto update(long id, WarehouseTransactionTypeDto warehouseTransactionTypeDto) {
        WarehouseTransactionType warehouseTransactionType = warehouseTransactionTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transaction type", "id", String.valueOf(id)));

        warehouseTransactionType.setName(warehouseTransactionTypeDto.getName());

        return mapToDto(warehouseTransactionTypeRepository.save(warehouseTransactionType));
    }

    @Override
    public void delete(long id) {
        WarehouseTransactionType warehouseTransactionType = warehouseTransactionTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transaction type", "id", String.valueOf(id)));

        warehouseTransactionTypeRepository.delete(warehouseTransactionType);
    }
}

package com.example.TTTN.service.impl;

import com.example.TTTN.entity.InventoryAdjustmentType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.InventoryAdjustmentTypeDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.InventoryAdjustmentTypeRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class InventoryAdjustmentTypeServiceImpl implements GenericService<InventoryAdjustmentTypeDto> {
    private final InventoryAdjustmentTypeRepository inventoryAdjustmentTypeRepository;
    private final ModelMapper modelMapper;

    public InventoryAdjustmentTypeServiceImpl(InventoryAdjustmentTypeRepository inventoryAdjustmentTypeRepository,
                                              ModelMapper modelMapper) {
        this.inventoryAdjustmentTypeRepository = inventoryAdjustmentTypeRepository;
        this.modelMapper = modelMapper;
    }

    private InventoryAdjustmentTypeDto mapToDto(InventoryAdjustmentType inventoryAdjustmentType) {
        return modelMapper.map(inventoryAdjustmentType, InventoryAdjustmentTypeDto.class);
    }

    private InventoryAdjustmentType mapToEntity(InventoryAdjustmentTypeDto inventoryAdjustmentTypeDto) {
        return modelMapper.map(inventoryAdjustmentTypeDto, InventoryAdjustmentType.class);
    }

    @Override
    public InventoryAdjustmentTypeDto getById(long id) {
        InventoryAdjustmentType inventoryAdjustmentType = inventoryAdjustmentTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Inventory adjustment type", "id", String.valueOf(id)));
        return mapToDto(inventoryAdjustmentType);
    }

    @Override
    public ListResponse<InventoryAdjustmentTypeDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<InventoryAdjustmentType> inventoryAdjustmentTypes = inventoryAdjustmentTypeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(inventoryAdjustmentTypes, this::mapToDto);
    }

    @Override
    public InventoryAdjustmentTypeDto create(InventoryAdjustmentTypeDto inventoryAdjustmentTypeDto) {
        InventoryAdjustmentType inventoryAdjustmentType = mapToEntity(inventoryAdjustmentTypeDto);
        return mapToDto(inventoryAdjustmentTypeRepository.save(inventoryAdjustmentType));
    }

    @Override
    public InventoryAdjustmentTypeDto update(long id, InventoryAdjustmentTypeDto inventoryAdjustmentTypeDto) {
        InventoryAdjustmentType inventoryAdjustmentType = inventoryAdjustmentTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Inventory adjustment type", "id", String.valueOf(id)));

        inventoryAdjustmentType.setName(inventoryAdjustmentTypeDto.getName());

        return mapToDto(inventoryAdjustmentTypeRepository.save(inventoryAdjustmentType));
    }

    @Override
    public void delete(long id) {
        InventoryAdjustmentType inventoryAdjustmentType = inventoryAdjustmentTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Inventory adjustment type", "id", String.valueOf(id)));

        inventoryAdjustmentTypeRepository.delete(inventoryAdjustmentType);
    }
}

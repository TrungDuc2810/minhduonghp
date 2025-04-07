package com.example.TTTN.service.impl;

import com.example.TTTN.entity.InventoryAdjustmentType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.InventoryAdjustmentTypeDto;
import com.example.TTTN.repository.InventoryAdjustmentTypeRepository;
import com.example.TTTN.service.InventoryAdjustmentTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class InventoryAdjustmentTypeServiceImpl implements InventoryAdjustmentTypeService {
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
    public InventoryAdjustmentTypeDto getInventoryAdjustmentTypeById(long id) {
        InventoryAdjustmentType inventoryAdjustmentType = inventoryAdjustmentTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Inventory adjustment type", "id", String.valueOf(id)));
        return mapToDto(inventoryAdjustmentType);
    }
}

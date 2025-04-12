package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Warehouse;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseDto;
import com.example.TTTN.repository.WarehouseRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl implements GenericService<WarehouseDto> {
    private final WarehouseRepository warehouseRepository;
    private final ModelMapper modelMapper;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository, ModelMapper modelMapper) {
        this.warehouseRepository = warehouseRepository;
        this.modelMapper = modelMapper;
    }

    private WarehouseDto mapToDto(Warehouse warehouse) {
        return modelMapper.map(warehouse, WarehouseDto.class);
    }

    private Warehouse mapToEntity(WarehouseDto dto) {
        return modelMapper.map(dto, Warehouse.class);
    }

    @Override
    public WarehouseDto getById(long id) {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(id)));
        return mapToDto(warehouse);
    }

    @Override
    public ListResponse<WarehouseDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<Warehouse> warehouses = warehouseRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(warehouses, this::mapToDto);
    }

    @Override
    @Transactional
    public WarehouseDto create(WarehouseDto warehouseDto) {
        Warehouse warehouse = mapToEntity(warehouseDto);
        return mapToDto(warehouseRepository.save(warehouse));
    }

    @Override
    @Transactional
    public WarehouseDto update(long id, WarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(id)));

        warehouse.setName(warehouseDto.getName());
        warehouse.setAddress(warehouseDto.getAddress());

        return mapToDto(warehouseRepository.save(warehouse));
    }

    @Override
    public void delete(long id) {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transaction type", "id", String.valueOf(id)));

        warehouseRepository.delete(warehouse);
    }
}

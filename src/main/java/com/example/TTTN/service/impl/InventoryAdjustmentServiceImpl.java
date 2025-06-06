package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.InventoryAdjustmentDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.*;
import com.example.TTTN.service.InventoryAdjustmentService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryAdjustmentServiceImpl implements InventoryAdjustmentService {
    private final InventoryAdjustmentRepository inventoryAdjustmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryAdjustmentTypeRepository inventoryAdjustmentTypeRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final ModelMapper modelMapper;

    public InventoryAdjustmentServiceImpl(InventoryAdjustmentRepository inventoryAdjustmentRepository,
                                          WarehouseRepository warehouseRepository,
                                          ProductRepository productRepository,
                                          InventoryAdjustmentTypeRepository inventoryAdjustmentTypeRepository,
                                          WarehouseProductRepository warehouseProductRepository,
                                          ModelMapper modelMapper) {
        this.inventoryAdjustmentRepository = inventoryAdjustmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.inventoryAdjustmentTypeRepository = inventoryAdjustmentTypeRepository;
        this.warehouseProductRepository = warehouseProductRepository;
        this.modelMapper = modelMapper;
    }

    private InventoryAdjustmentDto mapToDto(InventoryAdjustment inventoryAdjustment) {
        return modelMapper.map(inventoryAdjustment, InventoryAdjustmentDto.class);
    }

    private InventoryAdjustment mapToEntity(InventoryAdjustmentDto inventoryAdjustmentDto) {
        return modelMapper.map(inventoryAdjustmentDto, InventoryAdjustment.class);
    }

    @Override
    @Transactional
    public InventoryAdjustmentDto createInventoryAdjustment(InventoryAdjustmentDto inventoryAdjustmentDto) {
        InventoryAdjustment inventoryAdjustment = mapToEntity(inventoryAdjustmentDto);

        WarehouseProduct warehouseProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                inventoryAdjustmentDto.getWarehouseId(), inventoryAdjustmentDto.getProductId());

        Product product = productRepository.findById(inventoryAdjustmentDto.getProductId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(inventoryAdjustmentDto.getProductId())));

        if (warehouseProduct == null) {
            throw new ResourceNotFoundException("Warehouse product", "", "Not found");
        }

        if (warehouseProduct.getQuantity() < inventoryAdjustmentDto.getQuantity()) {
            throw new IllegalArgumentException("Kho không đủ số lượng.");
        }

        warehouseProduct.setQuantity(warehouseProduct.getQuantity() - inventoryAdjustmentDto.getQuantity());
        warehouseProductRepository.save(warehouseProduct);

        product.setQuantity(product.getQuantity() - inventoryAdjustmentDto.getQuantity());
        productRepository.save(product);

        return mapToDto(inventoryAdjustmentRepository.save(inventoryAdjustment));
    }

    @Override
    @Transactional
    public InventoryAdjustmentDto updateInventoryAdjustment(long id, InventoryAdjustmentDto inventoryAdjustmentDto) {
        InventoryAdjustment inventoryAdjustment = inventoryAdjustmentRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Inventory adjustment", "id", String.valueOf(id)));

        Warehouse warehouse = warehouseRepository.findById(inventoryAdjustmentDto.getWarehouseId()).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse", "id", String.valueOf(inventoryAdjustmentDto.getWarehouseId())));

        InventoryAdjustmentType inventoryAdjustmentType = inventoryAdjustmentTypeRepository.findById(inventoryAdjustmentDto.getInventoryAdjustmentTypeId()).orElseThrow(()
                -> new ResourceNotFoundException("Inventory adjustment type", "id", String.valueOf(inventoryAdjustmentDto.getInventoryAdjustmentTypeId())));

        WarehouseProduct newWarehouseProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                    inventoryAdjustmentDto.getWarehouseId(), inventoryAdjustmentDto.getProductId());

        WarehouseProduct oldWarehouseProduct = warehouseProductRepository.findByWarehouseIdAndProductId(
                inventoryAdjustment.getWarehouse().getId(), inventoryAdjustment.getProduct().getId());

        Product newProduct = productRepository.findById(inventoryAdjustmentDto.getProductId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(inventoryAdjustmentDto.getProductId())));

        Product oldProduct = productRepository.findById(inventoryAdjustment.getProduct().getId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(inventoryAdjustment.getProduct().getId())));

        if (newWarehouseProduct == null) {
            throw new ResourceNotFoundException("Warehouse product", "", "Not found");
        }

        if (newWarehouseProduct.getQuantity() < inventoryAdjustmentDto.getQuantity()) {
            throw new IllegalArgumentException("Kho không đủ số lượng.");
        }

        oldWarehouseProduct.setQuantity(oldWarehouseProduct.getQuantity() + inventoryAdjustment.getQuantity());
        newWarehouseProduct.setQuantity(newWarehouseProduct.getQuantity() - inventoryAdjustmentDto.getQuantity());
        warehouseProductRepository.save(oldWarehouseProduct);
        warehouseProductRepository.save(newWarehouseProduct);

        oldProduct.setQuantity(oldProduct.getQuantity() + inventoryAdjustment.getQuantity());
        newProduct.setQuantity(newProduct.getQuantity() - inventoryAdjustmentDto.getQuantity());
        productRepository.save(oldProduct);
        productRepository.save(newProduct);

        inventoryAdjustment.setWarehouse(warehouse);
        inventoryAdjustment.setProduct(newProduct);
        inventoryAdjustment.setInventoryAdjustmentType(inventoryAdjustmentType);
        inventoryAdjustment.setQuantity(inventoryAdjustmentDto.getQuantity());

        return mapToDto(inventoryAdjustmentRepository.save(inventoryAdjustment));
    }

    @Override
    public InventoryAdjustmentDto getInventoryAdjustment(long id) {
        InventoryAdjustment inventoryAdjustment = inventoryAdjustmentRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transfer", "id", String.valueOf(id)));

        return mapToDto(inventoryAdjustment);
    }

    @Override
    public ListResponse<InventoryAdjustmentDto> getAllInventoryAdjustments(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<InventoryAdjustment> inventoryAdjustments = inventoryAdjustmentRepository.findAll(pageRequest);

        List<InventoryAdjustment> listOfInventoryAdjustments = inventoryAdjustments.getContent();

        List<InventoryAdjustmentDto> content = listOfInventoryAdjustments.stream().map(this::mapToDto).toList();

        ListResponse<InventoryAdjustmentDto> response = new ListResponse<>();
        response.setContent(content);
        response.setPageNo(inventoryAdjustments.getNumber());
        response.setPageSize(inventoryAdjustments.getSize());
        response.setTotalElements((int)inventoryAdjustments.getTotalElements());
        response.setTotalPages(inventoryAdjustments.getTotalPages());
        response.setLast(inventoryAdjustments.isLast());

        return response;
    }

    @Override
    public void deleteInventoryAdjustment(long id) {
        InventoryAdjustment inventoryAdjustment = inventoryAdjustmentRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse transfer", "id", String.valueOf(id)));
        inventoryAdjustmentRepository.deleteById(id);
    }
}

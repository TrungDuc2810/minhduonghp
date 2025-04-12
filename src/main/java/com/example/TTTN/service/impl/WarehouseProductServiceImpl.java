package com.example.TTTN.service.impl;

import com.example.TTTN.entity.*;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.WarehouseProductDto;
import com.example.TTTN.repository.ProductRepository;
import com.example.TTTN.repository.WarehouseProductRepository;
import com.example.TTTN.repository.WarehouseRepository;
import com.example.TTTN.service.WarehouseProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseProductServiceImpl implements WarehouseProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductRepository warehouseProductRepository;

    public WarehouseProductServiceImpl(ProductRepository productRepository,
                                       ModelMapper modelMapper,
                                       WarehouseRepository warehouseRepository, WarehouseProductRepository warehouseProductRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.warehouseRepository = warehouseRepository;
        this.warehouseProductRepository = warehouseProductRepository;
    }

    private WarehouseProductDto mapToDto(WarehouseProduct warehouseProduct) {
        return modelMapper.map(warehouseProduct, WarehouseProductDto.class);
    }

    private WarehouseProduct mapToEntity(WarehouseProductDto warehouseProductDto) {
        return modelMapper.map(warehouseProductDto, WarehouseProduct.class);
    }

    @Override
    @Transactional
    public WarehouseProductDto createWarehouseProduct(WarehouseProductDto warehouseProductDto) {
        WarehouseProduct warehouseProduct = mapToEntity(warehouseProductDto);
        return mapToDto(warehouseProduct);
    }

    @Override
    public ListResponse<WarehouseProductDto> getAllWarehouseProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<WarehouseProduct> warehouseProducts = warehouseProductRepository.findAll(pageRequest);

        List<WarehouseProduct> listOfWarehouseProducts = warehouseProducts.getContent();

        List<WarehouseProductDto> content = listOfWarehouseProducts.stream().map(this::mapToDto).toList();

        ListResponse<WarehouseProductDto> warehouseProductResponse = new ListResponse<>();
        warehouseProductResponse.setContent(content);
        warehouseProductResponse.setPageNo(warehouseProducts.getNumber());
        warehouseProductResponse.setPageSize(warehouseProducts.getSize());
        warehouseProductResponse.setTotalElements((int)warehouseProducts.getTotalElements());
        warehouseProductResponse.setTotalPages(warehouseProducts.getTotalPages());
        warehouseProductResponse.setLast(warehouseProducts.isLast());

        return warehouseProductResponse;
    }

    @Override
    public WarehouseProductDto getWarehouseProductById(long id) {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse product", "id", String.valueOf(id)));

        return mapToDto(warehouseProduct);
    }

    @Override
    @Transactional
    public WarehouseProductDto updateWarehouseProduct(WarehouseProductDto warehouseProductDto, long id) {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse product", "id", String.valueOf(id)));

        Product product = productRepository.findById(warehouseProductDto.getProductId()).orElseThrow(() ->
                new ResourceNotFoundException("Product", "id", String.valueOf(warehouseProductDto.getProductId())));

        Warehouse warehouse = warehouseRepository.findById(warehouseProductDto.getWarehouseId()).orElseThrow(() ->
                new ResourceNotFoundException("Warehouse", "id", String.valueOf(warehouseProductDto.getWarehouseId())));

        warehouseProduct.setQuantity(warehouseProductDto.getQuantity());
        warehouseProduct.setProduct(product);
        warehouseProduct.setWarehouse(warehouse);

        WarehouseProduct updatedWarehouseProduct = warehouseProductRepository.save(warehouseProduct);

        return mapToDto(updatedWarehouseProduct);
    }

    @Override
    public void deleteWarehouseProductById(long id) {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Warehouse product", "id", String.valueOf(id)));
        warehouseProductRepository.delete(warehouseProduct);
    }
}

package com.example.TTTN.service.impl;

import com.example.TTTN.entity.ProductType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductTypeDto;
import com.example.TTTN.repository.ProductTypeRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeServiceImpl implements GenericService<ProductTypeDto> {
    private final ProductTypeRepository productTypeRepository;
    private final ModelMapper modelMapper;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository, ModelMapper modelMapper) {
        this.productTypeRepository = productTypeRepository;
        this.modelMapper = modelMapper;
    }

    private ProductTypeDto mapToDto(ProductType productType) {
        return modelMapper.map(productType, ProductTypeDto.class);
    }

    private ProductType mapToEntity(ProductTypeDto productTypeDto) {
        return modelMapper.map(productTypeDto, ProductType.class);
    }

    @Override
    public ProductTypeDto getById(long id) {
        ProductType productType = productTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product type", "id", String.valueOf(id)));
        return mapToDto(productType);
    }

    @Override
    public ListResponse<ProductTypeDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<ProductType> productTypePage = productTypeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(productTypePage, this::mapToDto);
    }

    @Override
    @Transactional
    public ProductTypeDto create(ProductTypeDto productTypeDto) {
        ProductType productType = mapToEntity(productTypeDto);
        return mapToDto(productTypeRepository.save(productType));
    }

    @Override
    @Transactional
    public ProductTypeDto update(long id, ProductTypeDto productTypeDto) {
        ProductType productType = productTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product type", "id", String.valueOf(id)));

        productType.setName(productTypeDto.getName());

        return mapToDto(productTypeRepository.save(productType));
    }

    @Override
    public void delete(long id) {
        ProductType productType = productTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product type", "id", String.valueOf(id)));

        productTypeRepository.delete(productType);
    }
}

package com.example.TTTN.service.impl;

import com.example.TTTN.entity.ProductType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ProductTypeDto;
import com.example.TTTN.repository.ProductTypeRepository;
import com.example.TTTN.service.ProductTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {
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
    public ProductTypeDto getProductTypeById(long id) {
        ProductType productType = productTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Product type", "id", String.valueOf(id)));
        return mapToDto(productType);
    }
}

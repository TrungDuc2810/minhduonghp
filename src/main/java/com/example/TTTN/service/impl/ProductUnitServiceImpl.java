package com.example.TTTN.service.impl;

import com.example.TTTN.entity.ProductUnit;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.ProductUnitDto;
import com.example.TTTN.repository.ProductUnitRepository;
import com.example.TTTN.service.ProductUnitService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {
    private final ProductUnitRepository productUnitRepository;
    private final ModelMapper modelMapper;

    public ProductUnitServiceImpl(ProductUnitRepository productUnitRepository, ModelMapper modelMapper) {
        this.productUnitRepository = productUnitRepository;
        this.modelMapper = modelMapper;
    }

    private ProductUnitDto mapToDto(ProductUnit productUnit) {
        return modelMapper.map(productUnit, ProductUnitDto.class);
    }

    private ProductUnit mapToEntity(ProductUnitDto productUnitDto) {
        return modelMapper.map(productUnitDto, ProductUnit.class);
    }

    @Override
    public ProductUnitDto getProductUnit(long productUnitId) {
        ProductUnit productUnit = productUnitRepository.findById(productUnitId).orElseThrow(()
                -> new ResourceNotFoundException("Product unit", "id", String.valueOf(productUnitId)));
        return mapToDto(productUnit);
    }

    @Override
    public ListResponse<ProductUnitDto> getAllProductUnits(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<ProductUnit> productUnitPage = productUnitRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(productUnitPage, this::mapToDto);
    }
}

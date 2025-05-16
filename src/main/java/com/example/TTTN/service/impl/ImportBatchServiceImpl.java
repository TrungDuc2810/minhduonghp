package com.example.TTTN.service.impl;

import com.example.TTTN.entity.ImportBatch;
import com.example.TTTN.entity.Product;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ImportBatchDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.ImportBatchRepository;
import com.example.TTTN.repository.ProductRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ImportBatchServiceImpl implements GenericService<ImportBatchDto> {
    private final ImportBatchRepository importBatchRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public ImportBatchServiceImpl(ImportBatchRepository importBatchRepository, ProductRepository productRepository) {
        this.importBatchRepository = importBatchRepository;
        this.modelMapper = new ModelMapper();
        this.productRepository = productRepository;
    }

    private ImportBatchDto mapToDto(ImportBatch importBatch) {
        return modelMapper.map(importBatch, ImportBatchDto.class);
    }

    private ImportBatch mapToEntity(ImportBatchDto importBatchDto) {
        return modelMapper.map(importBatchDto, ImportBatch.class);
    }

    @Override
    public ImportBatchDto getById(long id) {
        ImportBatch importBatch = importBatchRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Import batch", "id", String.valueOf(id)));
        return mapToDto(importBatch);
    }

    @Override
    public ListResponse<ImportBatchDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<ImportBatch> importBatches = importBatchRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(importBatches, this::mapToDto);
    }

    @Override
    @Transactional
    public ImportBatchDto create(ImportBatchDto importBatchDto) {
        ImportBatch importBatch = mapToEntity(importBatchDto);
        return mapToDto(importBatchRepository.save(importBatch));
    }

    @Override
    @Transactional
    public ImportBatchDto update(long id, ImportBatchDto importBatchDto) {
        ImportBatch importBatch = importBatchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Import batch", "id", String.valueOf(id)));

        Product product = productRepository.findById(importBatchDto.getProductId()).orElseThrow(()
                -> new ResourceNotFoundException("Product", "id", String.valueOf(importBatchDto.getProductId())));

        importBatch.setRemainQuantity(importBatchDto.getRemainQuantity());

        return mapToDto(importBatchRepository.save(importBatch));
    }

    @Override
    public void delete(long id) {
        ImportBatch importBatch = importBatchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Import batch", "id", String.valueOf(id)));

        importBatchRepository.delete(importBatch);
    }
}

package com.example.TTTN.service.impl;

import com.example.TTTN.entity.TransactionBatch;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.TransactionBatchDto;
import com.example.TTTN.repository.TransactionBatchRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class TransactionBatchServiceImpl implements GenericService<TransactionBatchDto> {
    private final TransactionBatchRepository transactionBatchRepository;
    private final ModelMapper modelMapper;

    public TransactionBatchServiceImpl(TransactionBatchRepository transactionBatchRepository, ModelMapper modelMapper) {
        this.transactionBatchRepository = transactionBatchRepository;
        this.modelMapper = modelMapper;
    }

    private TransactionBatchDto mapToDto(TransactionBatch transactionBatch) {
        return modelMapper.map(transactionBatch, TransactionBatchDto.class);
    }

    private TransactionBatch mapToEntity(TransactionBatchDto transactionBatchDto) {
        return modelMapper.map(transactionBatchDto, TransactionBatch.class);
    }

    @Override
    public TransactionBatchDto getById(long id) {
        TransactionBatch transactionBatch = transactionBatchRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Transaction batch", "id", String.valueOf(id)));
        return mapToDto(transactionBatch);
    }

    @Override
    public ListResponse<TransactionBatchDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<TransactionBatch> transactionBatches = transactionBatchRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(transactionBatches, this::mapToDto);
    }

    @Override
    @Transactional
    public TransactionBatchDto create(TransactionBatchDto transactionBatchDto) {
        TransactionBatch transactionBatch = mapToEntity(transactionBatchDto);
        return mapToDto(transactionBatchRepository.save(transactionBatch));
    }

    @Override
    @Transactional
    public TransactionBatchDto update(long id, TransactionBatchDto transactionBatchDto) {
        return null;
    }

    @Override
    public void delete(long id) {
    }
}


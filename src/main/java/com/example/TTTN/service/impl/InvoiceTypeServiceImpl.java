package com.example.TTTN.service.impl;

import com.example.TTTN.entity.InvoiceType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.InvoiceTypeDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.InvoiceTypeRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class InvoiceTypeServiceImpl implements GenericService<InvoiceTypeDto> {
    private final InvoiceTypeRepository invoiceTypeRepository;
    private final ModelMapper modelMapper;

    public InvoiceTypeServiceImpl(InvoiceTypeRepository invoiceTypeRepository, ModelMapper modelMapper) {
        this.invoiceTypeRepository = invoiceTypeRepository;
        this.modelMapper = modelMapper;
    }

    private InvoiceTypeDto mapToDto(InvoiceType invoiceType) {
        return modelMapper.map(invoiceType, InvoiceTypeDto.class);
    }

    private InvoiceType mapToEntity(InvoiceTypeDto invoiceTypeDto) {
        return modelMapper.map(invoiceTypeDto, InvoiceType.class);
    }

    @Override
    public InvoiceTypeDto getById(long id) {
        InvoiceType invoiceType = invoiceTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Invoice type", "id", String.valueOf(id)));
        return mapToDto(invoiceType);
    }

    @Override
    public ListResponse<InvoiceTypeDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<InvoiceType> invoiceTypes = invoiceTypeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(invoiceTypes, this::mapToDto);
    }

    @Override
    @Transactional
    public InvoiceTypeDto create(InvoiceTypeDto dto) {
        InvoiceType invoiceType = mapToEntity(dto);
        return mapToDto(invoiceTypeRepository.save(invoiceType));
    }

    @Override
    @Transactional
    public InvoiceTypeDto update(long id, InvoiceTypeDto dto) {
        InvoiceType invoiceType = invoiceTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Invoice type", "id", String.valueOf(id)));

        invoiceType.setName(dto.getName());

        return mapToDto(invoiceTypeRepository.save(invoiceType));
    }

    @Override
    public void delete(long id) {
        InvoiceType invoiceType = invoiceTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Invoice type", "id", String.valueOf(id)));

        invoiceTypeRepository.delete(invoiceType);
    }
}

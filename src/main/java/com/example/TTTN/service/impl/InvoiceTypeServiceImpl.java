package com.example.TTTN.service.impl;

import com.example.TTTN.entity.InvoiceType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.InvoiceTypeDto;
import com.example.TTTN.repository.InvoiceTypeRepository;
import com.example.TTTN.service.InvoiceTypeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class InvoiceTypeServiceImpl implements InvoiceTypeService {
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
    public InvoiceTypeDto getInvoiceTypeById(long id) {
        InvoiceType invoiceType = invoiceTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Invoice type", "id", String.valueOf(id)));
        return mapToDto(invoiceType);
    }
}

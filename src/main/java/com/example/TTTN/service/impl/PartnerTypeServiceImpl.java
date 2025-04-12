package com.example.TTTN.service.impl;

import com.example.TTTN.entity.PartnerType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PartnerTypeDto;
import com.example.TTTN.repository.PartnerTypeRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PartnerTypeServiceImpl implements GenericService<PartnerTypeDto> {
    private final PartnerTypeRepository partnerTypeRepository;
    private final ModelMapper modelMapper;

    public PartnerTypeServiceImpl(PartnerTypeRepository partnerTypeRepository, ModelMapper modelMapper) {
        this.partnerTypeRepository = partnerTypeRepository;
        this.modelMapper = modelMapper;
    }

    private PartnerTypeDto mapToDto(PartnerType partnerType) {
        return modelMapper.map(partnerType, PartnerTypeDto.class);
    }

    private PartnerType mapToEntity(PartnerTypeDto partnerTypeDto) {
        return modelMapper.map(partnerTypeDto, PartnerType.class);
    }

    @Override
    public PartnerTypeDto getById(long id) {
        PartnerType partnerType = partnerTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Partner type", "id", String.valueOf(id)));
        return mapToDto(partnerType);
    }

    @Override
    public ListResponse<PartnerTypeDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<PartnerType> partnerTypes = partnerTypeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(partnerTypes, this::mapToDto);
    }

    @Override
    @Transactional
    public PartnerTypeDto create(PartnerTypeDto partnerTypeDto) {
        PartnerType partnerType = mapToEntity(partnerTypeDto);
        return mapToDto(partnerTypeRepository.save(partnerType));
    }

    @Override
    @Transactional
    public PartnerTypeDto update(long id, PartnerTypeDto partnerTypeDto) {
        PartnerType partnerType = partnerTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Partner type", "id", String.valueOf(id)));

        partnerType.setName(partnerTypeDto.getName());

        return mapToDto(partnerTypeRepository.save(partnerType));
    }

    @Override
    public void delete(long id) {
        PartnerType partnerType = partnerTypeRepository.findById(id).orElseThrow(()
            -> new ResourceNotFoundException("Partner type", "id", String.valueOf(id)));

        partnerTypeRepository.delete(partnerType);
    }
}

package com.example.TTTN.service.impl;

import com.example.TTTN.entity.PartnerType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PartnerTypeDto;
import com.example.TTTN.repository.PartnerTypeRepository;
import com.example.TTTN.service.PartnerTypeService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PartnerTypeServiceImpl implements PartnerTypeService {
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
    public PartnerTypeDto getPartnerTypeById(long id) {
        PartnerType partnerType = partnerTypeRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Partner type", "id", String.valueOf(id)));
        return mapToDto(partnerType);
    }

    @Override
    public ListResponse<PartnerTypeDto> getAllPartnerTypes(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<PartnerType> partnerTypes = partnerTypeRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(partnerTypes, this::mapToDto);
    }
}

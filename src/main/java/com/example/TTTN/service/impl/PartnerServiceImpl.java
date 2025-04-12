package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Partner;
import com.example.TTTN.entity.PartnerType;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.PartnerDto;
import com.example.TTTN.repository.PartnerRepository;
import com.example.TTTN.repository.PartnerTypeRepository;
import com.example.TTTN.service.PartnerService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerTypeRepository partnerTypeRepository;
    private final ModelMapper modelMapper;

    public PartnerServiceImpl(
            PartnerRepository partnerRepository,
            PartnerTypeRepository partnerTypeRepository,
            ModelMapper modelMapper) {
        this.partnerRepository = partnerRepository;
        this.partnerTypeRepository = partnerTypeRepository;
        this.modelMapper = modelMapper;
    }

    private PartnerDto mapToDto(Partner partner) {
        return modelMapper.map(partner, PartnerDto.class);
    }

    private Partner mapToEntity(PartnerDto partnerDto) {
        return modelMapper.map(partnerDto, Partner.class);
    }

    @Override
    @Transactional
    public PartnerDto createPartner(PartnerDto partnerDto) {
        Partner partner = mapToEntity(partnerDto);
        return mapToDto(partnerRepository.save(partner));
    }

    @Override
    public ListResponse<PartnerDto> getAllPartners(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Partner> partners = partnerRepository.findAll(pageRequest);

        List<Partner> listOfPartners = partners.getContent();

        List<PartnerDto> content = listOfPartners.stream().map(this::mapToDto).toList();

        ListResponse<PartnerDto> partnerResponse = new ListResponse<>();
        partnerResponse.setContent(content);
        partnerResponse.setPageNo(partners.getNumber());
        partnerResponse.setPageSize(partners.getSize());
        partnerResponse.setTotalPages(partners.getTotalPages());
        partnerResponse.setTotalElements((int)partners.getTotalElements());
        partnerResponse.setLast(partners.isLast());

        return partnerResponse;
    }

    @Override
    public PartnerDto getPartnerById(long partnerId) {
        Partner partner = partnerRepository.findById(partnerId).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(partnerId)));
        return mapToDto(partner);
    }

    @Override
    @Transactional
    public PartnerDto updatePartner(long partnerId, PartnerDto partnerDto) {
        Partner partner = partnerRepository.findById(partnerId).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(partnerId)));

        PartnerType partnerType = partnerTypeRepository.findById(partnerDto.getPartnerTypeId()).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(partnerId)));

        partner.setName(partnerDto.getName());
        partner.setAddress(partnerDto.getAddress());
        partner.setEmail(partnerDto.getEmail());
        partner.setPhone(partnerDto.getPhone());
        partner.setPartnerType(partnerType);

        partnerRepository.save(partner);

        return mapToDto(partner);
    }

    @Override
    public void deletePartnerById(long partnerId) {
        Partner partner = partnerRepository.findById(partnerId).orElseThrow(()
                -> new ResourceNotFoundException("Partner", "id", String.valueOf(partnerId)));
        partnerRepository.delete(partner);
    }
}

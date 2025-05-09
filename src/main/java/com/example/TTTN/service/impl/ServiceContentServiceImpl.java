package com.example.TTTN.service.impl;

import com.example.TTTN.entity.ServiceContent;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ServiceContentDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.ServiceContentRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ServiceContentServiceImpl implements GenericService<ServiceContentDto> {
    private final ServiceContentRepository serviceContentRepository;
    private final ModelMapper modelMapper;

    public ServiceContentServiceImpl(ServiceContentRepository serviceContentRepository, ModelMapper modelMapper) {
        this.serviceContentRepository = serviceContentRepository;
        this.modelMapper = modelMapper;
    }

    private ServiceContentDto mapToDto(ServiceContent serviceContent) {
        return modelMapper.map(serviceContent, ServiceContentDto.class);
    }

    private ServiceContent mapToEntity(ServiceContentDto serviceContentDto) {
        return modelMapper.map(serviceContentDto, ServiceContent.class);
    }

    @Override
    public ServiceContentDto getById(long id) {
        ServiceContent serviceContent = serviceContentRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Home config", "id", String.valueOf(id)));
        return mapToDto(serviceContent);
    }

    @Override
    public ListResponse<ServiceContentDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<ServiceContent> homeConfigs = serviceContentRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(homeConfigs, this::mapToDto);
    }

    @Override
    @Transactional
    public ServiceContentDto create(ServiceContentDto serviceContentDto) {
        ServiceContent serviceContent = mapToEntity(serviceContentDto);
        return mapToDto(serviceContentRepository.save(serviceContent));
    }

    @Override
    @Transactional
    public ServiceContentDto update(long id, ServiceContentDto serviceContentDto) {
        ServiceContent serviceContent = serviceContentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Home config", "id", String.valueOf(id)));

        serviceContent.setTitle(serviceContentDto.getTitle());
        serviceContent.setDescription(serviceContentDto.getDescription());
        serviceContent.setThumbnail(serviceContentDto.getThumbnail());

        return mapToDto(serviceContentRepository.save(serviceContent));
    }

    @Override
    public void delete(long id) {
        ServiceContent serviceContent = serviceContentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Home config", "id", String.valueOf(id)));

        serviceContentRepository.delete(serviceContent);
    }
}

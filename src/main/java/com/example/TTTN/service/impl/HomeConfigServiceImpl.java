package com.example.TTTN.service.impl;

import com.example.TTTN.entity.HomeConfig;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.HomeConfigDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.HomeConfigRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class HomeConfigServiceImpl implements GenericService<HomeConfigDto> {
    private final HomeConfigRepository homeConfigRepository;
    private final ModelMapper modelMapper;

    public HomeConfigServiceImpl(HomeConfigRepository homeConfigRepository, ModelMapper modelMapper) {
        this.homeConfigRepository = homeConfigRepository;
        this.modelMapper = modelMapper;
    }

    private HomeConfigDto mapToDto(HomeConfig homeConfig) {
        return modelMapper.map(homeConfig, HomeConfigDto.class);
    }

    private HomeConfig mapToEntity(HomeConfigDto homeConfigDto) {
        return modelMapper.map(homeConfigDto, HomeConfig.class);
    }

    @Override
    public HomeConfigDto getById(long id) {
        HomeConfig homeConfig = homeConfigRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Home config", "id", String.valueOf(id)));
        return mapToDto(homeConfig);
    }

    @Override
    public ListResponse<HomeConfigDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<HomeConfig> homeConfigs = homeConfigRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(homeConfigs, this::mapToDto);
    }

    @Override
    @Transactional
    public HomeConfigDto create(HomeConfigDto homeConfigDto) {
        HomeConfig homeConfig = mapToEntity(homeConfigDto);
        return mapToDto(homeConfigRepository.save(homeConfig));
    }

    @Override
    @Transactional
    public HomeConfigDto update(long id, HomeConfigDto homeConfigDto) {
        HomeConfig homeConfig = homeConfigRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Home config", "id", String.valueOf(id)));

        homeConfig.setTitle(homeConfigDto.getTitle());
        homeConfig.setDescription(homeConfigDto.getDescription());
        homeConfig.setThumbnail(homeConfigDto.getThumbnail());

        return mapToDto(homeConfigRepository.save(homeConfig));
    }

    @Override
    public void delete(long id) {
        HomeConfig homeConfig = homeConfigRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Home config", "id", String.valueOf(id)));

        homeConfigRepository.delete(homeConfig);
    }
}

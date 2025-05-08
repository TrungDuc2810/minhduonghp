package com.example.TTTN.service.impl;

import com.example.TTTN.entity.AboutConfig;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.AboutConfigDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.AboutConfigRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class AboutConfigServiceImpl implements GenericService<AboutConfigDto> {
    private final AboutConfigRepository aboutConfigRepository;
    private final ModelMapper modelMapper;

    public AboutConfigServiceImpl(AboutConfigRepository aboutConfigRepository, ModelMapper modelMapper) {
        this.aboutConfigRepository = aboutConfigRepository;
        this.modelMapper = modelMapper;
    }

    private AboutConfigDto mapToDto(AboutConfig aboutConfig) {
        return modelMapper.map(aboutConfig, AboutConfigDto.class);
    }

    private AboutConfig mapToEntity(AboutConfigDto aboutConfigDto) {
        return modelMapper.map(aboutConfigDto, AboutConfig.class);
    }

    @Override
    public AboutConfigDto getById(long id) {
        AboutConfig aboutConfig = aboutConfigRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("About config", "id", String.valueOf(id)));
        return mapToDto(aboutConfig);
    }

    @Override
    public ListResponse<AboutConfigDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<AboutConfig> aboutConfigs = aboutConfigRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(aboutConfigs, this::mapToDto);
    }

    @Override
    @Transactional
    public AboutConfigDto create(AboutConfigDto aboutConfigDto) {
        AboutConfig aboutConfig = mapToEntity(aboutConfigDto);
        return mapToDto(aboutConfigRepository.save(aboutConfig));
    }

    @Override
    @Transactional
    public AboutConfigDto update(long id, AboutConfigDto aboutConfigDto) {
        AboutConfig aboutConfig = aboutConfigRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("About config", "id", String.valueOf(id)));

        aboutConfig.setTitle(aboutConfigDto.getTitle());
        aboutConfig.setDescription(aboutConfigDto.getDescription());
        aboutConfig.setIcon(aboutConfigDto.getIcon());

        return mapToDto(aboutConfigRepository.save(aboutConfig));
    }

    @Override
    public void delete(long id) {
        AboutConfig aboutConfig = aboutConfigRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("About config", "id", String.valueOf(id)));

        aboutConfigRepository.delete(aboutConfig);
    }
}

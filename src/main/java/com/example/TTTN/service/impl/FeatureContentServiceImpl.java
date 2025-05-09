package com.example.TTTN.service.impl;

import com.example.TTTN.entity.FeatureContent;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.FeatureContentDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.FeatureContentRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class FeatureContentServiceImpl implements GenericService<FeatureContentDto> {
    private final FeatureContentRepository featureContentRepository;
    private final ModelMapper modelMapper;

    public FeatureContentServiceImpl(FeatureContentRepository featureContentRepository, ModelMapper modelMapper) {
        this.featureContentRepository = featureContentRepository;
        this.modelMapper = modelMapper;
    }

    private FeatureContentDto mapToDto(FeatureContent featureContent) {
        return modelMapper.map(featureContent, FeatureContentDto.class);
    }

    private FeatureContent mapToEntity(FeatureContentDto featureContentDto) {
        return modelMapper.map(featureContentDto, FeatureContent.class);
    }

    @Override
    public FeatureContentDto getById(long id) {
        FeatureContent featureContent = featureContentRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("About config", "id", String.valueOf(id)));
        return mapToDto(featureContent);
    }

    @Override
    public ListResponse<FeatureContentDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<FeatureContent> aboutConfigs = featureContentRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(aboutConfigs, this::mapToDto);
    }

    @Override
    @Transactional
    public FeatureContentDto create(FeatureContentDto featureContentDto) {
        FeatureContent featureContent = mapToEntity(featureContentDto);
        return mapToDto(featureContentRepository.save(featureContent));
    }

    @Override
    @Transactional
    public FeatureContentDto update(long id, FeatureContentDto featureContentDto) {
        FeatureContent featureContent = featureContentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("About config", "id", String.valueOf(id)));

        featureContent.setTitle(featureContentDto.getTitle());
        featureContent.setDescription(featureContentDto.getDescription());
        featureContent.setIcon(featureContentDto.getIcon());

        return mapToDto(featureContentRepository.save(featureContent));
    }

    @Override
    public void delete(long id) {
        FeatureContent featureContent = featureContentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("About config", "id", String.valueOf(id)));

        featureContentRepository.delete(featureContent);
    }
}

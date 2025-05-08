package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Config;
import com.example.TTTN.entity.Employee;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ConfigDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.repository.ConfigRepository;
import com.example.TTTN.service.ConfigService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    private final ModelMapper modelMapper;

    public ConfigServiceImpl(ConfigRepository configRepository, ModelMapper modelMapper) {
        this.configRepository = configRepository;
        this.modelMapper = modelMapper;
    }

    private ConfigDto mapToDto(Config config) {
        return modelMapper.map(config, ConfigDto.class);
    }

    private Config mapToEntity(ConfigDto configDto) {
        return modelMapper.map(configDto, Config.class);
    }


    @Override
    public ConfigDto addConfig(ConfigDto configDto) {
        Config config = mapToEntity(configDto);
        return mapToDto(configRepository.save(config));
    }

    @Override
    public ConfigDto updateConfig(ConfigDto configDto, String field) {
        Config config = configRepository.findById(field).orElseThrow(()
                -> new ResourceNotFoundException("Field", "id", field));
        config.setValue(configDto.getValue());
        return mapToDto(configRepository.save(config));
    }

    @Override
    public ConfigDto getConfig(String field) {
        Config config = configRepository.findById(field).orElseThrow(()
                -> new ResourceNotFoundException("Field", "id", field));
        return mapToDto(config);
    }

    @Override
    public ListResponse<ConfigDto> getAllConfigs(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<Config> listOfConfig = configRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(listOfConfig, this::mapToDto);
    }

    @Override
    public void deleteConfig(String field) {
        Config config = configRepository.findById(field).orElseThrow(()
                -> new ResourceNotFoundException("Field", "id", field));
        configRepository.delete(config);
    }
}

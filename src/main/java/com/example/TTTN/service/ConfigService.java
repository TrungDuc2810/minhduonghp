package com.example.TTTN.service;

import com.example.TTTN.payload.ConfigDto;
import com.example.TTTN.payload.ListResponse;

public interface ConfigService {
    ConfigDto addConfig(ConfigDto configDto);
    ConfigDto updateConfig(ConfigDto configDto, String field);
    ConfigDto getConfig(String field);
    ListResponse<ConfigDto> getAllConfigs(int pageNo, int pageSize, String sortBy, String sortDir);
    void deleteConfig(String field);
}

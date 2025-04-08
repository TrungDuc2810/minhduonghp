package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.StatusDto;

public interface StatusService {
    StatusDto getStatusById(long id);
    ListResponse<StatusDto> getAllStatus(int pageNo, int pageSize, String sortBy, String sortDir);
}

package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Status;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.StatusDto;
import com.example.TTTN.repository.StatusRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements GenericService<StatusDto> {
    private final StatusRepository statusRepository;
    private final ModelMapper modelMapper;

    public StatusServiceImpl(StatusRepository statusRepository, ModelMapper modelMapper) {
        this.statusRepository = statusRepository;
        this.modelMapper = modelMapper;
    }

    private StatusDto mapToDto(Status status) {
        return modelMapper.map(status, StatusDto.class);
    }

    private Status mapToEntity(StatusDto statusDto) {
        return modelMapper.map(statusDto, Status.class);
    }

    @Override
    public StatusDto getById(long id) {
        Status status = statusRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Status", "id", String.valueOf(id)));
        return mapToDto(status);
    }

    @Override
    public ListResponse<StatusDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<Status> status = statusRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(status, this::mapToDto);
    }

    @Override
    @Transactional
    public StatusDto create(StatusDto statusDto) {
        Status status = mapToEntity(statusDto);
        return mapToDto(statusRepository.save(status));
    }

    @Override
    @Transactional
    public StatusDto update(long id, StatusDto statusDto) {
        Status status = statusRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Status", "id", String.valueOf(id)));

        status.setName(statusDto.getName());

        return mapToDto(statusRepository.save(status));
    }

    @Override
    public void delete(long id) {
        Status status = statusRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Status", "id", String.valueOf(id)));

        statusRepository.delete(status);
    }
}

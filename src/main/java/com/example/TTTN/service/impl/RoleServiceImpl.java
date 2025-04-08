package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Role;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.RoleDto;
import com.example.TTTN.repository.RoleRepository;
import com.example.TTTN.service.common.GenericService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements GenericService<RoleDto> {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    private RoleDto mapToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }

    private Role mapToEntity(RoleDto roleDto) {
        return modelMapper.map(roleDto, Role.class);
    }

    @Override
    public RoleDto getById(long id) {
        Role role = roleRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role", "id", String.valueOf(id)));
        return mapToDto(role);
    }

    @Override
    public ListResponse<RoleDto> getAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<Role> roles = roleRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(roles, this::mapToDto);
    }

    @Override
    public RoleDto create(RoleDto roleDto) {
        Role role = mapToEntity(roleDto);
        return mapToDto(roleRepository.save(role));
    }

    @Override
    public RoleDto update(long id, RoleDto roleDto) {
        Role role = roleRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role", "id", String.valueOf(id)));

        role.setName(roleDto.getName());

        return mapToDto(roleRepository.save(role));
    }

    @Override
    public void delete(long id) {
        Role role = roleRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role", "id", String.valueOf(id)));

        roleRepository.delete(role);
    }
}

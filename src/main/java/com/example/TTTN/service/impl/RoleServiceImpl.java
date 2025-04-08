package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Role;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.RoleDto;
import com.example.TTTN.repository.RoleRepository;
import com.example.TTTN.service.RoleService;
import com.example.TTTN.utils.PaginationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
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
    public RoleDto getRoleById(long id) {
        Role role = roleRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Role", "id", String.valueOf(id)));
        return mapToDto(role);
    }

    @Override
    public ListResponse<RoleDto> getAllRoles(int pageNo, int pageSize, String sortBy, String sortDir) {
        PageRequest pageRequest = PaginationUtils.createPageRequest(pageNo, pageSize, sortBy, sortDir);
        Page<Role> roles = roleRepository.findAll(pageRequest);

        return PaginationUtils.toListResponse(roles, this::mapToDto);
    }
}

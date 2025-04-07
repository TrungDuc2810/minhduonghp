package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Role;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.RoleDto;
import com.example.TTTN.repository.RoleRepository;
import com.example.TTTN.service.RoleService;
import org.modelmapper.ModelMapper;
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
}

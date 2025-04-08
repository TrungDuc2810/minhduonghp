package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.RoleDto;

public interface RoleService {
    RoleDto getRoleById(long id);
    ListResponse<RoleDto> getAllRoles(int pageNo, int pageSize, String sortBy, String sortDir);
}

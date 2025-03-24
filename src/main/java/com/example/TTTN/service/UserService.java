package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.UserDto;

public interface UserService {
    ListResponse<UserDto> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    void deleteUserById(long id);
}

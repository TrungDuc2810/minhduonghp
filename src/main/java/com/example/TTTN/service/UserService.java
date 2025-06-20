package com.example.TTTN.service;

import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.UserDto;

public interface UserService {
    ListResponse<UserDto> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    UserDto updateUser(long id, UserDto userDto);
    void deleteUserById(long id);
}

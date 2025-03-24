package com.example.TTTN.service.impl;

import com.example.TTTN.entity.User;
import com.example.TTTN.exception.ResourceNotFoundException;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.payload.UserDto;
import com.example.TTTN.repository.UserRepository;
import com.example.TTTN.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private UserDto mapToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private User mapToEntity(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public ListResponse<UserDto> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<User> users = userRepository.findAll(pageRequest);

        List<User> listOfUsers = users.getContent();

        List<UserDto> content = listOfUsers.stream().map(this::mapToDto).toList();

        ListResponse<UserDto> userResponse = new ListResponse<>();
        userResponse.setContent(content);
        userResponse.setPageNo(users.getNumber());
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements((int)users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    @Override
    public void deleteUserById(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", String.valueOf(id)));
        userRepository.delete(user);
    }
}

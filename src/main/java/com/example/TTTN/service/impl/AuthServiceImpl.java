package com.example.TTTN.service.impl;

import com.example.TTTN.entity.Role;
import com.example.TTTN.entity.User;
import com.example.TTTN.exception.WebAPIException;
import com.example.TTTN.payload.LoginDto;
import com.example.TTTN.payload.RegisterDto;
import com.example.TTTN.repository.RoleRepository;
import com.example.TTTN.repository.UserRepository;
import com.example.TTTN.security.JwtTokenProvider;
import com.example.TTTN.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String register(RegisterDto registerDto) {
        if(userRepository.existsByUsername(registerDto.getUsername())) {
            throw new WebAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!!!");
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        String roleName = registerDto.getRole();

        Role role = roleRepository.findByName(roleName).orElseThrow(()
                -> new WebAPIException(HttpStatus.BAD_REQUEST, "Invalid role"));

        user.setRole(role);

        userRepository.save(user);

        return "User registered successfully!!!";
    }
}

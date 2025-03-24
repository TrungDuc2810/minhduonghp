package com.example.TTTN.service;

import com.example.TTTN.payload.LoginDto;
import com.example.TTTN.payload.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}

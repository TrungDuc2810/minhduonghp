package com.example.TTTN.controller;

import com.example.TTTN.payload.LoginDto;
import com.example.TTTN.payload.RegisterDto;
import com.example.TTTN.security.JwtTokenProvider;
import com.example.TTTN.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);

        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24*60*60*7)
                .sameSite("Strict")
                .build();

        String role = jwtTokenProvider.getRole(token);

        ResponseCookie roleCookie = ResponseCookie.from("role", role)
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60 * 7)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, roleCookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Tạo cookie jwtToken hết hạn để xóa
        ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)  // Đặt thời gian hết hạn về 0 để xóa cookie
                .sameSite("Strict")
                .build();

        // Tạo cookie roles hết hạn để xóa
        ResponseCookie roleCookie = ResponseCookie.from("role", "")
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, roleCookie.toString())
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String responseMessage = authService.register(registerDto);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }
}

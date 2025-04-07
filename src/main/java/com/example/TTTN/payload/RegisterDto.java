package com.example.TTTN.payload;

import com.example.TTTN.entity.Role;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String username;
    private String password;
    private long roleId;
    private String createdAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now().toString();
    }
}

package com.example.TTTN.payload;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class HomeConfigDto {
    private long id;
    private String title;
    private String description;
    private String thumbnail;
}

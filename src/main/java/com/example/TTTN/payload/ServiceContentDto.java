package com.example.TTTN.payload;

import lombok.Data;

@Data
public class ServiceContentDto {
    private long id;
    private String title;
    private String description;
    private String thumbnail;
}

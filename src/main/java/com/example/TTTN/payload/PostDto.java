package com.example.TTTN.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private String thumbnail;
    private String postedAt;
    private String updatedAt;
}

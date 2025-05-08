package com.example.TTTN.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "config")
public class Config {
    @Id
    @Column(nullable = false, unique = true)
    private String field;
    @Column(columnDefinition = "LONGTEXT")
    private String value;
}

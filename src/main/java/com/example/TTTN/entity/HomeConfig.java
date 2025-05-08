package com.example.TTTN.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "home_config")
public class HomeConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;
    @Column(nullable = false)
    private String thumbnail;
}

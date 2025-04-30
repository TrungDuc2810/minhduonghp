package com.example.TTTN.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private String title;
    @Lob
    private String content;
    @Column(name = "thumbnail")
    private String thumbnail;
    @Column(name = "posted_at")
    private String postedAt;
    @Column(name = "updated_at")
    private String updatedAt;
    @PrePersist
    protected void onCreate() {
        this.postedAt = LocalDateTime.now().toString();
        this.updatedAt = LocalDateTime.now().toString();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }
}

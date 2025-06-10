package com.example.blog.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "role", length = 20)
    private String role = "user"; // "admin" or "user"
}

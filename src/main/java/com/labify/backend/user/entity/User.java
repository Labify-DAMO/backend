package com.labify.backend.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String affiliation;  // 소속

    @Column(nullable = false)
    private String status;

    private LocalDateTime lastLoginAt;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

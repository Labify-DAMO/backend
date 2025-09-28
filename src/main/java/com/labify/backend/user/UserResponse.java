package com.labify.backend.user;

import com.labify.backend.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String email;
    private String name;
    private Role role;
    private String affiliation;  // 소속
}
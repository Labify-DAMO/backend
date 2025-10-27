package com.labify.backend.auth.dto;

import com.labify.backend.user.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String name;

    private String email;

    private String password;

    private Role role;

    private boolean agreeTerms; // 약관 동의
}

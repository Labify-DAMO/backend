package com.labify.backend.auth.dto;

import com.labify.backend.user.entity.User;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private User user;
}

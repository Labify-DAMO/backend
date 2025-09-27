package com.labify.backend.auth.dto;

import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    private String refreshToken;
}

package com.labify.backend.user.controller;

import com.labify.backend.auth.service.CustomUserDetails;
import com.labify.backend.auth.service.CustomUserDetailsService;
import com.labify.backend.user.UserResponse;
import com.labify.backend.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/user")
public class UserController {

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        UserResponse response = new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getAffiliation()
        );

        return ResponseEntity.ok(response);
    }
}

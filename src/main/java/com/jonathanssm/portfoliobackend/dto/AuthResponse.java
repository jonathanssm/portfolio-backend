package com.jonathanssm.portfoliobackend.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record AuthResponse(
        String token,
        String type,
        Long expiresIn,
        LocalDateTime expiresAt,
        UserInfo user
) {
    public record UserInfo(
            Long id,
            String username,
            String email,
            String firstName,
            String lastName,
            Set<String> roles
    ) {
    }
}

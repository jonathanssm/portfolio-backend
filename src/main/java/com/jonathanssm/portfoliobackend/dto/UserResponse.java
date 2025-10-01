package com.jonathanssm.portfoliobackend.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO de resposta para dados do usuário (sem informações sensíveis)
 * Não inclui senha e outros dados internos
 * <p>
 * Conversões são feitas via UserMapper (MapStruct)
 */
public record UserResponse(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Set<String> profiles,
        Set<String> roles,
        Boolean isEnabled,
        LocalDateTime createdAt
) {
}

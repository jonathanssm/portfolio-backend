package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.SecurityConstants;
import com.jonathanssm.portfoliobackend.constants.TransactionConstants;
import com.jonathanssm.portfoliobackend.dto.AuthResponse;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.util.JwtUtil;
import com.jonathanssm.portfoliobackend.util.RequestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Service especializado em geração e validação de tokens JWT
 * <p>
 * SRP: Responsabilidade única - apenas operações com tokens
 * DIP: Depende de abstrações (JwtUtil injetado)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtUtil jwtUtil;

    /**
     * Gera tokens de acesso e refresh para um usuário
     */
    @Transactional(readOnly = true, timeout = TransactionConstants.AUTH_TIMEOUT)
    public TokenPair generateTokens(User user) {
        log.debug("Generating tokens for user: {}", user.getUsername());

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * Valida um token JWT
     */
    @Transactional(readOnly = true, timeout = TransactionConstants.AUTH_TIMEOUT)
    public boolean validateToken(String token) {
        String jwt = extractBearerToken(token);
        if (jwt == null) {
            return false;
        }
        return Boolean.TRUE.equals(jwtUtil.validateToken(jwt));
    }

    /**
     * Invalida um token JWT
     */
    @Transactional(timeout = TransactionConstants.AUTH_TIMEOUT)
    public void invalidateToken(String token) {
        String jwt = extractBearerToken(token);
        if (jwt != null) {
            jwtUtil.invalidateToken(jwt);
            log.debug("Token invalidated successfully");
        }
    }

    /**
     * Extrai username de um token JWT
     */
    @Transactional(readOnly = true, timeout = TransactionConstants.AUTH_TIMEOUT)
    public String extractUsername(String token) {
        String jwt = extractBearerToken(token);
        if (jwt == null) {
            return null;
        }
        return jwtUtil.extractUsername(jwt);
    }

    /**
     * Cria um AuthResponse padronizado para o usuário
     */
    @Transactional(readOnly = true, timeout = TransactionConstants.AUTH_TIMEOUT)
    public AuthResponse createAuthResponse(User user, String accessToken) {
        return new AuthResponse(
                accessToken,
                SecurityConstants.Authentication.TOKEN_TYPE,
                SecurityConstants.Authentication.TOKEN_EXPIRATION_SECONDS,
                LocalDateTime.now().plusHours(SecurityConstants.Authentication.TOKEN_EXPIRATION_HOURS),
                new AuthResponse.UserInfo(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getAllRoles().stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toSet())
                )
        );
    }

    /**
     * Extrai o token JWT do header Authorization Bearer
     */
    private String extractBearerToken(String authHeader) {
        return RequestHelper.extractBearerToken(authHeader);
    }

    /**
     * Record para encapsular par de tokens
     */
    public record TokenPair(String accessToken, String refreshToken) {
    }
}

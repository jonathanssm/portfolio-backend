package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.TransactionConstants;
import com.jonathanssm.portfoliobackend.dto.AuthResponse;
import com.jonathanssm.portfoliobackend.messaging.AuthProducer;
import com.jonathanssm.portfoliobackend.model.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthTokenService authTokenService;
    private final CookieService cookieService;
    private final UserAuthenticationService userAuthenticationService;
    private final AuthProducer authProducer;
    private final RateLimitingService rateLimitingService;
    private final MetricsService metricsService;

    /**
     * Resultado do login contendo tokens e response
     */
    public record LoginResult(AuthResponse authResponse, String accessToken, String refreshToken) {
    }

    /**
     * Autentica um usuário e retorna tokens
     * <p>
     * SRP: Responsabilidade única - orquestração do processo de login
     * DIP: Depende de abstrações (services injetados)
     */
    @Transactional(timeout = TransactionConstants.AUTH_TIMEOUT)
    public LoginResult login(String username, String password, String ipAddress, String userAgent) {
        // Configurar MDC para logging estruturado
        MDC.put("username", username);
        MDC.put("ipAddress", ipAddress);
        MDC.put("operation", "login");

        try {
            log.info("🔐 Attempting login for user: {}", username);
            metricsService.recordLoginAttempt();

            // Verificar rate limiting
            if (rateLimitingService.isAccountLocked(username, ipAddress)) {
                int remainingAttempts = rateLimitingService.getRemainingAttempts(username, ipAddress);
                metricsService.recordFailedLogin();
                throw new IllegalArgumentException("Account temporarily locked. Try again in 15 minutes. Remaining attempts: " + remainingAttempts);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            User user = (User) authentication.getPrincipal();
            AuthTokenService.TokenPair tokenPair = authTokenService.generateTokens(user);
            AuthResponse authResponse = authTokenService.createAuthResponse(user, tokenPair.accessToken());
            log.info("✅ Login successful for user: {}", user.getUsername());

            // Limpar tentativas de login após sucesso
            rateLimitingService.clearLoginAttempts(username, ipAddress);
            metricsService.recordSuccessfulLogin();

            // Publicar evento de login bem-sucedido
            authProducer.sendLoginSuccess(user, ipAddress, userAgent);
            metricsService.recordKafkaEventPublished();

            return new LoginResult(authResponse, tokenPair.accessToken(), tokenPair.refreshToken());
        } finally {
            MDC.clear();
        }
    }

    /**
     * Resultado do refresh token contendo tokens e response
     */
    public record RefreshResult(AuthResponse authResponse, String newAccessToken, String newRefreshToken) {
    }

    /**
     * Renova tokens usando refresh token
     * <p>
     * SRP: Responsabilidade única - orquestração do refresh de tokens
     * DIP: Depende de abstrações (services injetados)
     */
    @Transactional(timeout = TransactionConstants.AUTH_TIMEOUT)
    public RefreshResult refreshToken(String refreshToken, String ipAddress, String userAgent) {
        log.info("🔄 Attempting token refresh");

        if (!authTokenService.validateToken(refreshToken)) {
            authProducer.sendTokenValidationFailure(refreshToken, ipAddress, userAgent, "Invalid or expired refresh token");
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        String username = authTokenService.extractUsername(refreshToken);
        if (username == null) {
            authProducer.sendTokenValidationFailure(refreshToken, ipAddress, userAgent, "Invalid token format");
            throw new IllegalArgumentException("Invalid refresh token format");
        }

        User user = (User) userAuthenticationService.loadUserByUsername(username);
        AuthTokenService.TokenPair newTokenPair = authTokenService.generateTokens(user);
        authTokenService.invalidateToken(refreshToken);

        AuthResponse authResponse = authTokenService.createAuthResponse(user, newTokenPair.accessToken());
        log.info("✅ Token refresh successful for user: {}", username);

        // Publicar evento de refresh token
        authProducer.sendTokenRefresh(user, ipAddress, userAgent);

        return new RefreshResult(authResponse, newTokenPair.accessToken(), newTokenPair.refreshToken());
    }

    /**
     * Valida um token JWT
     * <p>
     * SRP: Delegação para AuthTokenService
     * DIP: Depende de abstração (AuthTokenService)
     */
    @Transactional(readOnly = true, timeout = TransactionConstants.AUTH_TIMEOUT)
    public boolean validateToken(String token) {
        return authTokenService.validateToken(token);
    }

    /**
     * Realiza logout invalidando o token
     * <p>
     * SRP: Responsabilidade única - orquestração do logout
     * DIP: Depende de abstrações (services injetados)
     */
    @Transactional(timeout = TransactionConstants.AUTH_TIMEOUT)
    public void logout(String token, String ipAddress, String userAgent) {
        log.info("🚪 Attempting logout");

        if (token != null) {
            String username = authTokenService.extractUsername(token);
            if (username != null) {
                User user = (User) userAuthenticationService.loadUserByUsername(username);
                authTokenService.invalidateToken(token);
                log.info("✅ Logout successful for user: {}", username);

                // Publicar evento de logout
                authProducer.sendLogout(user, ipAddress, userAgent);
            }
        }
    }

    /**
     * Define cookies seguros para tokens de autenticação
     * <p>
     * SRP: Delegação para CookieService
     * DIP: Depende de abstração (CookieService)
     */
    public void setSecureCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        cookieService.setSecureCookies(response, accessToken, refreshToken);
    }

    /**
     * Remove cookies de autenticação
     * <p>
     * SRP: Delegação para CookieService
     * DIP: Depende de abstração (CookieService)
     */
    public void clearAuthCookies(HttpServletResponse response) {
        cookieService.clearAuthCookies(response);
    }
}

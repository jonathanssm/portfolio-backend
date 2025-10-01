package com.jonathanssm.portfoliobackend.service;

import com.jonathanssm.portfoliobackend.constants.SecurityConstants;
import com.jonathanssm.portfoliobackend.dto.AuthResponse;
import com.jonathanssm.portfoliobackend.messaging.AuthProducer;
import com.jonathanssm.portfoliobackend.model.User;
import com.jonathanssm.portfoliobackend.util.JwtUtil;
import com.jonathanssm.portfoliobackend.util.RequestHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserAuthenticationService userAuthenticationService;
    private final AuthProducer authProducer;
    private final RateLimitingService rateLimitingService;
    private final MetricsService metricsService;

    /**
     * Resultado do login contendo tokens e response
     */
    public record LoginResult(AuthResponse authResponse, String accessToken, String refreshToken) {}

    /**
     * Autentica um usuário e retorna tokens
     */
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
            String accessToken = jwtUtil.generateToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            AuthResponse authResponse = createAuthResponse(user, accessToken);
            log.info("✅ Login successful for user: {}", user.getUsername());
            
            // Limpar tentativas de login após sucesso
            rateLimitingService.clearLoginAttempts(username, ipAddress);
            metricsService.recordSuccessfulLogin();
            
            // Publicar evento de login bem-sucedido
            authProducer.sendLoginSuccess(user, ipAddress, userAgent);
            metricsService.recordKafkaEventPublished();
            
            return new LoginResult(authResponse, accessToken, refreshToken);
        } finally {
            MDC.clear();
        }
    }

    /**
     * Resultado do refresh token contendo tokens e response
     */
    public record RefreshResult(AuthResponse authResponse, String newAccessToken, String newRefreshToken) {}

    /**
     * Renova tokens usando refresh token
     */
    public RefreshResult refreshToken(String refreshToken, String ipAddress, String userAgent) {
        log.info("🔄 Attempting token refresh");
        
        String jwt = extractBearerToken(refreshToken);
        if (jwt == null) {
            authProducer.sendTokenValidationFailure(refreshToken, ipAddress, userAgent, "Invalid token format");
            throw new IllegalArgumentException("Invalid refresh token format");
        }
        
        if (!jwtUtil.isRefreshToken(jwt) || !Boolean.TRUE.equals(jwtUtil.validateToken(jwt))) {
            authProducer.sendTokenValidationFailure(refreshToken, ipAddress, userAgent, "Invalid or expired refresh token");
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
        
               String username = jwtUtil.extractUsername(jwt);
               User user = (User) userAuthenticationService.loadUserByUsername(username);
        
        String newAccessToken = jwtUtil.generateToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        
        jwtUtil.invalidateToken(jwt);
        
        AuthResponse authResponse = createAuthResponse(user, newAccessToken);
        log.info("✅ Token refresh successful for user: {}", username);
        
        // Publicar evento de refresh token
        authProducer.sendTokenRefresh(user, ipAddress, userAgent);
        
        return new RefreshResult(authResponse, newAccessToken, newRefreshToken);
    }

    /**
     * Valida um token JWT
     */
    public boolean validateToken(String token) {
        String jwt = extractBearerToken(token);
        if (jwt == null) {
            return false;
        }
        return Boolean.TRUE.equals(jwtUtil.validateToken(jwt));
    }

    /**
     * Realiza logout invalidando o token
     */
    public void logout(String token, String ipAddress, String userAgent) {
        log.info("🚪 Attempting logout");
        
        if (token != null) {
            String jwt = extractBearerToken(token);
            if (jwt != null) {
               String username = jwtUtil.extractUsername(jwt);
               User user = (User) userAuthenticationService.loadUserByUsername(username);
                
                jwtUtil.invalidateToken(jwt);
                log.info("✅ Logout successful for user: {}", username);
                
                // Publicar evento de logout
                authProducer.sendLogout(user, ipAddress, userAgent);
            }
        }
    }

    /**
     * Cria um AuthResponse padronizado para o usuário
     */
    private AuthResponse createAuthResponse(User user, String accessToken) {
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
     * Define cookies seguros para tokens de autenticação
     */
    public void setSecureCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        setSecureCookie(response, "access_token", accessToken, 3600); // 1 hora
        setSecureCookie(response, "refresh_token", refreshToken, 604800); // 7 dias
        log.debug("Secure cookies set for authentication");
    }

    /**
     * Remove cookies de autenticação
     */
    public void clearAuthCookies(HttpServletResponse response) {
        setSecureCookie(response, "access_token", null, 0);
        setSecureCookie(response, "refresh_token", null, 0);
        log.debug("Authentication cookies cleared");
    }

    /**
     * Define um cookie seguro com configurações padrão
     */
    private void setSecureCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    /**
     * Extrai o token JWT do header Authorization Bearer
     * ✅ Usa RequestUtils para eliminar duplicação
     */
    private String extractBearerToken(String authHeader) {
        return RequestHelper.extractBearerToken(authHeader);
    }
}

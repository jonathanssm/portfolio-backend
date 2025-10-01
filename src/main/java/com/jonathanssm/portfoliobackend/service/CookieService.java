package com.jonathanssm.portfoliobackend.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service especializado em gerenciamento de cookies
 * <p>
 * SRP: Responsabilidade única - apenas operações com cookies
 * OCP: Aberto para extensão, fechado para modificação
 */
@Slf4j
@Service
public class CookieService {

    private static final String ACCESS_TOKEN_COOKIE = "access_token";
    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";
    private static final int ACCESS_TOKEN_MAX_AGE = 3600; // 1 hora
    private static final int REFRESH_TOKEN_MAX_AGE = 604800; // 7 dias

    /**
     * Define cookies seguros para tokens de autenticação
     */
    public void setSecureCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        setSecureCookie(response, ACCESS_TOKEN_COOKIE, accessToken, ACCESS_TOKEN_MAX_AGE);
        setSecureCookie(response, REFRESH_TOKEN_COOKIE, refreshToken, REFRESH_TOKEN_MAX_AGE);
        log.debug("Secure cookies set for authentication");
    }

    /**
     * Remove cookies de autenticação
     */
    public void clearAuthCookies(HttpServletResponse response) {
        setSecureCookie(response, ACCESS_TOKEN_COOKIE, null, 0);
        setSecureCookie(response, REFRESH_TOKEN_COOKIE, null, 0);
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
}

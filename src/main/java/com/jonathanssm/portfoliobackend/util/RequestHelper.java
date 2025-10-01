package com.jonathanssm.portfoliobackend.util;

import com.jonathanssm.portfoliobackend.constants.DefaultConstants;
import com.jonathanssm.portfoliobackend.constants.HttpConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class para operações com HttpServletRequest
 */
@UtilityClass
@Slf4j
public class RequestHelper {

    /**
     * Extrai o IP do cliente considerando proxies e load balancers
     * 
     * @param request HttpServletRequest
     * @return IP do cliente
     */
    public static String extractClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return DefaultConstants.UNKNOWN;
        }
        
        // Verificar X-Forwarded-For (mais comum em proxies/load balancers)
        String xForwardedFor = request.getHeader(HttpConstants.Headers.X_FORWARDED_FOR);
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !DefaultConstants.UNKNOWN.equalsIgnoreCase(xForwardedFor)) {
            // X-Forwarded-For pode conter múltiplos IPs separados por vírgula
            // O primeiro IP é o IP original do cliente
            return xForwardedFor.split(",")[0].trim();
        }
        
        // Verificar X-Real-IP (usado por alguns proxies)
        String xRealIp = request.getHeader(HttpConstants.Headers.X_REAL_IP);
        if (xRealIp != null && !xRealIp.isEmpty() && !DefaultConstants.UNKNOWN.equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        // Fallback para o IP direto
        return request.getRemoteAddr();
    }

    /**
     * Extrai o User-Agent do request
     * 
     * @param request HttpServletRequest
     * @return User-Agent ou "unknown" se não disponível
     */
    public static String extractUserAgent(HttpServletRequest request) {
        if (request == null) {
            return DefaultConstants.UNKNOWN;
        }
        
        String userAgent = request.getHeader(HttpConstants.Headers.USER_AGENT);
        return userAgent != null ? userAgent : DefaultConstants.UNKNOWN;
    }

    /**
     * Extrai Bearer token do header Authorization
     * 
     * @param authHeader Header Authorization
     * @return Token sem o prefixo "Bearer " ou null se não encontrado
     */
    public static String extractBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    /**
     * Extrai contexto completo do request
     * 
     * @param request HttpServletRequest
     * @return RequestContext com todas as informações
     */
    public static RequestContext extractRequestContext(HttpServletRequest request) {
        return new RequestContext(
                extractClientIpAddress(request),
                extractUserAgent(request),
                extractBearerToken(request.getHeader(HttpConstants.Headers.AUTHORIZATION))
        );
    }

    /**
     * Record para encapsular contexto do request
     */
    public record RequestContext(
            String ipAddress,
            String userAgent,
            String bearerToken
    ) {}
}

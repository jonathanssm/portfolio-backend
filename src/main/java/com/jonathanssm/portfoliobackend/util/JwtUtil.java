package com.jonathanssm.portfoliobackend.util;

import com.jonathanssm.portfoliobackend.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {

    private static final String BASE64URL_PATTERN = "^[A-Za-z0-9_-]+$";

    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String secret;

    @Value("${jwt.expiration:3600000}") // 1 hora (reduzido para melhor segurança)
    private Long expiration;

    @Value("${jwt.refresh-expiration:604800000}") // 7 dias
    private Long refreshExpiration;

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.debug("Token is null or empty");
            return false;
        }
        
        // Verificar formato básico do JWT (header.payload.signature)
        if (!isValidJwtFormat(token)) {
            log.debug("Token has invalid format");
            return false;
        }
        
        return !isTokenExpired(token) && !blacklistedTokens.contains(token);
    }

    /**
     * Valida o formato básico do JWT
     */
    private boolean isValidJwtFormat(String token) {
        // JWT deve ter exatamente 3 partes separadas por ponto
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }
        
        // Cada parte deve conter apenas caracteres válidos para Base64URL
        return parts[0].matches(BASE64URL_PATTERN) &&
               parts[1].matches(BASE64URL_PATTERN) &&
               parts[2].matches(BASE64URL_PATTERN);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstants.TokenFields.TYPE, SecurityConstants.TokenFields.REFRESH);
        return createRefreshToken(claims, userDetails.getUsername());
    }

    private String createRefreshToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
        log.info("Token invalidated and added to blacklist");
    }

    public boolean isRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return SecurityConstants.TokenFields.REFRESH.equals(claims.get(SecurityConstants.TokenFields.TYPE));
        } catch (Exception e) {
            return false;
        }
    }
}

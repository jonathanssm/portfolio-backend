package com.jonathanssm.portfoliobackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service responsável por controlar rate limiting de tentativas de login
 * Segue SRP - única responsabilidade: controlar tentativas de acesso
 */
@Service
@Slf4j
public class RateLimitingService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION = 15L * 60 * 1000; // 15 minutos
    
    private final Map<String, AtomicInteger> loginAttempts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastAttemptTime = new ConcurrentHashMap<>();

    /**
     * Verifica se a conta está bloqueada por tentativas excessivas
     */
    public boolean isAccountLocked(String username, String ipAddress) {
        String key = createKey(username, ipAddress);
        Long lastAttempt = lastAttemptTime.get(key);
        
        if (lastAttempt == null) {
            return false;
        }
        
        long timeSinceLastAttempt = System.currentTimeMillis() - lastAttempt;
        boolean isLocked = timeSinceLastAttempt < LOCKOUT_DURATION;
        
        if (isLocked) {
            log.warn("Account locked for user: {} from IP: {}", username, ipAddress);
        }
        
        return isLocked;
    }

    /**
     * Registra uma tentativa de login falhada
     */
    public void recordFailedAttempt(String username, String ipAddress) {
        String key = createKey(username, ipAddress);
        
        int attempts = loginAttempts.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
        lastAttemptTime.put(key, System.currentTimeMillis());
        
        log.warn("Failed login attempt {} for user: {} from IP: {}", attempts, username, ipAddress);
        
        if (attempts >= MAX_ATTEMPTS) {
            log.error("Account locked due to {} failed attempts for user: {} from IP: {}", 
                     attempts, username, ipAddress);
        }
    }

    /**
     * Limpa as tentativas de login após sucesso
     */
    public void clearLoginAttempts(String username, String ipAddress) {
        String key = createKey(username, ipAddress);
        loginAttempts.remove(key);
        lastAttemptTime.remove(key);
        log.debug("Login attempts cleared for user: {} from IP: {}", username, ipAddress);
    }

    /**
     * Obtém o número de tentativas restantes
     */
    public int getRemainingAttempts(String username, String ipAddress) {
        String key = createKey(username, ipAddress);
        AtomicInteger attempts = loginAttempts.get(key);
        return attempts != null ? Math.max(0, MAX_ATTEMPTS - attempts.get()) : MAX_ATTEMPTS;
    }

    private String createKey(String username, String ipAddress) {
        return username + ":" + ipAddress;
    }
}


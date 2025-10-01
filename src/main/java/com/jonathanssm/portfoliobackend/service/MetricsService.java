package com.jonathanssm.portfoliobackend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service responsável por coletar métricas básicas da aplicação
 * Segue SRP - única responsabilidade: coleta e fornecimento de métricas
 */
@Service
@Slf4j
public class MetricsService {

    private final AtomicLong loginAttempts = new AtomicLong(0);
    private final AtomicLong successfulLogins = new AtomicLong(0);
    private final AtomicLong failedLogins = new AtomicLong(0);
    private final AtomicLong experienceCreations = new AtomicLong(0);
    private final AtomicLong experienceFetches = new AtomicLong(0);
    private final AtomicLong kafkaEventsPublished = new AtomicLong(0);

    /**
     * Registra uma tentativa de login
     */
    public void recordLoginAttempt() {
        loginAttempts.incrementAndGet();
        log.debug("Login attempt recorded. Total: {}", loginAttempts.get());
    }

    /**
     * Registra um login bem-sucedido
     */
    public void recordSuccessfulLogin() {
        successfulLogins.incrementAndGet();
        log.debug("Successful login recorded. Total: {}", successfulLogins.get());
    }

    /**
     * Registra um login falhado
     */
    public void recordFailedLogin() {
        failedLogins.incrementAndGet();
        log.debug("Failed login recorded. Total: {}", failedLogins.get());
    }

    /**
     * Registra criação de experiência
     */
    public void recordExperienceCreation() {
        experienceCreations.incrementAndGet();
        log.debug("Experience creation recorded. Total: {}", experienceCreations.get());
    }

    /**
     * Registra busca de experiências
     */
    public void recordExperienceFetch(int count) {
        experienceFetches.addAndGet(count);
        log.debug("Experience fetch recorded. Count: {}, Total: {}", count, experienceFetches.get());
    }

    /**
     * Registra evento Kafka publicado
     */
    public void recordKafkaEventPublished() {
        kafkaEventsPublished.incrementAndGet();
        log.debug("Kafka event published. Total: {}", kafkaEventsPublished.get());
    }

    /**
     * Obtém todas as métricas coletadas
     */
    public Map<String, Object> getAllMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // Métricas de autenticação
        metrics.put("authentication.loginAttempts", loginAttempts.get());
        metrics.put("authentication.successfulLogins", successfulLogins.get());
        metrics.put("authentication.failedLogins", failedLogins.get());
        metrics.put("authentication.successRate", calculateSuccessRate());
        
        // Métricas de experiências
        metrics.put("experiences.creations", experienceCreations.get());
        metrics.put("experiences.fetches", experienceFetches.get());
        
        // Métricas de Kafka
        metrics.put("kafka.eventsPublished", kafkaEventsPublished.get());
        
        // Métricas gerais
        metrics.put("timestamp", System.currentTimeMillis());
        
        return metrics;
    }

    /**
     * Calcula a taxa de sucesso de login
     */
    private double calculateSuccessRate() {
        long total = loginAttempts.get();
        return total > 0 ? (double) successfulLogins.get() / total * 100 : 0.0;
    }

    /**
     * Reseta todas as métricas
     */
    public void resetMetrics() {
        loginAttempts.set(0);
        successfulLogins.set(0);
        failedLogins.set(0);
        experienceCreations.set(0);
        experienceFetches.set(0);
        kafkaEventsPublished.set(0);
        log.info("All metrics reset to zero");
    }
}


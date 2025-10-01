package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.constants.DefaultConstants;
import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import com.jonathanssm.portfoliobackend.model.User;

import java.util.Map;

/**
 * Factory para criação de eventos Kafka padronizados
 * Elimina duplicação de código entre diferentes producers
 */
public final class EventFactory {

    private EventFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Cria payload para eventos de usuário
     */
    public static Map<String, Object> createUserEventPayload(User user) {
        return Map.of(
                KafkaConstants.EventFields.USER_ID, user.getId(),
                KafkaConstants.EventFields.USERNAME, user.getUsername(),
                KafkaConstants.EventFields.EMAIL, user.getEmail(),
                KafkaConstants.EventFields.FIRST_NAME, user.getFirstName() != null ? user.getFirstName() : DefaultConstants.EMPTY_STRING,
                KafkaConstants.EventFields.LAST_NAME, user.getLastName() != null ? user.getLastName() : DefaultConstants.EMPTY_STRING,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );
    }

    /**
     * Cria payload para eventos de autenticação
     */
    public static Map<String, Object> createAuthEventPayload(User user, String ipAddress, String userAgent) {
        return Map.of(
                KafkaConstants.EventFields.USER_ID, user.getId(),
                KafkaConstants.EventFields.USERNAME, user.getUsername(),
                KafkaConstants.EventFields.EMAIL, user.getEmail(),
                KafkaConstants.EventFields.IP_ADDRESS, ipAddress != null ? ipAddress : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.USER_AGENT, userAgent != null ? userAgent : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );
    }

    /**
     * Cria payload para eventos de falha de autenticação
     */
    public static Map<String, Object> createAuthFailurePayload(String username, String ipAddress, String userAgent, String reason) {
        return Map.of(
                KafkaConstants.EventFields.USERNAME, username != null ? username : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.IP_ADDRESS, ipAddress != null ? ipAddress : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.USER_AGENT, userAgent != null ? userAgent : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.REASON, reason != null ? reason : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );
    }

    /**
     * Cria payload para eventos de admin
     */
    public static Map<String, Object> createAdminEventPayload(User adminUser, String createdBy, String ipAddress, String userAgent) {
        return Map.of(
                KafkaConstants.EventFields.ADMIN_USER_ID, adminUser.getId(),
                KafkaConstants.EventFields.ADMIN_USERNAME, adminUser.getUsername(),
                KafkaConstants.EventFields.ADMIN_EMAIL, adminUser.getEmail(),
                KafkaConstants.EventFields.CREATED_BY, createdBy != null ? createdBy : DefaultConstants.SYSTEM,
                KafkaConstants.EventFields.IP_ADDRESS, ipAddress != null ? ipAddress : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.USER_AGENT, userAgent != null ? userAgent : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );
    }

    /**
     * Cria payload para eventos de tentativa de admin não autorizada
     */
    public static Map<String, Object> createAdminUnauthorizedPayload(String username, String ipAddress, String userAgent) {
        return Map.of(
                KafkaConstants.EventFields.ATTEMPTED_USERNAME, username != null ? username : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.IP_ADDRESS, ipAddress != null ? ipAddress : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.USER_AGENT, userAgent != null ? userAgent : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );
    }

    /**
     * Cria payload para eventos de falha de admin
     */
    public static Map<String, Object> createAdminFailurePayload(String username, String reason, String ipAddress, String userAgent) {
        return Map.of(
                KafkaConstants.EventFields.ATTEMPTED_USERNAME, username != null ? username : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.REASON, reason != null ? reason : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.IP_ADDRESS, ipAddress != null ? ipAddress : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.USER_AGENT, userAgent != null ? userAgent : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );
    }

    /**
     * Cria payload para eventos de token inválido
     */
    public static Map<String, Object> createTokenValidationFailurePayload(String token, String ipAddress, String userAgent, String reason) {
        return Map.of(
                "token", token != null ? token.substring(0, Math.min(20, token.length())) + "..." : DefaultConstants.BooleanValues.NULL,
                KafkaConstants.EventFields.IP_ADDRESS, ipAddress != null ? ipAddress : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.USER_AGENT, userAgent != null ? userAgent : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.REASON, reason != null ? reason : DefaultConstants.UNKNOWN,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );
    }
}

package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import com.jonathanssm.portfoliobackend.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class AuthEventHandler {

    public void handleEvent(String key, Event event) {
        switch (key) {
            case KafkaConstants.EventKeys.AUTH_LOGIN_SUCCESS -> handleLoginSuccess(event);
            case KafkaConstants.EventKeys.AUTH_LOGIN_FAILURE -> handleLoginFailure(event);
            case KafkaConstants.EventKeys.AUTH_LOGOUT -> handleLogout(event);
            case KafkaConstants.EventKeys.AUTH_TOKEN_REFRESH -> handleTokenRefresh(event);
            case KafkaConstants.EventKeys.AUTH_TOKEN_INVALID -> handleTokenInvalid(event);
            default -> log.warn("⚠️ Unhandled auth event key: {}", key);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleLoginSuccess(Event event) {
        Map<String, Object> data = (Map<String, Object>) event.getPayload();
        String username = (String) data.get(KafkaConstants.EventFields.USERNAME);
        String ipAddress = (String) data.get(KafkaConstants.EventFields.IP_ADDRESS);

        log.info("✅ [SECURITY] Login successful - User: {}, IP: {}", username, ipAddress);

        // Event logged for audit purposes
    }

    @SuppressWarnings("unchecked")
    private void handleLoginFailure(Event event) {
        Map<String, Object> data = (Map<String, Object>) event.getPayload();
        String username = (String) data.get(KafkaConstants.EventFields.USERNAME);
        String ipAddress = (String) data.get(KafkaConstants.EventFields.IP_ADDRESS);
        String reason = (String) data.get(KafkaConstants.EventFields.REASON);

        log.warn("❌ [SECURITY] Login failed - User: {}, IP: {}, Reason: {}", username, ipAddress, reason);
        // Event logged for audit purposes
        // - Bloquear IP após X tentativas
        // - Enviar alerta de segurança
        // - Registrar para análise de fraudes
    }

    @SuppressWarnings("unchecked")
    private void handleLogout(Event event) {
        Map<String, Object> data = (Map<String, Object>) event.getPayload();
        String username = (String) data.get(KafkaConstants.EventFields.USERNAME);
        String ipAddress = (String) data.get(KafkaConstants.EventFields.IP_ADDRESS);

        log.info("🚪 [SECURITY] Logout - User: {}, IP: {}", username, ipAddress);

        // Event logged for audit purposes
    }

    @SuppressWarnings("unchecked")
    private void handleTokenRefresh(Event event) {
        Map<String, Object> data = (Map<String, Object>) event.getPayload();
        String username = (String) data.get(KafkaConstants.EventFields.USERNAME);
        String ipAddress = (String) data.get(KafkaConstants.EventFields.IP_ADDRESS);

        log.info("🔄 [SECURITY] Token refreshed - User: {}, IP: {}", username, ipAddress);

        // Event logged for audit purposes
    }

    @SuppressWarnings("unchecked")
    private void handleTokenInvalid(Event event) {
        Map<String, Object> data = (Map<String, Object>) event.getPayload();
        String ipAddress = (String) data.get(KafkaConstants.EventFields.IP_ADDRESS);
        String reason = (String) data.get(KafkaConstants.EventFields.REASON);

        log.warn("⚠️ [SECURITY] Invalid token attempt - IP: {}, Reason: {}", ipAddress, reason);
        // Event logged for audit purposes
        // - Possível tentativa de ataque
        // - Registrar para análise
    }
}

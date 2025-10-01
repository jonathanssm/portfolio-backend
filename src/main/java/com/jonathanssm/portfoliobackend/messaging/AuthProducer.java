package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import com.jonathanssm.portfoliobackend.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthProducer {

    private static final String TOPIC = KafkaConstants.AUTH_TOPIC;
    private final EventPublisher eventPublisher;

    /**
     * Evento de login bem-sucedido
     */
    public void sendLoginSuccess(User user, String ipAddress, String userAgent) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.AUTH_LOGIN_SUCCESS,
                EventFactory.createAuthEventPayload(user, ipAddress, userAgent));
        log.debug("🔐 Login success event published for user: {}", user.getUsername());
    }

    /**
     * Evento de logout
     */
    public void sendLogout(User user, String ipAddress, String userAgent) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.AUTH_LOGOUT,
                EventFactory.createAuthEventPayload(user, ipAddress, userAgent));
        log.debug("🚪 Logout event published for user: {}", user.getUsername());
    }

    /**
     * Evento de refresh token
     */
    public void sendTokenRefresh(User user, String ipAddress, String userAgent) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.AUTH_TOKEN_REFRESH,
                EventFactory.createAuthEventPayload(user, ipAddress, userAgent));
        log.debug("🔄 Token refresh event published for user: {}", user.getUsername());
    }

    /**
     * Evento de token inválido
     */
    public void sendTokenValidationFailure(String token, String ipAddress, String userAgent, String reason) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.AUTH_TOKEN_INVALID,
                EventFactory.createTokenValidationFailurePayload(token, ipAddress, userAgent, reason));
        log.debug("⚠️ Token validation failure event published");
    }
}

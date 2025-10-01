package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import com.jonathanssm.portfoliobackend.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminProducer {

    private static final String TOPIC = KafkaConstants.ADMIN_TOPIC;
    private final EventPublisher eventPublisher;

    /**
     * Evento de criação de usuário administrador
     */
    public void sendAdminCreated(User adminUser, User createdBy, String ipAddress, String userAgent) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.ADMIN_USER_CREATED,
                EventFactory.createAdminEventPayload(adminUser, createdBy != null ? createdBy.getUsername() : null, ipAddress, userAgent));
        log.debug("👤 Admin user created event published: {}", adminUser.getUsername());
    }
}

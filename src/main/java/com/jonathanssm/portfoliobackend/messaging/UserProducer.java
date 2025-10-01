package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.constants.DefaultConstants;
import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import com.jonathanssm.portfoliobackend.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProducer {

    private static final String TOPIC = KafkaConstants.USER_TOPIC;

    private final EventPublisher eventPublisher;

    /**
     * Evento de criação de usuário
     */
    public void sendUserCreated(User user) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.USER_CREATED, EventFactory.createUserEventPayload(user));
        log.debug("👤 User created event published: {}", user.getUsername());
    }

    /**
     * Evento de atualização de usuário
     */
    public void sendUserUpdated(User user) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.USER_UPDATED, EventFactory.createUserEventPayload(user));
        log.debug("♻️ User updated event published: {}", user.getUsername());
    }

    /**
     * Evento de exclusão de usuário
     */
    public void sendUserDeleted(Long userId, String username) {
        Map<String, Object> userData = Map.of(
                KafkaConstants.EventFields.USER_ID, userId,
                KafkaConstants.EventFields.USERNAME, username,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );

        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.USER_DELETED, userData);
        log.debug("🗑️ User deleted event published: {}", username);
    }

    /**
     * Evento de adição de perfil ao usuário
     */
    public void sendUserProfileAdded(User user, String profileName) {
        Map<String, Object> profileData = Map.of(
                KafkaConstants.EventFields.USER_ID, user.getId(),
                KafkaConstants.EventFields.USERNAME, user.getUsername(),
                KafkaConstants.EventFields.PROFILE_NAME, profileName,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );

        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.USER_PROFILE_ADDED, profileData);
        log.debug("➕ User profile added event published: {} -> {}", user.getUsername(), profileName);
    }

    /**
     * Evento de remoção de perfil do usuário
     */
    public void sendUserProfileRemoved(User user, String profileName) {
        Map<String, Object> profileData = Map.of(
                KafkaConstants.EventFields.USER_ID, user.getId(),
                KafkaConstants.EventFields.USERNAME, user.getUsername(),
                KafkaConstants.EventFields.PROFILE_NAME, profileName,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );

        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.USER_PROFILE_REMOVED, profileData);
        log.debug("➖ User profile removed event published: {} -> {}", user.getUsername(), profileName);
    }

    /**
     * Evento de registro de novo usuário (público)
     */
    public void sendUserRegistered(User user) {
        Map<String, Object> registrationData = Map.of(
                KafkaConstants.EventFields.USER_ID, user.getId(),
                KafkaConstants.EventFields.USERNAME, user.getUsername(),
                KafkaConstants.EventFields.EMAIL, user.getEmail(),
                KafkaConstants.EventFields.FIRST_NAME, user.getFirstName() != null ? user.getFirstName() : DefaultConstants.EMPTY_STRING,
                KafkaConstants.EventFields.LAST_NAME, user.getLastName() != null ? user.getLastName() : DefaultConstants.EMPTY_STRING,
                KafkaConstants.EventFields.REGISTRATION_TYPE, DefaultConstants.PUBLIC_REGISTRATION,
                KafkaConstants.EventFields.TIMESTAMP, System.currentTimeMillis()
        );

        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.USER_REGISTERED, registrationData);
        log.debug("📝 User registered event published: {}", user.getUsername());
    }
}
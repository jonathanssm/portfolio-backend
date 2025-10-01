package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import com.jonathanssm.portfoliobackend.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEventHandler {

    public void handleEvent(String key, Event event) {
        switch (key) {
            case KafkaConstants.EventKeys.USER_CREATED -> handleUserCreated(event);
            case KafkaConstants.EventKeys.USER_UPDATED -> handleUserUpdated(event);
            case KafkaConstants.EventKeys.USER_DELETED -> handleUserDeleted(event);
            case KafkaConstants.EventKeys.USER_REGISTERED -> handleUserRegistered(event);
            case KafkaConstants.EventKeys.USER_PROFILE_ADDED -> handleUserProfileAdded(event);
            case KafkaConstants.EventKeys.USER_PROFILE_REMOVED -> handleUserProfileRemoved(event);
            default -> log.warn("⚠️ Unhandled user event key: {}", key);
        }
    }

    private void handleUserCreated(Event event) {
        log.info("✅ User Created: {}", event.getPayload());
        // Event logged for audit purposes
    }

    private void handleUserUpdated(Event event) {
        log.info("♻️ User Updated: {}", event.getPayload());
        // Event logged for audit purposes
        // - Atualizar cache de usuário
        // - Notificar sistemas externos
        // - Registrar auditoria
    }

    private void handleUserDeleted(Event event) {
        log.info("🗑️ User Deleted: {}", event.getPayload());
        // Event logged for audit purposes
        // - Limpar dados relacionados
        // - Revogar tokens ativos
        // - Notificar sistemas externos
        // - Registrar auditoria de exclusão
    }

    private void handleUserRegistered(Event event) {
        log.info("🎉 User Registered: {}", event.getPayload());
        // Event logged for audit purposes
        // - Enviar email de confirmação
        // - Ativar perfil básico
        // - Atualizar métricas de registro
        // - Notificar equipe de marketing
    }

    private void handleUserProfileAdded(Event event) {
        log.info("👤 Profile Added: {}", event.getPayload());
        // Event logged for audit purposes
        // - Notificar mudança de permissões
        // - Atualizar cache de autorização
        // - Registrar auditoria de perfil
        // - Disparar workflows específicos por perfil
    }

    private void handleUserProfileRemoved(Event event) {
        log.info("👤 Profile Removed: {}", event.getPayload());
        // Event logged for audit purposes
        // - Revogar permissões específicas
        // - Atualizar cache de autorização
        // - Registrar auditoria de perfil
        // - Notificar sistemas afetados
    }
}

package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExperienceEventHandler {

    public void handleEvent(String key, Event event) {
        switch (key) {
            case "experience.created" -> handleCreated(event);
            case "experience.updated" -> handleUpdated(event);
            case "experience.deleted" -> handleDeleted(event);
            case "experience.fetched" -> handleFetched(event);
            default -> log.warn("⚠️ Unhandled event key: {}", key);
        }
    }

    private void handleCreated(Event event) {
        log.info("✅ Experience created: {}", event.getPayload());
        // TODO: enviar email, disparar notificação, etc
    }

    private void handleUpdated(Event event) {
        log.info("♻️ Experience updated: {}", event.getPayload());
    }

    private void handleDeleted(Event event) {
        log.info("🗑️ Experience deleted: {}", event.getPayload());
    }

    private void handleFetched(Event event) {
        log.info("📊 Experience fetched count: {}", event.getPayload());
    }
}

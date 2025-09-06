package com.jonathanssm.portfoliobackend.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExperienceConsumer {

    @KafkaListener(topics = "portfolio-experience-events", groupId = "experience-logger")
    public void logExperienceEvents(String message) {
        log.info("📨 [KAFKA] Experience Event: {}", message);
    }

    @KafkaListener(topics = "portfolio-experience-events", groupId = "experience-analytics")
    public void processAnalytics(String message) {
        if (message.contains("EXPERIENCE_FETCHED")) {
            log.info("📊 Analytics: Experiences were fetched");
        }
    }
}

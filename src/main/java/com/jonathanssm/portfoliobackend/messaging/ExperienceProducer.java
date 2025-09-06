package com.jonathanssm.portfoliobackend.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExperienceProducer {

    private static final String TOPIC = "portfolio-experience-events";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendExperienceCreated(String title) {
        kafkaTemplate.send(TOPIC, "EXPERIENCE_CREATED: " + title);
    }

    public void sendExperienceFetched(int count) {
        kafkaTemplate.send(TOPIC, "EXPERIENCE_FETCHED: " + count + " experiences");
    }

    public void sendExperienceDeleted(Long id) {
        kafkaTemplate.send(TOPIC, "EXPERIENCE_DELETED: " + id);
    }
}

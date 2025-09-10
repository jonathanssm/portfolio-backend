package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.model.Experience;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExperienceProducer {

    private static final String TOPIC = "portfolio-experience-events";

    private final EventPublisher eventPublisher;

    public void sendExperienceCreated(Experience experience) {
        eventPublisher.publish(TOPIC, "experience.created", experience);
    }

    public void sendExperienceUpdated(Experience experience) {
        eventPublisher.publish(TOPIC, "experience.updated", experience);
    }

    public void sendExperienceFetched(int count) {
        eventPublisher.publish(TOPIC, "experience.fetched", count);
    }

    public void sendExperienceDeleted(long id) {
        eventPublisher.publish(TOPIC, "experience.deleted", id);
    }
}

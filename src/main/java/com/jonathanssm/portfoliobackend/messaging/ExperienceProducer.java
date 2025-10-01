package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import com.jonathanssm.portfoliobackend.model.Experience;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExperienceProducer {

    private static final String TOPIC = KafkaConstants.EXPERIENCE_TOPIC;

    private final EventPublisher eventPublisher;

    public void sendExperienceCreated(Experience experience) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.EXPERIENCE_CREATED, experience);
    }

    public void sendExperienceUpdated(Experience experience) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.EXPERIENCE_UPDATED, experience);
    }

    public void sendExperienceFetched(int count) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.EXPERIENCE_FETCHED, count);
    }

    public void sendExperienceDeleted(long id) {
        eventPublisher.publish(TOPIC, KafkaConstants.EventKeys.EXPERIENCE_DELETED, id);
    }
}
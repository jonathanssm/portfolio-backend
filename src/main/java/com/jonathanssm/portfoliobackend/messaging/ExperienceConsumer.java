package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.model.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExperienceConsumer {

    private final ExperienceEventHandler handler;

    @KafkaListener(topics = "portfolio-experience-events", groupId = "experience-router")
    public void route(ConsumerRecord<String, Event> record) {
        String key = record.key();
        Event event = record.value();

        log.info("📨 [KAFKA] Received key={}, value={}", key, event);

        handler.handleEvent(key, event);
    }
}

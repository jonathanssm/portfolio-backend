package com.jonathanssm.portfoliobackend.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonathanssm.portfoliobackend.model.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publish(String topic, String key, Object payload) {
        Event event = Event.builder()
                .eventKey(key)
                .payload(payload)
                .timestamp(Instant.now())
                .build();

        kafkaTemplate.send(topic, key, event);

        String payloadType = payload != null ? payload.getClass().getSimpleName() : "null";
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);

            log.info(
                    "📤 Event published | topic: '{}' | key: '{}' | timestamp: {} | payload type: {} | payload: {}",
                    topic,
                    key,
                    event.getTimestamp(),
                    payloadType,
                    payloadJson
            );
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize event for logging: {}", e.getMessage());
            log.info("📤 Event published (raw) | topic: '{}' | key: '{}' | timestamp: {} | payload type: {} | payload: {}",
                    topic,
                    key,
                    event.getTimestamp(),
                    payloadType,
                    payload
            );
        }
    }
}

package com.jonathanssm.portfoliobackend.messaging;

import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import com.jonathanssm.portfoliobackend.model.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthConsumer {

    private final AuthEventHandler authEventHandler;

    @KafkaListener(topics = KafkaConstants.AUTH_TOPIC, groupId = KafkaConstants.AUTH_SECURITY_HANDLER)
    public void handleAuthEvent(ConsumerRecord<String, Event> consumenrRecord) {
        String key = consumenrRecord.key();
        Event event = consumenrRecord.value();

        log.info("📨 [KAFKA-AUTH] Received key={}, timestamp={}", key, event.getTimestamp());
        authEventHandler.handleEvent(key, event);
    }
}

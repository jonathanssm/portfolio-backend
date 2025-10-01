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
public class UserConsumer {

    private final UserEventHandler handler;

    @KafkaListener(topics = KafkaConstants.USER_TOPIC, groupId = KafkaConstants.USER_ROUTER)
    public void route(ConsumerRecord<String, Event> consumerRecord) {
        String key = consumerRecord.key();
        Event event = consumerRecord.value();

        log.info("📨 [KAFKA] Received user event | key={} | value={}", key, event);

        handler.handleEvent(key, event);
    }
}

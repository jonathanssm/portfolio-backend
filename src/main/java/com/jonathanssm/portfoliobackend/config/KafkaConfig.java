package com.jonathanssm.portfoliobackend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic experienceTopic() {
        return TopicBuilder.name("portfolio-experience-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}

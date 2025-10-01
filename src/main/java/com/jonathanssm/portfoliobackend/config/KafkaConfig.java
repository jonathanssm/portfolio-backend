package com.jonathanssm.portfoliobackend.config;

import com.jonathanssm.portfoliobackend.constants.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic experienceTopic() {
        return TopicBuilder.name(KafkaConstants.EXPERIENCE_TOPIC)
                .partitions(KafkaConstants.TOPIC_PARTITIONS)
                .replicas(KafkaConstants.TOPIC_REPLICAS)
                .build();
    }

    @Bean
    public NewTopic authTopic() {
        return TopicBuilder.name(KafkaConstants.AUTH_TOPIC)
                .partitions(KafkaConstants.TOPIC_PARTITIONS)
                .replicas(KafkaConstants.TOPIC_REPLICAS)
                .build();
    }

    @Bean
    public NewTopic adminTopic() {
        return TopicBuilder.name(KafkaConstants.ADMIN_TOPIC)
                .partitions(KafkaConstants.TOPIC_PARTITIONS)
                .replicas(KafkaConstants.TOPIC_REPLICAS)
                .build();
    }

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(KafkaConstants.USER_TOPIC)
                .partitions(KafkaConstants.TOPIC_PARTITIONS)
                .replicas(KafkaConstants.TOPIC_REPLICAS)
                .build();
    }
}

package com.gap.learning.foodapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.topic}")
    private String foodTopic;

    @Bean
    public NewTopic foodTopic() {
        return TopicBuilder
                .name(foodTopic)
                .replicas(1)
                .partitions(1)
                .build();
    }
}

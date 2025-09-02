package com.gap.learning.foodapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gap.learning.foodapp.message.FoodItemMessage;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;


    public CompletableFuture<SendResult<String, String>> sendMessageAsync(FoodItemMessage message) throws JsonProcessingException {
        String key = message.getId();
        String value  = objectMapper.writeValueAsString(message);

        Header headers = new RecordHeader("food-topic", "food-topic".getBytes());
        List<Header> headerList = List.of(headers);

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(kafkaTopic, null, key, value, headerList);

        CompletableFuture<SendResult<String, String>>  future = kafkaTemplate.send(producerRecord);

        return future.whenComplete((record, throwable) -> {
            if (throwable != null) {
                System.out.println("Message delivery unsuccessful  = " + record);
            } else {
                System.out.println("Successful record = " + record.getProducerRecord().key());
                System.out.println("Message sent to partition = " + record.getProducerRecord().partition());
            }
        });
    }
}

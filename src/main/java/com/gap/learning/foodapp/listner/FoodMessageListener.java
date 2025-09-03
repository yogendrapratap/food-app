package com.gap.learning.foodapp.listner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gap.learning.foodapp.message.FoodItemMessage;
import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.service.FoodService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FoodMessageListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FoodService foodService;

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${consumer.group-id}")
    public void onMessage(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {
        System.out.println("Received message partition: " + consumerRecord.partition());
        System.out.println("Received message value: " + consumerRecord.value());
        System.out.println("Received message key: " + consumerRecord.key());

        consumerRecord.headers().forEach(header -> System.out.println("Header Key: " + header.key() + " Header Value: " + header.value()));

        FoodItemMessage foodItemMessage = objectMapper.readValue(consumerRecord.value(), FoodItemMessage.class);

        Food existingFood = foodService.findFood(foodItemMessage.getItemName(), foodItemMessage.getVendorId());

        Food food = getFood(foodItemMessage);

        if(existingFood != null){
            foodService.updateFood(food);
        } else {
            foodService.createFood(food);
        }
    }

    private static Food getFood(FoodItemMessage foodItemMessage) {
        Food food = new Food();
        food.setItemId(foodItemMessage.getItemId());
        food.setItemName(foodItemMessage.getItemName());
        food.setVendorId(foodItemMessage.getVendorId());
        food.setPrice(foodItemMessage.getPrice());
        food.setAvailable(foodItemMessage.getAvailable());
        food.setId(foodItemMessage.getId());
        return food;
    }
}

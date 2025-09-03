package com.gap.learning.foodapp.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gap.learning.foodapp.dto.CartDTO;
import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.dto.FoodList;
import com.gap.learning.foodapp.dto.Role;
import com.gap.learning.foodapp.dto.User;
import com.gap.learning.foodapp.exception.ECommerceAPIValidationException;
import com.gap.learning.foodapp.message.FoodItemMessage;
import com.gap.learning.foodapp.repository.FoodRepository;
import com.gap.learning.foodapp.repository.UserRepository;
import com.gap.learning.foodapp.validator.EcommerceValidator;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private EcommerceValidator ecommerceValidator;

    public Food search(String foodName, String vendorId) {
        Food food =  foodRepository.findByItemNameAndVendorId(foodName, vendorId);
        ecommerceValidator.validate(food);
        return food;
    }

    public Food searchByIdAndVendorId(String itemId, String vendorId) {
        Food food =  foodRepository.findByIdAndVendorId(itemId, vendorId);
        ecommerceValidator.validate(food);
        return food;
    }

    public Food findFood(String foodName, String vendorId) {
        return  foodRepository.findByItemNameAndVendorId(foodName, vendorId);
    }

    public String createFoodMessage(Long userId, Food food) throws JsonProcessingException {
    	User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != Role.VENDOR) {
        	throw new ECommerceAPIValidationException("Only Vendors can add food items!");
        }
    	
        Food existingFood = foodRepository.findByItemNameAndVendorId(food.getItemName(), food.getVendorId());
        if (existingFood != null) {
            throw new ECommerceAPIValidationException("Food item already exists: " + food.getItemName());
        }

        boolean messageStatus = sendMessageToKafka(food);

        return messageStatus ? "Message Sent Successfully" : "Message Send Failed...";
    }

    public Food createFood(Food food) {
        Food existingFood = foodRepository.findByItemNameAndVendorId(food.getItemName(), food.getVendorId());
        if (existingFood != null) {
            throw new ECommerceAPIValidationException("Food item already exists: " + food.getItemName());
        }

       return foodRepository.save(food);
    }

    public String updateFoodMessage(Long userId,Food food) throws JsonProcessingException {
    	User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getRole() != Role.VENDOR) {
        	throw new ECommerceAPIValidationException("Only Vendors can add food items!");
        }
        Food existingFood = foodRepository.findByItemName(food.getItemName());
        if (existingFood == null) {
            throw new ECommerceAPIValidationException("Food item not exists: " + food.getItemName());
        }

        boolean messageStatus = sendMessageToKafka(food);

        return messageStatus ? "Message Sent Successfully" : "Message Send Failed...";
    }

    public Food updateFood(Food food) throws JsonProcessingException {
        Food existingFood = foodRepository.findByItemName(food.getItemName());
        if (existingFood == null) {
            throw new ECommerceAPIValidationException("Food item not exists: " + food.getItemName());
        }

        return foodRepository.save(food);
    }


    public FoodList searchProductListForCart(List<CartDTO> cartDTOs) {

        List<Food> foodItems = cartDTOs.stream()
                .map(cartDTO -> foodRepository.findByIdAndVendorId(cartDTO.getItemId(), cartDTO.getVendorId()))
                .collect(Collectors.toList());

        FoodList foodList = new FoodList();
        foodList.setFoodItems(foodItems);
        return foodList;
    }

    private boolean sendMessageToKafka(Food food) throws JsonProcessingException {
        FoodItemMessage foodItemMessage = getFoodItemMessage(food);

        CompletableFuture<SendResult<String, String>> completableFuture =
                kafkaProducerService.sendMessageAsync(foodItemMessage);

        boolean messageStatus = waitForKafkaResult(completableFuture);

        System.out.println(messageStatus ? "Message Sent Successfully" : "Message Send Failed...");
        return messageStatus;
    }

    private boolean waitForKafkaResult(CompletableFuture<SendResult<String, String>> future) {
        try {
            future.get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static FoodItemMessage getFoodItemMessage(Food food) {
        FoodItemMessage foodItemMessage = new FoodItemMessage();
        foodItemMessage.setId(food.getId());
        foodItemMessage.setItemName(food.getItemName());
        foodItemMessage.setVendorId(food.getVendorId());
        foodItemMessage.setPrice(food.getPrice());
        foodItemMessage.setAvailable(food.getAvailable());
        return foodItemMessage;
    }
}

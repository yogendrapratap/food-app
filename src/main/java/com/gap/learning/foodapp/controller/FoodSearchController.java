package com.gap.learning.foodapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.service.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FoodSearchController {

    private static final Logger logger = LoggerFactory.getLogger(FoodSearchController.class);

    @Autowired
    private FoodService foodService;

    @GetMapping("/search")
    public Food search(@RequestParam String foodName,
                       @RequestParam("vendorId") String vendorId) {
        logger.info("Searching for food: {} from vendor: {}", foodName, vendorId);
        Food result = foodService.search(foodName, vendorId);
        logger.info("Search result: {}", result);
        return foodService.search(foodName, vendorId);
    }

    @PostMapping("create-food")
    public String createFood(@RequestParam("userId") Long userId, @RequestBody Food food) throws JsonProcessingException {
        return foodService.createFoodMessage(userId,food);
    }

    @PutMapping("update-food")
    public String updateFood(@RequestParam("userId") Long userId, @RequestBody Food food) throws JsonProcessingException {
        return foodService.updateFoodMessage(userId,food);
    }

}

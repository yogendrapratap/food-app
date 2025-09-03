package com.gap.learning.foodapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class FoodSearchController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/search")
    public Food search(@RequestParam String foodName,
                       @RequestParam("vendorId") String vendorId) {

        return foodService.search(foodName, vendorId);
    }

    @PostMapping("create-food")
    public String createFood(@RequestBody Food food) throws JsonProcessingException {
        return foodService.createFoodMessage(food);
    }

    @PutMapping("update-food")
    public String updateFood(@RequestBody Food food) throws JsonProcessingException {
        return foodService.updateFoodMessage(food);
    }

}

package com.gap.learning.foodapp.controller;

import com.gap.learning.foodapp.response.Food;
import com.gap.learning.foodapp.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodSearchController {

    @Autowired
    private FoodService foodService;

    @GetMapping("/search")
    public Food search(@RequestParam String foodName) {

        return foodService.search(foodName);
    }
}

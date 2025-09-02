package com.gap.learning.foodapp.service;

import com.gap.learning.foodapp.repository.FoodRepository;
import com.gap.learning.foodapp.response.Food;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    public Food search(String foodName) {
        return foodRepository.findByItemName(foodName);

    }
}

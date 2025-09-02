package com.gap.learning.foodapp.repository;

import com.gap.learning.foodapp.response.Food;
import org.apache.el.stream.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends MongoRepository<Food, String> {
    Food findByItemName(String itemName);
}

package com.gap.learning.foodapp.repository;

import com.gap.learning.foodapp.dto.Food;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends MongoRepository<Food, String> {
    Food findByItemName(String itemName);

    Food findByItemNameAndVendorId(String foodName, String vendorId);

    List<Food> findAllByVendorIdAndItemIdIn(String vendorId, List<String> productIds);

    Food findByIdAndVendorId(String itemId, String vendorId);

    List<Food> findAllByVendorIdAndIdIn(String vendorId, List<String> productIds);
}

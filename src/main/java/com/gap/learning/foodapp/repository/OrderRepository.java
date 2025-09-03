package com.gap.learning.foodapp.repository;

import com.gap.learning.foodapp.dto.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}

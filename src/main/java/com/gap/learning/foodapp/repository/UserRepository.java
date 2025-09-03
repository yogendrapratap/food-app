package com.gap.learning.foodapp.repository;


import com.gap.learning.foodapp.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<com.gap.learning.foodapp.dto.User, Long> {
   User findByUsernameAndPassword(String username, String password);
}

package com.gap.learning.foodapp.repository;

import com.gap.learning.foodapp.dto.CartDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<CartDTO, String> {
    List<CartDTO> findAllByUserId(Long userId);

    void deleteCartDTOByUserId(Long userId);
}

package com.gap.learning.foodapp.validator;


import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.exception.ECommerceAPIValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EcommerceValidator {

    public void validate(BigDecimal amount, BigDecimal basketPrice) {
        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) == 0){
            throw new ECommerceAPIValidationException("Transaction not successful !!");
        }

        if (amount.compareTo(basketPrice) !=0){
            throw new ECommerceAPIValidationException("Transaction not successful  !!");
        }
    }

    public void validate(Food food) {
        if (food == null) {
            throw new ECommerceAPIValidationException("Food Item not found");
        }

        if(food.getAvailable() == null || !food.getAvailable()) {
            throw new ECommerceAPIValidationException("Food Item is not available");
        }
    }
}


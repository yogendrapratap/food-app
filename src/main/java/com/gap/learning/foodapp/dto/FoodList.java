package com.gap.learning.foodapp.dto;

import java.math.BigDecimal;
import java.util.List;

public class FoodList {

    private List<Food> foodItems;

    private BigDecimal basketPrice;

    public List<Food> getFoodItems() {
        return foodItems;
    }

    public FoodList setFoodItems(List<Food> foodItems) {
        this.foodItems = foodItems;
        return this;
    }

    public BigDecimal getBasketPrice() {
        return basketPrice;
    }

    public FoodList setBasketPrice(BigDecimal basketPrice) {
        this.basketPrice = basketPrice;
        return this;
    }
}

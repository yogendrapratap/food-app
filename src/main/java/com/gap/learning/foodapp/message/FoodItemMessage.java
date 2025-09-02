package com.gap.learning.foodapp.message;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public class FoodItemMessage {

    private String id;
    private String itemId;
    private String itemName;
    private String vendorId;
    private BigDecimal price;
    private Boolean isAvailable;

    public String getId() {
        return id;
    }

    public FoodItemMessage setId(String id) {
        this.id = id;
        return this;
    }

    public String getItemId() {
        return itemId;
    }

    public FoodItemMessage setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public FoodItemMessage setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public String getVendorId() {
        return vendorId;
    }

    public FoodItemMessage setVendorId(String vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public FoodItemMessage setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public FoodItemMessage setAvailable(Boolean available) {
        isAvailable = available;
        return this;
    }
}

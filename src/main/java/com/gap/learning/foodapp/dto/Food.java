package com.gap.learning.foodapp.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "food")
public class Food {
    @Id
    private String id;
    private String itemId;
    private String itemName;
    private String vendorId;
    private String vendorName;
    private BigDecimal price;
    private Boolean isAvailable;
    private Integer quantity;

    public String getId() {
        return id;
    }

    public Food setId(String id) {
        this.id = id;
        return this;
    }

    public String getItemId() {
        return itemId;
    }

    public Food setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public Food setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public String getVendorId() {
        return vendorId;
    }

    public Food setVendorId(String vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    public String getVendorName() {
        return vendorName;
    }

    public Food setVendorName(String vendorName) {
        this.vendorName = vendorName;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Food setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public Food setAvailable(Boolean available) {
        isAvailable = available;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Food setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}

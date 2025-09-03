package com.gap.learning.foodapp.dto;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cart")
public class CartDTO {

    private String itemId;
    private Long userId;
    private String vendorId;
    private Integer quantity;

    public CartDTO(String itemId, Long userId, String vendorId, Integer quantity) {
        this.itemId = itemId;
        this.userId = userId;
        this.vendorId = vendorId;
        this.quantity = quantity;
    }

    public String getItemId() {
        return itemId;
    }

    public CartDTO setItemId(String itemId) {
        this.itemId = itemId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public CartDTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getVendorId() {
        return vendorId;
    }

    public CartDTO setVendorId(String vendorId) {
        this.vendorId = vendorId;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartDTO setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}

package com.gap.learning.foodapp.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "order")
public class Order {

    private List<Food> items;
    private String orderId;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalPrice;
    private String transactionId;

    public List<Food> getItems() {
        return items;
    }

    public Order setItems(List<Food> items) {
        this.items = items;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public Order setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public Order setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Order setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public Order setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Order setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }
}

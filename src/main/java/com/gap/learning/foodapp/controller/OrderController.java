package com.gap.learning.foodapp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gap.learning.foodapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/placeOrder")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestParam("userId") Long userId) {
        orderService.placeOrder(userId);
        return  ResponseEntity.ok().body("Order placed successfully");
    }
}

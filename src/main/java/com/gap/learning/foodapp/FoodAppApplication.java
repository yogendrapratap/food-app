package com.gap.learning.foodapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FoodAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodAppApplication.class, args);
    }

}

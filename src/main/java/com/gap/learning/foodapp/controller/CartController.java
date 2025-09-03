package com.gap.learning.foodapp.controller;

import com.gap.learning.foodapp.dto.FoodList;
import com.gap.learning.foodapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gap.learning.foodapp.dto.CartListDTO;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping(value = "/addToCart/user/{userId}")
    public ResponseEntity<CartListDTO> addToCart(@RequestBody CartListDTO cartListDTO,
                                                 @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(cartService.addToCart(cartListDTO, userId));
    }

    @GetMapping(value = "/details", params = "userId")
    public ResponseEntity<FoodList> findCartProductDetails(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.findCartFoodDetails(userId));
    }

}

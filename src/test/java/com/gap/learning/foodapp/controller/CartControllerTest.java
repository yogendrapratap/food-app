package com.gap.learning.foodapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gap.learning.foodapp.dto.CartListDTO;
import com.gap.learning.foodapp.dto.FoodList;
import com.gap.learning.foodapp.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddToCart_Success() throws Exception {
        CartListDTO cartListDTO = new CartListDTO();
        Long userId = 1L;
        Mockito.when(cartService.addToCart(Mockito.any(CartListDTO.class), Mockito.eq(userId)))
                .thenReturn(cartListDTO);

        mockMvc.perform(post("/addToCart/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartListDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cartListDTO)));
    }

    @Test
    void testFindCartProductDetails_Success() throws Exception {
        FoodList foodList = new FoodList();
        Long userId = 2L;
        Mockito.when(cartService.findCartFoodDetails(userId)).thenReturn(foodList);

        mockMvc.perform(get("/details").param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(foodList)));
    }


}


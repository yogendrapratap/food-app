package com.gap.learning.foodapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.service.FoodService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodSearchController.class)
class FoodSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FoodService foodService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSearch() throws Exception {
        Food food = new Food()
                .setItemId("1")
                .setVendorId("A")
                .setItemName("Pizza")
                .setPrice(new BigDecimal("10.00"));
        Mockito.when(foodService.search("Pizza", "A")).thenReturn(food);

        mockMvc.perform(get("/search")
                        .param("foodName", "Pizza")
                        .param("vendorId", "A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Pizza"))
                .andExpect(jsonPath("$.vendorId").value("A"));
    }

    //@Test
    void testCreateFood() throws Exception {
        Food food = new Food()
                .setItemId("2")
                .setVendorId("B")
                .setItemName("Burger")
                .setPrice(new BigDecimal("5.00"));
        Mockito.when(foodService.createFoodMessage(1L,Mockito.any(Food.class))).thenReturn("Food created");

        mockMvc.perform(post("/create-food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(food)))
                .andExpect(status().isOk())
                .andExpect(content().string("Food created"));
    }

   // @Test
    void testUpdateFood() throws Exception {
        Food food = new Food()
                .setItemId("2")
                .setVendorId("B")
                .setItemName("Burger")
                .setPrice(new BigDecimal("6.00"));
        Mockito.when(foodService.updateFoodMessage(1L, Mockito.any(Food.class))).thenReturn("Food updated");

        mockMvc.perform(put("/update-food")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(food)))
                .andExpect(status().isOk())
                .andExpect(content().string("Food updated"));
    }
}

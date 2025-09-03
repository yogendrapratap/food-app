package com.gap.learning.foodapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gap.learning.foodapp.dto.CartDTO;
import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.dto.FoodList;
import com.gap.learning.foodapp.exception.ECommerceAPIValidationException;
import com.gap.learning.foodapp.message.FoodItemMessage;
import com.gap.learning.foodapp.repository.FoodRepository;
import com.gap.learning.foodapp.validator.EcommerceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.support.SendResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoodServiceTest {

    @InjectMocks
    private FoodService foodService;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private EcommerceValidator ecommerceValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- search methods ---
    @Test
    void testSearch_Found() {
        Food food = new Food();
        food.setId("1");
        when(foodRepository.findByItemNameAndVendorId("Pizza", "V1")).thenReturn(food);

        Food result = foodService.search("Pizza", "V1");

        verify(ecommerceValidator).validate(food);
        assertEquals("1", result.getId());
    }

    @Test
    void testSearchByIdAndVendorId_Found() {
        Food food = new Food();
        food.setId("101");
        when(foodRepository.findByIdAndVendorId("101", "V1")).thenReturn(food);

        Food result = foodService.searchByIdAndVendorId("101", "V1");

        verify(ecommerceValidator).validate(food);
        assertEquals("101", result.getId());
    }

    @Test
    void testFindFood() {
        Food food = new Food();
        when(foodRepository.findByItemNameAndVendorId("Burger", "V2")).thenReturn(food);

        Food result = foodService.findFood("Burger", "V2");

        assertEquals(food, result);
    }

    // --- createFoodMessage ---
    @Test
    void testCreateFoodMessage_Success() throws Exception {
        Food food = new Food();
        food.setItemName("Burger");
        food.setVendorId("V1");

        when(foodRepository.findByItemNameAndVendorId(food.getItemName(), food.getVendorId())).thenReturn(null);
        when(kafkaProducerService.sendMessageAsync(any(FoodItemMessage.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        String result = foodService.createFoodMessage(food);

        assertEquals("Message Sent Successfully", result);
    }

    @Test
    void testCreateFoodMessage_AlreadyExists() {
        Food food = new Food();
        food.setItemName("Burger");
        food.setVendorId("V1");

        when(foodRepository.findByItemNameAndVendorId(food.getItemName(), food.getVendorId()))
                .thenReturn(new Food());

        assertThrows(ECommerceAPIValidationException.class, () -> foodService.createFoodMessage(food));
    }

    @Test
    void testCreateFoodMessage_KafkaFail() throws Exception {
        Food food = new Food();
        food.setItemName("Pizza");
        food.setVendorId("V1");

        when(foodRepository.findByItemNameAndVendorId(food.getItemName(), food.getVendorId())).thenReturn(null);
        CompletableFuture<SendResult<String,String>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka failed"));
        when(kafkaProducerService.sendMessageAsync(any(FoodItemMessage.class))).thenReturn(future);

        String result = foodService.createFoodMessage(food);

        assertEquals("Message Send Failed...", result);
    }

    // --- createFood ---
    @Test
    void testCreateFood_Success() {
        Food food = new Food();
        food.setItemName("Sandwich");
        food.setVendorId("V1");

        when(foodRepository.findByItemNameAndVendorId(food.getItemName(), food.getVendorId())).thenReturn(null);
        when(foodRepository.save(food)).thenReturn(food);

        Food result = foodService.createFood(food);

        assertEquals(food, result);
    }

    @Test
    void testCreateFood_AlreadyExists() {
        Food food = new Food();
        food.setItemName("Sandwich");
        food.setVendorId("V1");

        when(foodRepository.findByItemNameAndVendorId(food.getItemName(), food.getVendorId()))
                .thenReturn(new Food());

        assertThrows(ECommerceAPIValidationException.class, () -> foodService.createFood(food));
    }

    // --- updateFoodMessage ---
    @Test
    void testUpdateFoodMessage_Success() throws Exception {
        Food food = new Food();
        food.setItemName("Pasta");

        when(foodRepository.findByItemName(food.getItemName())).thenReturn(food);
        when(kafkaProducerService.sendMessageAsync(any(FoodItemMessage.class)))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        String result = foodService.updateFoodMessage(food);

        assertEquals("Message Sent Successfully", result);
    }

    @Test
    void testUpdateFoodMessage_NotExists() {
        Food food = new Food();
        food.setItemName("Pasta");

        when(foodRepository.findByItemName(food.getItemName())).thenReturn(null);

        assertThrows(ECommerceAPIValidationException.class, () -> foodService.updateFoodMessage(food));
    }

    @Test
    void testUpdateFoodMessage_KafkaFail() throws Exception {
        Food food = new Food();
        food.setItemName("Pasta");

        when(foodRepository.findByItemName(food.getItemName())).thenReturn(food);
        CompletableFuture<SendResult<String,String>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka failed"));
        when(kafkaProducerService.sendMessageAsync(any(FoodItemMessage.class))).thenReturn(future);

        String result = foodService.updateFoodMessage(food);

        assertEquals("Message Send Failed...", result);
    }

    // --- updateFood ---
    @Test
    void testUpdateFood_Success() throws Exception {
        Food food = new Food();
        food.setItemName("Pizza");

        when(foodRepository.findByItemName(food.getItemName())).thenReturn(food);
        when(foodRepository.save(food)).thenReturn(food);

        Food result = foodService.updateFood(food);

        assertEquals(food, result);
    }

    @Test
    void testUpdateFood_NotExists() {
        Food food = new Food();
        food.setItemName("Pizza");

        when(foodRepository.findByItemName(food.getItemName())).thenReturn(null);

        assertThrows(ECommerceAPIValidationException.class, () -> foodService.updateFood(food));
    }

    // --- searchProductListForCart ---
    @Test
    void testSearchProductListForCart() {
        CartDTO cart1 = new CartDTO("101", null, "V1", 2);
        CartDTO cart2 = new CartDTO("102", null, "V2", 1);

        Food food1 = new Food();
        food1.setId("101");
        food1.setVendorId("V1");

        Food food2 = new Food();
        food2.setId("102");
        food2.setVendorId("V2");

        when(foodRepository.findByIdAndVendorId("101", "V1")).thenReturn(food1);
        when(foodRepository.findByIdAndVendorId("102", "V2")).thenReturn(food2);

        FoodList result = foodService.searchProductListForCart(List.of(cart1, cart2));

        assertEquals(2, result.getFoodItems().size());
        assertTrue(result.getFoodItems().contains(food1));
        assertTrue(result.getFoodItems().contains(food2));
    }
}

package com.gap.learning.foodapp.service;

import com.gap.learning.foodapp.dto.CartDTO;
import com.gap.learning.foodapp.dto.CartListDTO;
import com.gap.learning.foodapp.dto.Food;
import com.gap.learning.foodapp.dto.FoodList;
import com.gap.learning.foodapp.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private FoodService foodService;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart_NewItem() {
        Long userId = 1L;

        CartDTO newItem = new CartDTO("101", userId, "V1", 2);
        CartListDTO cartListDTO = new CartListDTO().setCartDTO(List.of(newItem));

        when(cartRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());
        when(cartRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        CartListDTO result = cartService.addToCart(cartListDTO, userId);

        assertNotNull(result);
        assertEquals(1, result.getCartDTO().size());
        assertEquals("101", result.getCartDTO().get(0).getItemId());
        verify(foodService).searchByIdAndVendorId("101", "V1");
        verify(cartRepository).saveAll(anyList());
    }

    @Test
    void testAddToCart_ExistingItemQuantityUpdated() {
        Long userId = 1L;

        CartDTO existingItem = new CartDTO("101", userId, "V1", 1);
        CartDTO newItem = new CartDTO("101", userId, "V1", 2);
        CartListDTO cartListDTO = new CartListDTO().setCartDTO(List.of(newItem));

        when(cartRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>(List.of(existingItem)));
        when(cartRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        CartListDTO result = cartService.addToCart(cartListDTO, userId);

        assertEquals(1, result.getCartDTO().size());
        assertEquals(3, result.getCartDTO().get(0).getQuantity());
    }

    @Test
    void testFindCartFoodDetails() {
        Long userId = 1L;

        CartDTO cartDTO = new CartDTO("101", userId, "V1", 2);
        Food food = new Food();
        food.setId("101");
        food.setVendorId("V1");
        food.setPrice(new BigDecimal("10.00"));

        FoodList foodList = new FoodList();
        foodList.setFoodItems(List.of(food));

        when(cartRepository.findAllByUserId(userId)).thenReturn(List.of(cartDTO));
        when(foodService.searchProductListForCart(anyList())).thenReturn(foodList);

        FoodList result = cartService.findCartFoodDetails(userId);

        assertEquals(new BigDecimal("20.00"), result.getBasketPrice());
        assertEquals(2, result.getFoodItems().get(0).getQuantity());
    }

    @Test
    void testCleanUpCartData() {
        Long userId = 1L;
        cartService.cleanUpCartData(userId);
        verify(cartRepository).deleteCartDTOByUserId(userId);
    }
}

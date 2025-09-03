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

    // --- addToCart tests ---

    @Test
    void testAddToCart_NewItemAddedToEmptyCart() {
        Long userId = 1L;
        CartDTO cartDTO = new CartDTO("101", userId, "V1", 2);

        CartListDTO cartListDTO = new CartListDTO();
        cartListDTO.setCartDTO(List.of(cartDTO));

        when(cartRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());
        when(cartRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        CartListDTO result = cartService.addToCart(cartListDTO, userId);

        assertEquals(1, result.getCartDTO().size());
        assertEquals(2, result.getCartDTO().get(0).getQuantity());
        verify(foodService).searchByIdAndVendorId(cartDTO.getItemId(), cartDTO.getVendorId());
        verify(cartRepository).saveAll(anyList());
    }

    @Test
    void testAddToCart_ExistingItemQuantityUpdated() {
        Long userId = 1L;

        CartDTO existing = new CartDTO("101", userId, "V1", 2);
        CartDTO newItem = new CartDTO("101", userId, "V1", 3);

        CartListDTO cartListDTO = new CartListDTO();
        cartListDTO.setCartDTO(List.of(newItem));

        when(cartRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>(List.of(existing)));
        when(cartRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        CartListDTO result = cartService.addToCart(cartListDTO, userId);

        assertEquals(1, result.getCartDTO().size());
        assertEquals(5, result.getCartDTO().get(0).getQuantity());
        verify(cartRepository).saveAll(anyList());
    }

    @Test
    void testAddToCart_MultipleItems_SomeExistingSomeNew() {
        Long userId = 1L;

        CartDTO existing = new CartDTO("101", userId, "V1", 2);
        CartDTO newItem1 = new CartDTO("101", userId, "V1", 1);
        CartDTO newItem2 = new CartDTO("102", userId, "V2", 3);

        CartListDTO cartListDTO = new CartListDTO();
        cartListDTO.setCartDTO(List.of(newItem1, newItem2));

        when(cartRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>(List.of(existing)));
        when(cartRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        CartListDTO result = cartService.addToCart(cartListDTO, userId);

        assertEquals(2, result.getCartDTO().size());
        assertTrue(result.getCartDTO().stream().anyMatch(c -> c.getItemId().equals("101") && c.getQuantity() == 3));
        assertTrue(result.getCartDTO().stream().anyMatch(c -> c.getItemId().equals("102") && c.getQuantity() == 3));
    }

    @Test
    void testAddToCart_EmptyCartList() {
        Long userId = 1L;

        CartListDTO cartListDTO = new CartListDTO();
        cartListDTO.setCartDTO(new ArrayList<>());

        when(cartRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());
        when(cartRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        CartListDTO result = cartService.addToCart(cartListDTO, userId);

        assertTrue(result.getCartDTO().isEmpty());
        verify(cartRepository).saveAll(anyList());
    }

    // --- findCartFoodDetails tests ---

    @Test
    void testFindCartFoodDetails_WithMatchingItems() {
        Long userId = 1L;

        CartDTO cartDTO = new CartDTO("101", userId, "V1", 2);
        Food food = new Food();
        food.setId("101");
        food.setVendorId("V1");
        food.setPrice(new BigDecimal("50"));

        FoodList foodList = new FoodList();
        foodList.setFoodItems(List.of(food));

        when(cartRepository.findAllByUserId(userId)).thenReturn(List.of(cartDTO));
        when(foodService.searchProductListForCart(anyList())).thenReturn(foodList);

        FoodList result = cartService.findCartFoodDetails(userId);

        assertEquals(1, result.getFoodItems().size());
        assertEquals(2, result.getFoodItems().get(0).getQuantity());
        assertEquals(new BigDecimal("100"), result.getBasketPrice());
    }

    @Test
    void testFindCartFoodDetails_WithNoMatchingItems() {
        Long userId = 1L;

        CartDTO cartDTO = new CartDTO("101", userId, "V1", 2);
        Food food = new Food();
        food.setId("999");
        food.setVendorId("V9");
        food.setPrice(new BigDecimal("50"));

        FoodList foodList = new FoodList();
        foodList.setFoodItems(List.of(food));

        when(cartRepository.findAllByUserId(userId)).thenReturn(List.of(cartDTO));
        when(foodService.searchProductListForCart(anyList())).thenReturn(foodList);

        FoodList result = cartService.findCartFoodDetails(userId);

        assertEquals(1, result.getFoodItems().size());
        assertEquals(0, result.getFoodItems().get(0).getQuantity());
        assertEquals(BigDecimal.ZERO, result.getBasketPrice());
    }

    @Test
    void testFindCartFoodDetails_EmptyCart() {
        Long userId = 1L;

        when(cartRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());

        FoodList emptyFoodList = new FoodList();
        emptyFoodList.setFoodItems(new ArrayList<>());
        when(foodService.searchProductListForCart(anyList())).thenReturn(emptyFoodList);

        FoodList result = cartService.findCartFoodDetails(userId);

        assertNotNull(result);
        assertEquals(0, result.getFoodItems().size());
        assertEquals(BigDecimal.ZERO, result.getBasketPrice());
    }


    @Test
    void testFindCartFoodDetails_MultipleCartItems() {
        Long userId = 1L;

        CartDTO cart1 = new CartDTO("101", userId, "V1", 2);
        CartDTO cart2 = new CartDTO("102", userId, "V2", 3);

        Food food1 = new Food();
        food1.setId("101");
        food1.setVendorId("V1");
        food1.setPrice(new BigDecimal("50"));

        Food food2 = new Food();
        food2.setId("102");
        food2.setVendorId("V2");
        food2.setPrice(new BigDecimal("30"));

        FoodList foodList = new FoodList();
        foodList.setFoodItems(List.of(food1, food2));

        when(cartRepository.findAllByUserId(userId)).thenReturn(List.of(cart1, cart2));
        when(foodService.searchProductListForCart(anyList())).thenReturn(foodList);

        FoodList result = cartService.findCartFoodDetails(userId);

        assertEquals(2, result.getFoodItems().size());
        assertEquals(2, result.getFoodItems().get(0).getQuantity());
        assertEquals(3, result.getFoodItems().get(1).getQuantity());
        assertEquals(new BigDecimal("190"), result.getBasketPrice());
    }

    // --- cleanUpCartData test ---

    @Test
    void testCleanUpCartData() {
        Long userId = 1L;

        doNothing().when(cartRepository).deleteCartDTOByUserId(userId);

        cartService.cleanUpCartData(userId);

        verify(cartRepository, times(1)).deleteCartDTOByUserId(userId);
    }
}

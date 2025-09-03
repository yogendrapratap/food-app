package com.gap.learning.foodapp.service;

import com.gap.learning.foodapp.dto.*;
import com.gap.learning.foodapp.feignclient.BankAppClient;
import com.gap.learning.foodapp.feignclient.FundTransferResponseDTO;
import com.gap.learning.foodapp.feignclient.UserFundTransferDTO;
import com.gap.learning.foodapp.repository.OrderRepository;
import com.gap.learning.foodapp.validator.EcommerceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private BankAppClient bankAppClient;

    @Mock
    private EcommerceValidator ecommerceValidator;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder_SuccessfulFlow() {
        Long userId = 1L;

        // Mock FoodList
        Food food = new Food();
        food.setId("101");
        food.setVendorId("V1");
        food.setPrice(new BigDecimal("10.00"));
        food.setQuantity(2);

        FoodList foodList = new FoodList();
        foodList.setFoodItems(List.of(food));
        foodList.setBasketPrice(new BigDecimal("20.00"));

        // Mock UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");

        // Mock FundTransferResponseDTO
        FundTransferResponseDTO fundTransferResponseDTO = new FundTransferResponseDTO();
        fundTransferResponseDTO.setTransactionId("TX123");
        fundTransferResponseDTO.setAmount(new BigDecimal("20.00"));

        // Stubbing
        when(cartService.findCartFoodDetails(userId)).thenReturn(foodList);
        when(userService.getUsersDetailsById(userId)).thenReturn(userDTO);
        when(bankAppClient.transferFundsForEcommerceAccount(any(UserFundTransferDTO.class), eq(userId)))
                .thenReturn(fundTransferResponseDTO);

        // Execute
        orderService.placeOrder(userId);

        // Verify
        verify(cartService).findCartFoodDetails(userId);
        verify(userService).getUsersDetailsById(userId);
        verify(bankAppClient).transferFundsForEcommerceAccount(any(UserFundTransferDTO.class), eq(userId));
        verify(ecommerceValidator).validate(new BigDecimal("20.00"), new BigDecimal("20.00"));
        verify(orderRepository).save(any(Order.class));
        verify(cartService).cleanUpCartData(userId);
    }
}
